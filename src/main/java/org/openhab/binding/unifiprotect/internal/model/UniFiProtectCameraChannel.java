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
package org.openhab.binding.unifiprotect.internal.model;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectCameraChannel} class defines channel enum.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public enum UniFiProtectCameraChannel {
    NAME,
    TYPE,
    MAC,
    HOST,
    STATE,
    UP_SINCE,
    LAST_SEEN,
    CONNECTED_SINCE,
    LAST_MOTION,
    MIC_VOLUME,
    STATUS_LIGHT,
    IS_MIC_ENABLED,
    IS_DARK,
    IS_RECORDING,
    IS_MOTION_DETECTED,
    RECORDING_MODE,
    HDR_MODE,
    HIGH_FPS_MODE,
    MOTION_SCORE,
    MOTION_THUMBNAIL,
    MOTION_HEATMAP,
    MOTION_EVENT,
    MOTION_DETECTION,
    SNAPSHOT,
    SNAPSHOT_IMG,
    A_SNAPSHOT,
    A_SNAPSHOT_IMG,
    IR_MODE,
    PRIVACY_ZONE,
    REBOOT,
    UNKNOWN;

    private static final String DASH = "-";
    private static final String UNDERSCORE = "_";
    private static final Logger logger = LoggerFactory.getLogger(UniFiProtectCameraChannel.class);

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    @SuppressWarnings("null")
    public static Stream<UniFiProtectCameraChannel> stream() {
        return Stream.of(UniFiProtectCameraChannel.values());
    }

    public static UniFiProtectCameraChannel fromString(String str) {
        UniFiProtectCameraChannel channel = UNKNOWN;
        try {
            channel = UniFiProtectCameraChannel.stream()
                    .filter(channelList -> str.replaceAll(DASH, UNDERSCORE).equalsIgnoreCase(channelList.name()))
                    .findFirst().get();
        } catch (Exception x) {
            logger.debug("Failed to load channel: {}", str);
        }
        return channel;
    }

    public String toChannelId() {
        return this.name().replaceAll(UNDERSCORE, DASH).toLowerCase();
    }
}
