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
package org.openhab.binding.unifiprotect.websocket;

/**
 * The {@link UniFiProtectPayloadFormat}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public enum UniFiProtectPayloadFormat {
    JSON_OBJECT,
    UTF8_STRING,
    NODE_BUFFER,
    UNKNOWN;

    private int payloadValue;

    public static UniFiProtectPayloadFormat fromInt(int i) {
        // 1 - JSON object, 2 - UTF8-encoded string, 3 - Node Buffer.
        if (i == 1) {
            return UniFiProtectPayloadFormat.JSON_OBJECT;
        }

        if (i == 2) {
            return UniFiProtectPayloadFormat.UTF8_STRING;
        }

        if (i == 3) {
            return UniFiProtectPayloadFormat.NODE_BUFFER;
        }

        UniFiProtectPayloadFormat format = UniFiProtectPayloadFormat.UNKNOWN;
        format.setPayloadValue(i);
        return format;
    }

    public int getPayloadValue() {
        return payloadValue;
    }

    private void setPayloadValue(int payloadValue) {
        this.payloadValue = payloadValue;
    }
}
