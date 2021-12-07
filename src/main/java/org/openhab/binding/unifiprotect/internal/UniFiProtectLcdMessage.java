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
package org.openhab.binding.unifiprotect.internal;

import java.util.Arrays;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;

/**
 * The {@link UniFiProtectLcdMessage}
 * 
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UniFiProtectLcdMessage {

    private final String text;
    private final Long resetAt;
    private final LcdMessageType type;

    private static final String JSON_LEAVE_PACKAGE = "{\"lcdMessage\":{\"type\":\"LEAVE_PACKAGE_AT_DOOR\",\"text\":\"Lamna pakket\",\"resetAt\":null}}";
    // "{\"lcdMessage\":{\"type\":\"LEAVE_PACKAGE_AT_DOOR\",\"text\":\"\",\"resetAt\":null}}";
    private static final String JSON_DO_NOT_DISTURB = "{\"lcdMessage\":{\"type\":\"DO_NOT_DISTURB\",\"text\":\"\",\"resetAt\":null}}";
    private static final String JSON_RESET = "{\"lcdMessage\":{\"resetAt\":1614952810643}}";
    private static final String JSON_CUSTOM_START = "{\"lcdMessage\":{\"type\":\"CUSTOM_MESSAGE\",\"text\":\"";
    private static final String JSON_CUSTOM_END = "\",\"resetAt\":null}}";

    public UniFiProtectLcdMessage(String text, Long resetAt, LcdMessageType type) {
        this.text = text;
        this.resetAt = resetAt;
        this.type = type;
    }

    public String getJsonRaw() {
        switch (type) {
            case CUSTOM_MESSAGE:
                return JSON_CUSTOM_START + text + JSON_CUSTOM_END;
            case DO_NOT_DISTURB:
                return JSON_DO_NOT_DISTURB;
            case LEAVE_PACKAGE_AT_DOOR:
                return JSON_LEAVE_PACKAGE;
            case RESET:
                return JSON_RESET;
            default:
                break;
        }
        return null;
    }

    public enum LcdMessageType {
        LEAVE_PACKAGE_AT_DOOR,
        DO_NOT_DISTURB,
        CUSTOM_MESSAGE,
        RESET,
        INVALID;

        public static UniFiProtectLcdMessage.LcdMessageType parse(String str) {
            Optional<UniFiProtectLcdMessage.@NonNull LcdMessageType> findAny = Arrays.stream(LcdMessageType.values())
                    .filter(e -> e.name().toUpperCase().equals(str.toUpperCase())).findAny();
            return findAny.isPresent() ? findAny.get() : UniFiProtectLcdMessage.LcdMessageType.INVALID;
        }
    }
}
