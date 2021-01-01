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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final Map<String, UniFiProtectEvent> eventIdToEvent = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectEventCache.class);

    public void put(UniFiProtectEvent event) {
        logger.debug("Adding event to cache: {}", event);
        cameraToEvent.put(generateCameraKey(event), event);
        eventIdToEvent.put(generateEventKey(event), event);
    }

    private String generateEventKey(UniFiProtectEvent event) {
        return event.getId().toLowerCase();
    }

    private String generateCameraKey(UniFiProtectEvent event) {
        return event.getCamera().toLowerCase();
    }

    public @Nullable UniFiProtectEvent getEvent(String cameraId) {
        return cameraToEvent.get(cameraId);
    }

    public void clear() {
        cameraToEvent.clear();
        eventIdToEvent.clear();
    }

    public void putAll(List<UniFiProtectEvent> events) {
        events.stream().forEach(event -> put(event));
    }

    public @Nullable UniFiProtectEvent getEventFromEventId(String id) {
        return eventIdToEvent.get(id.toLowerCase());
    }

    public UniFiProtectEvent[] getEvents() {
        return (UniFiProtectEvent[]) eventIdToEvent.values().toArray();
    }
}
