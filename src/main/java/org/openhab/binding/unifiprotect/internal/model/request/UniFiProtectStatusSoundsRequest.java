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
 * The {@link UniFiProtectStatusSoundsRequest}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectStatusSoundsRequest extends UniFiProtectRequest {

    private static final String JSON_RAW_ENABLED = "{\"speakerSettings\": {\"areSystemSoundsEnabled\": true }}";
    private static final String JSON_RAW_DISBLED = "{\"speakerSettings\": {\"areSystemSoundsEnabled\": false }}";

    public UniFiProtectStatusSoundsRequest(HttpClient httpClient, String cameraId, UniFiProtectNvrThingConfig config,
            String token, boolean enabled) {
        super(httpClient, config);
        setPath(API_CAMERAS.concat(cameraId));
        setHeader(UniFiProtectRequest.HEADER_X_CSRF_TOKEN, token);
        setJsonRaw(enabled ? JSON_RAW_ENABLED : JSON_RAW_DISBLED);
    }

    @Override
    protected String getHttpMethod() {
        return HTTP_METHOD_PATCH;
    }
}
