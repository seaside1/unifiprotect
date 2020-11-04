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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

    public static @Nullable UniFiProtectLoginContext getLoginContextFromJson(Gson gson, String jsonContent) {
        JsonObject jsonObject = parseJson(gson, jsonContent);
        String id = jsonObject.get(PROPERTY_JSON_ID).getAsString();
        if (id != null && !id.isEmpty()) {
            UniFiProtectLoginContext context = new UniFiProtectLoginContext();
            context.setId(id);
            return context;
        }
        return null;
    }

    public static JsonObject parseJson(Gson gson, String jsonContent) {
        return new JsonParser().parse(jsonContent).getAsJsonObject();
    }

    public static UniFiProtectCamera[] getCamerasFromJson(Gson gson, JsonObject jsonObject) {
        if (jsonObject.has(PROPERTY_JSON_CAMERA) && jsonObject.get(PROPERTY_JSON_CAMERA).isJsonArray()) {
            return gson.fromJson(jsonObject.getAsJsonArray(PROPERTY_JSON_CAMERA), UniFiProtectCamera[].class);
        }
        return new UniFiProtectCamera[0];
    }

    public static @Nullable UniFiProtectNvrDevice getNvrDeviceFromJson(Gson gson, JsonObject jsonObject) {
        if (jsonObject.has(PROPERTY_JSON_NVR_DEVICE)) {
            return gson.fromJson(jsonObject.getAsJsonObject(PROPERTY_JSON_NVR_DEVICE), UniFiProtectNvrDevice.class);
        }
        return null;
    }

    public static @Nullable UniFiProtectNvrUser[] getNvrUsersFromJson(Gson gson, JsonObject jsonObject) {
        if (jsonObject.has(PROPERTY_JSON_NVR_USERS)) {
            return gson.fromJson(jsonObject.getAsJsonArray(PROPERTY_JSON_NVR_USERS), UniFiProtectNvrUser[].class);
        }
        return new UniFiProtectNvrUser[0];
    }

    public static UniFiProtectEvent[] getEventsFromJson(Gson gson, String jsonContent) {
        JsonArray asJsonArray = new JsonParser().parse(jsonContent).getAsJsonArray();
        return gson.fromJson(asJsonArray, UniFiProtectEvent[].class);
    }

}
