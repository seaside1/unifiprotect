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
package org.openhab.binding.unifiprotect.internal.image;

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

    private UniFiProtectImageCache motionHeatmapCache = new UniFiProtectImageCache();
    private UniFiProtectImageCache motionThumbnailCache = new UniFiProtectImageCache();

    private UniFiProtectImageCache smartDetectThumbnailCache = new UniFiProtectImageCache();
    private UniFiProtectImageCache ringThumbnailCache = new UniFiProtectImageCache();

    private UniFiProtectImageCache snapshotCache = new UniFiProtectImageCache();

    private UniFiProtectImageCache anonSnapshotCache = new UniFiProtectImageCache();

    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_PNG = "image/png";
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectImageHandler.class);

    public synchronized void setMotionThumbnail(UniFiProtectCamera camera, UniFiProtectImage thumbnail) {
        motionThumbnailCache.put(camera, thumbnail);
    }

    public synchronized void setRingThumbnail(UniFiProtectCamera camera, UniFiProtectImage thumbnail) {
        ringThumbnailCache.put(camera, thumbnail);
    }

    public synchronized void setSmartDetectThumbnail(UniFiProtectCamera camera, UniFiProtectImage thumbnail) {
        smartDetectThumbnailCache.put(camera, thumbnail);
    }

    public synchronized void setSnapshot(UniFiProtectCamera camera, UniFiProtectImage snapshot) {
        snapshotCache.put(camera, snapshot);
    }

    public synchronized void setAnonSnapshot(UniFiProtectCamera camera, UniFiProtectImage image) {
        anonSnapshotCache.put(camera, image);
    }

    public synchronized void setMotionHeatmap(UniFiProtectCamera camera, UniFiProtectImage image) {
        motionHeatmapCache.put(camera, image);
    }

    public synchronized RawType getAnonSnapshot(UniFiProtectCamera camera) {
        final UniFiProtectImage image = anonSnapshotCache.getImage(camera);
        return image != null ? new RawType(image.getData(), image.getMimeType()) : null;
    }

    public synchronized RawType getSnapshot(UniFiProtectCamera camera) {
        final UniFiProtectImage image = snapshotCache.getImage(camera);
        return image != null ? new RawType(image.getData(), image.getMimeType()) : null;
    }

    public synchronized RawType getMotionThumbnail(UniFiProtectCamera camera) {
        final UniFiProtectImage image = motionThumbnailCache.getImage(camera);
        return image != null ? new RawType(image.getData(), image.getMimeType()) : null;
    }

    public synchronized RawType getRingThumbnail(UniFiProtectCamera camera) {
        final UniFiProtectImage image = ringThumbnailCache.getImage(camera);
        return image != null ? new RawType(image.getData(), image.getMimeType()) : null;
    }

    public synchronized RawType getSmartDetectThumbnail(UniFiProtectCamera camera) {
        final UniFiProtectImage image = smartDetectThumbnailCache.getImage(camera);
        return image != null ? new RawType(image.getData(), image.getMimeType()) : null;
    }

    public synchronized RawType getMotionHeatmap(UniFiProtectCamera camera) {
        final UniFiProtectImage image = motionHeatmapCache.getImage(camera);
        return image != null ? new RawType(image.getData(), image.getMimeType()) : null;
    }
}
