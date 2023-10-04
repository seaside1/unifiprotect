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
package org.openhab.binding.unifiprotect.websocket;

import java.io.IOException;
import java.net.URI;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectJsonParser;
import org.openhab.binding.unifiprotect.internal.thing.UniFiProtectNvrThingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectEventWsClient}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
public class UniFiProtectEventWsClient {
    private static final String PROXY_PROTECT_WS_UPDATES = "/proxy/protect/ws/updates";
    private static final String WSS = "wss://";
    private final String uri;
    private HttpClient httpClient;
    private UniFiProtectJsonParser uniFiProtectJsonParser;
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectEventWsClient.class);
    private WebSocketClient client;

    public UniFiProtectEventWsClient(HttpClient httpClient, UniFiProtectJsonParser uniFiProtectJsonParser,
            UniFiProtectNvrThingConfig config) {
        uri = WSS.concat(config.getHost()).concat(PROXY_PROTECT_WS_UPDATES);
        this.httpClient = httpClient;
        this.uniFiProtectJsonParser = uniFiProtectJsonParser;
    }

    public UniFiProtectEventWebSocket start() throws IOException, Exception {
        final HttpClient httpClient = new HttpClient(new SslContextFactory.Client(true));
        client = new WebSocketClient(httpClient);
        final UniFiProtectEventWebSocket socket = new UniFiProtectEventWebSocket(uniFiProtectJsonParser);
        client.start();
        final URI destUri = new URI(uri);
        final ClientUpgradeRequest request = new ClientUpgradeRequest();
        request.setCookies(httpClient.getCookieStore().getCookies());
        logger.debug("Connecting to: {}", destUri);
        client.connect(socket, destUri, request);
        return socket;
    }

    public void stop() throws Exception {
        if (client != null) {
            client.stop();
        }
    }
}
