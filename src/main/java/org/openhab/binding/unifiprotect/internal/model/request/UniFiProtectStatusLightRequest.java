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
import org.eclipse.jetty.client.HttpClient;
import org.openhab.binding.unifiprotect.internal.UniFiProtectNvrThingConfig;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvrType;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;

/**
 * The {@link UniFiProtectStatusLightRequest}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectStatusLightRequest extends UniFiProtectRequest {

    private static final String JSON_RAW_ENABLED = "{\"ledSettings\": {\"isEnabled\": true }}";
    private static final String JSON_RAW_DISBLED = "{\"ledSettings\": {\"isEnabled\": false }}";

    public UniFiProtectStatusLightRequest(HttpClient httpClient, UniFiProtectCamera camera,
            UniFiProtectNvrThingConfig config, UniFiProtectNvrType nvrType, boolean enabled) {
        super(httpClient, config);
        setPath(API_CAMERAS.concat(camera.getId()));
        setHeader(nvrType.getAuthHeaderName(), nvrType.getAuthHeaderValue());
        setJsonRaw(enabled ? JSON_RAW_ENABLED : JSON_RAW_DISBLED);
    }

    @Override
    protected String getHttpMethod() {
        return HTTP_METHOD_PATCH;
    }
}
