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
package org.openhab.binding.unifiprotect.internal.model.json;

import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * The {@link UniFiProtectEvent}
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectEvent {
    private @Nullable String type;
    private @Nullable Long start;
    private @Nullable Long end;
    private String camera = "";
    private @Nullable Long score;
    private @Nullable String id;
    private @Nullable MetaData metadata;
    private @Nullable String modelKey;
    private @Nullable String partition;
    private @Nullable String thumbnail;
    private @Nullable String heatmap;

    private String @Nullable [] smartDetectTypes;
    private String @Nullable [] smartDetectEvents;

    public @Nullable String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public @Nullable Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public @Nullable Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public @Nullable String getCamera() {
        return camera;
    }

    @Override
    public String toString() {
        return "UniFiProtectEvent [type=" + type + ", start=" + start + ", end=" + end + ", camera=" + camera
                + ", score=" + score + ", id=" + id + ", metadata=" + metadata + ", modelKey=" + modelKey
                + ", partition=" + partition + ", thumbnail=" + thumbnail + ", heatmap=" + heatmap
                + ", smartDetectTypes=" + Arrays.toString(smartDetectTypes) + ", smartDetectEvents="
                + Arrays.toString(smartDetectEvents) + "]";
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public @Nullable Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public @Nullable String getId() {
        final int dash = id.indexOf('-');
        if (dash > 0) {
            id = id.substring(0, dash);
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @Nullable MetaData getMetadata() {
        return metadata;
    }

    public void setMetadata(MetaData metadata) {
        this.metadata = metadata;
    }

    public @Nullable String getModelKey() {
        return modelKey;
    }

    public void setModelKey(String modelKey) {
        this.modelKey = modelKey;
    }

    public @Nullable String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public @Nullable String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public @Nullable String getHeatmap() {
        return heatmap;
    }

    public void setHeatmap(String heatmap) {
        this.heatmap = heatmap;
    }

    public String @Nullable [] getSmartDetectTypes() {
        return smartDetectTypes;
    }

    public void setSmartDetectTypes(String @Nullable [] smartDetectTypes) {
        this.smartDetectTypes = smartDetectTypes;
    }

    public String @Nullable [] getSmartDetectEvents() {
        return smartDetectEvents;
    }

    public void setSmartDetectEvents(String @Nullable [] smartDetectEvents) {
        this.smartDetectEvents = smartDetectEvents;
    }

    @SuppressWarnings("null")
    @NonNullByDefault
    class MetaData {
        private @Nullable String objectType;
        private @Nullable String objectCoords;
        private @Nullable String objectConfidence;

        @Override
        public String toString() {
            return "MetaData [objectType=" + objectType + ", objectCoords=" + objectCoords + ", objectConfidence="
                    + objectConfidence + "]";
        }

        public @Nullable String getObjectType() {
            return objectType;
        }

        public void setObjectType(String objectType) {
            this.objectType = objectType;
        }

        public @Nullable String getObjectCoords() {
            return objectCoords;
        }

        public void setObjectCoords(String objectCoords) {
            this.objectCoords = objectCoords;
        }

        public @Nullable String getObjectConfidence() {
            return objectConfidence;
        }

        public void setObjectConfidence(String objectConfidence) {
            this.objectConfidence = objectConfidence;
        }
    }
}
