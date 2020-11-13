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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link UniFiProtectNvrDevice}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectNvrDevice {

    private static final String LEFT_RIGHT_B_REGEX = "\\]|\\[";
    private String mac = StringUtils.EMPTY;
    private String host = StringUtils.EMPTY;
    private String name = StringUtils.EMPTY;
    private Boolean canAutoUpdate = Boolean.FALSE;
    private Boolean isStatsGatheringEnabled = Boolean.FALSE;
    private String timezone = StringUtils.EMPTY;
    private String version = StringUtils.EMPTY;
    private String firmwareVersion = StringUtils.EMPTY;
    private String hardwarePlatform = StringUtils.EMPTY;
    private Map<String, Integer> ports = new HashMap<>();
    private Long uptime = new Long(-1);
    private Long lastSeen = new Long(-1);
    private Boolean isUpdating = Boolean.FALSE;
    private Long lastUpdateAt = new Long(-1);
    private Boolean isConnectedToCloud = Boolean.FALSE;
    private Boolean isStation = Boolean.FALSE;
    private Boolean enableAutomaticBackups = Boolean.FALSE;
    private Boolean enableStatsReporting = Boolean.FALSE;
    private Boolean isSshEnabled = Boolean.FALSE;
    private String releaseChannel = StringUtils.EMPTY;
    private String[] hosts = new String[0];
    private String hardwareId = StringUtils.EMPTY;
    private String hardwareRevision = StringUtils.EMPTY;
    private Integer hostType = new Integer(-1);
    private String hostShortname = StringUtils.EMPTY;
    private Boolean isHardware = Boolean.FALSE;
    private String timeFormat = StringUtils.EMPTY;
    private Long recordingRetentionDurationMs = new Long(-1);
    private Boolean enableCrashReporting = Boolean.FALSE;
    private Boolean disableAudio = Boolean.FALSE;
    private UniFiProtectSystemInfo systemInfo = new UniFiProtectSystemInfo();

    @Override
    public String toString() {
        return "UniFiProtectNvrDevice [mac=" + mac + ", host=" + host + ", name=" + name + ", canAutoUpdate="
                + canAutoUpdate + ", isStatsGatheringEnabled=" + isStatsGatheringEnabled + ", timezone=" + timezone
                + ", version=" + version + ", firmwareVersion=" + firmwareVersion + ", hardwarePlatform="
                + hardwarePlatform + ", ports=" + ports + ", uptime=" + uptime + ", lastSeen=" + lastSeen
                + ", isUpdating=" + isUpdating + ", lastUpdateAt=" + lastUpdateAt + ", isConnectedToCloud="
                + isConnectedToCloud + ", isStation=" + isStation + ", enableAutomaticBackups=" + enableAutomaticBackups
                + ", enableStatsReporting=" + enableStatsReporting + ", isSshEnabled=" + isSshEnabled
                + ", releaseChannel=" + releaseChannel + ", hosts=" + Arrays.toString(hosts) + ", hardwareId="
                + hardwareId + ", hardwareRevision=" + hardwareRevision + ", hostType=" + hostType + ", hostShortname="
                + hostShortname + ", isHardware=" + isHardware + ", timeFormat=" + timeFormat
                + ", recordingRetentionDurationMs=" + recordingRetentionDurationMs + ", enableCrashReporting="
                + enableCrashReporting + ", disableAudio=" + disableAudio + ", storageInfo=" + systemInfo + "]";
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCanAutoUpdate() {
        return canAutoUpdate;
    }

    public void setCanAutoUpdate(Boolean canAutoUpdate) {
        this.canAutoUpdate = canAutoUpdate;
    }

    public Boolean getIsStatsGatheringEnabled() {
        return isStatsGatheringEnabled;
    }

    public void setIsStatsGatheringEnabled(Boolean isStatsGatheringEnabled) {
        this.isStatsGatheringEnabled = isStatsGatheringEnabled;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getHardwarePlatform() {
        return hardwarePlatform;
    }

    public void setHardwarePlatform(String hardwarePlatform) {
        this.hardwarePlatform = hardwarePlatform;
    }

    public Map<String, Integer> getPorts() {
        return ports;
    }

    public void setPorts(Map<String, Integer> ports) {
        this.ports = ports;
    }

    public Long getUptime() {
        return uptime;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    public Long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Boolean getIsUpdating() {
        return isUpdating;
    }

    public void setIsUpdating(Boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    public Long getLastUpdateAt() {
        return lastUpdateAt;
    }

    public void setLastUpdateAt(Long lastUpdateAt) {
        this.lastUpdateAt = lastUpdateAt;
    }

    public Boolean getIsConnectedToCloud() {
        return isConnectedToCloud;
    }

    public void setIsConnectedToCloud(Boolean isConnectedToCloud) {
        this.isConnectedToCloud = isConnectedToCloud;
    }

    public Boolean getIsStation() {
        return isStation;
    }

    public void setIsStation(Boolean isStation) {
        this.isStation = isStation;
    }

    public Boolean getEnableAutomaticBackups() {
        return enableAutomaticBackups;
    }

    public void setEnableAutomaticBackups(Boolean enableAutomaticBackups) {
        this.enableAutomaticBackups = enableAutomaticBackups;
    }

    public Boolean getEnableStatsReporting() {
        return enableStatsReporting;
    }

    public void setEnableStatsReporting(Boolean enableStatsReporting) {
        this.enableStatsReporting = enableStatsReporting;
    }

    public Boolean getIsSshEnabled() {
        return isSshEnabled;
    }

    public void setIsSshEnabled(Boolean isSshEnabled) {
        this.isSshEnabled = isSshEnabled;
    }

    public String getReleaseChannel() {
        return releaseChannel;
    }

    public void setReleaseChannel(String releaseChannel) {
        this.releaseChannel = releaseChannel;
    }

    public String getHosts() {
        if (hosts.length > 0) {
            return Arrays.toString(hosts).replaceAll(LEFT_RIGHT_B_REGEX, StringUtils.EMPTY);
        }
        return StringUtils.EMPTY;
    }

    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getHardwareRevision() {
        return hardwareRevision;
    }

    public void setHardwareRevision(String hardwareRevision) {
        this.hardwareRevision = hardwareRevision;
    }

    public Integer getHostType() {
        return hostType;
    }

    public void setHostType(Integer hostType) {
        this.hostType = hostType;
    }

    public String getHostShortname() {
        return hostShortname;
    }

    public void setHostShortname(String hostShortname) {
        this.hostShortname = hostShortname;
    }

    public Boolean getIsHardware() {
        return isHardware;
    }

    public void setIsHardware(Boolean isHardware) {
        this.isHardware = isHardware;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public Long getRecordingRetentionDurationMs() {
        return recordingRetentionDurationMs;
    }

    public void setRecordingRetentionDurationMs(Long recordingRetentionDurationMs) {
        this.recordingRetentionDurationMs = recordingRetentionDurationMs;
    }

    public Boolean getEnableCrashReporting() {
        return enableCrashReporting;
    }

    public void setEnableCrashReporting(Boolean enableCrashReporting) {
        this.enableCrashReporting = enableCrashReporting;
    }

    public Boolean getDisableAudio() {
        return disableAudio;
    }

    public void setDisableAudio(Boolean disableAudio) {
        this.disableAudio = disableAudio;
    }

    public UniFiProtectSystemInfo getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(UniFiProtectSystemInfo systemInfo) {
        this.systemInfo = systemInfo;
    }

    // public String getHardDrive0Name() {
    // return hardDrive0isPresent() ? systemInfo.getStorage().getDevices()[0].getModel() : StringUtils.EMPTY;
    // }
    //
    // public String getHardDrive0Health() {
    // return hardDrive0isPresent() ? systemInfo.getHardDrives()[0].getHealth() : StringUtils.EMPTY;
    // }
    //
    // public String getHardDrive0Status() {
    // if (hardDrive0isPresent()) {
    // return systemInfo.getStorage().getDevices()[0].getHealthy() ? "Healthy" : "Not Healthy";
    // }
    // return StringUtils.EMPTY;
    // }
    //
    // @SuppressWarnings("null")
    // private boolean hardDrive0isPresent() {
    // return systemInfo != null && systemInfo.getStorage().getDevices() != null
    // && systemInfo.getStorage().getDevices()[0] != null;
    // }
    //
    // public Long getHardDrive0Size() {
    // return hardDrive0isPresent() ? systemInfo.getStorage().getDevices()[0].getSize() : 0L;
    // }
}
