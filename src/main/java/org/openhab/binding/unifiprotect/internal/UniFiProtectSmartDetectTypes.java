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
package org.openhab.binding.unifiprotect.internal;

import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link UniFiProtectSmartDetectTypes}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public enum UniFiProtectSmartDetectTypes {
    PERSON,
    VEHICLE,
    PERSON_AND_VEHICLE,
    EMPTY,
    UNDEF;

    private static final String VEHICLE_STR = "vehicle";
    private static final String PERSON_STR = "person";
    private static final String EMPTY_JSON = "{\"smartDetectSettings\":{\"objectTypes\":[]}}";
    private static final String PERSON_JSON = "{\"smartDetectSettings\":{\"objectTypes\":[\"person\"]}}";
    private static final String VEHICLE_JSON = "{\"smartDetectSettings\":{\"objectTypes\":[\"vehicle\"]}}";
    private static final String PERSON_AND_VEHICLE_JSON = "{\"smartDetectSettings\":{\"objectTypes\":[\"person\",\"vehicle\"]}}";
    private static final String DUMMY = "UNDEF OBJ SmartDetectType";

    public boolean containsPerson() {
        return this == PERSON || this == PERSON_AND_VEHICLE;
    }

    public boolean containsVehicle() {
        return this == VEHICLE || this == PERSON_AND_VEHICLE;
    }

    public String getJsonRaw() {
        switch (this) {
            case PERSON_AND_VEHICLE:
                return PERSON_AND_VEHICLE_JSON;
            case PERSON:
                return PERSON_JSON;
            case EMPTY:
                return EMPTY_JSON;
            case UNDEF:
                return DUMMY;
            case VEHICLE:
                return VEHICLE_JSON;
            default:
                return DUMMY;
        }
    }

    public static UniFiProtectSmartDetectTypes fromArray(String @Nullable [] objectTypes) {
        if (objectTypes == null || objectTypes.length <= 0) {
            return EMPTY;
        }

        final boolean person = Arrays.stream(objectTypes).anyMatch(type -> type.equals(PERSON_STR));
        final boolean vehicle = Arrays.stream(objectTypes).anyMatch(type -> type.equals(VEHICLE_STR));
        if (person && vehicle) {
            return PERSON_AND_VEHICLE;
        } else if (person) {
            return PERSON;
        } else if (vehicle) {
            return VEHICLE;
        }
        return EMPTY;
    }

    public String[] getObjectTypesAsArray() {
        switch (this) {
            case EMPTY:
                return new String[] {};
            case PERSON:
                return new String[] { PERSON_STR };
            case PERSON_AND_VEHICLE:
                return new String[] { PERSON_STR, VEHICLE_STR };
            case UNDEF:
                return new String[] {};
            case VEHICLE:
                return new String[] { VEHICLE_STR };
            default:
                return new String[] {};
        }
    }
}
