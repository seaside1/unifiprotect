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
package org.openhab.binding.unifiprotect.internal.model.request;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.http.MimeTypes;
import org.openhab.binding.unifiprotect.internal.UniFiProtectNvrThingConfig;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectStatus;
import org.openhab.binding.unifiprotect.internal.model.UniFiProtectStatus.SendStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

/**
 * The {@link UniFiProtectRequest}
 *
 * @author Joseph Seaside Hagberg - Initial contribution
 */
@NonNullByDefault
public abstract class UniFiProtectRequest {

    public static final String QUERY_PARAM_WIDTH = "w";
    public static final String QUERY_PARAM_HEIGHT = "h";
    public static final String API_CAMERAS = "/proxy/protect/api/cameras/";
    public static final String HTTP_METHOD_PATCH = "PATCH";
    public static final String HEADER_X_CSRF_TOKEN = "x-csrf-token";

    private static final int HTTP_STATUS_200 = 200;

    private static final int HTTP_STATUS_401 = 401;

    private static final String CONTENT_TYPE_APPLICATION_JSON = MimeTypes.Type.APPLICATION_JSON.asString();

    private static final long TIMEOUT_SECONDS = 5;

    public static final String PROPERTY_CAMERAS = "cameras";

    private static final UniFiProtectStatus RESPONSE_STATUS_OK = new UniFiProtectStatus(SendStatus.SUCCESS);
    private static final String SCHEME_SEPARATOR = "://";

    private final Logger logger = LoggerFactory.getLogger(UniFiProtectRequest.class);

    protected final HttpClient httpClient;

    private String host = "";

    private String path = "/";

    private Map<String, String> queryParameters = new HashMap<>();

    private Map<String, String> bodyParameters = new HashMap<>();

    private Map<String, String> headers = new HashMap<>();

    protected @Nullable String jsonContent;
    protected @Nullable ContentResponse response;

    @Nullable
    private String jsonRaw;

    public UniFiProtectRequest(HttpClient httpClient, UniFiProtectNvrThingConfig config) {
        this.httpClient = httpClient;
        this.setHost(config.getHost());
    }

    public synchronized void setPath(String path) {
        this.path = path;
    }

    public synchronized void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public synchronized void setBodyParameter(String key, Object value) {
        this.bodyParameters.put(key, String.valueOf(value));
    }

    public synchronized void setQueryParameter(String key, Object value) {
        this.queryParameters.put(key, String.valueOf(value));
    }

    public synchronized UniFiProtectStatus sendRequest() {
        return sendRequestGetResponse();
    }

    protected String getHttpScheme() {
        return HttpScheme.HTTPS.asString();
    }

    @SuppressWarnings("null")
    public synchronized boolean isOk() {
        return getResponse() != null && getResponse().getStatus() == HTTP_STATUS_200;
    }

    @SuppressWarnings("null")
    public synchronized boolean creditialsExpired() {
        return getResponse() == null || getResponse().getStatus() == HTTP_STATUS_401;
    }

    public synchronized String getStatusCodeMessage() {
        if (getResponse() == null) {
            return "Unknown status code, no response present";
        }
        @SuppressWarnings("null")
        int status = getResponse().getStatus();
        switch (status) {
            case HttpStatus.OK_200:
                return "Ok";
            case HttpStatus.BAD_REQUEST_400:
                return "Invalid Credentials";
            case HttpStatus.UNAUTHORIZED_401:
                return "Expired Credentials";
            case HttpStatus.FORBIDDEN_403:
                return "Unauthorized Access";
            default:
                return "Unknown HTTP status code: " + status;
        }
    }

    @Nullable
    public synchronized String getJsonContent() {
        final ContentResponse response = getResponse();
        return response != null ? response.getContentAsString() : null;
    }

    private synchronized UniFiProtectStatus sendRequestGetResponse() {
        Request request = newRequest();
        logger.debug("New request: {}", request);
        try {
            logger.debug(">> {} {}", request.getMethod(), request.getURI());
            response = request.send();
        } catch (TimeoutException e) {
            return new UniFiProtectStatus(SendStatus.TIMEOUT, e);
        } catch (InterruptedException e) {
            return new UniFiProtectStatus(SendStatus.INTERRUPTED, e);
        } catch (ExecutionException e) {
            return new UniFiProtectStatus(SendStatus.EXECUTION_FAULT, e);
        } catch (Exception x) {
            logger.debug("Unexpected exception: {}", x.getMessage(), x);
        }
        return RESPONSE_STATUS_OK;
    }

    protected String getHttpMethod() {
        return bodyParameters.isEmpty() ? HttpMethod.GET.name() : HttpMethod.POST.name();
    }

    @SuppressWarnings("null")
    private synchronized Request newRequest() {
        HttpURI uri = new HttpURI(getHttpScheme().concat(SCHEME_SEPARATOR).concat(host).concat(path));
        final Request request = httpClient.newRequest(uri.toString()).timeout(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        request.method(getHttpMethod());
        for (Entry<String, String> entry : queryParameters.entrySet()) {
            request.param(entry.getKey(), entry.getValue());
        }
        if (!bodyParameters.isEmpty()) {
            String jsonBody = getRequestBodyAsJson();
            ContentProvider content = new StringContentProvider(CONTENT_TYPE_APPLICATION_JSON, jsonBody,
                    StandardCharsets.UTF_8);
            request.content(content);
            logger.debug("Set json Body: {}", jsonBody);
        }

        if (getJsonRaw() != null && !getJsonRaw().isEmpty()) {
            ContentProvider content = new StringContentProvider(CONTENT_TYPE_APPLICATION_JSON, getJsonRaw(),
                    StandardCharsets.UTF_8);
            request.content(content);
            logger.debug("Set json RAW: {}", getJsonRaw());
        }

        if (!headers.isEmpty()) {
            headers.keySet().stream().forEach(key -> logger.debug("Key: {}, value: {}", key, headers.get(key)));
            headers.keySet().stream().forEach(key -> request.header(key, headers.get((key))));
        }
        logger.debug("Created new request host: {}, scheme: {}, path: {}", host, getHttpScheme(), path);
        return request;
    }

    private synchronized String getRequestBodyAsJson() {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = new JsonObject();
        JsonElement jsonElement = null;
        for (Entry<String, String> entry : bodyParameters.entrySet()) {
            try {
                jsonElement = jsonParser.parse(entry.getValue());
            } catch (JsonSyntaxException e) {
                jsonElement = new JsonPrimitive(entry.getValue());
            }
            jsonObject.add(entry.getKey(), jsonElement);
        }
        return jsonObject.toString();
    }

    public synchronized @Nullable ContentResponse getResponse() {
        if (response == null) {
            logger.debug("Response is null, it shouldn't be");
        }
        return response;
    }

    @Nullable
    public String getJsonRaw() {
        return jsonRaw;
    }

    public void setJsonRaw(String jsonRaw) {
        logger.debug("JSONRAW: {}", jsonRaw);
        this.jsonRaw = jsonRaw;
    }

    public void setHost(String host) {
        if (host != null) {
            this.host = host;
        }
    }
}
