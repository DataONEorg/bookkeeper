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
import java.util.Objects;


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
    @NotEmpty
    @NotNull
    @Pattern(regexp = "active|inactive")
    private String status;

    /**
     * A UsageStatus represents the active or inactive status of a Usage object as a light weight response
     */
    public UsageStatus() {}

    /**
     * Construct a UsageStatus from a JSON string
     * @param json the JSON usagestatus object
     * @throws IOException when an I/O exception occurs
     */
    public UsageStatus(String json) throws IOException {

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
    public UsageStatus(@NotNull @NotEmpty @Pattern(regexp = "usagestatus") String object,
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
     * @param object the usage object type, always 'usagestatus'
     */
    public void setObject(String object) { this.object = object; }

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

    /**
     * Determine object equality based on the equality of all fields
     * @param o the object to be compared
     * @return  true if the given object is equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsageStatus usagestatus = (UsageStatus) o;
        return Objects.equals(getObject(), usagestatus.getObject()) &&
                Objects.equals(getStatus(), usagestatus.getStatus());
    }

    /**
     * Calculate a hash based on all fields
     * @return hashcode  the hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getObject(), getStatus());
    }
}
