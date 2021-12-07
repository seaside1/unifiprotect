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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link UniFiProtectRecordingMode}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public enum UniFiProtectRecordingMode {
    NEVER,
    ALWAYS,
    DETECTIONS,
    INVALID;

    private static final String JSON_RAW_NEVER = "{\"recordingSettings\":{\"mode\":\"never\"}}";
    private static final String JSON_RAW_ALWAYS = "{\"recordingSettings\":{\"mode\":\"always\"}}";
    private static final String JSON_RAW_DETECTIONS = "{\"recordingSettings\":{\"mode\":\"detections\"}}";

    public @Nullable String getJsonRaw() {
        switch (this) {
            case ALWAYS:
                return JSON_RAW_ALWAYS;
            case INVALID:
                return null;
            case DETECTIONS:
                return JSON_RAW_DETECTIONS;
            case NEVER:
                return JSON_RAW_NEVER;
            default:
                break;
        }
        return null;
    }

    public static UniFiProtectRecordingMode create(long value) {
        if (value == 0) {
            return NEVER;
        }

        if (value == 1) {
            return ALWAYS;
        }

        if (value == 2) {
            return DETECTIONS;
        }

        return INVALID;
    }
}
