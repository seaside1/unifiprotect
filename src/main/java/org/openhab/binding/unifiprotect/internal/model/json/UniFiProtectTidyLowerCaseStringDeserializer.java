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

import java.lang.reflect.Type;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * The {@link UniFiProtectTidyLowerCaseStringDeserializer}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectTidyLowerCaseStringDeserializer implements JsonDeserializer<String> {

    @Override
    public String deserialize(@Nullable JsonElement json, @Nullable Type type,
            @Nullable JsonDeserializationContext context) throws JsonParseException {
        if (json == null || type == null || context == null) {
            return "";
        }
        String s = json.getAsJsonPrimitive().getAsString();
        return StringUtils.lowerCase(StringUtils.strip(s));
    }
}