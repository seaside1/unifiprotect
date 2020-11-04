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
package org.openhab.binding.unifiprotect.internal.model.request;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.unifiprotect.internal.UniFiProtectNvrThingConfig;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvrType;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvrType.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectLoginRequest}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectLoginRequest extends UniFiProtectRequest {
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String AUTH_PATH = "/api/auth";
    private static final String HEADER_X_CSRF_TOKEN = "X-CSRF-Token";
    private static final String GEN2_PLUS_AUTH_PREFIX = "Bearer ";
    private static final String REMEMBER = "remember";
    private static final String STRICT = "strict";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectLoginRequest.class);

    public UniFiProtectLoginRequest(HttpClient httpClient, UniFiProtectNvrThingConfig config) {
        super(httpClient, config);
        setPath(AUTH_PATH);
        setBodyParameter(USERNAME, config.getUserName());
        setBodyParameter(PASSWORD, config.getPassword());
        setBodyParameter(STRICT, false);
        setBodyParameter(REMEMBER, false);
    }

    @SuppressWarnings("null")
    public @Nullable UniFiProtectNvrType getNvrTypeFromResponse() {
        if (getResponse() == null) {
            logger.warn("Failed to get type of controller nvr, got null response on login request.");
            return null;
        }
        if (getResponse().getStatus() == 200) {
            String authValue = getResponse().getHeaders().get(HEADER_X_CSRF_TOKEN);
            String authName = HEADER_X_CSRF_TOKEN;
            UniFiProtectNvrType.Type type = authValue == null ? Type.CLOUD_KEY_GEN2_PLUS : Type.UNIFI_OS;
            if (authValue == null) {
                authValue = getResponse().getHeaders().get(HEADER_AUTHORIZATION);
                if (authValue != null) {
                    authValue = GEN2_PLUS_AUTH_PREFIX.concat(authValue);
                }
                authName = HEADER_AUTHORIZATION;
            }
            if (authValue == null) {
                logger.error("Failed to get type of controller nvr, got response 200 but auth header is missing.");
                return null;
            }
            logger.debug("Cookie Size login: {}", httpClient.getCookieStore().getCookies().size());
            return new UniFiProtectNvrType(type, authName, authValue);
        }
        logger.warn("Failed to get type of controller nvr, got error response: {}", getResponse().getStatus());
        return null;
    }
}
