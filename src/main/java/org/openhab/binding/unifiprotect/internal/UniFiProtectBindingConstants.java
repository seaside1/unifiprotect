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
package org.openhab.binding.unifiprotect.internal;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link UniFiProtectBindingConstants} class defines common constants, which are
 * used across the Ruckus unifiprotect binding.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectBindingConstants {

    public static final String BINDING_ID = "unifiprotect";

    /* Thing Types */
    public static final ThingTypeUID THING_TYPE_NVR = new ThingTypeUID(BINDING_ID, "nvr");
    public static final ThingTypeUID THING_TYPE_G3_CAMERA = new ThingTypeUID(BINDING_ID, "g3camera");
    public static final ThingTypeUID THING_TYPE_G4_CAMERA = new ThingTypeUID(BINDING_ID, "g4camera");
    public static final ThingTypeUID THING_TYPE_G5_CAMERA = new ThingTypeUID(BINDING_ID, "g5camera");
    public static final ThingTypeUID THING_TYPE_G4_DOORBELL = new ThingTypeUID(BINDING_ID, "g4doorbell");
    public static final String G4_DOORBELL = "UVC G4 Doorbell";
    public static final String G3_CAMERA_PREFIX = "UVC G3";
    public static final String G4_CAMERA_PREFIX = "UVC G4";
    public static final String G5_CAMERA_PREFIX = "UVC G5";
    public static final String EVENT_TYPE_MOTION = "motion";
    public static final String EVENT_TYPE_SMART_DETECT_ZONE = "smartDetectZone";
    public static final String EVENT_TYPE_RING = "ring";
    public static final int MOTION_EVENT_WAIT_TIME = 5;

    public static final Set<ThingTypeUID> SUPPORTED_DEVICE_THING_TYPES_UIDS = Collections.unmodifiableSet(
            Stream.of(THING_TYPE_G3_CAMERA, THING_TYPE_G4_CAMERA, THING_TYPE_NVR, THING_TYPE_G4_DOORBELL)
                    .collect(Collectors.toSet()));

    /* Parameters */
    public static final String PARAMETER_HOST = "host";
    public static final String PARAMETER_PORT = "port";
    public static final String PARAMETER_USERNAME = "username";
    public static final String PARAMETER_PASSWORD = "password";
    public static final String PARAMETER_REFRESH = "refresh";
    public static final String CAMERA_PROP_HOST = "host";
    public static final String CAMERA_PROP_MAC = "mac";
    public static final String CAMERA_PROP_NAME = "name";

    public static final String PROPERTY_EVENT_ACTION_ADD = "EVENT_ACTION_ADD";

    public static final String EMPTY_STRING = "";

}
