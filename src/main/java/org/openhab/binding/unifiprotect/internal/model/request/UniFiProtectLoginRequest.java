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
package org.openhab.binding.unifiprotect.internal.model.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.openhab.binding.unifiprotect.internal.UniFiProtectBindingConstants;
import org.openhab.binding.unifiprotect.internal.thing.UniFiProtectNvrThingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectLoginRequest}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectLoginRequest extends UniFiProtectRequest {

    public static final String AUTH_PATH_UNIFI_OS = "/api/auth/login";

    public static final String REMEMBER = "remember";
    public static final String REMEMBER_ME = "rememberMe";

    private static final String STRICT = "strict";
    public static final String PASSWORD = "password";
    public static final String USERNAME = "username";
    public static final String TOKEN = "token";

    private final Logger logger = LoggerFactory.getLogger(UniFiProtectLoginRequest.class);

    public UniFiProtectLoginRequest(String token, HttpClient httpClient, UniFiProtectNvrThingConfig config) {
        super(httpClient, config);
        setPath(AUTH_PATH_UNIFI_OS);
        setBodyParameter(USERNAME, config.getUserName());
        setBodyParameter(PASSWORD, config.getPassword());
        setBodyParameter(STRICT, false);
        setBodyParameter(REMEMBER, false);
        setBodyParameter(REMEMBER_ME, false);
        setBodyParameter(TOKEN, UniFiProtectBindingConstants.EMPTY_STRING);
    }

    public String getToken() {
        final ContentResponse response = getResponse();

        return response != null && response.getHeaders() != null
                && response.getHeaders().get(HEADER_X_CSRF_TOKEN) != null
                        ? response.getHeaders().get(HEADER_X_CSRF_TOKEN)
                        : response != null && response.getHeaders().get(HEADER_X_CSRF_TOKEN.toLowerCase()) != null
                                ? response.getHeaders().get(HEADER_X_CSRF_TOKEN.toLowerCase())
                                : UniFiProtectBindingConstants.EMPTY_STRING;
    }
}
