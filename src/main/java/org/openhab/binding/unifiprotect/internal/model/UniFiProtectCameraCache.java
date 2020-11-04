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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;

/**
 * The {@link UniFiProtectCameraCache}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectCameraCache {

    @Override
    public String toString() {
        final String toString = "";
        macToCamera.entrySet().stream().forEach(entry -> toString.concat("key".concat(entry.getKey()))
                .concat(" value".concat(entry.getValue().toString())));
        return "UniFiProtectCameraCache [size= " + macToCamera.size() + " macToCamera=" + toString + "]";
    }

    private final Map<String, UniFiProtectCamera> macToCamera = new HashMap<>();

    private String generateKey(UniFiProtectCamera camera) {
        return camera.getMac().toLowerCase();
    }

    public @Nullable UniFiProtectCamera getCamera(String mac) {
        return macToCamera.get(mac.toLowerCase());
    }

    public void put(UniFiProtectCamera camera) {
        macToCamera.put(generateKey(camera), camera);
    }

    public void clear() {
        macToCamera.clear();
    }

    public void putAll(List<UniFiProtectCamera> cameras) {
        cameras.stream().forEach(camera -> put(camera));
    }

    public Collection<UniFiProtectCamera> getCameras() {
        return macToCamera.values();
    }
}
