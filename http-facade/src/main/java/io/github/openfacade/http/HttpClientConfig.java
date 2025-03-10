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

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Setter
public class HttpClientConfig {
    private HttpClientEngine engine;

    private Duration timeout;

    private Duration connectTimeout;

    private List<RequestFilter> requestFilters;

    private TlsConfig tlsConfig;

    private OkHttpConfig okHttpConfig;

    private HttpClientConfig(Builder builder) {
        this.engine = builder.engine;
        this.timeout = builder.timeout;
        this.connectTimeout = builder.connectTimeout;
        this.requestFilters = builder.requestFilters;
        this.tlsConfig = builder.tlsConfig;
        this.okHttpConfig = builder.okHttpConfig;
    }

    public HttpClientEngine engine() {
        return engine;
    }

    public Duration timeout() {
        return timeout;
    }

    public Duration connectTimeout() {
        return connectTimeout;
    }

    @NotNull
    public List<RequestFilter> addRequestFilter(RequestFilter filter) {
        this.requestFilters.add(filter);
        return this.requestFilters;
    }

    @NotNull
    public List<RequestFilter> requestFilters() {
        return requestFilters;
    }

    public TlsConfig tlsConfig() {
        return tlsConfig;
    }

    public OkHttpConfig okHttpConfig() {
        return okHttpConfig;
    }

    public static class Builder {
        private HttpClientEngine engine;

        private Duration timeout;

        private Duration connectTimeout;

        private List<RequestFilter> requestFilters = new ArrayList<>();

        private TlsConfig tlsConfig;

        private OkHttpConfig okHttpConfig;

        public Builder engine(HttpClientEngine engine) {
            this.engine = engine;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder requestFilter(List<RequestFilter> requestFilters) {
            this.requestFilters = requestFilters;
            return this;
        }

        public Builder addRequestFilter(RequestFilter filter) {
            this.requestFilters.add(filter);
            return this;
        }

        public Builder tlsConfig(TlsConfig tlsConfig) {
            this.tlsConfig = tlsConfig;
            return this;
        }

        public Builder okHttpConfig(OkHttpConfig okHttpConfig) {
            this.okHttpConfig = okHttpConfig;
            return this;
        }

        public HttpClientConfig build() {
            return new HttpClientConfig(this);
        }
    }

    @Setter
    public static class OkHttpConfig {
        private boolean retryOnConnectionFailure;

        private ConnectionPoolConfig connectionPoolConfig;

        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class ConnectionPoolConfig {
            private int maxIdleConnections;

            private Duration keepAliveDuration;

            public int maxIdleConnections() {
                return maxIdleConnections;
            }

            public Duration keepAliveDuration() {
                return keepAliveDuration;
            }
        }

        public boolean retryOnConnectionFailure() {
            return retryOnConnectionFailure;
        }

        public ConnectionPoolConfig connectionPoolConfig() {
            return connectionPoolConfig;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HttpClientConfig{\n");
        sb.append("engine=").append(engine).append("\n");
        if (timeout != null) {
            sb.append("timeout=").append(timeout).append("\n");
        }
        if (connectTimeout != null) {
            sb.append("connectTimeout=").append(connectTimeout).append("\n");
        }
        if (tlsConfig != null) {
            sb.append("tlsConfig=").append(tlsConfig).append("\n");
        }
        if (okHttpConfig != null) {
            sb.append("okHttpConfig=").append(okHttpConfig).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
