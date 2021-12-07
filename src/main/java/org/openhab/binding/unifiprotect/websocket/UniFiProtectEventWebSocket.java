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
package org.openhab.binding.unifiprotect.websocket;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
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

import com.google.gson.JsonSyntaxException;

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
    private final UniFiProtectJsonParser uniFiProtectJsonParser;
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectEventWebSocket.class);
    private final PropertyChangeSupport propertyChangeSupport;

    public UniFiProtectEventWebSocket(UniFiProtectJsonParser uniFiProtectJsonParser) {
        this.closeLatch = new CountDownLatch(1);
        this.uniFiProtectJsonParser = uniFiProtectJsonParser;
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
        propertyChangeSupport.firePropertyChange(UniFiProtectAction.PROPERTY_SOCKET_CLOSED, null,
                "statusCode: " + statusCode + " reason: " + reason);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        logger.debug("Got connect: {}", session);
        this.session = session;
        propertyChangeSupport.firePropertyChange(UniFiProtectAction.PROPERTY_SOCKET_CONNECTED, null,
                session.toString());
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        logger.debug("Got msg: {}", msg);
    }

    @OnWebSocketFrame
    public synchronized void onFrame(Frame frame) {
        if (session == null) {
            return;
        }
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
            logger.debug("Frame: {}", jsonContent);
            UniFiProtectAction action = null;
            try {
                action = uniFiProtectJsonParser.getActionFromJson(jsonContent);
            } catch (JsonSyntaxException syntax) {
                logger.error("Failed to parse json");
            }
            if (action == null) {
                return;
            }
            if (action.getModelKey().equals(UniFiProtectAction.MODEL_KEY_EVENT)) {
                logger.debug("ModelKeyEvent Got: {} and event action: {}", action.getAction(), upFrame);
                if (action.getAction().equals(UniFiProtectAction.ACTION_ADD)) {
                    propertyChangeSupport.firePropertyChange(UniFiProtectAction.PROPERTY_EVENT_ACTION_ADD, null,
                            action);
                } else if (action.getAction().equals(UniFiProtectAction.ACTION_UPDATE)) {
                    propertyChangeSupport.firePropertyChange(UniFiProtectAction.PROPERTY_EVENT_ACTION_UPDATE, null,
                            action);
                }
            } else if (action.getModelKey().equals(UniFiProtectAction.MODEL_KEY_CAMERA)) {
                logger.debug("ModelKeyCamera Got: {} and event action: {}", action.getAction(), upFrame);
            }
        }
    }

    @OnWebSocketError
    public void onError(Throwable cause) {
        logger.debug("Error in websocket: {}", cause.getMessage(), cause);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public synchronized void dispose() {
        try {
            session.close();
            session = null;
        } catch (Exception x) {
            try {
                session.disconnect();
                session = null;
            } catch (IOException e) {
            }
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
    }
}
