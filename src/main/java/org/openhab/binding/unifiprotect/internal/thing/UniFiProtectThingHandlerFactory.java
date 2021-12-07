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
package org.openhab.binding.unifiprotect.internal.thing;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unifiprotect.internal.UniFiProtectDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryService;
import org.openhab.core.thing.Bridge;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
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
        if (UniFiProtectNvrThingHandler.supportsThingType(thingTypeUID)) {
            UniFiProtectNvrThingHandler uniFiProtectNvrThingHandler = new UniFiProtectNvrThingHandler((Bridge) thing);
            registerDeviceDiscoveryService(uniFiProtectNvrThingHandler);
            return uniFiProtectNvrThingHandler;
        } else if (UniFiProtectG4DoorbellThingHandler.supportsThingType(thingTypeUID)) {
            return new UniFiProtectG4DoorbellThingHandler(thing);
        } else if (UniFiProtectG4CameraThingHandler.supportsThingType(thingTypeUID)) {
            return new UniFiProtectG4CameraThingHandler(thing);
        } else if (UniFiProtectG3CameraThingHandler.supportsThingType(thingTypeUID)) {
            return new UniFiProtectG3CameraThingHandler(thing);
        }
        logger.error("Failed to creat thing returning null");
        return null;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return UniFiProtectNvrThingHandler.supportsThingType(thingTypeUID)
                || UniFiProtectG4CameraThingHandler.supportsThingType(thingTypeUID)
                || UniFiProtectG4DoorbellThingHandler.supportsThingType(thingTypeUID)
                || UniFiProtectG3CameraThingHandler.supportsThingType(thingTypeUID);
    }

    private synchronized void registerDeviceDiscoveryService(UniFiProtectNvrThingHandler handler) {
        UniFiProtectDiscoveryService discoveryService = new UniFiProtectDiscoveryService(handler);
        discoveryServiceRegs.put(handler.getThing().getUID(),
                bundleContext.registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<>()));
    }
}
