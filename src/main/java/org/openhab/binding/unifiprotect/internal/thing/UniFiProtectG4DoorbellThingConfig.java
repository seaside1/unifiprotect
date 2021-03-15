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
package org.openhab.binding.unifiprotect.internal.thing;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link UniFiProtectG4DoorbellThingConfig}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectG4DoorbellThingConfig extends UniFiProtectG4CameraThingConfig {

    private @Nullable String lcdCustomMessage;

    @Override
    public String toString() {
        return "UniFiProtectG4DoorbellThingConfig [lcdCustomMessage=" + lcdCustomMessage + "]" + super.toString();
    }

    public @Nullable String getLcdCustomMessage() {
        return lcdCustomMessage;
    }

    public void setLcdCustomMessage(String lcdCustomMessage) {
        this.lcdCustomMessage = lcdCustomMessage;
    }
}
