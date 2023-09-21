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
package org.openhab.binding.unifiprotect.internal.types;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unifiprotect.internal.UniFiProtectBindingConstants;
import org.openhab.binding.unifiprotect.internal.UniFiProtectSmartDetectTypes;

/**
 * The {@link UniFiProtectCamera}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectCamera {
    private @Nullable String motionThumbnailUrl;
    private @Nullable String motionHeatmapUrl;
    private @Nullable String snapshotUrl;
    private @Nullable String aSnapshotUrl;
    private @Nullable Long upSince;
    private String mac = UniFiProtectBindingConstants.EMPTY_STRING;
    private String host = UniFiProtectBindingConstants.EMPTY_STRING;
    private @Nullable String type;
    private String name = UniFiProtectBindingConstants.EMPTY_STRING;
    private @Nullable String videoMode;
    private @Nullable Long lastSeen;
    private @Nullable Long connectedSince;
    private @Nullable String state;
    private @Nullable Integer hardwareRevision;
    private @Nullable UniFiProtectPrivacyZone[] privacyZones = new UniFiProtectPrivacyZone[0];
    private @Nullable String firmwareVersion;
    private @Nullable String firmwareBuild;
    private @Nullable Boolean isUpdating;
    private @Nullable Boolean isAdopting;
    private @Nullable Boolean isAdopted;
    private @Nullable Boolean isProvisioned;
    private @Nullable Boolean isRebooting;
    private @Nullable Boolean isSshEnabled;
    private @Nullable Boolean canAdopt;
    private @Nullable Boolean isAttemptingToConnect;
    private @Nullable Boolean isHidden;
    private @Nullable Long lastMotion;
    private @Nullable Integer micVolume;
    private @Nullable Boolean isMicEnabled;
    private @Nullable Boolean isRecording;
    private @Nullable Boolean isMotionDetected;
    private @Nullable SpeakerSettings speakerSettings;
    private @Nullable LedSettings ledSettings;
    private @Nullable IspSettings ispSettings;
    private @Nullable RecordingSettings recordingSettings;
    private @Nullable Integer phyRate;
    private @Nullable String id;
    private @Nullable Boolean hdrMode;
    private @Nullable Boolean isProbingForWifi;
    private @Nullable Integer chimeDuration;
    private @Nullable Boolean isDark;
    // Doorbell
    private @Nullable LcdMessage lcdMessage;
    private @Nullable String ringThumbnailUrl;
    private @Nullable String ringHeatmapUrl;
    private @Nullable Long lastRing;

    // G4
    private @Nullable SmartDetectSettings smartDetectSettings;
    private @Nullable String smartDetectThumbnailUrl;
    private @Nullable String smartDetectHeatmapUrl;

    @SuppressWarnings("null")
    public @Nullable String getIrLedMode() {
        return ispSettings != null && ispSettings.getIrLedMode() != null ? ispSettings.getIrLedMode() : null;
    }

    @SuppressWarnings("null")
    public @Nullable String getLcdMessageText() {
        return lcdMessage != null && lcdMessage.getText() != null ? lcdMessage.getText() : null;
    }

    @SuppressWarnings("null")
    public UniFiProtectSmartDetectTypes getSmartDetectObjectTypes() {
        final String[] objectTypes = smartDetectSettings != null && smartDetectSettings.getObjectTypes() != null
                ? smartDetectSettings.getObjectTypes()
                : null;
        final String[] audioTypes = smartDetectSettings != null && smartDetectSettings.getAudioTypes() != null
                ? smartDetectSettings.getAudioTypes()
                : null;
        return UniFiProtectSmartDetectTypes.fromArray(objectTypes, audioTypes);
    }

    @SuppressWarnings("null")
    public void setSmartDetectObjectTypes(UniFiProtectSmartDetectTypes smartDetectTypes) {
        smartDetectSettings.setObjectTypes(smartDetectTypes.getObjectTypesAsArray());
        smartDetectSettings.setAudioTypes(smartDetectTypes.getAudioTypesAsArray());
    }

    @SuppressWarnings("null")
    public @Nullable String getLcdMessageType() {
        return lcdMessage != null && lcdMessage.getType() != null ? lcdMessage.getType() : null;
    }

    @SuppressWarnings("null")
    public @Nullable String getRecoringMode() {
        return recordingSettings != null && recordingSettings.getMode() != null ? recordingSettings.getMode() : null;
    }

    @SuppressWarnings("null")
    public @Nullable Boolean getMotionDetection() {
        return recordingSettings != null && recordingSettings.getEnableMotionDetection() != null
                ? recordingSettings.getEnableMotionDetection()
                : null;
    }

    @SuppressWarnings("null")
    public @Nullable Boolean getStatusLight() {
        return ledSettings != null && ledSettings.getIsEnabled() != null ? ledSettings.getIsEnabled() : null;
    }

    @SuppressWarnings("null")
    public @Nullable Boolean getStatusSounds() {
        return speakerSettings != null && speakerSettings.getIsEnabled() != null ? speakerSettings.getIsEnabled()
                : null;
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

    public @Nullable String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @Nullable Long getUpSince() {
        return upSince;
    }

    public void setUpSince(Long upSince) {
        this.upSince = upSince;
    }

    public @Nullable Long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public @Nullable Long getConnectedSince() {
        return connectedSince;
    }

    public void setConnectedSince(Long connectedSince) {
        this.connectedSince = connectedSince;
    }

    public @Nullable String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public @Nullable Integer getHardwareRevision() {
        return hardwareRevision;
    }

    public void setHardwareRevision(Integer hardwareRevision) {
        this.hardwareRevision = hardwareRevision;
    }

    public @Nullable String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public @Nullable String getFirmwareBuild() {
        return firmwareBuild;
    }

    public void setFirmwareBuild(String fimwareBuild) {
        this.firmwareBuild = fimwareBuild;
    }

    public @Nullable Boolean getIsUpdating() {
        return isUpdating;
    }

    public void setIsUpdating(Boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    public @Nullable Boolean getIsAdopting() {
        return isAdopting;
    }

    public void setIsAdopting(Boolean isAdopting) {
        this.isAdopting = isAdopting;
    }

    public @Nullable Boolean getIsAdopted() {
        return isAdopted;
    }

    public void setIsAdopted(Boolean isAdopted) {
        this.isAdopted = isAdopted;
    }

    public @Nullable Boolean getIsProvisioned() {
        return isProvisioned;
    }

    public void setIsProvisioned(Boolean isProvisioned) {
        this.isProvisioned = isProvisioned;
    }

    public @Nullable Boolean getIsRebooting() {
        return isRebooting;
    }

    public void setIsRebooting(Boolean isRebooting) {
        this.isRebooting = isRebooting;
    }

    public @Nullable Boolean getIsSshEnabled() {
        return isSshEnabled;
    }

    public void setIsSshEnabled(Boolean isSshEnabled) {
        this.isSshEnabled = isSshEnabled;
    }

    public @Nullable Boolean getCanAdopt() {
        return canAdopt;
    }

    public void setCanAdopt(Boolean canAdopt) {
        this.canAdopt = canAdopt;
    }

    public @Nullable Boolean getIsAttemptingToConnect() {
        return isAttemptingToConnect;
    }

    public void setIsAttemptingToConnect(Boolean isAttemptingToConnect) {
        this.isAttemptingToConnect = isAttemptingToConnect;
    }

    public @Nullable Boolean getIsHidden() {
        return isHidden;
    }

    public void setIsHidden(Boolean isHidden) {
        this.isHidden = isHidden;
    }

    public @Nullable Long getLastMotion() {
        return lastMotion;
    }

    public void setLastMotion(Long lastMotion) {
        this.lastMotion = lastMotion;
    }

    public @Nullable Integer getMicVolume() {
        return micVolume;
    }

    public void setMicVolume(Integer micVolume) {
        this.micVolume = micVolume;
    }

    public @Nullable Boolean getIsMicEnabled() {
        return isMicEnabled;
    }

    public void setIsMicEnabled(Boolean isMicEnabled) {
        this.isMicEnabled = isMicEnabled;
    }

    public @Nullable Boolean getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(Boolean isRecording) {
        this.isRecording = isRecording;
    }

    public @Nullable Boolean getIsMotionDetected() {
        return isMotionDetected;
    }

    public void setIsMotionDetected(Boolean isMotionDetected) {
        this.isMotionDetected = isMotionDetected;
    }

    public @Nullable Integer getPhyRate() {
        return phyRate;
    }

    public void setPhyRate(Integer phyRate) {
        this.phyRate = phyRate;
    }

    public @Nullable Boolean getHdrMode() {
        return hdrMode;
    }

    public void setHdrMode(Boolean hdrMode) {
        this.hdrMode = hdrMode;
    }

    public @Nullable Boolean getIsProbingForWifi() {
        return isProbingForWifi;
    }

    public void setIsProbingForWifi(Boolean isProbingForWifi) {
        this.isProbingForWifi = isProbingForWifi;
    }

    public @Nullable Integer getChimeDuration() {
        return chimeDuration;
    }

    public void setChimeDuration(Integer chimeDuration) {
        this.chimeDuration = chimeDuration;
    }

    public @Nullable Boolean getIsDark() {
        return isDark;
    }

    public void setIsDark(Boolean isDark) {
        this.isDark = isDark;
    }

    public String toStringDetailed() {
        return "UniFiProtectCamera [thumbnailUrl=" + motionThumbnailUrl + ", heatmapUrl=" + motionHeatmapUrl
                + ", snapshotUrl=" + snapshotUrl + ", aSnapshotUrl=" + aSnapshotUrl + ", upSince=" + upSince + ", mac="
                + mac + ", host=" + host + ", type=" + type + ", name=" + name + ", lastSeen=" + lastSeen
                + ", connectedSince=" + connectedSince + ", state=" + state + ", hardwareRevision=" + hardwareRevision
                + ", firmwareVersion=" + firmwareVersion + ", firmwareBuild=" + firmwareBuild + ", isUpdating="
                + isUpdating + ", isAdopting=" + isAdopting + ", isAdopted=" + isAdopted + ", isProvisioned="
                + isProvisioned + ", isRebooting=" + isRebooting + ", isSshEnabled=" + isSshEnabled + ", canAdopt="
                + canAdopt + ", isAttemptingToConnect=" + isAttemptingToConnect + ", isHidden=" + isHidden
                + ", lastMotion=" + lastMotion + ", micVolume=" + micVolume + ", isMicEnabled=" + isMicEnabled
                + ", isRecording=" + isRecording + ", isMotionDetected=" + isMotionDetected + ", ledSettings="
                + ledSettings + ", phyRate=" + phyRate + ", id=" + id + ", hdrMode=" + hdrMode + ", isProbingForWifi="
                + isProbingForWifi + ", chimeDuration=" + chimeDuration + ", isDark=" + isDark + "]";
    }

    @Override
    public String toString() {
        return "UniFiProtectCamera [name=" + name + ", id=" + id + "]";
    }

    public void setStatusLight(boolean status) {
    }

    public @Nullable String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @Nullable String getMotionThumbnailUrl() {
        return motionThumbnailUrl;
    }

    public void setMotionThumbnailUrl(String motionThumbnailUrl) {
        this.motionThumbnailUrl = motionThumbnailUrl;
    }

    public @Nullable String getMotionHeatmapUrl() {
        return motionHeatmapUrl;
    }

    public void setMotionHeatmapUrl(String motionHeatmapUrl) {
        this.motionHeatmapUrl = motionHeatmapUrl;
    }

    public @Nullable String getSnapshotUrl() {
        return snapshotUrl;
    }

    public void setSnapshotUrl(String snapshotUrl) {
        this.snapshotUrl = snapshotUrl;
    }

    public @Nullable String getaSnapshotUrl() {
        return aSnapshotUrl;
    }

    public void setaSnapshotUrl(String aSnapshotUrl) {
        this.aSnapshotUrl = aSnapshotUrl;
    }

    public @Nullable LedSettings getLedSettings() {
        return ledSettings;
    }

    public void setLedSettings(LedSettings ledSettings) {
        this.ledSettings = ledSettings;
    }

    public @Nullable IspSettings getIspSettings() {
        return ispSettings;
    }

    public void setIspSettings(IspSettings ispSettings) {
        this.ispSettings = ispSettings;
    }

    public @Nullable String getVideoMode() {
        return videoMode;
    }

    public void setVideoMode(String videoMode) {
        this.videoMode = videoMode;
    }

    public @Nullable RecordingSettings getRecordingSettings() {
        return recordingSettings;
    }

    public void setRecordingSettings(RecordingSettings recordingSettings) {
        this.recordingSettings = recordingSettings;
    }

    public @Nullable LcdMessage getLcdMessage() {
        return lcdMessage;
    }

    public void setLcdMessage(LcdMessage lcdMessage) {
        this.lcdMessage = lcdMessage;
    }

    public @Nullable Long getLastRing() {
        return lastRing;
    }

    public void setLastRing(Long lastRing) {
        this.lastRing = lastRing;
    }

    @SuppressWarnings("null")
    @NonNullByDefault
    class RecordingSettings {
        private @Nullable String mode;
        private @Nullable Boolean enableMotionDetection;

        public @Nullable String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public @Nullable Boolean getEnableMotionDetection() {
            return enableMotionDetection;
        }

        public void setEnableMotionDetection(@Nullable Boolean enableMotionDetection) {
            this.enableMotionDetection = enableMotionDetection;
        }
    }

    @SuppressWarnings("null")
    @NonNullByDefault
    // "lcdMessage":{"type":"LEAVE_PACKAGE_AT_DOOR","text":"LEAVE PACKAGE AT DOOR","resetAt":null}
    public class LcdMessage {
        private @Nullable String type;
        private @Nullable String text;
        private @Nullable Long resetAt;

        @Override
        public String toString() {
            return "LcdMessage [type=" + type + ", text=" + text + ", resetAt=" + resetAt + "]";
        }

        public @Nullable String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public @Nullable String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public @Nullable Long getResetAt() {
            return resetAt;
        }

        public void setResetAt(Long resetAt) {
            this.resetAt = resetAt;
        }
    }

    @SuppressWarnings("null")
    @NonNullByDefault
    class IspSettings {
        // ispSettings":{"aeMode":"auto","irLedMode":"auto","irLedLevel":255,"wdr":1
        // ,"icrSensitivity":0,"brightness":50,"contrast":50,"hue":50,"saturation":50
        // ,"sharpness":50,"denoise":50,"isFlippedVertical":false,"isFlippedHorizontal":false
        // ,"isAutoRotateEnabled":false,"isLdcEnabled":true,"is3dnrEnabled":true
        // ,"isExternalIrEnabled":false,"isAggressiveAntiFlickerEnabled":false,"isPauseMotionEnabled":false
        // ,"dZoomCenterX":50,"dZoomCenterY":50,"dZoomScale":0,"dZoomStreamId":4,"focusMode":"touch"
        // ,"focusPosition":0,"touchFocusX":239,"touchFocusY":562,"zoomPosition":26}
        //
        private @Nullable String aeMode;

        @Override
        public String toString() {
            return "IspSettings [aeMode=" + aeMode + ", irLedMode=" + irLedMode + ", irLedLevel=" + irLedLevel + "]";
        }

        private @Nullable String irLedMode;
        private @Nullable Integer irLedLevel;

        public @Nullable String getAeMode() {
            return aeMode;
        }

        public void setAeMode(String aeMode) {
            this.aeMode = aeMode;
        }

        public @Nullable String getIrLedMode() {
            return irLedMode;
        }

        public void setIrLedMode(String irLedMode) {
            this.irLedMode = irLedMode;
        }

        public @Nullable Integer getIrLedLevel() {
            return irLedLevel;
        }

        public void setIrLedLevel(Integer irLedLevel) {
            this.irLedLevel = irLedLevel;
        }
    }

    @SuppressWarnings("null")
    @NonNullByDefault
    // smartDetectSettings":{"objectTypes":["person","vehicle","package"],"audioTypes":["smoke_cmonx"]}}
    class SmartDetectSettings {
        private String @Nullable [] objectTypes;
        private String @Nullable [] audioTypes;

        public String @Nullable [] getObjectTypes() {
            return objectTypes;
        }

        public void setObjectTypes(String @Nullable [] objectTypes) {
            this.objectTypes = objectTypes;
        }

        public String @Nullable [] getAudioTypes() {
            return audioTypes;
        }

        public void setAudioTypes(String @Nullable [] audioTypes) {
            this.audioTypes = audioTypes;
        }
    }

    @SuppressWarnings("null")
    @NonNullByDefault
    class LedSettings {
        private @Nullable Boolean isEnabled;

        @Override
        public String toString() {
            return "LedSettings [isEnabled=" + isEnabled + ", blinkRate=" + blinkRate + "]";
        }

        private @Nullable Integer blinkRate;

        public @Nullable Boolean getIsEnabled() {
            return isEnabled;
        }

        public void setIsEnabled(Boolean isEnabled) {
            this.isEnabled = isEnabled;
        }

        public @Nullable Integer getBlinkRate() {
            return blinkRate;
        }

        public void setBlinkRate(Integer blinkRate) {
            this.blinkRate = blinkRate;
        }
    }

    @SuppressWarnings("null")
    @NonNullByDefault
    class SpeakerSettings {
        private @Nullable Boolean isEnabled;

        @Override
        public String toString() {
            return "SpeakerSettings [isEnabled=" + isEnabled + "]";
        }

        public @Nullable Boolean getIsEnabled() {
            return isEnabled;
        }

        public void setIsEnabled(Boolean isEnabled) {
            this.isEnabled = isEnabled;
        }
    }

    public @Nullable String getRingThumbnailUrl() {
        return ringThumbnailUrl;
    }

    public void setRingThumbnailUrl(String ringThumbnailUrl) {
        this.ringThumbnailUrl = ringThumbnailUrl;
    }

    public @Nullable String getRingHeatmapUrl() {
        return ringHeatmapUrl;
    }

    public void setRingHeatmapUrl(String ringHeatmapUrl) {
        this.ringHeatmapUrl = ringHeatmapUrl;
    }

    public void setSmartDetectSettings(SmartDetectSettings smartDetectSettings) {
        this.smartDetectSettings = smartDetectSettings;
    }

    public @Nullable String getSmartDetectThumbnailUrl() {
        return smartDetectThumbnailUrl;
    }

    public void setSmartDetectThumbnailUrl(String smartDetectThumbnailUrl) {
        this.smartDetectThumbnailUrl = smartDetectThumbnailUrl;
    }

    public @Nullable String getSmartDetectHeatmapUrl() {
        return smartDetectHeatmapUrl;
    }

    public void setSmartDetectHeatmapUrl(String smartDetectHeatmapUrl) {
        this.smartDetectHeatmapUrl = smartDetectHeatmapUrl;
    }

    public @Nullable UniFiProtectPrivacyZone[] getPrivacyZones() {
        return privacyZones;
    }

    public void setPrivacyZones(@Nullable UniFiProtectPrivacyZone[] privacyZones) {
        this.privacyZones = privacyZones;
    }
}
