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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.unifiprotect.internal.thing.UniFiProtectNvrThingHandler;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryResult;
import org.openhab.core.config.discovery.DiscoveryResultBuilder;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link UniFiProtectDiscoveryService}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectDiscoveryService extends AbstractDiscoveryService {

    private static final String UNIFI_PROTECT_VENDOR = "Ubiquiti UniFi Protect";
    private static final String UNIFI_PROTECT = "UniFi Protect: ";

    private static final int TIMEOUT = 30;
    private UniFiProtectNvrThingHandler bridge;

    private final Logger logger = LoggerFactory.getLogger(UniFiProtectDiscoveryService.class);

    public UniFiProtectDiscoveryService(UniFiProtectNvrThingHandler bridge) throws IllegalArgumentException {
        super(UniFiProtectBindingConstants.SUPPORTED_DEVICE_THING_TYPES_UIDS, TIMEOUT);
        this.bridge = bridge;
        new UniFiProtectScan();
        logger.debug("Initializing UniFiProtect Discovery Nvr: {}", bridge);
        activate(null);
    }

    @SuppressWarnings({ "null", "unused" })
    @Override
    protected void startScan() {

        if (bridge == null) {
            logger.debug("Can't start scanning for devices, UniFiProtect bridge handler not found!");
            return;
        }

        if (!bridge.getThing().getStatus().equals(ThingStatus.ONLINE)) {
            logger.debug("Bridge is OFFLINE, can't scan for devices!");
            return;
        }

        if (bridge.getNvr() == null) {
            logger.debug("Failed to start discovery scan due to no nvr exists in the bridge");
            return;
        }

        logger.debug("Starting scan of UniFiProtect Server {}", bridge.getThing().getUID());

        bridge.getNvr().getCameraInsightCache().getCameras()
                .forEach(camera -> logger.debug("Found Camera: {}", camera));

        for (Thing thing : bridge.getThing().getThings()) {
            if (thing instanceof UniFiProtectCamera) {
                logger.debug("Found existing camera already!");
            }
        }
        ThingUID bridgeUid = bridge.getThing().getUID();
        for (UniFiProtectCamera camera : bridge.getNvr().getCameraInsightCache().getCameras()) {
            ThingUID thingUID = new ThingUID(getThingType(camera), bridgeUid, camera.getMac());

            DiscoveryResult discoveryResult = DiscoveryResultBuilder.create(thingUID)
                    .withLabel(UNIFI_PROTECT.concat(camera.getName())).withBridge(bridgeUid)
                    .withProperty(Thing.PROPERTY_VENDOR, UNIFI_PROTECT_VENDOR)
                    .withProperty(UniFiProtectBindingConstants.CAMERA_PROP_HOST, camera.getHost())
                    .withProperty(UniFiProtectBindingConstants.CAMERA_PROP_MAC, camera.getMac())
                    .withProperty(UniFiProtectBindingConstants.CAMERA_PROP_NAME, camera.getName()).build();

            thingDiscovered(discoveryResult);
        }
    }

    private ThingTypeUID getThingType(UniFiProtectCamera camera) {
        final String type = camera.getType();
        if (type != null) {
            if (type.startsWith(UniFiProtectBindingConstants.G4_DOORBELL)) {
                return UniFiProtectBindingConstants.THING_TYPE_G4_DOORBELL;
            }

            if (type.startsWith(UniFiProtectBindingConstants.G4_CAMERA_PREFIX)) {
                return UniFiProtectBindingConstants.THING_TYPE_G4_CAMERA;
            }

            if (type.startsWith(UniFiProtectBindingConstants.G3_CAMERA_PREFIX)) {
                return UniFiProtectBindingConstants.THING_TYPE_G3_CAMERA;
            }
        }
        logger.error("Faild to identify UnifiProtect camera, assuming g3 type: {}", type);
        return UniFiProtectBindingConstants.THING_TYPE_G3_CAMERA;
    }

    @Override
    protected synchronized void stopScan() {
        super.stopScan();
        removeOlderResults(getTimestampOfLastScan());
    }

    @Override
    protected void startBackgroundDiscovery() {
        /* Not Implemented */
    }

    @SuppressWarnings("null")
    @NonNullByDefault
    public class UniFiProtectScan implements Runnable {
        @Override
        public void run() {
            startScan();
        }
    }
}
