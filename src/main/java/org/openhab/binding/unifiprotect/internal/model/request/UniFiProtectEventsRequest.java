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
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectEventsRequest}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectEventsRequest extends UniFiProtectRequest {

    private static final String END = "end";
    private static final String START = "start";
    private static final String API_EVENTS = "/proxy/protect/api/events";
    private static final long TIME_DELAY_SECONDS_MILLIS = 10000;

    private static final Logger logger = LoggerFactory.getLogger(UniFiProtectEventsRequest.class);

    public UniFiProtectEventsRequest(HttpClient httpClient, UniFiProtectCamera camera,
            UniFiProtectNvrThingConfig config, String token) {
        super(httpClient, config);
        setPath(API_EVENTS);
        setHeader(UniFiProtectRequest.HEADER_X_CSRF_TOKEN, token);
        long now = System.currentTimeMillis();
        long start = UniFiProtectUtil.calculateStartTimeForEvent(config.getEventsTimePeriodLength());
        setQueryParameter(START, start);
        setQueryParameter(END, now + TIME_DELAY_SECONDS_MILLIS);
        logger.info("Requesting events start seconds: {}s end seconds: {}s", (now - start), TIME_DELAY_SECONDS_MILLIS);
    }
}
