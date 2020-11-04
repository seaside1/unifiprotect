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
package org.openhab.binding.unifiprotect.internal;

import org.eclipse.smarthome.core.library.types.RawType;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectImageHandler}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UniFiProtectImageHandler {
    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_PNG = "image/png";
    private volatile RawType anonSnapshot;
    private volatile RawType snapshot;
    private volatile RawType thumbnail;
    private volatile RawType heatmap;
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectImageHandler.class);

    public synchronized void setThumbnail(UniFiProtectImage thumbnail) {
        this.thumbnail = new RawType(thumbnail.getData(), thumbnail.getMimeType());
    }

    public synchronized void setSnapshot(UniFiProtectImage snapshot) {
        this.snapshot = new RawType(snapshot.getData(), snapshot.getMimeType());
    }

    public synchronized void setAnonSnapshot(UniFiProtectImage image) {
        logger.debug("Setting image: {}", image);
        anonSnapshot = new RawType(image.getData(), image.getMimeType());
    }

    public synchronized void setHeatmap(UniFiProtectImage image) {
        logger.debug("Setting image: {}", image);
        heatmap = new RawType(image.getData(), image.getMimeType());
    }

    public synchronized RawType getAnonSnapshot() {
        return anonSnapshot;
    }

    public synchronized RawType getSnapshot() {
        return snapshot;
    }

    public synchronized RawType getThumbnail() {
        return thumbnail;
    }

    public synchronized RawType getHeatmap() {
        return heatmap;
    }
}
