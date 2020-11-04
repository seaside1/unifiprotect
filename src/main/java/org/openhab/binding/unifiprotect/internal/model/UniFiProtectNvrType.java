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
package org.openhab.binding.unifiprotect.internal.model;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link UniFiProtectNvrType}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectNvrType {

    public static final UniFiProtectNvrType UNKNOWN = new UniFiProtectNvrType(Type.UNKNOWN, "", "");

    @Override
    public String toString() {
        return "UniFiProtectNvrType [type=" + type + ", authValue=" + authValue + ", authName=" + authName + "]";
    }

    private final UniFiProtectNvrType.Type type;
    private final String authValue;
    private final String authName;

    public UniFiProtectNvrType(Type type, String authName, String authValue) {
        this.type = type;
        this.authName = authName;
        this.authValue = authValue;
    }

    public enum Type {
        CLOUD_KEY_GEN2_PLUS,
        UNIFI_OS,
        UNKNOWN;
    }

    public String getAuthHeaderName() {
        return authName;
    }

    public String getAuthHeaderValue() {
        return authValue;
    }
}
