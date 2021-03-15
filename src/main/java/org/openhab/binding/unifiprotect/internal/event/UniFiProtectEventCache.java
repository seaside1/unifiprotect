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
package org.openhab.binding.unifiprotect.internal.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unifiprotect.internal.UniFiProtectBindingConstants;
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

    private final Map<String, CameraEventsObject> cameraToEvents = new HashMap<>();
    private final Map<String, UniFiProtectEvent> eventIdToEvent = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectEventCache.class);

    public void put(UniFiProtectEvent event) {
        logger.debug("Adding event to cache: {}", event);
        CameraEventsObject cameraEventsObject = cameraToEvents.get(generateCameraKey(event));
        if (cameraEventsObject == null) {
            cameraEventsObject = new CameraEventsObject();
            cameraToEvents.put(generateCameraKey(event), cameraEventsObject);
        }
        if (event.getType().equals(UniFiProtectBindingConstants.EVENT_TYPE_MOTION)) {
            UniFiProtectEvent oldEvent = cameraEventsObject.getLastMotionEvent();
            if (oldEvent != null) {
                logger.debug("Removing id {} camera: {}", oldEvent.getId(), oldEvent.getCamera());
                eventIdToEvent.remove(generateEventKey(oldEvent));
            }
            cameraEventsObject.setLastMotionEvent(event);
        } else if (event.getType().equals(UniFiProtectBindingConstants.EVENT_TYPE_RING)) {
            UniFiProtectEvent oldEvent = cameraEventsObject.getLastRingEvent();
            if (oldEvent != null) {
                logger.debug("Removing id {} camera: {}", oldEvent.getId(), oldEvent.getCamera());
                eventIdToEvent.remove(generateEventKey(oldEvent));
            }
            cameraEventsObject.setLastRingEvent(event);
        }
        eventIdToEvent.put(generateEventKey(event), event);
    }

    private String generateEventKey(UniFiProtectEvent event) {
        return event.getId().toLowerCase();
    }

    private String generateCameraKey(UniFiProtectEvent event) {
        return event.getCamera().toLowerCase();
    }

    public @Nullable UniFiProtectEvent getLatestMotionEvent(String cameraId) {
        CameraEventsObject eventsObject = cameraToEvents.get(cameraId);
        if (eventsObject == null) {
            return null;
        }
        return eventsObject.getLastMotionEvent();
    }

    public @Nullable UniFiProtectEvent getLatestRingEvent(String cameraId) {
        CameraEventsObject eventsObject = cameraToEvents.get(cameraId);
        if (eventsObject == null) {
            return null;
        }
        return eventsObject.getLastRingEvent();
    }

    public void clear() {
        cameraToEvents.clear();
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

    private class CameraEventsObject {
        @Nullable
        private volatile UniFiProtectEvent lastMotionEvent;
        @Nullable
        private volatile UniFiProtectEvent lastRingEvent;

        public @Nullable UniFiProtectEvent getLastMotionEvent() {
            return lastMotionEvent;
        }

        public void setLastMotionEvent(UniFiProtectEvent lastMotionEvent) {
            this.lastMotionEvent = lastMotionEvent;
        }

        public @Nullable UniFiProtectEvent getLastRingEvent() {
            return lastRingEvent;
        }

        public void setLastRingEvent(UniFiProtectEvent lastRingEvent) {
            this.lastRingEvent = lastRingEvent;
        }
    }
}
