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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.unifiprotect.internal.model.json.UniFiProtectEvent;
import org.openhab.binding.unifiprotect.internal.model.request.UniFiProtectRequest;
import org.openhab.binding.unifiprotect.internal.types.UniFiProtectCamera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The {@link UniFiProtectUtil} is responsible for creating things and thing
 * handlers.
 *
 * @author Joseph (Seaside) Hagberg - Initial contribution
 */
@NonNullByDefault
public class UniFiProtectUtil {

    private static final int MILLIS_FACTOR = 1000;
    private static final String THUMBNAIL_SUFFIX = "-thmb.jpg";
    private static final double HEIGHT_DIVISOR = 16.0;
    private static final double HEIGHT_FACTOR = 9.0;
    private static final Logger logger = LoggerFactory.getLogger(UniFiProtectUtil.class);
    private static final String EVENT_MOTION = "motion";
    private static final String HEATMAP_SUFFIX = "-hmap.png";
    private static final String SNAPSHOT_SUFFIX = "-snap.jpg";
    private static final String ANON_SNAPSHOT_SUFFIX = "-asnap.jpg";

    public static double calculateHeightFromWidth(double thumbnailWidth) {
        return (thumbnailWidth / HEIGHT_DIVISOR) * HEIGHT_FACTOR;
    }

    public static byte[] zlibDecompress(byte[] compressed) throws IOException, DataFormatException {
        final Inflater inflater = new Inflater();
        ByteArrayOutputStream out = null;
        // System.out.println("Compressed length: " + compressed.length);
        try {
            out = new ByteArrayOutputStream(compressed.length);
            inflater.setInput(compressed);
            byte[] bytes = new byte[1024];
            int size = 1;
            while (size > 0) {
                size = inflater.inflate(bytes);
                out.write(bytes, 0, size);
            }
            return out.toByteArray();
        } finally {
            inflater.end();
            if (out != null) {
                out.close();
            }
        }
    }

    @SuppressWarnings("null")
    public static boolean requestHasContentOfSize(UniFiProtectRequest request, int size) {
        return request != null && request.getResponse() != null && request.getResponse().getContent() != null
                && request.getResponse().getContent().length > size;
    }

    public static String prettyPrintJson(String content) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(content).getAsJsonObject();
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        return prettyGson.toJson(json);
    }

    @Nullable
    public static File writeThumbnailToImageFolder(String imageFolder, String cameraId, byte[] content) {
        return writeFileToImageFolder(imageFolder, cameraId, content, THUMBNAIL_SUFFIX);
    }

    public static <T> CompletableFuture<T> scheduleAsync(ScheduledExecutorService executor,
            Supplier<CompletableFuture<T>> command, long delay, TimeUnit unit) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        executor.schedule((() -> {
            command.get().thenAccept(t -> {
                completableFuture.complete(t);
            }).exceptionally(t -> {
                completableFuture.completeExceptionally(t);
                return null;
            });
        }), delay, unit);
        return completableFuture;
    }

    @Nullable
    private static File writeFileToImageFolder(String imageFolder, String cameraId, byte[] content, String suffix) {
        FileOutputStream fout = null;
        File image = null;
        try {
            image = new File(imageFolder.concat(File.separator).concat(cameraId).concat(suffix));
            fout = new FileOutputStream(image);
            fout.write(content);
        } catch (IOException iox) {
            logger.error("Failed to write to file: {} , for camera id: {} ", image.getAbsolutePath(), cameraId, iox);
            return null;
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (Exception x) {
                // Best effort
            }
        }
        return image;
    }

    public static long calculateStartTimeForEvent(int eventsTimePeriodLength) {
        return System.currentTimeMillis() - (MILLIS_FACTOR * eventsTimePeriodLength);
    }

    @Nullable
    public static UniFiProtectEvent findFirstMotionEventForCamera(UniFiProtectEvent[] events,
            UniFiProtectCamera camera) {
        return findFirstEventTypeForCamera(events, camera, EVENT_MOTION);
    }

    @Nullable
    public static UniFiProtectEvent findLastMotionEventForCamera(UniFiProtectEvent[] events,
            UniFiProtectCamera camera) {
        return findLastEventTypeForCamera(events, camera, EVENT_MOTION);
    }

    @Nullable
    public static UniFiProtectEvent findFirstEventTypeForCamera(UniFiProtectEvent[] events, UniFiProtectCamera camera,
            String eventType) {
        List<UniFiProtectEvent> filteredEvents = Arrays.stream(events).filter(
                e -> e.getCamera() != null && e.getCamera().equals(camera.getId()) && e.getType().equals(eventType))
                .collect(Collectors.toList());

        if (!filteredEvents.isEmpty()) {
            return filteredEvents.get(0);
        }
        return null;
    }

    @Nullable
    public static UniFiProtectEvent findLastEventTypeForCamera(UniFiProtectEvent[] events, UniFiProtectCamera camera,
            String eventType) {
        Arrays.stream(events).forEach(e -> logger.debug("FindLastEvent for camera: {} , id: {}, from e: {}",
                camera.getName(), camera.getId(), e));
        List<UniFiProtectEvent> filteredEvents = Arrays.stream(events).filter(
                e -> e.getCamera() != null && e.getCamera().equals(camera.getId()) && e.getType().equals(eventType))
                .collect(Collectors.toList());

        if (!filteredEvents.isEmpty()) {
            return filteredEvents.get(filteredEvents.size() - 1);
        }
        return null;
    }

    @Nullable
    public static File writeHeatmapToFile(String imageFolder, String cameraId, byte[] content) {
        return writeFileToImageFolder(imageFolder, cameraId, content, HEATMAP_SUFFIX);
    }

    @Nullable
    public static File writeSnapshotToFile(String imageFolder, String cameraId, byte[] content) {
        return writeFileToImageFolder(imageFolder, cameraId, content, SNAPSHOT_SUFFIX);
    }

    @Nullable
    public static File writeAnonSnapshotToFile(String imageFolder, String cameraId, byte[] content) {
        return writeFileToImageFolder(imageFolder, cameraId, content, ANON_SNAPSHOT_SUFFIX);
    }

    public static boolean isNotBlank(@Nullable String string) {
        return string != null && !string.isBlank();
    }

    public static boolean isEmpty(@Nullable String string) {
        return string == null || !string.isEmpty();
    }
}
