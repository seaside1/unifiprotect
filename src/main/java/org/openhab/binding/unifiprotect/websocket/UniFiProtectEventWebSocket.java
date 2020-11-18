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
package org.openhab.binding.unifiprotect.websocket;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectJsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * The {@link UniFiProtectEventWebSocket}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@WebSocket
public class UniFiProtectEventWebSocket {

    private static final String UTF_8 = "UTF-8";
    private static final int FRAME_MIN_SIZE = 9;
    private final CountDownLatch closeLatch;
    @SuppressWarnings("unused")
    private Session session;
    private final Gson gson;
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectEventWebSocket.class);
    private final PropertyChangeSupport propertyChangeSupport;

    public UniFiProtectEventWebSocket(Gson gson) {
        this.closeLatch = new CountDownLatch(1);
        this.gson = gson;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        logger.debug("Connection closed: {} - {}", statusCode, reason);
        this.session = null;
        this.closeLatch.countDown(); // trigger latch
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        logger.debug("Got connect: {}", session);
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        logger.debug("Got msg: {}", msg);
    }

    @OnWebSocketFrame
    public void onFrame(Frame frame) {
        if (frame.getPayloadLength() <= FRAME_MIN_SIZE) {
            logger.error("Failed to decode frame, not enough byte: {}", frame.getPayloadLength());
            return;
        }
        final byte[] bytes = new byte[frame.getPayloadLength()];
        frame.getPayload().get(bytes);
        final UniFiProtectFrame upFrame = new UniFiProtectFrame(bytes);
        if (upFrame.getType() == UniFiProtectFrameType.ACTION
                && upFrame.getFormat() == UniFiProtectPayloadFormat.JSON_OBJECT) {
            String jsonContent = null;
            try {
                jsonContent = new String(upFrame.getPayload(true), UTF_8);
            } catch (UnsupportedEncodingException e) {
                logger.error("Failed to decode json as UTF-8", e);
                return;
            }
            UniFiProtectAction action = UniFiProtectJsonParser.getActionFromJson(gson, jsonContent);
            if (action == null) {
                logger.debug("Got non action frame ignored: {}", upFrame);
                return;
            }
            if (action.getModelKey().equals(UniFiProtectAction.MODEL_KEY_EVENT)) {
                logger.debug("Got: {} and event action: {}", action.getAction(), upFrame);
                if (action.getAction().equals(UniFiProtectAction.ACTION_ADD)) {
                    propertyChangeSupport.firePropertyChange(UniFiProtectAction.PROPERTY_EVENT_ACTION_ADD, null,
                            action);
                } else if (action.getAction().equals(UniFiProtectAction.ACTION_UPDATE)) {
                    propertyChangeSupport.firePropertyChange(UniFiProtectAction.PROPERTY_EVENT_ACTION_UPDATE, null,
                            action);
                }
            } else {
                logger.debug("Got non action frame ignored: {}", upFrame);
            }
        } else {
            logger.debug("Got non action/json frame ignored: {}", upFrame);
        }
    }

    @OnWebSocketError
    public void onError(Throwable cause) {
        logger.debug("Error in websocket: {}", cause.getMessage());
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
    }
}
