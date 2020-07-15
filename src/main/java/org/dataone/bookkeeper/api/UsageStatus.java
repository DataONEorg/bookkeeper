/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2020. All rights reserved.
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

package org.dataone.bookkeeper.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.Jackson;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;


/**
 * UsageStatus represent the current state of a usage ("active" | "inactive")
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsageStatus {


    /* The usagestatus object type */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "usagestatus")
    private String object;

    /*  The status of the quota usage, either active or inactive */
    @Pattern(regexp = "active|inactive")
    private String status;


    public UsageStatus() {
        super();
    }

    /**
     * Construct a UsageStatus from a JSON string
     * @param json the JSON usagestatus object
     * @throws IOException when an I/O exception occurs
     */
    public UsageStatus(String json) throws IOException {
        super();

        // Return an empty Quota instance when the JSON object is empty
        if ( ! json.equals("{}") ) {

            // Otherwise try to build the UsageStatus
            UsageStatus usagestatus = Jackson.newObjectMapper().readValue(json, UsageStatus.class);
            this.object = usagestatus.object;
            this.status = usagestatus.status;
        }
    }

    /**
     * Construct a Usage instance
     * @param status  the usage status, either active or inactive
     */
    public UsageStatus(@NotNull @NotEmpty String object,
                       @NotNull @NotEmpty String status ) {
        this.object = object;
        this.status = status;
    }

    /**
     * Get the usage object type
     * @return object the usage object type, always 'usagestatus'
     */
    public String getObject() { return object; }

    /**
     * Set the usage object type
     * @param status the usage object type, always 'usagestatus'
     */
    public void setObject(String status) { this.status = status; }

    /**
     * Get the usage status
     * @return status  the usage status, either active or inactive
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the usage status
     * @param status  the usage status, either active or inactive
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
