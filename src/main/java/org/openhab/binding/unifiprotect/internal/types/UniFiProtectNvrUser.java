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
package org.openhab.binding.unifiprotect.internal.types;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link UniFiProtectNvrUser}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectNvrUser {

    private @Nullable Boolean enableNotifications;
    private @Nullable String localUsername;
    private @Nullable String firstName;
    private @Nullable String id;

    @Override
    public String toString() {
        return "UniFiProtectNvrUser [enableNotifications=" + enableNotifications + ", localUsername=" + localUsername
                + ", firstName=" + firstName + ", id=" + id + "]";
    }

    public @Nullable Boolean getEnableNotifications() {
        return enableNotifications;
    }

    public void setEnableNotifications(Boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public @Nullable String getLocalUsername() {
        return localUsername;
    }

    public void setLocalUsername(String localUsername) {
        this.localUsername = localUsername;
    }

    public @Nullable String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public @Nullable String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
