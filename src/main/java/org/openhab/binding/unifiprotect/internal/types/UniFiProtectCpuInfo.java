/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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

/**
 * The {@link UniFiProtectCpuInfo}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectCpuInfo {

    private Double averageLoad = new Double(-1.0);
    private Double temperature = new Double(-1.0);

    @Override
    public String toString() {
        return "UniFiProtectCpuInfo [averageLoad=" + averageLoad + ", temperature=" + temperature + "]";
    }

    public Double getAverageLoad() {
        return averageLoad;
    }

    public void setAverageLoad(Double averageLoad) {
        this.averageLoad = averageLoad;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}
