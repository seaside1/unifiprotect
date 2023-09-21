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
package org.openhab.binding.unifiprotect.internal.thing;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link UniFiProtectBaseThingConfig}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectBaseThingConfig {
    private boolean eventDownloadThumbnail = true;
    private boolean eventDownloadHeatMap = true;

    @Override
    public String toString() {
        return "UniFiProtectBaseThingConfig [mac=" + mac + "]";
    }

    /* Camera Id should mac address */
    protected String mac = "";

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isValid() {
        return !mac.isEmpty();
    }

    public boolean isEventDownloadThumbnail() {
        return eventDownloadThumbnail;
    }

    public void setEventDownloadThumbnail(boolean eventDownloadThumbnail) {
        this.eventDownloadThumbnail = eventDownloadThumbnail;
    }

    public boolean isEventDownloadHeatMap() {
        return eventDownloadHeatMap;
    }

    public void setEventDownloadHeatMap(boolean eventDownloadHeatMap) {
        this.eventDownloadHeatMap = eventDownloadHeatMap;
    }
}
