/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
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
package org.openhab.binding.unifiprotect.internal.thing;

import static org.openhab.core.thing.ThingStatus.*;
import static org.openhab.core.thing.ThingStatusDetail.CONFIGURATION_ERROR;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unifiprotect.internal.UniFiProtectBindingConstants;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectNvr;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatusDetail;
import org.openhab.core.thing.ThingTypeUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectG3CameraThingHandler}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectG3CameraThingHandler extends UniFiProtectBaseThingHandler {

    protected UniFiProtectG3CameraThingConfig config = new UniFiProtectG3CameraThingConfig();
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectG3CameraThingHandler.class);

    public UniFiProtectG3CameraThingHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void refresh() {
        super.refresh();
    }

    @Override
    protected synchronized @Nullable UniFiProtectCamera getCamera(UniFiProtectNvr nvr) {
        return nvr.getCamera(config);
    }

    @Override
    protected void refreshChannel(UniFiProtectCamera camera, ChannelUID channelUID, UniFiProtectNvr controller) {
        super.refreshChannel(camera, channelUID, controller);
        logger.debug("Refresh channel in g3camera");
    }

    @Override
    public void initialize() {
        Bridge bridge = getBridge();
        if (bridge == null || bridge.getHandler() == null
                || !(bridge.getHandler() instanceof UniFiProtectNvrThingHandler)) {
            updateStatus(OFFLINE, ThingStatusDetail.CONFIGURATION_ERROR,
                    "You must choose a UniFiProtect NVR for this thing.");
            return;
        }
        if (bridge.getStatus() == OFFLINE) {
            updateStatus(OFFLINE, ThingStatusDetail.BRIDGE_OFFLINE,
                    "The UniFiProtect Controller is currently offline.");
            return;
        }
        UniFiProtectG3CameraThingConfig config = getConfigAs(UniFiProtectG3CameraThingConfig.class);
        initialize(config);
    }

    @Override
    protected void initialize(UniFiProtectBaseThingConfig config) {
        if (thing.getStatus() == INITIALIZING) {
            logger.debug("Initializing the UniFiProtect Client Handler with config = {}", config);
            if (!config.isValid()) {
                updateStatus(OFFLINE, CONFIGURATION_ERROR, "You must define a MAC address.");
                return;
            }
            this.config = (UniFiProtectG3CameraThingConfig) config;
            updateStatus(ONLINE);
        }
    }

    public static boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return UniFiProtectBindingConstants.THING_TYPE_G3_CAMERA.equals(thingTypeUID);
    }
}
