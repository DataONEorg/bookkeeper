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
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * The main Bookkeeper configuration class, populated by the
 * Dropwizard YAML configuaration file.
 */
public class BookkeeperConfiguration extends Configuration {

    /* The database YAML configuration keyword */
    private static final String DATABASE = "database";

    /* The dataone YAML configuration keyword */
    private static final String DATAONE = "dataone";

    /* The DataONE configuration */
    private DataONEConfiguration dataone = new DataONEConfiguration();

    /* The DataSource to be used for persistence */
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    /**
     * Set the DataSource factory
     * @param factory  the DataSource factory
     */
    @JsonProperty(DATABASE)
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    /**
     * Return the DataSource factory
     * @return database - the DataSource factory
     */
    @JsonProperty(DATABASE)
    public DataSourceFactory getDataSourceFactory() {
        return this.database;
    }

    /**
     * Get the DataONE configuration
     * @return dataONEConfiguration  the DataONE configuration
     */
    @JsonProperty(DATAONE)
    public DataONEConfiguration getDataONEConfiguration() {
        return dataone;
    }

    /**
     * Set the DataONE configuration
     * @param dataONEConfiguration  the DataONE configuration
     */
    @JsonProperty(DATAONE)
    public void setDataONEConfiguration(DataONEConfiguration dataONEConfiguration) {
        this.dataone = dataONEConfiguration;
    }
}
