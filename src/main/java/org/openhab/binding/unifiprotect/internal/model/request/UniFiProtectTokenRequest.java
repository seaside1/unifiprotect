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
package org.openhab.binding.unifiprotect.internal.model.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.openhab.binding.unifiprotect.internal.UniFiProtectBindingConstants;
import org.openhab.binding.unifiprotect.internal.thing.UniFiProtectNvrThingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectTokenRequest}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectTokenRequest extends UniFiProtectRequest {

    public static final String AUTH_PATH_UNIFI_OS = "/api/auth/login";
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectTokenRequest.class);
    public static final String HEADER_X_CSRF_TOKEN = "X-CSRF-Token";

    public UniFiProtectTokenRequest(HttpClient httpClient, UniFiProtectNvrThingConfig config) {
        super(httpClient, config);
        setPath(AUTH_PATH_UNIFI_OS);
    }

    public String getToken() {
        final ContentResponse response = getResponse();
        return response != null && response.getHeaders() != null
                && response.getHeaders().get(HEADER_X_CSRF_TOKEN) != null
                        ? response.getHeaders().get(HEADER_X_CSRF_TOKEN)
                        : UniFiProtectBindingConstants.EMPTY_STRING;
    }
}
