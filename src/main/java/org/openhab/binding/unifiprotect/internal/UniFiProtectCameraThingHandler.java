/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
package org.openhab.binding.unifiprotect.internal;

import static org.eclipse.smarthome.core.thing.ThingStatus.*;
import static org.eclipse.smarthome.core.thing.ThingStatusDetail.CONFIGURATION_ERROR;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DateTimeType;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectCameraChannel;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectImage;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvr;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectCameraThingHandler}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectCameraThingHandler extends BaseThingHandler {

    private static final String HIGH_FPS = "highFps";
    private UniFiProtectCameraThingConfig config = new UniFiProtectCameraThingConfig();
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectCameraThingHandler.class);

    private UniFiProtectImageHandler imageHandler = new UniFiProtectImageHandler();

    public UniFiProtectCameraThingHandler(Thing thing) {
        super(thing);
    }

    protected synchronized @Nullable UniFiProtectCamera getCamera(UniFiProtectNvr nvr) {
        UniFiProtectCamera camera = nvr.getCamera(config);
        return camera == null ? null : camera;
    }

    protected synchronized @Nullable UniFiProtectCamera getCamera() {
        UniFiProtectNvr nvr = getNvr();
        return nvr == null ? null : getCamera(nvr);
    }

    public final void refresh() {
        logger.debug("Camera Refresh!");
        if (getThing().getStatus() == ONLINE) {
            UniFiProtectNvr nvr = getNvr();
            if (nvr != null) {
                UniFiProtectCamera camera = getCamera(nvr);
                logger.debug("Camera Refresh! {}", camera);
                if (camera != null) {
                    for (Channel channel : getThing().getChannels()) {
                        ChannelUID channelUID = channel.getUID();
                        refreshChannel(camera, channelUID, nvr);
                    }
                }
            }
        }
    }

    private void refreshChannel(UniFiProtectCamera camera, ChannelUID channelUID, UniFiProtectNvr controller) {
        String channelID = channelUID.getIdWithoutGroup();
        State state = UnDefType.NULL;
        logger.debug("Refresh Channel: {}", channelID);
        UniFiProtectCameraChannel channel = UniFiProtectCameraChannel.fromString(channelID);
        switch (channel) {
            case CONNECTED_SINCE:
                if (camera.getConnectedSince() != null && camera.getConnectedSince() > 0) {
                    state = new DateTimeType(ZonedDateTime.ofInstant(Instant.ofEpochMilli(camera.getConnectedSince()),
                            ZoneId.systemDefault()));
                }
                break;
            case REBOOT:
                Boolean reboot = camera.getIsRebooting();
                if (reboot != null) {
                    state = OnOffType.from(reboot.booleanValue());
                }
                break;
            case STATUS_LIGHT:
                Boolean statusLight = camera.getStatusLight();
                if (statusLight != null) {
                    state = OnOffType.from(statusLight.booleanValue());
                }
                break;
            case IR_MODE:
                String irLedMode = camera.getIrLedMode();
                if (irLedMode != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Transforming Ir Mode: {} , ordinal: {}", irLedMode,
                                UniFiProtectIrMode.parse(irLedMode).ordinal());
                    }
                    state = new DecimalType(UniFiProtectIrMode.parse(irLedMode).ordinal());
                }
                break;
            case HDR_MODE:
                Boolean hdrMode = camera.getHdrMode();
                if (hdrMode != null) {
                    state = OnOffType.from(hdrMode.booleanValue());
                }
                break;
            case HIGH_FPS_MODE:
                String videoModeValue = camera.getVideoMode();
                if (videoModeValue != null && !videoModeValue.isEmpty()) {
                    boolean videoMode = videoModeValue.equals(HIGH_FPS);
                    state = OnOffType.from(videoMode);
                }
                break;
            case RECORDING_MODE:
                String recordingModeValue = camera.getRecoringMode();
                if (recordingModeValue != null && !recordingModeValue.isEmpty()) {
                    state = new DecimalType(
                            UniFiProtectRecordingMode.valueOf(recordingModeValue.toUpperCase()).ordinal());
                }
                break;
            case SNAPSHOT_IMG:
                if (imageHandler.getSnapshot() != null) {
                    logger.debug("Setting Snapimage on refresh to len: {} ",
                            imageHandler.getSnapshot().getBytes().length);
                    state = imageHandler.getSnapshot();
                }
                break;
            case A_SNAPSHOT_IMG:
                if (imageHandler.getAnonSnapshot() != null) {
                    logger.debug("Setting image on refresh to len: {} ",
                            imageHandler.getAnonSnapshot().getBytes().length);
                    state = imageHandler.getAnonSnapshot();
                }
                break;
            case HEATMAP_IMG:
                if (imageHandler.getHeatmap() != null) {
                    logger.debug("Setting image on refresh to len: {} ", imageHandler.getHeatmap().getBytes().length);
                    state = imageHandler.getHeatmap();
                }

                break;
            case THUMBNAIL_IMG:
                if (imageHandler.getThumbnail() != null) {
                    logger.debug("Setting image on refresh to len: {} ", imageHandler.getThumbnail().getBytes().length);
                    state = imageHandler.getThumbnail();
                }
                break;
            case HOST:
                if (StringUtils.isNotBlank(camera.getHost())) {
                    state = StringType.valueOf(camera.getHost());
                }
                break;
            case IS_DARK:
                if (camera.getIsDark() != null) {
                    state = OnOffType.from(camera.getIsDark());
                }
                break;
            case IS_MIC_ENABLED:
                if (camera.getIsMicEnabled() != null) {
                    state = OnOffType.from(camera.getIsMicEnabled());
                }
                break;
            case IS_MOTION_DETECTED:
                if (camera.getIsMotionDetected() != null) {
                    state = OnOffType.from(camera.getIsMotionDetected());
                }
                break;
            case IS_RECORDING:
                if (camera.getIsRecording() != null) {
                    state = OnOffType.from(camera.getIsRecording());
                }
                break;
            case LAST_MOTION:
                if (camera.getLastMotion() != null && camera.getLastMotion() > 0) {
                    state = new DateTimeType(ZonedDateTime.ofInstant(Instant.ofEpochMilli(camera.getLastMotion()),
                            ZoneId.systemDefault()));
                }
                break;
            case LAST_SEEN:
                if (camera.getLastSeen() != null && camera.getLastSeen() > 0) {
                    state = new DateTimeType(ZonedDateTime.ofInstant(Instant.ofEpochMilli(camera.getLastSeen()),
                            ZoneId.systemDefault()));
                }
                break;
            case MAC:
                if (StringUtils.isNotBlank(camera.getMac())) {
                    state = StringType.valueOf(camera.getMac());
                }
                break;
            case MIC_VOLUME:
                if (camera.getMicVolume() != null) {
                    state = new DecimalType(camera.getMicVolume());
                }
                break;
            case NAME:
                if (StringUtils.isNotBlank(camera.getName())) {
                    state = StringType.valueOf(camera.getName());
                }
                break;
            case STATE:
                if (StringUtils.isNotBlank(camera.getState())) {
                    state = StringType.valueOf(camera.getState());
                }
                break;
            case TYPE:
                if (StringUtils.isNotBlank(camera.getType())) {
                    state = StringType.valueOf(camera.getType());
                }
                break;
            case UP_SINCE:
                if (camera.getUpSince() != null && camera.getUpSince() > 0) {
                    state = new DateTimeType(ZonedDateTime.ofInstant(Instant.ofEpochMilli(camera.getLastMotion()),
                            ZoneId.systemDefault()));
                }
                break;
            default:
                break;

        }
        if (state != UnDefType.NULL)

        {
            updateState(channelID, state);
        }
    }

    /**
     * Utility method to access the {@link UniFiProtectNvr} instance associated with this thing.
     *
     * @return
     */
    @SuppressWarnings("null")
    protected final @Nullable UniFiProtectNvr getNvr() {
        Bridge bridge = getBridge();
        if (bridge != null && bridge.getHandler() != null
                && (bridge.getHandler() instanceof UniFiProtectNvrThingHandler)) {
            return ((UniFiProtectNvrThingHandler) bridge.getHandler()).getNvr();
        }
        return null;
    }

    @Override
    public final void initialize() {
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
        UniFiProtectCameraThingConfig config = getConfigAs(UniFiProtectCameraThingConfig.class);
        initialize(config);
    }

    private synchronized void initialize(UniFiProtectCameraThingConfig config) {
        if (thing.getStatus() == INITIALIZING) {
            logger.debug("Initializing the UniFiProtect Client Handler with config = {}", config);
            if (!config.isValid()) {
                updateStatus(OFFLINE, CONFIGURATION_ERROR, "You must define a MAC address.");
                return;
            }
            this.config = config;
            updateStatus(ONLINE);

        }
    }

    public static boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return UniFiProtectBindingConstants.THING_TYPE_CAMERA.equals(thingTypeUID);

    }

    @Override
    public final void handleCommand(ChannelUID channelUID, Command command) {
        String channelId = channelUID.getIdWithoutGroup();
        UniFiProtectCameraChannel channel = UniFiProtectCameraChannel.fromString(channelId);
        UniFiProtectCamera camera = getCamera();
        if (camera == null) {
            logger.debug("Failed to handle command since there is no camera");
            return;
        }
        switch (channel) {
            case STATUS_LIGHT:
                handleStatusLightCommand(camera, channelUID, command);
                break;
            case REBOOT:
                handleReboot(camera, channelUID, command);
                break;
            case RECORDING_MODE:
                handleRecordingMode(camera, channelUID, command);
                break;
            case IR_MODE:
                handleIrMode(camera, channelUID, command);
                break;
            case HDR_MODE:
                handleHdrMode(camera, channelUID, command);
                break;
            case HIGH_FPS_MODE:
                handleHighFpsMode(camera, channelUID, command);
                break;
            case THUMBNAIL:
                handleThumbnail(camera, channelUID, command);
                break;
            case HEATMAP:
                handleHeatmap(camera, channelUID, command);
                break;
            case SNAPSHOT:
                handleSnapshot(camera, channelUID, command);
                break;
            case A_SNAPSHOT:
                handleAnonSnapshot(camera, channelUID, command);
                break;
            case CONNECTED_SINCE:
            case HOST:
            case IS_DARK:
            case IS_MIC_ENABLED:
            case IS_MOTION_DETECTED:
            case IS_RECORDING:
            case LAST_MOTION:
            case LAST_SEEN:
            case MAC:
            case MIC_VOLUME:
            case NAME:
            case STATE:
            case TYPE:
            case UP_SINCE:
                break;
            default:
                break;
        }
    }

    private synchronized void handleAnonSnapshot(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof OnOffType)) {
            logger.warn("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }
        if (command == OnOffType.ON) {
            logger.info("Getting Anon snapshot for camera: {}, ip: {}", camera.getName(), camera.getHost());
            @SuppressWarnings("null")
            UniFiProtectImage anonSnapshot = getNvr().getAnonSnapshot(camera);
            camera.setaSnapshotUrl(anonSnapshot.getFile().getAbsolutePath());
            imageHandler.setAnonSnapshot(anonSnapshot);
        }
    }

    private synchronized void handleSnapshot(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof OnOffType)) {
            logger.warn("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }
        if (command == OnOffType.ON) {
            logger.info("Getting snapshot for camera: {}, ip: {}", camera.getName(), camera.getHost());
            @SuppressWarnings("null")

            UniFiProtectImage snapshot = getNvr().getSnapshot(camera);
            if (snapshot != null) {
                camera.setSnapshotUrl(snapshot.getFile().getAbsolutePath());
                imageHandler.setSnapshot(snapshot);
            }
        }
    }

    private synchronized void handleHeatmap(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof OnOffType)) {
            logger.warn("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }
        if (command == OnOffType.ON) {
            logger.info("Getting Heatmap camera: {}, ip: {}", camera.getName(), camera.getHost());
            @SuppressWarnings("null")
            UniFiProtectImage heatmap = getNvr().getHeatmap(camera);
            if (heatmap != null) {
                camera.setHeatmapUrl(heatmap.getFile().getAbsolutePath());
                imageHandler.setHeatmap(heatmap);
            }
        }
    }

    private synchronized void handleThumbnail(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof OnOffType)) {
            logger.warn("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }

        if (command == OnOffType.ON) {
            logger.info("Getting Thumbnail camera: {}, ip: {}", camera.getName(), camera.getHost());
            @SuppressWarnings("null")
            UniFiProtectImage thumbnail = getNvr().getThumbnail(camera);
            if (thumbnail != null) {
                camera.setThumbnailUrl(thumbnail.getFile().getAbsolutePath());
                imageHandler.setThumbnail(thumbnail);
            }
        }

    }

    @SuppressWarnings("null")
    private synchronized void handleHighFpsMode(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof OnOffType)) {
            logger.warn("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }

        logger.info("Setting High FPS Mode: {}  camera: {}, ip: {}", command == OnOffType.ON, camera.getName(),
                camera.getHost());
        getNvr().turnOnOrOffHighFpsMode(camera, command == OnOffType.ON);

    }

    @SuppressWarnings("null")
    private synchronized void handleHdrMode(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof OnOffType)) {
            logger.warn("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }

        logger.info("Setting HDR Mode: {}  camera: {}, ip: {}", command == OnOffType.ON, camera.getName(),
                camera.getHost());
        getNvr().turnOnOrOffHdrMode(camera, command == OnOffType.ON);

    }

    @SuppressWarnings("null")
    private synchronized void handleIrMode(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof DecimalType)) {
            logger.warn("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }
        DecimalType irMode = (DecimalType) command;
        long value = irMode.longValue();
        logger.info("Setting IR mode = {} camera: {}, ip: {}", value, camera.getName(), camera.getHost());
        getNvr().setIrMode(camera, UniFiProtectIrMode.create(value));

    }

    @SuppressWarnings("null")
    private void handleRecordingMode(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        synchronized (this) {
            if (!(command instanceof DecimalType)) {
                logger.warn("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                        command, channelUID);
                return;
            }
            DecimalType recordingMode = (DecimalType) command;
            long value = recordingMode.longValue();
            logger.info("Setting Recording mode = {} camera: {}, ip: {}", value, camera.getName(), camera.getHost());
            getNvr().setRecordingMode(camera, UniFiProtectRecordingMode.create(value));
        }
    }

    @SuppressWarnings("null")
    private void handleReboot(@Nullable UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        synchronized (this) {
            if (!(command instanceof OnOffType)) {
                logger.warn("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                        command, channelUID);
                return;
            }

            if (command == OnOffType.ON) {
                logger.info("Rebooting Camera = {} camera: {}, ip: {}", command == OnOffType.ON, camera.getName(),
                        camera.getHost());
                getNvr().rebootCamera(camera);
            }
        }
    }

    @SuppressWarnings("null")
    private void handleStatusLightCommand(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        synchronized (this) {
            if (!(command instanceof OnOffType)) {
                logger.warn("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                        command, channelUID);
                return;
            }

            logger.info("Setting status light = {} camera: {}, ip: {}", command == OnOffType.ON, camera.getName(),
                    camera.getHost());
            getNvr().setStatusLightOn(camera, command == OnOffType.ON);
        }
    }
}