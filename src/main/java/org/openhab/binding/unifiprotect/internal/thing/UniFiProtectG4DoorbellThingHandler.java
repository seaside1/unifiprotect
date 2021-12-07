/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.unifiprotect.internal.thing;

import static org.openhab.core.thing.ThingStatus.*;
import static org.openhab.core.thing.ThingStatusDetail.CONFIGURATION_ERROR;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unifiprotect.internal.UniFiProtectBindingConstants;
import org.openhab.binding.unifiprotect.internal.UniFiProtectLcdMessage;
import org.openhab.binding.unifiprotect.internal.UniFiProtectLcdMessage.LcdMessageType;
import org.openhab.binding.unifiprotect.internal.UniFiProtectUtil;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectG4DoorbellChannel;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvr;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectG4DoorbellThingHandler}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectG4DoorbellThingHandler extends UniFiProtectG4CameraThingHandler {

    private static final String PROPERTY_CUSTOM_LCD_TEXT = "CUSTOM_LCD_TEXT";
    protected UniFiProtectG4DoorbellThingConfig config = new UniFiProtectG4DoorbellThingConfig();
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectG4DoorbellThingHandler.class);
    private volatile boolean isRinging = false;

    public UniFiProtectG4DoorbellThingHandler(Thing thing) {
        super(thing);
    }

    @Override
    protected synchronized @Nullable UniFiProtectCamera getCamera(UniFiProtectNvr nvr) {
        return nvr.getCamera(config);
    }

    @Override
    public void refresh() {
        super.refresh();
        refreshG4Doorbell();
    }

    @Override
    protected void refreshChannel(UniFiProtectCamera camera, ChannelUID channelUID, UniFiProtectNvr controller) {
        super.refreshChannel(camera, channelUID, controller);
        logger.debug("Refresh channel in doorbell");
    }

    private void refreshG4Doorbell() {
        if (getThing().getStatus() == ONLINE) {
            UniFiProtectNvr nvr = getNvr();
            if (nvr != null) {
                UniFiProtectCamera g4Doorbell = getCamera(nvr);
                logger.debug("Doorbell Refresh! {}", g4Doorbell);
                if (g4Doorbell != null) {
                    for (Channel channel : getThing().getChannels()) {
                        ChannelUID channelUID = channel.getUID();
                        refreshG4DoorbellChannel(g4Doorbell, channelUID, nvr);
                    }
                }
            }
        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        super.handleCommand(channelUID, command);
        String channelId = channelUID.getIdWithoutGroup();
        UniFiProtectG4DoorbellChannel channel = UniFiProtectG4DoorbellChannel.fromString(channelId);
        UniFiProtectCamera camera = getCamera();
        if (camera == null) {
            logger.debug("Failed to handle command since there is no camera");
            return;
        }
        switch (channel) {
            case LCD_CUSTOM:
                handleLcdCustom(camera, channelUID, command);
                break;
            case LCD_CUSTOM_TEXT:
                handleLcdCustomText(camera, channelUID, command);
                break;
            case LCD_DO_NOT_DISTURB:
                handleLcdDoNotDisturb(camera, channelUID, command);
                break;
            case LCD_LEAVE_PACKAGE:
                handleLcdLeavePackage(camera, channelUID, command);
                break;
            case UNKNOWN:
                break;
            default:
                break;
        }
    }

    private void handleLcdCustomText(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (command instanceof StringType) {
            String lcdCustomText = ((StringType) command).toString();
            storeProperty(PROPERTY_CUSTOM_LCD_TEXT, lcdCustomText);
        }
    }

    private void storeProperty(String key, String value) {
        Map<String, String> properties = editProperties();
        properties.put(key, value);
        updateProperties(properties);
    }

    private synchronized @Nullable String loadPropertyCustomLcdText() {
        return thing.getProperties().get(PROPERTY_CUSTOM_LCD_TEXT);
    }

    private synchronized void handleLcdCustom(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof OnOffType)) {
            logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }
        if (command == OnOffType.ON) {
            logger.info("Using config: {}", config);
            String customLcdText = loadPropertyCustomLcdText();
            if (customLcdText == null || customLcdText.isEmpty()) {
                customLcdText = config.getLcdCustomMessage();
                logger.info("No custom Lcd text found in in property, trying config: {}", customLcdText);
            }
            if (customLcdText == null || customLcdText.isEmpty()) {
                logger.info("No custom Lcd text found in config aborted");
                return;
            }
            setLcdMessageOn(UniFiProtectLcdMessage.LcdMessageType.CUSTOM_MESSAGE, customLcdText, camera);
        } else if (command == OnOffType.OFF) {
            setLcdMessageOff(camera);
        }
    }

    private synchronized void handleLcdLeavePackage(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof OnOffType)) {
            logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }

        if (command == OnOffType.ON) {
            setLcdMessageOn(UniFiProtectLcdMessage.LcdMessageType.LEAVE_PACKAGE_AT_DOOR, "", camera);
        } else if (command == OnOffType.OFF) {
            setLcdMessageOff(camera);
        }
    }

    @SuppressWarnings("null")
    private synchronized void handleLcdDoNotDisturb(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof OnOffType)) {
            logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }

        if (command == OnOffType.ON) {
            setLcdMessageOn(UniFiProtectLcdMessage.LcdMessageType.DO_NOT_DISTURB, "", camera);
        } else if (command == OnOffType.OFF) {
            setLcdMessageOff(camera);
        }
    }

    @SuppressWarnings("null")
    private synchronized void setLcdMessageOff(UniFiProtectCamera camera) {
        logger.info("Turning off LCD Message camera: {}, ip: {}", camera.getName(), camera.getHost());
        UniFiProtectLcdMessage message = new UniFiProtectLcdMessage("", null,
                UniFiProtectLcdMessage.LcdMessageType.RESET);
        getNvr().setLcdMessage(camera, message);
    }

    @SuppressWarnings("null")
    private synchronized void setLcdMessageOn(UniFiProtectLcdMessage.LcdMessageType type, String text,
            UniFiProtectCamera camera) {
        logger.info("Turning on LCD Message type: {} camera: {}, ip: {}", type.name(), camera.getName(),
                camera.getHost());
        UniFiProtectLcdMessage message = new UniFiProtectLcdMessage(text, null, type);
        getNvr().setLcdMessage(camera, message);
    }

    private void refreshG4DoorbellChannel(UniFiProtectCamera camera, ChannelUID channelUID, UniFiProtectNvr nvr) {
        String channelID = channelUID.getIdWithoutGroup();
        State state = UnDefType.NULL;
        logger.debug("Refresh Doorbell channel: {}", camera.getName());
        UniFiProtectG4DoorbellChannel channel = UniFiProtectG4DoorbellChannel.fromString(channelID);
        final String lcdMessageType = camera.getLcdMessageType();
        switch (channel) {
            case RING_THUMBNAIL:
                if (imageHandler.getRingThumbnail(camera) != null) {
                    logger.debug("Setting Ring thumbail on refresh to len: {} ",
                            imageHandler.getRingThumbnail(camera).getBytes().length);
                    state = imageHandler.getRingThumbnail(camera);
                }
                break;
            case IS_RINGING:
                state = OnOffType.from(isRinging);
                break;
            case LAST_RING:
                if (camera.getLastRing() != null && camera.getLastRing() > 0) {
                    state = new DateTimeType(ZonedDateTime.ofInstant(Instant.ofEpochMilli(camera.getLastRing()),
                            ZoneId.systemDefault()));
                }
                break;
            case LCD_DO_NOT_DISTURB:
                if (lcdMessageType != null) {
                    LcdMessageType type = UniFiProtectLcdMessage.LcdMessageType.parse(lcdMessageType);
                    state = type == UniFiProtectLcdMessage.LcdMessageType.DO_NOT_DISTURB ? OnOffType.ON : OnOffType.OFF;
                }
                break;
            case LCD_LEAVE_PACKAGE:
                if (lcdMessageType != null) {
                    LcdMessageType type = UniFiProtectLcdMessage.LcdMessageType.parse(lcdMessageType);
                    state = type == UniFiProtectLcdMessage.LcdMessageType.LEAVE_PACKAGE_AT_DOOR ? state = OnOffType.ON
                            : OnOffType.OFF;
                }
                break;
            case LCD_CUSTOM:
                if (lcdMessageType != null) {
                    LcdMessageType type = UniFiProtectLcdMessage.LcdMessageType.parse(lcdMessageType);
                    state = type == UniFiProtectLcdMessage.LcdMessageType.CUSTOM_MESSAGE ? state = OnOffType.ON
                            : OnOffType.OFF;
                }
                break;
            case LCD_CUSTOM_TEXT:
                if (lcdMessageType != null) {
                    String lcdCustomText = loadPropertyCustomLcdText();
                    if (!UniFiProtectUtil.isEmpty(lcdCustomText)) {
                        state = StringType.valueOf(camera.getLcdMessageText());
                    }
                }
                break;
            default:
                break;

        }
        if (state != UnDefType.NULL) {
            updateState(channelID, state);
        }
    }

    @Override
    public void initialize() {
        Bridge bridge = getBridge();
        if (bridge == null || bridge.getHandler() == null
                || !(bridge.getHandler() instanceof UniFiProtectNvrThingHandler)) {
            updateStatus(OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "You must choose a UniFiProtect NVR for this thing.");
            return;
        }
        if (bridge.getStatus() == OFFLINE) {
            updateStatus(OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE,
                    "The UniFiProtect Controller is currently offline.");
            return;
        }
        UniFiProtectG4DoorbellThingConfig config = getConfigAs(UniFiProtectG4DoorbellThingConfig.class);
        initialize(config);
    }

    @Override
    protected synchronized void initialize(UniFiProtectBaseThingConfig config) {
        if (thing.getStatus() == INITIALIZING) {
            logger.debug("Initializing the UniFiProtect Client Handler with config = {}", config);
            if (!config.isValid()) {
                updateStatus(OFFLINE, CONFIGURATION_ERROR, "You must define a MAC address.");
                return;
            }
            this.config = (UniFiProtectG4DoorbellThingConfig) config;
            updateStatus(ONLINE);
        }
    }

    public static boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return UniFiProtectBindingConstants.THING_TYPE_G4_DOORBELL.equals(thingTypeUID);
    }

    public synchronized void handleRingAddEvent(String eventId) {
        isRinging = true;
        refreshIsRinging();
        super.handleThumbnailEvent(2, eventId);
        scehduleRingToBeTurnedOff();// set to false after 5 secs
    }

    private synchronized void refreshIsRinging() {
        Channel ringChannel = getThing().getChannel(UniFiProtectG4DoorbellChannel.IS_RINGING.name());
        UniFiProtectCamera camera = getCamera();
        UniFiProtectNvr nvr = getNvr();
        if (camera != null && ringChannel != null && nvr != null) {
            refreshG4DoorbellChannel(camera, ringChannel.getUID(), nvr);
        }
    }

    private synchronized void scehduleRingToBeTurnedOff() {
        UniFiProtectCamera camera = getCamera();
        String cameraId = camera.getId();
        if (cameraId == null) {
            logger.error("Failed to ring event, camera null");
            return;
        }
        Supplier<CompletableFuture<UniFiProtectCamera>> asyncTask = () -> CompletableFuture.completedFuture(camera);
        CompletableFuture<UniFiProtectCamera> future = UniFiProtectUtil.scheduleAsync(scheduler, asyncTask, 5,
                TimeUnit.SECONDS);
        future.thenAccept(cam -> {
            isRinging = false;
            refreshIsRinging();
        });
    }
}
