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
package org.openhab.binding.unifiprotect.internal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openhab.binding.unifiprotect.internal.event.UniFiProtectEventManager;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectCameraCache;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvr;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectEvent;
import org.openhab.binding.unifiprotect.internal.thing.UniFiProtectNvrThingConfig;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * The {@link UniFiProtectRequestTester} Test for fetching and parsing clients
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectRequestTester {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

    private String user = "";
    private String host = "";
    private String password = "";
    private UniFiProtectNvrThingConfig config = new UniFiProtectNvrThingConfig();

    @BeforeEach
    public void setUp() throws Exception {
        Properties properties = new Properties();
        InputStream stream = this.getClass().getResourceAsStream("credentials.test");
        properties.load(stream);
        stream.close();
        user = properties.getProperty("user");
        password = properties.getProperty("password");
        host = properties.getProperty("host");
        config = new UniFiProtectNvrThingConfig();
        config.setHost(host);
        config.setPassword(password);
        config.setUserName(user);
        config.setEventsTimePeriodLength(13000);
        Logger rootLogger = (Logger) LoggerFactory.getLogger("org.openhab");
        rootLogger.setLevel(Level.DEBUG);
        rootLogger = (Logger) LoggerFactory.getLogger("org.eclipse.jetty");
        rootLogger.setLevel(Level.WARN);
    }

    @Test
    @Disabled
    public void getWebsocketMessage() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        nvr.getNvrUser();
        logger.debug("Fetch NvrUser: {}", nvr.getNvrUser());
        UniFiProtectEventManager em = new UniFiProtectEventManager(nvr.getHttpClient(), nvr.getUniFiProtectJsonParser(),
                config);
        em.start();
        em.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(@Nullable PropertyChangeEvent evt) {
                logger.info(evt.getPropertyName());
            }
        });
        Thread.sleep(40000);
        nvr.refreshProtect();

        nvr.getEvents().forEach(e -> logger.info(e.toString()));
    }

    @Test
    @Disabled
    public void getBootstrap() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        nvr.getNvrUser();
        logger.debug("Fetch NvrUser: {}", nvr.getNvrUser());
    }

    @Test
    @Disabled
    public void getAnonSnapshot() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.getAnonSnapshot(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get());
    }

    @Test
    @Disabled
    public void getSnapshot() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.getSnapshot(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get());
    }

    @Test
    @Disabled
    public void getHeatmap() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        UniFiProtectCamera camera = cameraInsightCache.getCamera("7483C22FA4A5");
        if (camera != null) {
            UniFiProtectEvent lastMotionEvent = nvr.getLastMotionEvent(camera);
            if (lastMotionEvent != null) {
                nvr.getHeatmap(camera, lastMotionEvent);
            }
        }
    }

    @Test
    @Disabled
    public void getThumbnail() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        UniFiProtectCamera camera = cameraInsightCache.getCamera("7483C22FA4A5");
        if (camera != null) {
            UniFiProtectEvent lastMotionEvent = nvr.getLastMotionEvent(camera);
            if (lastMotionEvent != null) {
                nvr.getThumbnail(camera, lastMotionEvent);
            }
        }
    }

    @Test
    @Disabled
    public void setAlerts() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.turnOnOrOffAlerts(false);
    }

    @Test
    @Disabled
    public void setSmartDetectTypes() {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        Optional<UniFiProtectCamera> optCamera = cameraInsightCache.getCameras().stream()
                .filter(camera -> camera.getType().toLowerCase().contains("doorbell")).findAny();
        UniFiProtectCamera camera = optCamera.get();
        // (camera -> logger.debug(camera.toString()));
        UniFiProtectSmartDetectTypes types = UniFiProtectSmartDetectTypes.fromArray(
                new String[] { UniFiProtectSmartDetectTypes.PERSON_STR, UniFiProtectSmartDetectTypes.VEHICLE_STR });

        nvr.setSmartDetectTypes(camera, types);
    }

    @Test
    @Disabled
    public void setLcdMessagePackage() {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        Optional<UniFiProtectCamera> optCamera = cameraInsightCache.getCameras().stream()
                .filter(camera -> camera.getType().toLowerCase().contains("doorbell")).findAny();
        UniFiProtectCamera camera = optCamera.get();
        // (camera -> logger.debug(camera.toString()));
        nvr.setLcdMessage(camera,
                new UniFiProtectLcdMessage("Leffe", null, UniFiProtectLcdMessage.LcdMessageType.CUSTOM_MESSAGE));
    }

    @Test
    @Disabled
    public void setHighFpsMode() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.turnOnOrOffHighFpsMode(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get(),
                false);
    }

    @Test
    @Disabled
    public void setHDRMode() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.turnOnOrOffHdrMode(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get(), false);
    }

    @Test
    @Disabled
    public void setIrMode() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.setIrMode(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get(),
                UniFiProtectIrMode.ON);
    }

    @Test
    @Disabled
    public void setRecordingMode() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.setRecordingMode(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get(),
                UniFiProtectRecordingMode.DETECTIONS);
    }

    @Test
    @Disabled
    public void setStatusLightOn() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.setStatusLightOn(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get(), true);
    }

    @Test
    @Disabled
    public void rebootCamera() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvr(config);
        nvr.init();
        nvr.login();
        nvr.refreshProtect();
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().stream().forEach(camera -> logger.debug(camera.toString()));
        nvr.rebootCamera(cameraInsightCache.getCameras().stream().reduce((first, second) -> second).get());
    }
}
