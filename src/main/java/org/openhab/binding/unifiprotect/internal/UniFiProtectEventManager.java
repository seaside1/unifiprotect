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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.unifiprotect.websocket.UniFiProtectAction;
import org.openhab.binding.unifiprotect.websocket.UniFiProtectEventWebSocket;
import org.openhab.binding.unifiprotect.websocket.UniFiProtectEventWsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * The {@link UniFiProtectEventManager} is a specific cache for caching clients.
 *
 * The cache uses mac address as key
 *
 * @author Joseph Hagberg - Initial contribution
 */
public class UniFiProtectEventManager implements PropertyChangeListener {

    public static final String EVENT_MOTION = "EVENT_MOTION";
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectEventManager.class);
    private final UniFiProtectEventWsClient wsClient;
    private UniFiProtectEventWebSocket socket;
    private final PropertyChangeSupport propertyChangeSupport;

    public UniFiProtectEventManager(HttpClient httpClient, Gson gson, UniFiProtectNvrThingConfig config) {
        propertyChangeSupport = new PropertyChangeSupport(this);
        wsClient = new UniFiProtectEventWsClient(httpClient, gson, config);
    }

    public void start() {
        try {
            socket = wsClient.start();
            socket.addPropertyChangeListener(this);
        } catch (Exception e) {
            logger.error("Failed to start event api websocket listener", e);
        }
    }

    @Override
    public void propertyChange(@Nullable PropertyChangeEvent evt) {
        logger.debug("Property Changed: {}", evt.getPropertyName());
        if (evt.getPropertyName() == UniFiProtectAction.PROPERTY_EVENT_ACTION_ADD) {
            UniFiProtectAction action = (UniFiProtectAction) evt.getNewValue();
            logger.debug("Got action property add change: {}", action);
            motionDetected(action);
            String eventId = action.getId();
        } else if (evt.getPropertyName() == UniFiProtectAction.PROPERTY_EVENT_ACTION_UPDATE) {
            UniFiProtectAction action = (UniFiProtectAction) evt.getNewValue();
            logger.debug("Got action property upd change: {}", action);
            motionDetected(action);
            String eventId = action.getId();
        }
    }

    private synchronized void motionDetected(UniFiProtectAction action) {
        propertyChangeSupport.firePropertyChange(EVENT_MOTION, null, action);
        // nvrHandler.refresh();
        // nvrHandler.getNvr().refreshProtect();
        // nvrHandler.getNvr().refreshEvents();
        // nvrHandler.refreshCameras();
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
    }

    public void stop() {
        try {
            wsClient.stop();
        } catch (Exception e) {
            logger.debug("Failed to stop manager", e);
        }
    }

    public void dispose() {
        try {
            wsClient.stop();
        } catch (Exception e) {
            logger.debug("Failed to stop websocket client on dispose", e);
        }
    }
}
