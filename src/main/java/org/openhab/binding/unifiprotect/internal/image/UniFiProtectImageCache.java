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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectImage;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;

/**
 * The {@link UniFiProtectImageCache} is a specific cache for caching clients.
 *
 * The cache uses mac address as key
 *
 * @author Joseph Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectImageCache {

    private final Map<String, UniFiProtectImage> keyToImage = new HashMap<>();

    public void put(UniFiProtectCamera camera, UniFiProtectImage unifiProtectImage) {
        keyToImage.put(generateKey(camera), unifiProtectImage);
    }

    private String generateKey(UniFiProtectCamera camera) {
        return camera.getId().toLowerCase();
    }

    public @Nullable UniFiProtectImage getImage(UniFiProtectCamera camera) {
        return keyToImage.get(generateKey(camera));
    }

    public void clear() {
        keyToImage.clear();
    }
}
