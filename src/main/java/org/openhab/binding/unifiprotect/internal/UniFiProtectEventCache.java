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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectEvent;

/**
 * The {@link UniFiProtectEventCache} is a specific cache for caching clients.
 *
 * The cache uses mac address as key
 *
 * @author Joseph Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectEventCache {

    private final Map<String, UniFiProtectEvent> cameraToEvent = new HashMap<>();

    public void put(UniFiProtectEvent event) {
        cameraToEvent.put(generateKey(event), event);
    }

    private String generateKey(UniFiProtectEvent event) {
        return event.getCamera().toLowerCase();
    }

    public @Nullable UniFiProtectEvent getEvent(String camera) {
        return cameraToEvent.get(camera);
    }

    public void clear() {
        cameraToEvent.clear();
    }

    public void putAll(List<UniFiProtectEvent> events) {
        events.stream().forEach(event -> put(event));
    }
}
