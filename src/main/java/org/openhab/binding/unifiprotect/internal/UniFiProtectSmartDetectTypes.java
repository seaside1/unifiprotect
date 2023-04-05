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

import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link UniFiProtectSmartDetectTypes}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectSmartDetectTypes {

    @Nullable
    private Boolean vehicle = null;
    @Nullable
    private Boolean person = null;
    @Nullable
    private Boolean package1 = null;

    private static final UniFiProtectSmartDetectTypes EMPTY = new UniFiProtectSmartDetectTypes(Boolean.FALSE,
            Boolean.FALSE, Boolean.FALSE);
    public static final String VEHICLE_STR = "vehicle";
    public static final String PERSON_STR = "person";
    public static final String PACKAGE_STR = "package";

    private static final String EMPTY_JSON = "{\"smartDetectSettings\":{\"objectTypes\":[]}}";
    private static final String ONE_JSON = "{\"smartDetectSettings\":{\"objectTypes\":[\"%s\"]}}";
    private static final String TWO_JSON = "{\"smartDetectSettings\":{\"objectTypes\":[\"%s\",\"%s\"]}}";
    private static final String THREE_JSON = "{\"smartDetectSettings\":{\"objectTypes\":[\"%s\",\"%s\",\"%s\"]}}";

    private static final String DUMMY = "UNDEF OBJ SmartDetectType";

    private UniFiProtectSmartDetectTypes(Boolean vehicle, Boolean person, Boolean package1) {
        this.setVehicle(vehicle);
        this.setPerson(person);
        this.setPackage(package1);
    }

    private String[] getTypes(String... args) {
        return Arrays.stream(args).filter(r -> r != null).toArray(String[]::new);
    }

    public String getJsonRaw() {
        final String[] types = getObjectTypesAsArray();
        if (types.length == 0) {
            return EMPTY_JSON;
        } else if (types.length == 1) {
            return String.format(ONE_JSON, types[0]);
        } else if (types.length == 2) {
            return String.format(TWO_JSON, types[0], types[1]);
        } else if (types.length == 3) {
            return String.format(THREE_JSON, types[0], types[1], types[2]);
        }
        return DUMMY;
    }

    public static UniFiProtectSmartDetectTypes fromArray(String @Nullable [] objectTypes) {
        if (objectTypes == null || objectTypes.length <= 0) {
            return EMPTY;
        }

        final boolean vehicleType = Arrays.stream(objectTypes).anyMatch(type -> type.equals(VEHICLE_STR));
        final boolean personType = Arrays.stream(objectTypes).anyMatch(type -> type.equals(PERSON_STR));
        final boolean packageType = Arrays.stream(objectTypes).anyMatch(type -> type.equals(PACKAGE_STR));
        return new UniFiProtectSmartDetectTypes(vehicleType, personType, packageType);
    }

    @Override
    public String toString() {
        return "SmartDetect " + VEHICLE_STR + ": " + vehicle + " " + PERSON_STR + ": " + person + ": " + PACKAGE_STR
                + ": " + package1;
    }

    public String[] getObjectTypesAsArray() {
        return getTypes(vehicle ? VEHICLE_STR : null, person ? PERSON_STR : null, package1 ? PACKAGE_STR : null);
    }

    public boolean containsPerson() {
        return person == null ? false : person.booleanValue();
    }

    public boolean containsVehicle() {
        return vehicle == null ? false : vehicle.booleanValue();
    }

    public boolean containsPackage() {
        return package1 == null ? false : package1.booleanValue();
    }

    public void setVehicle(@Nullable Boolean vehicle) {
        this.vehicle = vehicle;
    }

    public void setPerson(@Nullable Boolean person) {
        this.person = person;
    }

    public void setPackage(@Nullable Boolean package1) {
        this.package1 = package1;
    }
}
