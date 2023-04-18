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
    @Nullable
    private Boolean smokeCoAlarm = null;
    private static final UniFiProtectSmartDetectTypes EMPTY = new UniFiProtectSmartDetectTypes(Boolean.FALSE,
            Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
    public static final String VEHICLE_STR = "vehicle";
    public static final String PERSON_STR = "person";
    public static final String PACKAGE_STR = "package";
    public static final String SMOKE_CO_ALARM_STR_ESC = "\"smoke_cmonx\"";
    public static final String SMOKE_CO_ALARM_STR = "smoke_cmonx";
    private static final String EMPTY_JSON = "{\"smartDetectSettings\":{\"objectTypes\":[],\"audioTypes\":[%s]}}";
    private static final String ONE_JSON = "{\"smartDetectSettings\":{\"objectTypes\":[\"%s\"],\"audioTypes\":[%s]}}";
    private static final String TWO_JSON = "{\"smartDetectSettings\":{\"objectTypes\":[\"%s\",\"%s\"],\"audioTypes\":[%s]}}";
    private static final String THREE_JSON = "{\"smartDetectSettings\":{\"objectTypes\":[\"%s\",\"%s\",\"%s\"],\"audioTypes\":[%s]}}";

    private static final String DUMMY = "UNDEF OBJ SmartDetectType";

    private UniFiProtectSmartDetectTypes(Boolean vehicle, Boolean person, Boolean package1, Boolean smokeCoAlarm) {
        this.vehicle = vehicle;
        this.person = person;
        this.package1 = package1;
        this.smokeCoAlarm = smokeCoAlarm;
    }

    private String[] getTypes(String... args) {
        return Arrays.stream(args).filter(r -> r != null).toArray(String[]::new);
    }

    public String getJsonRaw() {
        final String[] objectTypes = getObjectTypesAsArray();
        final String[] audioTypes = getAudioTypesAsArray();
        if (objectTypes.length == 0) {
            return String.format(EMPTY_JSON, getSmoke(audioTypes[0]));
        } else if (objectTypes.length == 1) {
            return String.format(ONE_JSON, objectTypes[0], getSmoke(audioTypes[0]));
        } else if (objectTypes.length == 2) {
            return String.format(TWO_JSON, objectTypes[0], objectTypes[1], getSmoke(audioTypes[0]));
        } else if (objectTypes.length == 3) {
            return String.format(THREE_JSON, objectTypes[0], objectTypes[1], objectTypes[2], getSmoke(audioTypes[0]));
        }
        return DUMMY;
    }

    private String getSmoke(String smoke) {
        return smoke.equals("") ? "" : SMOKE_CO_ALARM_STR_ESC;
    }

    public static UniFiProtectSmartDetectTypes fromArray(String @Nullable [] objectTypes,
            String @Nullable [] audioTypes) {
        if (objectTypes == null || objectTypes.length <= 0) {
            return EMPTY;
        }
        final boolean vehicleType = Arrays.stream(objectTypes).anyMatch(type -> type.equals(VEHICLE_STR));
        final boolean personType = Arrays.stream(objectTypes).anyMatch(type -> type.equals(PERSON_STR));
        final boolean packageType = Arrays.stream(objectTypes).anyMatch(type -> type.equals(PACKAGE_STR));
        final boolean smokecoAlarmType = Arrays.stream(audioTypes).anyMatch(type -> type.equals(SMOKE_CO_ALARM_STR));
        return new UniFiProtectSmartDetectTypes(vehicleType, personType, packageType, smokecoAlarmType);
    }

    @Override
    public String toString() {
        return "SmartDetect vehile: " + vehicle + " " + PERSON_STR + ": " + person + " " + PACKAGE_STR + ": " + package1
                + " " + SMOKE_CO_ALARM_STR + ": " + smokeCoAlarm;
    }

    public String[] getObjectTypesAsArray() {
        return getTypes(vehicle ? VEHICLE_STR : null, person ? PERSON_STR : null, package1 ? PACKAGE_STR : null);
    }

    public String[] getAudioTypesAsArray() {
        return getTypes(smokeCoAlarm ? SMOKE_CO_ALARM_STR : "");
    }

    public boolean containsSmokeCoAlarm() {
        return smokeCoAlarm == null ? false : smokeCoAlarm.booleanValue();
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

    public void setSmokeCoAlarm(@Nullable Boolean smokeCoAlarm) {
        this.smokeCoAlarm = smokeCoAlarm;
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
