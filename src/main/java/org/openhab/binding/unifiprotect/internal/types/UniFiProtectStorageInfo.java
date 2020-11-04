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

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link UniFiProtectStorageInfo}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectStorageInfo {
    private Long totalSize = new Long(-1);
    private Long totalSpaceUsed = new Long(-1);
    private UniFiProtectHardDrive[] hardDrives = new UniFiProtectHardDrive[0];

    @Override
    public String toString() {
        return "StorageInfo [totalSize=" + totalSize + ", totalSpaceUsed=" + totalSpaceUsed + ", harddrives="
                + Arrays.toString(hardDrives) + "]";
    }

    public Long getTotalSpaceUsed() {
        return totalSpaceUsed;
    }

    public void setTotalSpaceUsed(Long totalSpaceUsed) {
        this.totalSpaceUsed = totalSpaceUsed;
    }

    public UniFiProtectHardDrive[] getHardDrives() {
        return hardDrives;
    }

    public void setHardDrives(UniFiProtectHardDrive[] hardDrives) {
        this.hardDrives = hardDrives;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }
}
