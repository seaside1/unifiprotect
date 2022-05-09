/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.unifiprotect.internal.thing;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link UniFiProtectNvrThingConfig}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectNvrThingConfig {

    private String host = "";
    private String username = "";
    private String password = "";
    private int refresh = 60;
    private int eventsTimePeriodLength = 40;
    private double thumbnailWidth = 640;
    private static final String TEMP_DIR_PROPERTY = "java.io.tmpdir";
    private String imageFolder;
    private int g4SnapshotWidth = 3840;
    private int g4SnapshotHeight = 2160;
    private int defaultSnapshotWidth = 1920;
    private int defaultSnapshotHeight = 1080;

    public @Nullable String g4SnapshotWidthAsString;
    public @Nullable String g4SnapshotHeightAsString;
    public @Nullable String defaultSnapshotWidthAsString;
    public @Nullable String defaultSnapshotHeightAsString;

    public UniFiProtectNvrThingConfig() {
        final String tmpDir = System.getProperty(TEMP_DIR_PROPERTY);
        imageFolder = tmpDir == null ? "/tmp" : tmpDir;
        setDefaultSnapshotHeight(defaultSnapshotHeight);
        setDefaultSnapshotWidth(defaultSnapshotWidth);
        setG4SnapshotHeight(g4SnapshotHeight);
        setG4SnapshotWidth(g4SnapshotWidth);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "UniFiProtectNvrThingConfig [host=" + host + ", username=" + username + ", password=" + "*************"
                + ", refresh=" + refresh + ", eventsTimePeriodLength=" + eventsTimePeriodLength + ", thumbnailWidth="
                + thumbnailWidth + ", imageFolder=" + imageFolder + ", g4SnapshotWidth=" + g4SnapshotWidth
                + ", g4SnapshotHeight=" + g4SnapshotHeight + ", defaultSnapshotWidth=" + defaultSnapshotWidth
                + ", defaultSnapshotHeight=" + defaultSnapshotHeight + ", g4SnapshotWidthAsString="
                + g4SnapshotWidthAsString + ", g4SnapshotHeightAsString=" + g4SnapshotHeightAsString
                + ", defaultSnapshotWidthAsString=" + defaultSnapshotWidthAsString + ", defaultSnapshotHeightAsString="
                + defaultSnapshotHeightAsString + "]";
    }

    public int getRefresh() {
        return refresh;
    }

    public int getEventsTimePeriodLength() {
        return eventsTimePeriodLength;
    }

    public void setEventsTimePeriodLength(int eventsTimePeriodLength) {
        this.eventsTimePeriodLength = eventsTimePeriodLength;
    }

    public double getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(double thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }

    public String getImageFolder() {
        return imageFolder;
    }

    public void setImageFolder(String imageFolder) {
        this.imageFolder = imageFolder;
    }

    public int getG4SnapshotWidth() {
        return g4SnapshotWidth;
    }

    public void setG4SnapshotWidth(int g4SnapshotWidth) {
        this.g4SnapshotWidth = g4SnapshotWidth;
        g4SnapshotWidthAsString = "" + g4SnapshotWidth;
    }

    public int getG4SnapshotHeight() {
        return g4SnapshotHeight;
    }

    public void setG4SnapshotHeight(int g4SnapshotHeight) {
        this.g4SnapshotHeight = g4SnapshotHeight;
        g4SnapshotHeightAsString = "" + g4SnapshotHeight;
    }

    public int getDefaultSnapshotWidth() {
        return defaultSnapshotWidth;
    }

    public void setDefaultSnapshotWidth(int defaultSnapshotWidth) {
        this.defaultSnapshotWidth = defaultSnapshotWidth;
        defaultSnapshotWidthAsString = "" + defaultSnapshotWidth;
    }

    public int getDefaultSnapshotHeight() {
        return defaultSnapshotHeight;
    }

    public void setDefaultSnapshotHeight(int defaultSnapshotHeight) {
        this.defaultSnapshotHeight = defaultSnapshotHeight;
        defaultSnapshotHeightAsString = "" + defaultSnapshotHeight;
    }

    public @Nullable String getG4SnapshotWidthAsString() {
        return g4SnapshotWidthAsString;
    }

    public @Nullable String getG4SnapshotHeightAsString() {
        return g4SnapshotHeightAsString;
    }

    public @Nullable String getDefaultSnapshotWidthAsString() {
        return defaultSnapshotWidthAsString;
    }

    public @Nullable String getDefaultSnapshotHeightAsString() {
        return defaultSnapshotHeightAsString;
    }
}
