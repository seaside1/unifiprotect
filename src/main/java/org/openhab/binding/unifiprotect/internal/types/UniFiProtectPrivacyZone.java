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

// "privacyZones":[{"id":1,"name":"OH-PRIV","color":"#586CED","points":[[0,0],[1,0],[1,1],[0,1]]}]
/**
 * The {@link UniFiProtectPrivacyZone}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectPrivacyZone {

    public static final String OH_PRIVACY_ZONE_NAME = "OH-PRIV";
    private @Nullable String id;
    private String name = "";
    private @Nullable String color;
    private @Nullable Integer[][] points = new Integer[4][2];

    public @Nullable String getId() {
        return id;
    }

    public void setId(@Nullable String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @Nullable String getColor() {
        return color;
    }

    public void setColor(@Nullable String color) {
        this.color = color;
    }

    public @Nullable Integer[][] getPoints() {
        return points;
    }

    public void setPoints(@Nullable Integer[][] points) {
        this.points = points;
    }
}
