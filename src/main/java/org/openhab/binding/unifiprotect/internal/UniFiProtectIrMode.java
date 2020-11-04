/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNull;

/**
 * The {@link UniFiProtectIrMode}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public enum UniFiProtectIrMode {
    AUTO,
    ON,
    AUTO_FILTER_ONLY,
    INVALID;

    public static UniFiProtectIrMode parse(String str) {
        Optional<@NonNull UniFiProtectIrMode> findAny = Arrays.stream(UniFiProtectIrMode.values())
                .filter(e -> e.name().replaceAll("_", StringUtils.EMPTY).toUpperCase().equals(str.toUpperCase()))
                .findAny();
        return findAny.isPresent() ? findAny.get() : UniFiProtectIrMode.INVALID;
    }

    private static final String JSON_RAW_AUTO = "{\"ispSettings\": {\"irLedMode\": \"auto\"}}";
    private static final String JSON_RAW_ON = "{\"ispSettings\": {\"irLedMode\": \"on\"}}";
    private static final String JSON_RAW_AUTO_FILTER_ONLY = "{\"ispSettings\": {\"irLedMode\": \"autoFilterOnly\"}}";

    public String getJsonRaw() {
        switch (this) {
            case AUTO:
                return JSON_RAW_AUTO;
            case INVALID:
                return null;
            case ON:
                return JSON_RAW_ON;
            case AUTO_FILTER_ONLY:
                return JSON_RAW_AUTO_FILTER_ONLY;
            default:
                break;
        }
        return null;
    }

    public static UniFiProtectIrMode create(long value) {
        if (value == 0) {
            return AUTO;
        }

        if (value == 1) {
            return ON;
        }

        if (value == 2) {
            return AUTO_FILTER_ONLY;
        }

        return INVALID;
    }
}
