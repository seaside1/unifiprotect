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
package org.openhab.binding.unifiprotect.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectCameraCache;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvr;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectStatus;
import org.openhab.binding.unifiprotect.internal.thing.UniFiProtectNvrThingConfig;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

/**
 * The {@link UniFiProtectBoostrapTest} Test for fetching and parsing clients
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectBoostrapTest {
    private static final Logger logger = (Logger) LoggerFactory.getLogger("org.openhab.binding.unifiprotect.internal");

    private UniFiProtectNvrThingConfig config = new UniFiProtectNvrThingConfig();

    private String bootstrap = "";

    @BeforeEach
    public void setUp() throws Exception {
        InputStream stream = this.getClass().getResourceAsStream("bootstrap.test");
        bootstrap = getStringFromStream(stream);
        stream.close();
        config.setEventsTimePeriodLength(13000);
        Logger rootLogger = (Logger) LoggerFactory.getLogger("org.openhab");
        rootLogger.setLevel(Level.DEBUG);

        rootLogger = (Logger) LoggerFactory.getLogger("org.eclipse.jetty");
        rootLogger.setLevel(Level.WARN);

    }

    @SuppressWarnings("null")
    private static String getStringFromStream(@Nullable InputStream stream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = stream.read(buffer)) != -1;) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    @Test
    @Disabled
    public void testBootstrap() throws Exception {
        UniFiProtectNvr nvr = new UniFiProtectNvrCustomBootstrap(config);
        nvr.refreshProtect();
        logger.info("Looking att camera cache {}", nvr.getCameraInsightCache().getCameras().size());
        UniFiProtectCameraCache cameraInsightCache = nvr.getCameraInsightCache();
        cameraInsightCache.getCameras().forEach(c -> logger.info(c.getType()));
    }

    private class UniFiProtectNvrCustomBootstrap extends UniFiProtectNvr {

        public UniFiProtectNvrCustomBootstrap(UniFiProtectNvrThingConfig config) {
            super(config);
        }

        @Override
        protected synchronized UniFiProtectStatus refreshBootstrap() {
            return refreshBootstrap(bootstrap);
        }

        @Override
        public synchronized UniFiProtectStatus login() {
            return UniFiProtectStatus.STATUS_SUCCESS;
        }
    }
}
