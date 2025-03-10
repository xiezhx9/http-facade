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
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

class OkHttpSslContextFactory {
    static OkHttpSslContext createOkHttpSslContext(TlsConfig tlsConfig) {
        try {
            // get key manager for client certificate auth
            KeyManager[] keyManagers = getKeyManagers(tlsConfig.keyStorePath(), tlsConfig.keyStorePassword());

            // get trust manager for server certificate auth
            TrustManager[] trustManagers = getTrustManagers(tlsConfig.trustStorePath(), tlsConfig.trustStorePassword(),
                    tlsConfig.verifyDisabled());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, new SecureRandom());
            X509TrustManager x509TrustManager = (X509TrustManager) trustManagers[0];
            return new OkHttpSslContext(sslContext.getSocketFactory(), x509TrustManager);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static KeyManager[] getKeyManagers(String keyStorePath,
                                               char[] keyStorePassword) {
        try {
            if (keyStorePath == null) {
                return null;
            }

            KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePassword);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                    KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword);
            return keyManagerFactory.getKeyManagers();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @NotNull
    private static TrustManager[] getTrustManagers(String trustStorePath,
                                                   char[] trustStorePassword,
                                                   boolean disableSslVerify) {
        try {
            if (disableSslVerify) {
                return new TrustManager[]{new InsecureTrustManager()};
            }

            KeyStore trustStore = loadKeyStore(trustStorePath, trustStorePassword);
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            return trustManagerFactory.getTrustManagers();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static KeyStore loadKeyStore(String keyStorePath,
                                         char[] password) {
        try (FileInputStream trustStoreFile = new FileInputStream(keyStorePath)) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(trustStoreFile, password);
            return keyStore;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Getter
    @AllArgsConstructor
    static class OkHttpSslContext {
        SSLSocketFactory sslSocketFactory;
        X509TrustManager x509TrustManager;
    }
}
