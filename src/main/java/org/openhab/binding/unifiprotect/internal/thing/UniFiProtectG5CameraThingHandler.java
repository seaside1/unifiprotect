/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.unifiprotect.internal.UniFiProtectBindingConstants;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectG5CameraThingHandler}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectG5CameraThingHandler extends UniFiProtectG4CameraThingHandler {

    private static final String NOT_INITIALIZED = "Not initialized";
    protected UniFiProtectG5CameraThingConfig config = new UniFiProtectG5CameraThingConfig();
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectG5CameraThingHandler.class);

    public UniFiProtectG5CameraThingHandler(Thing thing) {
        super(thing);
        UniFiProtectCamera camera = getCamera();
        logger.debug("Initializing G5 Camera name: {} type: {}", camera != null ? camera.getName() : NOT_INITIALIZED,
                camera != null ? camera.getType() : NOT_INITIALIZED);
    }

    public static boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return UniFiProtectBindingConstants.THING_TYPE_G5_CAMERA.equals(thingTypeUID);
    }
}
