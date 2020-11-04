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
 * The {@link UniFiProtectSnapshotRequest}
 *
 * @author Joseph Seaside Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectSnapshotRequest extends UniFiProtectRequest {

    private static final String SNAPSHOT = "/snapshot";
    private static final String G4_MODEL = "G4";
    private static final String QUERY_PARAM_FORCE = "force";
    private static final String QUERY_PARAM_TIME_SINCE = "ts";
    private static final String TRUE = "true";

    public UniFiProtectSnapshotRequest(HttpClient httpClient, UniFiProtectCamera camera, UniFiProtectNvrType nvrType,
            UniFiProtectNvrThingConfig config) {
        super(httpClient, config);
        setPath(API_CAMERAS.concat(camera.getId()).concat(SNAPSHOT));
        setHeader(nvrType.getAuthHeaderName(), nvrType.getAuthHeaderValue());
        final boolean isG4 = camera.getType().contains(G4_MODEL);
        setQueryParameter(QUERY_PARAM_WIDTH, isG4 ? config.getG4SnapshotWidth() : config.getDefaultSnapshotWidth());
        setQueryParameter(QUERY_PARAM_HEIGHT, isG4 ? config.getG4SnapshotHeight() : config.getDefaultSnapshotHeight());
        setQueryParameter(QUERY_PARAM_FORCE, TRUE);
        setQueryParameter(QUERY_PARAM_TIME_SINCE,
                UniFiProtectUtil.calculateStartTimeForEvent(config.getEventsTimePeriodLength()));
    }
}
