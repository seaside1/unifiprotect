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
package org.openhab.binding.unifiprotect.internal.model.json;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectNvrDevice;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectNvrUser;
import org.openhab.binding.unifiprotect.websocket.UniFiProtectAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * The {@link UniFiProtectJsonParser}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectJsonParser {

    public static final String PROPERTY_JSON_CAMERA = "cameras";
    public static final String PROPERTY_JSON_START = "start";

    public static final String PROPERTY_JSON_NVR_DEVICE = "nvr";
    public static final String PROPERTY_JSON_NVR_USERS = "users";

    public static final String PROPERTY_JSON_ID = "id";
    private static final String PROPERTY_JSON_ACTION = "action";
    protected static final String G4_CAMERA_TYPE_PREFIX = "";
    protected static final String G4_DOORBELL_TYPE = "";
    protected static final String G3_CAMERA_TYPE_PREFIX = "";
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectJsonParser.class);

    private final Gson gson;

    private @Nullable JsonObject bootStrapJsonObject;

    public UniFiProtectJsonParser() {
        UniFiProtectCameraInstanceCreator uniFiProtectCameraInstanceCreator = new UniFiProtectCameraInstanceCreator();

        UniFiProtectNvrInstanceCreator uniFiProtectNvrInstanceCreator = new UniFiProtectNvrInstanceCreator();
        gson = new GsonBuilder().registerTypeAdapter(UniFiProtectNvrDevice.class, uniFiProtectNvrInstanceCreator)
                .registerTypeAdapter(UniFiProtectCamera.class, uniFiProtectCameraInstanceCreator)
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
    }

    public boolean parseBootstrap(String bootstrapJsonContent) {
        try {
            bootStrapJsonObject = parseJson(gson, bootstrapJsonContent);
        } catch (JsonSyntaxException x) {
            logger.error("Failed to parse bootstrap json");
            logger.debug("Failed to parse bootstrap json: {}", bootstrapJsonContent, x);
            return false;
        }
        return true;
    }

    public static JsonObject parseJson(Gson gson, String jsonContent) {
        return new JsonParser().parse(jsonContent).getAsJsonObject();
    }

    public static UniFiProtectCamera[] getCamerasFromJson(Gson gson, @Nullable JsonObject jsonObject) {
        UniFiProtectCamera[] cameras = null;
        if (jsonObject != null && jsonObject.has(PROPERTY_JSON_CAMERA)
                && jsonObject.get(PROPERTY_JSON_CAMERA).isJsonArray()) {
            cameras = gson.fromJson(jsonObject.getAsJsonArray(PROPERTY_JSON_CAMERA), UniFiProtectCamera[].class);
        }
        return cameras == null ? new UniFiProtectCamera[0] : cameras;
    }

    public static @Nullable UniFiProtectNvrDevice getNvrDeviceFromJson(Gson gson, @Nullable JsonObject jsonObject) {
        if (jsonObject != null && jsonObject.has(PROPERTY_JSON_NVR_DEVICE)) {
            return gson.fromJson(jsonObject.getAsJsonObject(PROPERTY_JSON_NVR_DEVICE), UniFiProtectNvrDevice.class);
        }
        return null;
    }

    public static UniFiProtectNvrUser[] getNvrUsersFromJson(Gson gson, @Nullable JsonObject jsonObject) {
        UniFiProtectNvrUser[] users = null;
        if (jsonObject != null && jsonObject.has(PROPERTY_JSON_NVR_USERS)) {
            users = gson.fromJson(jsonObject.getAsJsonArray(PROPERTY_JSON_NVR_USERS), UniFiProtectNvrUser[].class);
        }
        return users == null ? new UniFiProtectNvrUser[0] : users;
    }

    public UniFiProtectEvent[] getEventsFromJson(String jsonContent) {
        UniFiProtectEvent[] events = null;
        JsonArray asJsonArray = new JsonParser().parse(jsonContent).getAsJsonArray();
        events = gson.fromJson(asJsonArray, UniFiProtectEvent[].class);
        return events == null ? new UniFiProtectEvent[0] : events;
    }

    public @Nullable UniFiProtectAction getActionFromJson(String jsonContent) {
        JsonObject jsonObject = parseJson(gson, jsonContent);
        if (jsonObject.has(PROPERTY_JSON_ACTION)) {
            return gson.fromJson(jsonObject.getAsJsonObject(), UniFiProtectAction.class);
        }
        return null;
    }

    public UniFiProtectCamera @Nullable [] getCamerasFromBootstrap() {
        if (bootStrapJsonObject == null) {
            logger.error("Bootstrap object is null due to failed parsing");
        }
        return bootStrapJsonObject == null ? null
                : UniFiProtectJsonParser.getCamerasFromJson(gson, bootStrapJsonObject);
    }

    public @Nullable UniFiProtectNvrDevice getNvrDeviceFromBootstrap() {
        if (bootStrapJsonObject == null) {
            logger.error("Bootstrap object is null due to failed parsing");
        }
        return bootStrapJsonObject == null ? null
                : UniFiProtectJsonParser.getNvrDeviceFromJson(gson, bootStrapJsonObject);
    }

    public UniFiProtectNvrUser @Nullable [] getNvrUsersFromBootstrap() {
        if (bootStrapJsonObject == null) {
            logger.error("Bootstrap object is null due to failed parsing");
        }
        return bootStrapJsonObject == null ? null
                : UniFiProtectJsonParser.getNvrUsersFromJson(gson, bootStrapJsonObject);
    }
}
