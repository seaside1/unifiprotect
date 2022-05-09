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

/**
 * The {@link UniFiProtectAlertsRequest}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectAlertsRequest extends UniFiProtectRequest {

    private static final String JSON_RAW_ENABLE = "{\"enableNotifications\": true}";
    private static final String JSON_RAW_DISBLE = "{\"enableNotifications\": false}";

    private static final String API_USERS = "/proxy/protect/api/users/";

    public UniFiProtectAlertsRequest(HttpClient httpClient, UniFiProtectNvrThingConfig config, String token,
            String userId, boolean enable) {
        super(httpClient, config);
        setPath(API_USERS.concat(userId));
        setHeader(UniFiProtectRequest.HEADER_X_CSRF_TOKEN, token);
        setJsonRaw(enable ? JSON_RAW_ENABLE : JSON_RAW_DISBLE);
    }

    @Override
    protected String getHttpMethod() {
        return HTTP_METHOD_PATCH;
    }
}
