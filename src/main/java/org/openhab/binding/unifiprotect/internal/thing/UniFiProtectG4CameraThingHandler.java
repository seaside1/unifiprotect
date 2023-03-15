/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unifiprotect.internal.UniFiProtectBindingConstants;
import org.openhab.binding.unifiprotect.internal.UniFiProtectSmartDetectTypes;
import org.openhab.binding.unifiprotect.internal.UniFiProtectUtil;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectG4Channel;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvr;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectEvent;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
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
 * The {@link UniFiProtectG4CameraThingHandler}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectG4CameraThingHandler extends UniFiProtectBaseThingHandler {

    private static final int THUMBNAIL_WAIT_TIME = 2;
    protected UniFiProtectG4CameraThingConfig config = new UniFiProtectG4CameraThingConfig();
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectG4CameraThingHandler.class);
    private volatile boolean isSmartMotionDetected;
    private @Nullable volatile Long smartDetectScore = null;
    private @Nullable volatile Instant smartDetectLast = null;
    private @Nullable volatile String smartDetectType = null;

    public UniFiProtectG4CameraThingHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void refresh() {
        super.refresh();
        refresG4Camera();
    }

    @Override
    protected synchronized @Nullable UniFiProtectCamera getCamera(UniFiProtectNvr nvr) {
        return nvr.getCamera(config);
    }

    @Override
    protected void refreshChannel(UniFiProtectCamera camera, ChannelUID channelUID, UniFiProtectNvr controller) {
        super.refreshChannel(camera, channelUID, controller);
        logger.debug("Refresh channel in g4camra");
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        super.handleCommand(channelUID, command);
        String channelId = channelUID.getIdWithoutGroup();
        UniFiProtectG4Channel channel = UniFiProtectG4Channel.fromString(channelId);
        UniFiProtectCamera camera = getCamera();
        if (camera == null) {
            logger.debug("Failed to handle command since there is no camera");
            return;
        }
        switch (channel) {
            case SMART_DETECT_PERSON:
                handleSmartDetectPerson(camera, channelUID, command);
                break;
            case SMART_DETECT_VEHICLE:
                handleSmartDetectVehicle(camera, channelUID, command);
                break;
            case UNKNOWN:
                break;
            default:
                break;
        }
    }

    private synchronized void handleSmartDetectPerson(UniFiProtectCamera camera, ChannelUID channelUID,
            Command command) {
        if (!(command instanceof OnOffType)) {
            logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }
        UniFiProtectSmartDetectTypes type = camera.getSmartDetectObjectTypes();
        UniFiProtectSmartDetectTypes newType = null;
        switch (type) {
            case PERSON:
            case EMPTY:
                if (command == OnOffType.ON) {
                    newType = UniFiProtectSmartDetectTypes.PERSON;
                } else if (command == OnOffType.OFF) {
                    newType = UniFiProtectSmartDetectTypes.EMPTY;
                } else {
                    newType = UniFiProtectSmartDetectTypes.UNDEF;
                }
                break;
            case PERSON_AND_VEHICLE:
            case VEHICLE:
                if (command == OnOffType.ON) {
                    newType = UniFiProtectSmartDetectTypes.PERSON_AND_VEHICLE;
                } else if (command == OnOffType.OFF) {
                    newType = UniFiProtectSmartDetectTypes.VEHICLE;
                } else {
                    newType = UniFiProtectSmartDetectTypes.UNDEF;
                }
                break;
            case UNDEF:
                logger.error("Invalid type when trying to activate smart function");
                break;
            default:
                break;
        }
        if (newType == null) {
            logger.error("Failed to get correct type, ignoring command");
            return;
        }
        sendSmartDetectMessage(newType, camera);
    }

    private synchronized void handleSmartDetectVehicle(UniFiProtectCamera camera, ChannelUID channelUID,
            Command command) {
        if (!(command instanceof OnOffType)) {
            logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }
        UniFiProtectSmartDetectTypes type = camera.getSmartDetectObjectTypes();
        UniFiProtectSmartDetectTypes newType = null;
        switch (type) {
            case EMPTY:
            case VEHICLE:
                if (command == OnOffType.ON) {
                    newType = UniFiProtectSmartDetectTypes.VEHICLE;
                } else if (command == OnOffType.OFF) {
                    newType = UniFiProtectSmartDetectTypes.EMPTY;
                } else {
                    newType = UniFiProtectSmartDetectTypes.UNDEF;
                }
                break;
            case PERSON:
            case PERSON_AND_VEHICLE:
                if (command == OnOffType.ON) {
                    newType = UniFiProtectSmartDetectTypes.PERSON_AND_VEHICLE;
                } else if (command == OnOffType.OFF) {
                    newType = UniFiProtectSmartDetectTypes.PERSON;
                } else {
                    newType = UniFiProtectSmartDetectTypes.UNDEF;
                }
                break;
            case UNDEF:
                logger.error("Invalid type when trying to activate smart function");
                break;
            default:
                break;
        }
        if (newType == null) {
            logger.error("Failed to get correct type, ignoring command");
            return;
        }
        sendSmartDetectMessage(newType, camera);
    }

    protected synchronized void sendSmartDetectMessage(UniFiProtectSmartDetectTypes newType,
            UniFiProtectCamera camera) {
        logger.info("Sending turn on/off SmatDetect Settings: {} camera: {}, ip: {}", newType.name(), camera.getName(),
                camera.getHost());
        getNvr().setSmartDetectTypes(camera, newType);
    }

    private void refresG4Camera() {
        if (getThing().getStatus() == ONLINE) {
            UniFiProtectNvr nvr = getNvr();
            if (nvr != null) {
                UniFiProtectCamera camera = getCamera(nvr);
                logger.debug("G4Camera Refresh! {}", camera);
                if (camera != null) {
                    for (Channel channel : getThing().getChannels()) {
                        ChannelUID channelUID = channel.getUID();
                        refreshG4CameraChannel(camera, channelUID, nvr);
                    }
                }
            }
        }
    }

    private void refreshG4CameraChannel(UniFiProtectCamera camera, ChannelUID channelUID, UniFiProtectNvr nvr) {
        String channelID = channelUID.getIdWithoutGroup();
        State state = UnDefType.NULL;
        logger.debug("Refresh G4 channel: {}", camera.getName());
        UniFiProtectG4Channel channel = UniFiProtectG4Channel.fromString(channelID);
        UniFiProtectSmartDetectTypes detectTypes = camera.getSmartDetectObjectTypes();
        switch (channel) {
            case SMART_DETECT_TYPE:
                if (smartDetectType != null) {
                    state = StringType.valueOf(smartDetectType);
                }
                break;
            case SMART_DETECT_LAST:
                if (smartDetectLast != null) {
                    Instant detectedLast = smartDetectLast;
                    // Ugly workaround for Nonull in interface
                    if (detectedLast != null) {
                        state = new DateTimeType(ZonedDateTime.ofInstant(detectedLast, ZoneId.systemDefault()));
                    }
                }
                break;
            case SMART_DETECT_SCORE:
                final long score = (smartDetectScore != null) ? smartDetectScore : -1;
                if (score > -1) {
                    state = new DecimalType(score);
                }
                break;
            case SMART_DETECT_PERSON:
                state = OnOffType.from(detectTypes.containsPerson());
                break;
            case SMART_DETECT_VEHICLE:
                state = OnOffType.from(detectTypes.containsVehicle());
                break;
            case SMART_DETECT_PACKAGE:
                state = OnOffType.from(detectTypes.containsPackage());
                break;
            case SMART_DETECT_MOTION:
                state = OnOffType.from(isSmartMotionDetected);
                break;
            case SMART_DETECT_THUMBNAIL:
                if (imageHandler.getSmartDetectThumbnail(camera) != null) {
                    logger.debug("Setting SmartDetect thumbail on refresh to len: {} ",
                            imageHandler.getSmartDetectThumbnail(camera).getBytes().length);
                    state = imageHandler.getSmartDetectThumbnail(camera);
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
        UniFiProtectG4CameraThingConfig config = getConfigAs(UniFiProtectG4CameraThingConfig.class);
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
            this.config = (UniFiProtectG4CameraThingConfig) config;
            updateStatus(ONLINE);
        }
    }

    public static boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return UniFiProtectBindingConstants.THING_TYPE_G4_CAMERA.equals(thingTypeUID);
    }

    public synchronized void handleSmartDetectAddEvent(String eventId) {
        isSmartMotionDetected = true;
        refreshSmartDetection();
        scehduleSmatDetectionToBeTurnedOff();
        smartDetectLast = Instant.now();
    }

    private synchronized void refreshSmartDetection() {
        Channel smartDetectionChannel = getThing().getChannel(UniFiProtectG4Channel.SMART_DETECT_MOTION.toChannelId());
        UniFiProtectCamera camera = getCamera();
        UniFiProtectNvr nvr = getNvr();
        if (camera != null && smartDetectionChannel != null && nvr != null) {
            refreshG4CameraChannel(camera, smartDetectionChannel.getUID(), nvr);
        }
    }

    private synchronized void scehduleSmatDetectionToBeTurnedOff() {
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
            isSmartMotionDetected = false;
            refreshSmartDetection();
        });
    }

    public synchronized void handleSmartDetectUpdEvent(String eventId) {
        UniFiProtectEvent event = getNvr().getEventFromId(eventId);
        if (event != null) {
            smartDetectScore = event.getScore();
            if (event.getSmartDetectTypes() != null) {
                // Ugly Workaround for multiple type of detections
                smartDetectType = event.getSmartDetectTypes() != null && event.getSmartDetectTypes().length > 0
                        ? event.getSmartDetectTypes()[0]
                        : UniFiProtectBindingConstants.EMPTY_STRING;
            }
        } else {
            logger.warn("Failed to find eventId: {} Events in cache:", eventId);
        }
        super.handleThumbnailEvent(THUMBNAIL_WAIT_TIME, eventId);
        refresh();
    }
}
