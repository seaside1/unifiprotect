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

import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;

import com.google.gson.InstanceCreator;

/**
 * The {@link UniFiClientInstanceCreator}
 *
 * @author Joseph Seaside Hagberg - Initial contribution
 */
public class UniFiProtectCameraInstanceCreator implements InstanceCreator<UniFiProtectCamera> {

    @Override
    public UniFiProtectCamera createInstance(Type type) {
        if (UniFiProtectCamera.class.equals(type)) {
            return new UniFiProtectCamera();
        }
        return null;
    }
}
