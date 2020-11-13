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
package org.openhab.binding.unifiprotect.internal.types;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link UniFiProtectStorageInfo}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectStorageInfo {

    private UniFiProtectHardDrive[] devices = new UniFiProtectHardDrive[0];
    private Long available = -1L;
    private Boolean isRecycling = Boolean.FALSE;
    private Long size = -1L;
    private String type = StringUtils.EMPTY;
    private Long used = -1L;

    @Override
    public String toString() {
        return "UniFiProtectStorageInfo [devices=" + Arrays.toString(getDevices()) + ", available=" + getAvailable()
                + ", isRecycling=" + getIsRecycling() + ", size=" + getSize() + ", type=" + getType() + ", used="
                + getUsed() + "]";
    }

    public UniFiProtectHardDrive[] getDevices() {
        return devices;
    }

    public void setDevices(UniFiProtectHardDrive[] devices) {
        this.devices = devices;
    }

    public Long getAvailable() {
        return available;
    }

    public void setAvailable(Long available) {
        this.available = available;
    }

    public Boolean getIsRecycling() {
        return isRecycling;
    }

    public void setIsRecycling(Boolean isRecycling) {
        this.isRecycling = isRecycling;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
    }
}
