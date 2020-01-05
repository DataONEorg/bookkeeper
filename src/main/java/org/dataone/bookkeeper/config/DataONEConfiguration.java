/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2019
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.dataone.bookkeeper.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class DataONEConfiguration extends Configuration {

    /* The dataone configuration key */
    private final String DATAONE = "dataone";

    /* The Coordinating Node base URL */
    private String cnBaseUrl;

    /* The Identity API endpoint */
    private String cnIdentityServiceEndpoint;

    /* The path to the CN private key */
    private String cnPrivateKeyPath;

    /* The path to the CN private key */
    private String cnPublicCertPath;

    /**
     * Get the CN base URL
     * @return cnBaseUrl  the CN base URL
     */
    @JsonProperty(DATAONE)
    public String getCnBaseUrl() {
        return cnBaseUrl;
    }

    /**
     * Get the CN base URL
     * @param cnBaseUrl the CN base URL
     */
    @JsonProperty(DATAONE)
    public void setCnBaseUrl(String cnBaseUrl) {
        this.cnBaseUrl = cnBaseUrl;
    }

    /**
     * Get the CN identity service endpoint
     * @return cnIdentityServiceEndpoint  the CN identity service endpoint
     */
    @JsonProperty(DATAONE)
    public String getCnIdentityServiceEndpoint() {
        return cnIdentityServiceEndpoint;
    }

    /**
     * Set the CN identity service endpoint
     * @param cnIdentityServiceEndpoint the CN identity service endpoint
     */
    @JsonProperty(DATAONE)
    public void setCnIdentityServiceEndpoint(String cnIdentityServiceEndpoint) {
        this.cnIdentityServiceEndpoint = cnIdentityServiceEndpoint;
    }

    /**
     * Get the CN base URL
     * @return cnPrivateKeyPath the CN private key path
     */
    @JsonProperty(DATAONE)
    public String getCnPrivateKeyPath() {
        return cnPrivateKeyPath;
    }

    /**
     * Set the CN base URL
     * @param cnPrivateKeyPath  the CN private key path
     */
    @JsonProperty(DATAONE)
    public void setCnPrivateKeyPath(String cnPrivateKeyPath) {
        this.cnPrivateKeyPath = cnPrivateKeyPath;
    }

    /**
     * Get the CN public certificate path
     * @return cnPublicCertPath the CN public certificate path
     */
    @JsonProperty(DATAONE)
    public String getCnPublicCertPath() {
        return cnPublicCertPath;
    }

    /**
     * Set the CN public certificate path
     * @param cnPublicCertPath  the CN public certificate path
     */
    @JsonProperty(DATAONE)
    public void setCnPublicCertPath(String cnPublicCertPath) {
        this.cnPublicCertPath = cnPublicCertPath;
    }
}
