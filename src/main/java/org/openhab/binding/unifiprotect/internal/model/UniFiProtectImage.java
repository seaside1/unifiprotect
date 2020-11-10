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
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectImage}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectImage {
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectImage.class);

    @Override
    public String toString() {
        return "UniFiProtectImage [mimeType=" + mimeType + ", file=" + file + "]";
    }

    private final String mimeType;
    private final File file;

    public UniFiProtectImage(String mimeType, File file) {
        this.mimeType = mimeType;
        this.file = file;
    }

    public byte[] getData() {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            logger.error("Failed to read file on disk: {}", file.getAbsolutePath());
            return new byte[0];
        }
    }

    public String getMimeType() {
        return mimeType;
    }

    public File getFile() {
        return file;
    }
}
