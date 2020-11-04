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
 * 17:14:03.872 [main] DEBUG org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvr -
 * UniFiProtectProperties: UniFiProtectNvr
 * [mac=7483C2161619, host=10.0.44.4, name=DrakeProtect
 * , canAutoUpdate=false, isStatsGatheringEnabled=true, timezone=Europe/Stockholm
 * , version=1.13.4, firmwareVersion=1.1.13, hardwarePlatform=apq8053
 * , ports={ump=7449, http=7080, https=7443, rtsp=7447, rtmp=1935
 * , devicesWss=7442, cameraHttps=7444, cameraTcp=7877, liveWs=7445
 * , liveWss=7446, tcpStreams=7448, emsCLI=7440, emsLiveFLV=7550
 * , cameraEvents=7551, discoveryClient=0}, uptime=140845000
 * , lastSeen=1601651634112, isUpdating=false, lastUpdateAt=1596392274129
 * , isConnectedToCloud=true, isStation=false, enableAutomaticBackups=true
 * , enableStatsReporting=false, isSshEnabled=false, releaseChannel=release
 * , hosts=[10.0.44.4], hardwareId=2cb7390c-fc8b-5707-8158-3fca88e06713
 * , hardwareRevision=113-02570-16, hostType=59760, hostShortname=UCKP
 * , isHardware=true, timeFormat=12h, recordingRetentionDurationMs=8640000000
 * , enableCrashReporting=true, disableAudio=false,
 * storageInfo=StorageInfo [totalSize=942307909632, totalSpaceUsed=909433630720
 * , harddrives=[HardDrive [status=present, name=TOSHIBA MQ01ABD100V
 * , serial=19OQTSHDT, firmware=AX001Q, size=1000204886016, rpm=5400
 * , ataVersion=ATA8-ACS (minor revision not indicated)
 * , sataVersion=SATA 2.6, 3.0 Gb/s (current: 3.0 Gb/s), health=passed]]]]
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
    private UniFiProtectStorageInfo storageInfo = new UniFiProtectStorageInfo();

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
                + enableCrashReporting + ", disableAudio=" + disableAudio + ", storageInfo=" + storageInfo + "]";
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

    public UniFiProtectStorageInfo getStorageInfo() {
        return storageInfo;
    }

    public void setUniFiProtectStorageInfo(UniFiProtectStorageInfo storageInfo) {
        this.storageInfo = storageInfo;
    }

    public String getHardDrive0Name() {
        return hardDrive0isPresent() ? storageInfo.getHardDrives()[0].getName() : StringUtils.EMPTY;
    }

    public String getHardDrive0Health() {
        return hardDrive0isPresent() ? storageInfo.getHardDrives()[0].getHealth() : StringUtils.EMPTY;
    }

    public String getHardDrive0Status() {
        return hardDrive0isPresent() ? storageInfo.getHardDrives()[0].getStatus() : StringUtils.EMPTY;
    }

    @SuppressWarnings("null")
    private boolean hardDrive0isPresent() {
        return storageInfo != null && storageInfo.getHardDrives() != null && storageInfo.getHardDrives()[0] != null;
    }

    public Long getHardDrive0Size() {
        return hardDrive0isPresent() ? storageInfo.getHardDrives()[0].getSize() : 0L;
    }
}
