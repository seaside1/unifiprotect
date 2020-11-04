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
import org.openhab.binding.unifiprotect.internal.UniFiProtectUtil;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvrType;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;

/**
 * The {@link UniFiProtectEventsRequest}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectEventsRequest extends UniFiProtectRequest {

    private static final String END = "end";
    private static final String START = "start";
    private static final String API_EVENTS = "/api/events";
    private static final long TEN_SECONDS_MILLIS = 20000;

    public UniFiProtectEventsRequest(HttpClient httpClient, UniFiProtectCamera camera,
            UniFiProtectNvrThingConfig config, UniFiProtectNvrType nvrType) {
        super(httpClient, config);
        setPath(API_EVENTS);
        setHeader(nvrType.getAuthHeaderName(), nvrType.getAuthHeaderValue());
        setQueryParameter(START, UniFiProtectUtil.calculateStartTimeForEvent(config.getEventsTimePeriodLength()));
        setQueryParameter(END, System.currentTimeMillis() + TEN_SECONDS_MILLIS);
    }
}
