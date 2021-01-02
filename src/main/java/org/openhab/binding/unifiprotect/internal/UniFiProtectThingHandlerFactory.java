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
package org.openhab.binding.unifiprotect.internal;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectThingHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@Component(service = ThingHandlerFactory.class, configurationPid = "binding.unifiprotect")
@NonNullByDefault
public class UniFiProtectThingHandlerFactory extends BaseThingHandlerFactory {
    private Map<ThingUID, ServiceRegistration<?>> discoveryServiceRegs = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(UniFiProtectThingHandlerFactory.class);

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        logger.debug("Creating thing supports: {}", UniFiProtectNvrThingHandler.supportsThingType(thingTypeUID));
        if (UniFiProtectNvrThingHandler.supportsThingType(thingTypeUID)) {
            UniFiProtectNvrThingHandler uniFiProtectNvrThingHandler = new UniFiProtectNvrThingHandler((Bridge) thing);
            registerDeviceDiscoveryService(uniFiProtectNvrThingHandler);
            return uniFiProtectNvrThingHandler;
        } else if (UniFiProtectCameraThingHandler.supportsThingType(thingTypeUID)) {
            return new UniFiProtectCameraThingHandler(thing);
        }
        logger.debug("Failed to creat thing returning null");
        return null;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return UniFiProtectNvrThingHandler.supportsThingType(thingTypeUID)
                || UniFiProtectCameraThingHandler.supportsThingType(thingTypeUID);
    }

    private synchronized void registerDeviceDiscoveryService(UniFiProtectNvrThingHandler handler) {
        UniFiProtectDiscoveryService discoveryService = new UniFiProtectDiscoveryService(handler);
        discoveryServiceRegs.put(handler.getThing().getUID(),
                bundleContext.registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<>()));
    }
}
