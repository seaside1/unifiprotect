/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.CompletableFuture;

import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectJsonParser;
import org.openhab.binding.unifiprotect.internal.thing.UniFiProtectNvrThingConfig;
import org.openhab.binding.unifiprotect.websocket.UniFiProtectAction;
import org.openhab.binding.unifiprotect.websocket.UniFiProtectEventWebSocket;
import org.openhab.binding.unifiprotect.websocket.UniFiProtectEventWsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectEventManager} is a specific cache for caching clients.
 *
 * The cache uses mac address as key
 *
 * @author Joseph Hagberg - Initial contribution
 */
public class UniFiProtectEventManager implements PropertyChangeListener {
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectEventManager.class);
    private final UniFiProtectEventWsClient wsClient;
    private UniFiProtectEventWebSocket socket;
    private final PropertyChangeSupport propertyChangeSupport;
    private volatile CompletableFuture<Void> reInitializationFuture = null;

    public UniFiProtectEventManager(UniFiProtectJsonParser uniFiProtectJsonParser, UniFiProtectNvrThingConfig config) {
        propertyChangeSupport = new PropertyChangeSupport(this);
        wsClient = new UniFiProtectEventWsClient(uniFiProtectJsonParser, config);
    }

    public boolean isStarted() {
        return socket != null;
    }

    public synchronized boolean start(HttpClient httpClient) {
        try {
            logger.debug("Trying to start new websocket");
            socket = wsClient.start(httpClient);
            socket.addPropertyChangeListener(this);
            return true;
        } catch (Exception e) {
            logger.error("Failed to start event api websocket listener", e);
        }
        return false;
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        logger.debug("Property Changed: {}", evt != null ? evt.getPropertyName() : "null");
        if (evt.getPropertyName().equals(UniFiProtectAction.PROPERTY_EVENT_ACTION_ADD)) {
            UniFiProtectAction action = (UniFiProtectAction) evt.getNewValue();
            logger.debug("Got action property add change: {}", action);
            actionEventDetected(action, UniFiProtectAction.PROPERTY_EVENT_ACTION_ADD);
        } else if (evt.getPropertyName().equals(UniFiProtectAction.PROPERTY_EVENT_ACTION_UPDATE)) {
            UniFiProtectAction action = (UniFiProtectAction) evt.getNewValue();
            logger.debug("Got action property upd change: {}", action);
            actionEventDetected(action, UniFiProtectAction.PROPERTY_EVENT_ACTION_UPDATE);
        } else if (evt.getPropertyName().equals(UniFiProtectAction.PROPERTY_SOCKET_CONNECTED)) {
            logger.debug("Socket connected!");
        } else if (evt.getPropertyName().equals(UniFiProtectAction.PROPERTY_SOCKET_CLOSED)) {
            logger.debug("Socket disconnected!");
            actionSocketClosed(evt);
        } else {
            logger.debug("Unhandled Property change in UniFiProtectEventManager: {}", evt.getPropertyName());
        }
    }

    private synchronized void cancelReinitFuture() {
        if (reInitializationFuture != null) {
            reInitializationFuture.cancel(true);
            reInitializationFuture = null;
        }
    }

    private void actionSocketClosed(PropertyChangeEvent evt) {
        propertyChangeSupport.firePropertyChange(evt);
    }

    private void actionEventDetected(UniFiProtectAction action, String property) {
        propertyChangeSupport.firePropertyChange(property, null, action);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
    }

    public synchronized void stop() {
        try {
            socket.removePropertyChangeListener(this);
            socket.dispose();
            wsClient.stop();
            cancelReinitFuture();
            socket = null;
        } catch (Exception e) {
            logger.debug("Failed to stop manager", e);
        }
    }

    public synchronized void dispose() {
        try {
            stop();
        } catch (Exception e) {
            logger.debug("Failed to stop websocket client on dispose", e);
        }
    }
}
