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
 * The {@link UniFiProtectG4DoorbellChannel} class defines channel enum.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public enum UniFiProtectG4DoorbellChannel {
    LCD_LEAVE_PACKAGE,
    LCD_DO_NOT_DISTURB,
    LCD_CUSTOM_TEXT,
    IS_RINGING,
    RING_THUMBNAIL,
    LAST_RING,
    STATUS_SOUNDS,
    UNKNOWN;

    private static final String DASH = "-";
    private static final String UNDERSCORE = "_";
    private static final Logger logger = LoggerFactory.getLogger(UniFiProtectG4DoorbellChannel.class);

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    @SuppressWarnings("null")
    public static Stream<UniFiProtectG4DoorbellChannel> stream() {
        return Stream.of(UniFiProtectG4DoorbellChannel.values());
    }

    public static UniFiProtectG4DoorbellChannel fromString(String str) {
        UniFiProtectG4DoorbellChannel channel = UNKNOWN;
        try {
            channel = UniFiProtectG4DoorbellChannel.stream()
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
