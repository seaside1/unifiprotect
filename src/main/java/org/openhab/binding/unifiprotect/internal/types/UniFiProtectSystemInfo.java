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
package org.openhab.binding.unifiprotect.internal.types;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link UniFiProtectSystemInfo}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectSystemInfo {
    private UniFiProtectCpuInfo cpu = new UniFiProtectCpuInfo();
    private UniFiProtectMemoryInfo memory = new UniFiProtectMemoryInfo();
    private UniFiProtectStorageInfo storage = new UniFiProtectStorageInfo();

    @Override
    public String toString() {
        return "UniFiProtectSystemInfo [cpu=" + cpu + ", memory=" + memory + ", storage=" + storage + "]";
    }

    public UniFiProtectCpuInfo getCpu() {
        return cpu;
    }

    public void setCpu(UniFiProtectCpuInfo cpu) {
        this.cpu = cpu;
    }

    public UniFiProtectMemoryInfo getMemory() {
        return memory;
    }

    public void setMemory(UniFiProtectMemoryInfo memory) {
        this.memory = memory;
    }

    public UniFiProtectStorageInfo getStorage() {
        return storage;
    }

    public void setStorage(UniFiProtectStorageInfo storage) {
        this.storage = storage;
    }
}
