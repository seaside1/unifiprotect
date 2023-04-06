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
package org.openhab.binding.unifiprotect.internal.model;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectG4Channel} class defines channel enum.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public enum UniFiProtectG4Channel {
    SMART_DETECT_PERSON,
    SMART_DETECT_VEHICLE,
    SMART_DETECT_PACKAGE,
    SMART_DETECT_SMOKE,
    SMART_DETECT_MOTION,
    SMART_DETECT_THUMBNAIL,
    SMART_DETECT_SCORE,
    SMART_DETECT_TYPE,
    SMART_DETECT_LAST,
    UNKNOWN;

    private static final String DASH = "-";
    private static final String UNDERSCORE = "_";
    private static final Logger logger = LoggerFactory.getLogger(UniFiProtectG4Channel.class);

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    @SuppressWarnings("null")
    public static Stream<UniFiProtectG4Channel> stream() {
        return Stream.of(UniFiProtectG4Channel.values());
    }

    public static UniFiProtectG4Channel fromString(String str) {
        UniFiProtectG4Channel channel = UNKNOWN;
        try {
            channel = UniFiProtectG4Channel.stream()
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
