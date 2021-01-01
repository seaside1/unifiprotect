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

import org.openhab.binding.unifiprotect.internal.model.UniFiProtectImage;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.openhab.core.library.types.RawType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectImageHandler}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UniFiProtectImageHandler {

    private UniFiProtectImageCache heatmapCache = new UniFiProtectImageCache();

    private UniFiProtectImageCache thumbnailCache = new UniFiProtectImageCache();

    private UniFiProtectImageCache snapshotCache = new UniFiProtectImageCache();

    private UniFiProtectImageCache anonSnapshotCache = new UniFiProtectImageCache();

    // private UniFiProtectThumbnailCache imageCache = new UniFiProtectHeatmapCache();

    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_PNG = "image/png";
    // private volatile RawType anonSnapshot;
    // private volatile RawType snapshot;
    // private volatile RawType thumbnail;
    // private volatile RawType heatmap;
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectImageHandler.class);

    public synchronized void setThumbnail(UniFiProtectCamera camera, UniFiProtectImage thumbnail) {
        thumbnailCache.put(camera, thumbnail);
        // this.thumbnail = new RawType(thumbnail.getData(), thumbnail.getMimeType());
    }

    public synchronized void setSnapshot(UniFiProtectCamera camera, UniFiProtectImage snapshot) {
        snapshotCache.put(camera, snapshot);
    }

    public synchronized void setAnonSnapshot(UniFiProtectCamera camera, UniFiProtectImage image) {
        anonSnapshotCache.put(camera, image);
        // logger.debug("Setting image: {}", image);
        // anonSnapshot = new RawType(image.getData(), image.getMimeType());
    }

    public synchronized void setHeatmap(UniFiProtectCamera camera, UniFiProtectImage image) {
        heatmapCache.put(camera, image);
        // logger.debug("Setting image: {}", image);
        // heatmap = new RawType(image.getData(), image.getMimeType());
    }

    public synchronized RawType getAnonSnapshot(UniFiProtectCamera camera) {
        final UniFiProtectImage image = anonSnapshotCache.getImage(camera);
        return image != null ? new RawType(image.getData(), image.getMimeType()) : null;
    }

    public synchronized RawType getSnapshot(UniFiProtectCamera camera) {
        final UniFiProtectImage image = snapshotCache.getImage(camera);
        return image != null ? new RawType(image.getData(), image.getMimeType()) : null;
    }

    public synchronized RawType getThumbnail(UniFiProtectCamera camera) {
        final UniFiProtectImage image = thumbnailCache.getImage(camera);
        return image != null ? new RawType(image.getData(), image.getMimeType()) : null;
    }

    public synchronized RawType getHeatmap(UniFiProtectCamera camera) {
        final UniFiProtectImage image = heatmapCache.getImage(camera);
        return image != null ? new RawType(image.getData(), image.getMimeType()) : null;
    }
}
