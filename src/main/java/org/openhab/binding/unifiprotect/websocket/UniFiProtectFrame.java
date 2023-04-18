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
package org.openhab.binding.unifiprotect.websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.DataFormatException;

import org.openhab.binding.unifiprotect.internal.UniFiProtectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectFrame}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UniFiProtectFrame {
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectFrame.class);

    private final UniFiProtectFrameType type;
    private final UniFiProtectPayloadFormat format;
    private final int size;
    private final boolean compressed;
    private byte[] payload;

    public UniFiProtectFrame(byte[] bytes) {
        type = getTypeFromByte(bytes[0]);
        format = getFormatFromByte(bytes[1]);
        compressed = getCompressedFromByte(bytes[2]);
        size = getSizeFromBytes(bytes);
        payload = getPayload(bytes, size);
    }

    public byte[] getPayload(boolean decompress) {
        return (decompress && compressed) ? decompressPayload(payload) : payload;
    }

    private byte[] decompressPayload(byte[] payload) {
        try {
            return UniFiProtectUtil.zlibDecompress(payload);
        } catch (IOException | DataFormatException e) {
            logger.error("Failed to decompress payload: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String toString() {
        return "UniFiProtectFrame [type=" + getType() + ", format=" + getFormat() + ", size=" + size + ", compressed="
                + compressed + ", payload=" + new String(getPayload(true)) + "]";
    }

    private byte[] getPayload(byte[] bytes, int size) {
        return Arrays.copyOfRange(bytes, 8, size + 9);
    }

    private int getSizeFromBytes(byte[] bytes) {
        byte[] sizeBytes = Arrays.copyOfRange(bytes, 4, 8);
        return ByteBuffer.wrap(sizeBytes).getInt();
    }

    private boolean getCompressedFromByte(byte b) {
        return b == 1;
    }

    private UniFiProtectPayloadFormat getFormatFromByte(byte formatByte) {
        return UniFiProtectPayloadFormat.fromInt(formatByte);
    }

    private UniFiProtectFrameType getTypeFromByte(byte typeByte) {
        return UniFiProtectFrameType.fromInt(typeByte);
    }

    public UniFiProtectFrameType getType() {
        return type;
    }

    public UniFiProtectPayloadFormat getFormat() {
        return format;
    }
}
