/*
 * Copyright 2024 OpenFacade Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.openfacade.http;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {

    private final int statusCode;

    @NotNull
    private final byte[] body;

    @NotNull
    private final Map<String, List<String>> headers;

    public HttpResponse(int statusCode) {
        this.statusCode = statusCode;
        this.body = new byte[0];
        this.headers = new HashMap<>();
    }

    public HttpResponse(int statusCode, @NotNull byte[] body) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = new HashMap<>();
    }

    public HttpResponse(int statusCode, @NotNull byte[] body, @NotNull Map<String, List<String>> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public int statusCode() {
        return statusCode;
    }

    @NotNull
    public byte[] body() {
        return body;
    }

    @NotNull
    public String bodyAsString() {
        return new String(body, StandardCharsets.UTF_8);
    }

    @NotNull
    public Map<String, List<String>> headers() {
        return headers;
    }
}
