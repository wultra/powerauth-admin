/*
 * Copyright 2017 Lime - HighTech Solutions s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.getlime.security;

import io.getlime.security.powerauth.soap.spring.client.PowerAuthServiceClient;
import org.apache.ws.security.WSConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;

import javax.net.ssl.*;

/**
 * PowerAuth SOAP WebService Configuration
 *
 * @author Petr Dvorak
 */
@Configuration
public class PowerAuthWebServiceConfiguration {

    private ApplicationConfiguration configuration;

    @Autowired
    public PowerAuthWebServiceConfiguration(ApplicationConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Checks if given client token is the current client token.
     * @param clientToken Client Token to be checked.
     * @return True if the provided client token is the same one as the one being used, false otherwise.
     */
    public boolean isCurrentSecuritySettings(String clientToken) {
        return this.configuration.getClientToken() != null
                && this.configuration.getClientToken().equals(clientToken);
    }

    /**
     * Return WS-Security interceptor instance using UsernameToken authentication.
     * @return Wss4jSecurityInterceptor instance.
     */
    @Bean
    public Wss4jSecurityInterceptor securityInterceptor(){
        Wss4jSecurityInterceptor wss4jSecurityInterceptor = new Wss4jSecurityInterceptor();
        wss4jSecurityInterceptor.setSecurementActions("UsernameToken");
        wss4jSecurityInterceptor.setSecurementUsername(configuration.getClientToken());
        wss4jSecurityInterceptor.setSecurementPassword(configuration.getClientSecret());
        wss4jSecurityInterceptor.setSecurementPasswordType(WSConstants.PW_TEXT);
        return wss4jSecurityInterceptor;
    }

    /**
     * Return SOAP service marshaller.
     *
     * @return Marshaller instance with a correct context path.
     */
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("io.getlime.powerauth.soap");
        return marshaller;
    }

    /**
     * Return a correctly configured PowerAuthServiceClient instance.
     *
     * @param marshaller SOAP service marshaller.
     * @return Correctly configured PowerAuthServiceClient instance.
     */
    @Bean
    public PowerAuthServiceClient powerAuthClient(Jaxb2Marshaller marshaller) {
        PowerAuthServiceClient client = new PowerAuthServiceClient();
        client.setDefaultUri(configuration.getPowerAuthServiceUrl());
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);

        // if invalid SSL certificates should be accepted
        if (configuration.isAcceptInvalidSslCertificate()) {

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

            }};

            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                // ... ignore
            }

        }

        // if there is a configuration with security credentials, add interceptor
        if (!configuration.getClientToken().isEmpty()) {
            ClientInterceptor[] interceptors = new ClientInterceptor[] {
                    securityInterceptor()
            };
            client.setInterceptors(interceptors);
        }
        return client;
    }

}