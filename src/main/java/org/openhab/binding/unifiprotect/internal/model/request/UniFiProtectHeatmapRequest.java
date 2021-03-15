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
import org.openhab.binding.unifiprotect.internal.thing.UniFiProtectNvrThingConfig;

/**
 * The {@link UniFiProtectHeatmapRequest}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectHeatmapRequest extends UniFiProtectRequest {

    private static final String API_HEATMAP = "/proxy/protect/api/heatmaps/";

    public UniFiProtectHeatmapRequest(HttpClient httpClient, String token, String heatmap,
            UniFiProtectNvrThingConfig config) {
        super(httpClient, config);
        setPath(API_HEATMAP.concat(heatmap));
        setHeader(UniFiProtectRequest.HEADER_X_CSRF_TOKEN, token);
    }
}
