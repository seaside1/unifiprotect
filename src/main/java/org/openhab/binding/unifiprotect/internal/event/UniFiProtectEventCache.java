/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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

import java.util.Collection;
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
        final String camera = event.getCamera();
        if (camera == null) {
            logger.debug("Ignoring non camera event: {}", event);
            return;
        }
        CameraEventsObject cameraEventsObject = cameraToEvents.get(generateCameraKey(camera));
        if (cameraEventsObject == null) {
            cameraEventsObject = new CameraEventsObject();
            cameraToEvents.put(generateCameraKey(camera), cameraEventsObject);
        }
        final String eventTypeString = event.getType();
        if (eventTypeString == null) {
            logger.debug("Ignoring event since type is null: {}", event);
            return;
        }
        final String idString = event.getId();
        if (idString == null) {
            logger.debug("Ignoring event since id is null: {}", event);
            return;
        }

        if (eventTypeString.equals(UniFiProtectBindingConstants.EVENT_TYPE_MOTION)) {
            UniFiProtectEvent oldEvent = cameraEventsObject.getLastMotionEvent();
            if (oldEvent != null) {
                logger.debug("Removing id {} camera: {}", oldEvent.getId(), oldEvent.getCamera());
                final String oldId = oldEvent.getId();
                if (oldId != null) {
                    eventIdToEvent.remove(generateEventKey(oldId));
                }
            }
            cameraEventsObject.setLastMotionEvent(event);
        } else if (eventTypeString.equals(UniFiProtectBindingConstants.EVENT_TYPE_RING)) {
            UniFiProtectEvent oldEvent = cameraEventsObject.getLastRingEvent();
            if (oldEvent != null) {
                logger.debug("Removing id {} camera: {}", oldEvent.getId(), oldEvent.getCamera());
                final String oldId = oldEvent.getId();
                if (oldId != null) {
                    eventIdToEvent.remove(generateEventKey(oldId));
                }
            }
            cameraEventsObject.setLastRingEvent(event);
        }
        eventIdToEvent.put(generateEventKey(idString), event);
    }

    private String generateEventKey(String eventIdString) {
        return eventIdString.toLowerCase();
    }

    private String generateCameraKey(String cameraString) {
        return cameraString.toLowerCase();
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

    public Collection<UniFiProtectEvent> getEvents() {
        return eventIdToEvent.values();
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
