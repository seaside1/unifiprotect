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

import java.io.File;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link UniFiProtectImage}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectImage {
    @Override
    public String toString() {
        return "UniFiProtectImage [dataLen=" + data != null ? "" + data.length
                : "null" + ", mimeType=" + mimeType + ", file=" + file + "]";
    }

    private final byte[] data;
    private final String mimeType;
    private final File file;

    public UniFiProtectImage(byte[] data, String mimeType, File file) {
        this.data = data;
        this.mimeType = mimeType;
        this.file = file;
    }

    public byte[] getData() {
        return data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public File getFile() {
        return file;
    }
}
