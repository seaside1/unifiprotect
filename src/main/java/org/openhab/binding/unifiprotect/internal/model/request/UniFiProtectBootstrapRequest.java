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

/**
 * The {@link UniFiProtectBootstrapRequest}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectBootstrapRequest extends UniFiProtectRequest {

    private static final String API_BOOTSTRAP = "/api/bootstrap";

    public UniFiProtectBootstrapRequest(HttpClient httpClient, UniFiProtectNvrThingConfig config,
            UniFiProtectNvrType nvrType) {
        super(httpClient, config);
        setPath(API_BOOTSTRAP);
        setHeader(nvrType.getAuthHeaderName(), nvrType.getAuthHeaderValue());
    }
}
