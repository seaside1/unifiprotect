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
package org.openhab.binding.unifiprotect.internal.model;

import java.io.File;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.openhab.binding.unifiprotect.internal.UniFiProtectCameraThingConfig;
import org.openhab.binding.unifiprotect.internal.UniFiProtectImageHandler;
import org.openhab.binding.unifiprotect.internal.UniFiProtectIrMode;
import org.openhab.binding.unifiprotect.internal.UniFiProtectNvrThingConfig;
import org.openhab.binding.unifiprotect.internal.UniFiProtectRecordingMode;
import org.openhab.binding.unifiprotect.internal.UniFiProtectUtil;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectStatus.SendStatus;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectCameraInstanceCreator;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectEvent;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectJsonParser;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectNvrInstanceCreator;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectAlertsRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectAnonymousSnapshotRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectBootstrapRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectEventsRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectHdrModeRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectHeatmapRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectHighFpsModeRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectIrModeRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectLoginRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectRebootCameraRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectRecordingModeRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectSnapshotRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectStatusLightRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectThumbnailRequest;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectTokenRequest;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectNvrDevice;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectNvrUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * The {@link UniFiProtectNvr}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectNvr {

    private static final int IMAGE_MIN_SIZE = 800;
    private volatile String token = "";
    private @Nullable UniFiProtectNvrDevice nvrDevice;
    private @Nullable UniFiProtectNvrUser nvrUser;

    private UniFiProtectCameraCache cameraInsightCache = new UniFiProtectCameraCache();
    private final HttpClient httpClient;
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectNvr.class);
    private final Gson gson;
    private final UniFiProtectNvrThingConfig config;

    private UniFiProtectCameraInstanceCreator uniFiProtectCameraInstanceCreator;
    private UniFiProtectNvrInstanceCreator uniFiProtectNvrInstanceCreator;

    public UniFiProtectNvr(UniFiProtectNvrThingConfig config) {
        this.config = config;
        httpClient = new HttpClient(new SslContextFactory(true));
        httpClient.setFollowRedirects(false);
        uniFiProtectCameraInstanceCreator = new UniFiProtectCameraInstanceCreator();
        uniFiProtectNvrInstanceCreator = new UniFiProtectNvrInstanceCreator();
        gson = new GsonBuilder().registerTypeAdapter(UniFiProtectNvrDevice.class, uniFiProtectNvrInstanceCreator)
                .registerTypeAdapter(UniFiProtectCamera.class, uniFiProtectCameraInstanceCreator)
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
    }

    public boolean init() {
        try {
            httpClient.start();
            logger.info("Initializing the binding, with config: {}", getConfig());
        } catch (Exception e) {
            logger.error("Failed to start binding: ", e);
            return false;
        }
        return true;

    }

    public synchronized UniFiProtectStatus login() {
        UniFiProtectTokenRequest tokenRequest = new UniFiProtectTokenRequest(httpClient, config);
        UniFiProtectStatus sendStatus = tokenRequest.sendRequest();
        if (!requestSuccessFullySent(sendStatus)) {
            return sendStatus;
        }
        token = tokenRequest.getToken();
        if (token.isEmpty()) {
            return UniFiProtectStatus.STATUS_TOKEN_MISSING;
        }
        UniFiProtectLoginRequest loginRequest = new UniFiProtectLoginRequest(token, httpClient, getConfig());
        sendStatus = loginRequest.sendRequest();
        if (!requestSuccessFullySent(sendStatus)) {
            return sendStatus;
        }
        return sendStatus;
    }

    @SuppressWarnings("null")
    public synchronized UniFiProtectStatus refreshProtect() {
        logger.debug("Refresh Protect fetch cameras");
        UniFiProtectStatus status = null;
        if (!isLoggedIn()) {
            status = login();
        }
        if (status != null && status.getStatus() != SendStatus.SUCCESS) {
            logger.error("Failed to updated Cameras since we can't seem to login");
            return status;
        }
        UniFiProtectBootstrapRequest request = new UniFiProtectBootstrapRequest(httpClient, getConfig(), token);
        UniFiProtectStatus bootStrapRequestStatus = request.sendRequest();
        if (!requestSuccessFullySent(bootStrapRequestStatus)) {
            if (request.creditialsExpired()) {
                logger.debug("Credentials expired, logging in again");
                status = login();
                if (status.getStatus() == SendStatus.SUCCESS) {
                    request = new UniFiProtectBootstrapRequest(httpClient, getConfig(), token);
                    bootStrapRequestStatus = request.sendRequest();
                } else {
                    return bootStrapRequestStatus;
                }
            }
        }
        logger.debug("Request is ok, parsing cameras");
        String jsonContent = request.getJsonContent();
        JsonObject jsonObject = UniFiProtectJsonParser.parseJson(gson, jsonContent);
        UniFiProtectCamera[] cameras = UniFiProtectJsonParser.getCamerasFromJson(gson, jsonObject);
        Arrays.stream(cameras).forEach(camera -> logger.debug("Camera: {}", camera));
        logger.debug("Got Cameras size: {}", cameras.length);
        getCameraInsightCache().clear();
        getCameraInsightCache().putAll(Arrays.asList(cameras));
        logger.debug("Put all size: {}", getCameraInsightCache().getCameras().size());
        nvrDevice = UniFiProtectJsonParser.getNvrDeviceFromJson(gson, jsonObject);
        logger.debug("UniFiProtectNvrDevice: {}", getNvrDevice());
        UniFiProtectNvrUser[] nvrUsersFromJson = UniFiProtectJsonParser.getNvrUsersFromJson(gson, jsonObject);
        Optional<@Nullable UniFiProtectNvrUser> findAny = Arrays.stream(nvrUsersFromJson)
                .filter(u -> (u.getLocalUsername() != null && u.getLocalUsername().equals(getConfig().getUserName()))
                        || u.getFirstName() != null && u.getFirstName().toLowerCase().equals(getConfig().getUserName()))
                .findAny();
        try {
            nvrUser = findAny.get();
        } catch (NoSuchElementException x) {
            logger.error("Could not find any valid user. Looking for: {}", getConfig().getUserName());
            Arrays.stream(nvrUsersFromJson).forEach(user -> logger.debug("User in response: {}", user));
            logger.debug("Json response: {}", jsonContent);
        }
        if (nvrUser != null && (nvrUser.getLocalUsername() == null || nvrUser.getLocalUsername().isEmpty())) {
            nvrUser.setLocalUsername(nvrUser.getFirstName().toLowerCase()); // Ugly workaround for localusername being
                                                                            // null in response
        }
        logger.debug("UniFiProtectNvrUser: {}", getNvrUser());
        logger.debug("Login Token Success: {}", token);
        return bootStrapRequestStatus;
    }

    private boolean isLoggedIn() {
        return token != null && !token.isEmpty();
    }

    public synchronized UniFiProtectCameraCache getCameraInsightCache() {
        return cameraInsightCache;
    }

    public UniFiProtectStatus start() {
        synchronized (this) {
            return login();
        }
    }

    public synchronized @Nullable UniFiProtectCamera getCamera(UniFiProtectCameraThingConfig config) {
        logger.debug("Getting camera from cache configMac: {}", config.getMac());
        logger.debug("InsightCache: {}", cameraInsightCache.toString());
        return cameraInsightCache.getCamera(config.getMac());
    }

    public @Nullable UniFiProtectNvrDevice getNvrDevice() {
        return nvrDevice;
    }

    public synchronized void setStatusLightOn(UniFiProtectCamera camera, boolean enabled) {
        UniFiProtectStatusLightRequest request = new UniFiProtectStatusLightRequest(httpClient, camera, getConfig(),
                token, enabled);
        if (!requestSuccessFullySent(request.sendRequest())) {
            return;
        }
        String jsonContent = request.getJsonContent();
        logger.debug("StatusLight on result jsonResult: {}", jsonContent);
    }

    public synchronized void rebootCamera(UniFiProtectCamera camera) {
        UniFiProtectRebootCameraRequest request = new UniFiProtectRebootCameraRequest(httpClient, camera, getConfig(),
                token);
        if (!requestSuccessFullySent(request.sendRequest())) {
            return;
        }
        String jsonContent = request.getJsonContent();
        logger.debug("Reboot camera Result: {}", jsonContent);
    }

    public synchronized void setRecordingMode(UniFiProtectCamera camera, UniFiProtectRecordingMode recordingMode) {
        if (recordingMode == UniFiProtectRecordingMode.INVALID) {
            logger.error("Invalid recording mode, ignored");
            return;
        }

        UniFiProtectRecordingModeRequest request = new UniFiProtectRecordingModeRequest(httpClient, camera, getConfig(),
                token, recordingMode);
        if (!requestSuccessFullySent(request.sendRequest())) {
            return;
        }
        String jsonContent = request.getJsonContent();
        logger.debug("Set Recording mode on camera Result: {}", jsonContent);
    }

    @SuppressWarnings("null")
    public synchronized void turnOnOrOffAlerts(boolean enable) {
        if (nvrUser == null) {
            logger.error("No user set for UniFiProtect, can't turn on or off");
            return;
        }
        String id = nvrUser.getId();
        if (id != null && !id.isEmpty()) {
            UniFiProtectAlertsRequest request = new UniFiProtectAlertsRequest(httpClient, getConfig(), token, id,
                    enable);
            if (!requestSuccessFullySent(request.sendRequest())) {
                return;
            }
            String jsonContent = request.getJsonContent();
            logger.debug("Enable Alerts result jsonResult: {}", jsonContent);
        }
    }

    public synchronized void setIrMode(UniFiProtectCamera camera, UniFiProtectIrMode irMode) {
        if (irMode == UniFiProtectIrMode.INVALID) {
            logger.error("Invalid recording mode, ignored");
            return;
        }

        UniFiProtectIrModeRequest request = new UniFiProtectIrModeRequest(httpClient, camera, getConfig(), token,
                irMode);
        if (!requestSuccessFullySent(request.sendRequest())) {
            return;
        }
        String jsonContent = request.getJsonContent();
        logger.debug("Set IR mode on camera Result: {}", jsonContent);
    }

    public synchronized void turnOnOrOffHdrMode(UniFiProtectCamera camera, boolean enable) {
        UniFiProtectHdrModeRequest request = new UniFiProtectHdrModeRequest(httpClient, camera, getConfig(), token,
                enable);
        if (!requestSuccessFullySent(request.sendRequest())) {
            return;
        }
        String jsonContent = request.getJsonContent();
        logger.debug("Hdr mode result jsonResult: {}", jsonContent);
    }

    public synchronized void turnOnOrOffHighFpsMode(UniFiProtectCamera camera, boolean enable) {
        UniFiProtectHighFpsModeRequest request = new UniFiProtectHighFpsModeRequest(httpClient, camera, getConfig(),
                token, enable);
        if (!requestSuccessFullySent(request.sendRequest())) {
            return;
        }
        String jsonContent = request.getJsonContent();
        logger.debug("High FPS mode result jsonResult: {}", jsonContent);
    }

    @SuppressWarnings("null")
    public synchronized @Nullable UniFiProtectImage getThumbnail(UniFiProtectCamera camera, UniFiProtectEvent event) {
        final String thumbnail = event != null ? event.getThumbnail() : null;
        if (thumbnail == null || thumbnail.isEmpty()) {
            logger.debug("Could not find any thumbnails in events for camera: {}", camera.getId());
            return null;
        }
        UniFiProtectImage thumbnailImage = null;
        UniFiProtectThumbnailRequest request = new UniFiProtectThumbnailRequest(httpClient, camera, token, thumbnail,
                getConfig());
        if (!requestSuccessFullySent(request.sendRequest())) {
            return null;
        }
        if (UniFiProtectUtil.requestHasContentOfSize(request, IMAGE_MIN_SIZE)) {
            byte[] data = request.getResponse().getContent();
            logger.debug("Content size for thumbnail request: {}", data.length);
            File thumbnailFile = UniFiProtectUtil.writeThumbnailToImageFolder(getConfig().getImageFolder(), camera,
                    request.getResponse().getContent());
            if (thumbnailFile != null) {
                thumbnailImage = new UniFiProtectImage(UniFiProtectImageHandler.IMAGE_JPEG, thumbnailFile);
                logger.debug("Wrote thumbnail file: {} size: {}", thumbnailFile.getAbsolutePath(), data.length);
            }
        }
        return thumbnailImage;
    }

    private synchronized boolean requestSuccessFullySent(UniFiProtectStatus status) {
        switch (status.getStatus()) {
            case EXECUTION_FAULT:
            case INTERRUPTED:
            case NOT_SENT:
            case TIMEOUT:
                logger.debug("Request failed reason: {} message: {}", status.getStatus().name(), status.getMessage(),
                        status.getException());
                return false;
            case SUCCESS:
                logger.debug("Successfullt sent request");
                return true;
            default:
                logger.debug("Unhandled case: {} message: {}", status.getStatus().name(), status.getMessage(),
                        status.getException());
                return true;
        }
    }

    @SuppressWarnings("null")
    public synchronized @Nullable UniFiProtectEvent getLastMotionEvent(UniFiProtectCamera camera) {
        UniFiProtectEventsRequest eventsRequest = new UniFiProtectEventsRequest(httpClient, camera, getConfig(), token);
        if (!requestSuccessFullySent(eventsRequest.sendRequest())) {
            return null;
        }
        String jsonContent = eventsRequest.getJsonContent();
        UniFiProtectEvent[] events = UniFiProtectJsonParser.getEventsFromJson(gson, jsonContent);
        Arrays.stream(events).forEach(event -> logger.debug("Events resultT: {}", event));
        return UniFiProtectUtil.findLastMotionEventForCamera(events, camera);
    }

    @SuppressWarnings("null")
    public synchronized @Nullable UniFiProtectImage getHeatmap(UniFiProtectCamera camera, UniFiProtectEvent event) {
        String heatmap = event != null ? event.getHeatmap() : null;
        if (heatmap == null || heatmap.isEmpty()) {
            logger.debug("Could not find any heatMap in events for camera: {}", camera.getId());
            return null;
        }
        UniFiProtectImage heatmapImage = null;
        UniFiProtectHeatmapRequest request = new UniFiProtectHeatmapRequest(httpClient, camera, token, heatmap,
                getConfig());
        if (!requestSuccessFullySent(request.sendRequest())) {
            return null;
        }

        if (UniFiProtectUtil.requestHasContentOfSize(request, IMAGE_MIN_SIZE)) {
            byte[] data = request.getResponse().getContent();
            File heatmapFile = UniFiProtectUtil.writeHeatmapToFile(getConfig().getImageFolder(), camera, data);
            logger.debug("Content size for heatmap request: {}", data.length);
            if (heatmapFile != null) {
                heatmapImage = new UniFiProtectImage(UniFiProtectImageHandler.IMAGE_PNG, heatmapFile);
                logger.debug("Wrote heatmap file: {} size: {}", heatmapImage.getFile().getAbsolutePath(),
                        heatmapImage.getData().length);
            }
        }
        return heatmapImage;
    }

    @SuppressWarnings("null")
    public synchronized @Nullable UniFiProtectImage getSnapshot(UniFiProtectCamera camera) {
        UniFiProtectImage snapshot = null;
        UniFiProtectSnapshotRequest request = new UniFiProtectSnapshotRequest(httpClient, camera, token, getConfig());
        if (!requestSuccessFullySent(request.sendRequest())) {
            return null;
        }
        if (UniFiProtectUtil.requestHasContentOfSize(request, IMAGE_MIN_SIZE)) {
            logger.debug("Content size for snapshot request: {}", request.getResponse().getContent().length);
            File file = UniFiProtectUtil.writeSnapshotToFile(getConfig().getImageFolder(), camera,
                    request.getResponse().getContent());
            if (file != null) {
                logger.debug("Wrote snapshot file: {} size: {}", file.getAbsolutePath(), file.length());
                snapshot = new UniFiProtectImage(UniFiProtectImageHandler.IMAGE_JPEG, file);
            }
        } else {
            logger.debug("Snapshot not reccieved");
        }
        return snapshot;
    }

    @SuppressWarnings("null")
    public synchronized @Nullable UniFiProtectImage getAnonSnapshot(@Nullable UniFiProtectCamera camera) {
        if (camera == null) {
            return null;
        }
        UniFiProtectImage anonSnapshotImage = null;
        UniFiProtectAnonymousSnapshotRequest request = new UniFiProtectAnonymousSnapshotRequest(httpClient, camera,
                token, getConfig());
        if (!requestSuccessFullySent(request.sendRequest())) {
            return null;
        }
        if (UniFiProtectUtil.requestHasContentOfSize(request, IMAGE_MIN_SIZE)) {
            logger.debug("Content size for anon snapshot request: {}", request.getResponse().getContent().length);
            final byte[] data = request.getResponse().getContent();
            File file = UniFiProtectUtil.writeAnonSnapshotToFile(getConfig().getImageFolder(), camera, data);
            if (file != null) {
                anonSnapshotImage = new UniFiProtectImage(UniFiProtectImageHandler.IMAGE_JPEG, file);
                logger.debug("Wrote anon snapshot file: {} size: {}", file.getAbsolutePath(), data.length);
            }
        }
        return anonSnapshotImage;
    }

    public @Nullable synchronized UniFiProtectNvrUser getNvrUser() {
        return nvrUser;
    }

    public UniFiProtectNvrThingConfig getConfig() {
        return config;
    }
}
