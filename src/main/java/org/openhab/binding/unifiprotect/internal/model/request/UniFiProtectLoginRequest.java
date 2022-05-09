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

    private static final String REMEMBER = "remember";
    private static final String STRICT = "strict";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectLoginRequest.class);

    public UniFiProtectLoginRequest(String token, HttpClient httpClient, UniFiProtectNvrThingConfig config) {
        super(httpClient, config);
        setPath(AUTH_PATH_UNIFI_OS);
        setBodyParameter(USERNAME, config.getUserName());
        setBodyParameter(PASSWORD, config.getPassword());
        setBodyParameter(STRICT, false);
        setBodyParameter(REMEMBER, false);
        setHeader(HEADER_X_CSRF_TOKEN, token);
    }
}
