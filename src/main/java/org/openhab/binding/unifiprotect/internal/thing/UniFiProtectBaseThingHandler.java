/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unifiprotect.internal.UniFiProtectBindingConstants;
import org.openhab.binding.unifiprotect.internal.UniFiProtectIrMode;
import org.openhab.binding.unifiprotect.internal.UniFiProtectRecordingMode;
import org.openhab.binding.unifiprotect.internal.UniFiProtectUtil;
import org.openhab.binding.unifiprotect.internal.image.UniFiProtectImageHandler;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectCameraChannel;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectImage;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvr;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectEvent;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.openhab.core.common.ThreadPoolManager;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Channel;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectBaseThingHandler}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectBaseThingHandler extends BaseThingHandler {

    private static final String HIGH_FPS = "highFps";
    private static final String HEAT_DL = "HEAT_DL";
    private static final String THMB_DL = "THMB_DL";
    private static final long REFRESH_DELAY = 1L;
    protected UniFiProtectBaseThingConfig cameraConfig = new UniFiProtectBaseThingConfig();
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectBaseThingHandler.class);
    private volatile boolean motionDetected = false;
    private @Nullable CompletableFuture<Void> heatMapDelayDlFuture = null;
    private @Nullable CompletableFuture<Void> thumbnailDelayDlFuture = null;

    protected UniFiProtectImageHandler imageHandler = new UniFiProtectImageHandler();

    protected final ScheduledExecutorService scheduler = ThreadPoolManager
            .getScheduledPool(ThreadPoolManager.THREAD_POOL_NAME_COMMON);
    protected final Map<String, CompletableFuture<UniFiProtectCamera>> futures = new HashMap<String, CompletableFuture<UniFiProtectCamera>>();

    public UniFiProtectBaseThingHandler(Thing thing) {
        super(thing);
    }

    protected synchronized @Nullable UniFiProtectCamera getCamera(UniFiProtectNvr nvr) {
        return nvr.getCamera(cameraConfig);
    }

    protected synchronized @Nullable UniFiProtectCamera getCamera() {
        UniFiProtectNvr nvr = getNvr();
        return nvr == null ? null : getCamera(nvr);
    }

    public void refresh() {
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

    protected void refreshChannel(UniFiProtectCamera camera, ChannelUID channelUID, UniFiProtectNvr controller) {
        String channelID = channelUID.getIdWithoutGroup();
        State state = UnDefType.NULL;
        logger.debug("Refresh channel: {}", camera.getName());
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
            case MOTION_DETECTION:
                Boolean motionDetection = camera.getMotionDetection();
                if (motionDetection != null) {
                    state = OnOffType.from(motionDetection.booleanValue());
                }
                break;
            case SNAPSHOT_IMG:
                if (imageHandler.getSnapshot(camera) != null) {
                    logger.debug("Setting Snapimage on refresh to len: {} ",
                            imageHandler.getSnapshot(camera).getBytes().length);
                    state = imageHandler.getSnapshot(camera);
                }
                break;
            case A_SNAPSHOT_IMG:
                if (imageHandler.getAnonSnapshot(camera) != null) {
                    logger.debug("Setting image on refresh to len: {} ",
                            imageHandler.getAnonSnapshot(camera).getBytes().length);
                    state = imageHandler.getAnonSnapshot(camera);
                }
                break;
            case MOTION_THUMBNAIL:
                if (imageHandler.getMotionThumbnail(camera) != null) {
                    logger.debug("Setting thumbail on refresh to len: {} ",
                            imageHandler.getMotionThumbnail(camera).getBytes().length);
                    state = imageHandler.getMotionThumbnail(camera);
                }
                break;
            case MOTION_HEATMAP:
                if (imageHandler.getMotionHeatmap(camera) != null) {
                    logger.debug("Setting heatmap on refresh to len: {} ",
                            imageHandler.getMotionHeatmap(camera).getBytes().length);
                    state = imageHandler.getMotionHeatmap(camera);
                }
                break;
            case MOTION_SCORE:
                final String id = camera.getId();
                final UniFiProtectEvent event = getNvr().getLastMotionEventFromCamera(camera);
                if (event != null) {
                    final Long score = event.getScore();
                    if (score != null) {
                        state = new DecimalType(score);
                    }
                }
                break;
            case HOST:
                if (!UniFiProtectUtil.isEmpty(camera.getHost())) {
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
                state = OnOffType.from(motionDetected);
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
                if (!UniFiProtectUtil.isEmpty(camera.getMac())) {
                    state = StringType.valueOf(camera.getMac());
                }
                break;
            case MIC_VOLUME:
                Integer micVolume = camera.getMicVolume();
                if (micVolume != null) {
                    state = new DecimalType(micVolume);
                }
                break;
            case NAME:
                if (!UniFiProtectUtil.isEmpty(camera.getName())) {
                    state = StringType.valueOf(camera.getName());
                }
                break;
            case STATE:
                if (!UniFiProtectUtil.isEmpty(camera.getState())) {
                    state = StringType.valueOf(camera.getState());
                }
                break;
            case TYPE:
                if (!UniFiProtectUtil.isEmpty(camera.getType())) {
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
        if (state != UnDefType.NULL) {
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
        UniFiProtectBaseThingConfig config = getConfigAs(UniFiProtectBaseThingConfig.class);
        initialize(config);
    }

    protected void initialize(UniFiProtectBaseThingConfig config) {
        if (thing.getStatus() == INITIALIZING) {
            logger.debug("Initializing the UniFiProtect Client Handler with config = {}", config);
            if (!config.isValid()) {
                updateStatus(OFFLINE, CONFIGURATION_ERROR, "You must define a MAC address.");
                return;
            }
            this.cameraConfig = config;
            updateStatus(ONLINE);

        }
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
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
            case SNAPSHOT:
                handleSnapshot(camera, channelUID, command);
                break;
            case A_SNAPSHOT:
                handleAnonSnapshot(camera, channelUID, command);
                break;
            case MOTION_DETECTION:
                handleMotionDetection(camera, channelUID, command);
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
            logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }
        if (command == OnOffType.ON) {
            logger.info("Getting Anon snapshot for camera: {}, ip: {}", camera.getName(), camera.getHost());
            UniFiProtectImage anonSnapshot = getNvr().getAnonSnapshot(camera);
            if (anonSnapshot != null) {
                camera.setaSnapshotUrl(anonSnapshot.getFile().getAbsolutePath());
                imageHandler.setAnonSnapshot(camera, anonSnapshot);
                refresh();
            }
        }
    }

    private synchronized void handleSnapshot(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof OnOffType)) {
            logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }
        if (command == OnOffType.ON) {
            logger.info("Getting snapshot for camera: {}, ip: {}", camera.getName(), camera.getHost());
            @SuppressWarnings("null")

            UniFiProtectImage snapshot = getNvr().getSnapshot(camera);
            if (snapshot != null) {
                camera.setSnapshotUrl(snapshot.getFile().getAbsolutePath());
                imageHandler.setSnapshot(camera, snapshot);
            }
            refresh();
        }
    }

    private synchronized void handleHeatmap(UniFiProtectCamera camera, UniFiProtectEvent event) {
        final UniFiProtectImage heatmap = getNvr().getHeatmap(camera, event);
        final String type = event.getType();
        if (type == null || !type.equals(UniFiProtectBindingConstants.EVENT_TYPE_MOTION)) {
            return;
        }
        if (heatmap == null) { // retry
            heatMapDelayDlFuture = UniFiProtectUtil.delayedExecution(REFRESH_DELAY, TimeUnit.SECONDS);
            heatMapDelayDlFuture.thenAccept(s -> {
                try {
                    final UniFiProtectImage heatmap2 = getNvr().getHeatmap(camera, event);
                    if (heatmap2 == null) {
                        logger.error("Failed to set heatmap, event type not present: {} or invalid heatmap: {}", event,
                                heatmap);
                        return;
                    }
                    setResultingHeatmap(heatmap2, camera);
                } finally {
                    heatMapDelayDlFuture = null;
                }
            });
        } else {
            setResultingHeatmap(heatmap, camera);
        }
    }

    private synchronized void setResultingHeatmap(UniFiProtectImage heatmap, UniFiProtectCamera camera) {
        camera.setMotionHeatmapUrl(heatmap.getFile().getAbsolutePath());
        imageHandler.setMotionHeatmap(camera, heatmap);
    }

    private synchronized void handleThumbnail(UniFiProtectCamera camera, UniFiProtectEvent event) {
        final UniFiProtectImage thumbnail = getNvr().getThumbnail(camera, event);
        final String type = event.getType();
        if (type == null) {
            logger.error("Failed to write thumbnail, event type not present: {}", event);
            return;
        }
        if (thumbnail != null) {
            setThumbnail(thumbnail, type, camera);
        } else {
            thumbnailDelayDlFuture = UniFiProtectUtil.delayedExecution(REFRESH_DELAY, TimeUnit.SECONDS);
            thumbnailDelayDlFuture.thenAccept(s -> {
                try {
                    final UniFiProtectImage thumbnail2 = getNvr().getThumbnail(camera, event);
                    if (thumbnail2 == null) {
                        logger.error("Failed to set thumbnail, event type not present: {} or invalid heatmap: {}",
                                event, thumbnail2);
                        return;
                    }
                    setThumbnail(thumbnail2, type, camera);
                } finally {
                    thumbnailDelayDlFuture = null;
                }
            });
        }
    }

    private synchronized void setThumbnail(UniFiProtectImage thumbnail, String type, UniFiProtectCamera camera) {
        if (type.equals(UniFiProtectBindingConstants.EVENT_TYPE_MOTION)) {
            camera.setMotionThumbnailUrl(thumbnail.getFile().getAbsolutePath());
            imageHandler.setMotionThumbnail(camera, thumbnail);
        } else if (type.equals(UniFiProtectBindingConstants.EVENT_TYPE_RING)) {
            camera.setRingThumbnailUrl(thumbnail.getFile().getAbsolutePath());
            imageHandler.setRingThumbnail(camera, thumbnail);
        } else if (type.equals(UniFiProtectBindingConstants.EVENT_TYPE_SMART_DETECT_ZONE)) {
            camera.setSmartDetectThumbnailUrl(thumbnail.getFile().getAbsolutePath());
            imageHandler.setSmartDetectThumbnail(camera, thumbnail);
        }
    }

    public synchronized void handleEventDownload(String type, String eventId, UniFiProtectCamera cam) {
        UniFiProtectEvent event = getNvr().getEventFromId(eventId);
        if (event == null) {
            getNvr().refreshEvents();
            event = getNvr().getEventFromId(eventId);
            if (event == null) {
                logger.warn("Failed to find event fo eventId: {} cacheSize: {}", eventId, getNvr().getEvents().size());
                getNvr().getEvents().forEach(ev -> logger.debug("Event in cache: {}", ev.getId()));
                return;
            }
        }
        logger.debug("Handling event Download by getting event for camera: {} eventid: {} event: {}", cam.getName(),
                event.getId(), event);
        if (type.equals(HEAT_DL)) {
            handleHeatmap(cam, event);
        } else if (type.equals(THMB_DL)) {
            handleThumbnail(cam, event);
        }
        futures.remove(cam.getId() + type);
    }

    public synchronized void handleHeatmapEvent(int delay, String eventId) {
        UniFiProtectCamera camera = getCamera();
        String cameraId = camera.getId();
        if (cameraId == null) {
            logger.error("Failed to handle event, camera null");
            return;
        }
        logger.debug("Scehduling completable future for camera: {} delay: {}", camera.getName(), delay);
        CompletableFuture<UniFiProtectCamera> future = futures.get(cameraId + eventId + HEAT_DL);
        if (future == null || future.isDone()) {
            Supplier<CompletableFuture<UniFiProtectCamera>> asyncTask = () -> CompletableFuture.completedFuture(camera);
            future = UniFiProtectUtil.scheduleAsync(scheduler, asyncTask, delay, TimeUnit.SECONDS);
            future.thenAccept(cam -> {
                getNvr().refreshEvents();
                handleEventDownload(HEAT_DL, eventId, camera);
            });
            futures.put(cameraId + eventId + HEAT_DL, future);
        }
    }

    public synchronized void handleThumbnailEvent(int delay, String eventId) {
        UniFiProtectCamera camera = getCamera();
        String cameraId = camera.getId();
        if (cameraId == null) {
            logger.error("Failed to handle event, camera null");
            return;
        }
        logger.debug("Scehduling completable future for camera: {} delay: {}", camera.getName(), delay);
        CompletableFuture<UniFiProtectCamera> future = futures.get(cameraId + eventId + THMB_DL);
        if (future == null || future.isDone()) {
            Supplier<CompletableFuture<UniFiProtectCamera>> asyncTask = () -> CompletableFuture.completedFuture(camera);
            future = UniFiProtectUtil.scheduleAsync(scheduler, asyncTask, delay, TimeUnit.SECONDS);
            future.thenAccept(cam -> {
                handleEventDownload(THMB_DL, eventId, camera);
            });
            futures.put(cameraId + eventId + THMB_DL, future);
        }
    }

    @SuppressWarnings("null")
    private synchronized void handleHighFpsMode(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof OnOffType)) {
            logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
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
            logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }

        logger.info("Setting HDR Mode: {}  camera: {}, ip: {}", command == OnOffType.ON, camera.getName(),
                camera.getHost());
        getNvr().turnOnOrOffHdrMode(camera, command == OnOffType.ON);
    }

    @SuppressWarnings("null")
    private synchronized void handleMotionDetection(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof OnOffType)) {
            logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                    command, channelUID);
            return;
        }

        logger.info("Setting Motion Detection: {}  camera: {}, ip: {}", command == OnOffType.ON, camera.getName(),
                camera.getHost());
        getNvr().turnOnOrOffMotionDetection(camera, command == OnOffType.ON);
    }

    @SuppressWarnings("null")
    private synchronized void handleIrMode(UniFiProtectCamera camera, ChannelUID channelUID, Command command) {
        if (!(command instanceof DecimalType)) {
            logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
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
                logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
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
                logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
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
                logger.debug("Ignoring unsupported command = {} for channel = {} - valid commands types are: OnOffType",
                        command, channelUID);
                return;
            }

            logger.info("Setting status light = {} camera: {}, ip: {}", command == OnOffType.ON, camera.getName(),
                    camera.getHost());
            getNvr().setStatusLightOn(camera, command == OnOffType.ON);
        }
    }

    private synchronized void scehduleMotionEventToBeTurnedOff() {
        UniFiProtectCamera camera = getCamera();
        String cameraId = camera.getId();
        if (cameraId == null) {
            logger.error("Failed schedule motion  event off, camera null");
            return;
        }
        Supplier<CompletableFuture<UniFiProtectCamera>> asyncTask = () -> CompletableFuture.completedFuture(camera);
        CompletableFuture<UniFiProtectCamera> future = UniFiProtectUtil.scheduleAsync(scheduler, asyncTask, 5,
                TimeUnit.SECONDS);
        future.thenAccept(cam -> {
            motionDetected = false;
            refreshIsMotionDetected();
        });
    }

    private synchronized void refreshIsMotionDetected() {
        Channel motionDetectionChannel = getThing()
                .getChannel(UniFiProtectCameraChannel.IS_MOTION_DETECTED.toChannelId());
        UniFiProtectCamera camera = getCamera();
        UniFiProtectNvr nvr = getNvr();
        if (camera != null && motionDetectionChannel != null && nvr != null) {
            refreshChannel(camera, motionDetectionChannel.getUID(), nvr);
        } else {
            logger.error("Failed to refresh due to null element refreshChannel: {}",
                    UniFiProtectCameraChannel.IS_MOTION_DETECTED.toChannelId());
        }
    }

    public synchronized void handleMotionAddEvent(String eventId) {
        logger.debug("Motion detected camId: {} name: {}", getCamera().getId(), getCamera().getName());
        motionDetected = true;
        refreshIsMotionDetected();
        scehduleMotionEventToBeTurnedOff();
    }
}
