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
import java.util.stream.Stream;

/**
 * The {@link UniFiProtectCameraChannel} class defines channel enum.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public enum UniFiProtectNvrChannel {

    HOST,
    NAME,
    VERSION,
    HOSTS,
    FIRMWARE_VERSION,
    IS_CONNECTED_TO_CLOUD,
    ENABLE_AUTOMATIC_BACKUPS,
    UPTIME,
    RECORDING_RETENTION_DURATION,
    CPU_AVERAGE_LOAD,
    CPU_TEMPERATURE,
    MEM_AVAILABLE,
    MEM_FREE,
    MEM_TOTAL,
    STORAGE_AVAILABLE,
    STORAGE_TYPE,
    STORAGE_TOTAL_SIZE,
    STORAGE_USED,
    DEVICE_0_MODEL,
    DEVICE_0_SIZE,
    DEVICE_0_HEALTHY,
    LAST_SEEN,
    HOST_SHORT_NAME,
    LAST_UPDATED_AT,
    ALERTS;

    private static final String DASH = "-";
    private static final String UNDERSCORE = "_";

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    @SuppressWarnings("null")
    public static Stream<UniFiProtectNvrChannel> stream() {
        return Stream.of(UniFiProtectNvrChannel.values());
    }

    public static UniFiProtectNvrChannel fromString(String str) {
        return UniFiProtectNvrChannel.stream()
                .filter(channelList -> str.replaceAll(DASH, UNDERSCORE).equalsIgnoreCase(channelList.name()))
                .findFirst().get();
    }
}
