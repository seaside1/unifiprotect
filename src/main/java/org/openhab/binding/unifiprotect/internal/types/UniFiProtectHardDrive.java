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

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link UniFiProtectHardDrive}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectHardDrive {
    @Override
    public String toString() {
        return "HardDrive [status=" + status + ", name=" + name + ", serial=" + serial + ", firmware=" + firmware
                + ", size=" + size + ", rpm=" + rpm + ", ataVersion=" + ataVersion + ", sataVersion=" + sataVersion
                + ", health=" + health + "]";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getRpm() {
        return rpm;
    }

    public void setRpm(String rpm) {
        this.rpm = rpm;
    }

    public String getAtaVersion() {
        return ataVersion;
    }

    public void setAtaVersion(String ataVersion) {
        this.ataVersion = ataVersion;
    }

    public String getSataVersion() {
        return sataVersion;
    }

    public void setSataVersion(String sataVersion) {
        this.sataVersion = sataVersion;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    private String status = StringUtils.EMPTY;
    private String name = StringUtils.EMPTY;
    private String serial = StringUtils.EMPTY;
    private String firmware = StringUtils.EMPTY;
    private Long size = -1L;
    @SerializedName(value = "RPM", alternate = "rpm")
    private String rpm = StringUtils.EMPTY;
    private String ataVersion = StringUtils.EMPTY;
    private String sataVersion = StringUtils.EMPTY;
    private String health = StringUtils.EMPTY;

}
