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
 * The {@link UniFiProtectFrameType}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public enum UniFiProtectFrameType {
    ACTION,
    PAYLOAD,
    UNKNOWN;

    private int frameValue;

    public static UniFiProtectFrameType fromInt(int i) {
        if (i == 1) {
            return UniFiProtectFrameType.ACTION;
        }

        if (i == 2) {
            return UniFiProtectFrameType.PAYLOAD;
        }
        UniFiProtectFrameType type = UniFiProtectFrameType.UNKNOWN;
        type.setFrameValue(i);
        return type;
    }

    public int getFrameValue() {
        return frameValue;
    }

    private void setFrameValue(int frameValue) {
        this.frameValue = frameValue;
    }
}
