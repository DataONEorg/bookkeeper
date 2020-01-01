/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2019. All rights reserved.
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.dropwizard.jackson.Jackson;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.Objects;

/**
 * Quotas represent limits placed on services resources (storage, etc.)
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Quota {
    /* The quota id (assigned by db layer) */
    private Integer id;

    /* The quota object type */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "quota")
    private String object;

    /* The quota name */
    @NotEmpty
    @NotNull
    private String name;

    /* The quota soft limit */
    @NotNull
    private Integer softLimit;

    /* The quota hard limit */
    @NotNull
    private Integer hardLimit;

    /* The usage of the quota */
    private Integer usage;

    /* The quota unit */
    @NotEmpty
    @NotNull
    private String unit;

    /* The quota subscription id */
    private Integer subscriptionId;

    /* The quota subject id */
    private String subject;

    /**
     * Construct an empty Quota
     */
    public Quota() {
        super();
    }

    /**
     * Construct a Quota from a JSON string
     * @param json
     * @throws IOException
     */
    public Quota(String json) throws IOException {
        super();

        // Return an empty Quota instance when the JSON object is empty
        if ( ! json.equals("{}") ) {

            // Otherwise try to build the Quota
            Quota quota = Jackson.newObjectMapper().readValue(json, Quota.class);
            this.id = quota.id;
            this.object = quota.object;
            this.name = quota.name;
            this.softLimit = quota.softLimit;
            this.hardLimit = quota.hardLimit;
            this.usage = quota.usage;
            this.unit = quota.unit;
            this.subscriptionId = quota.subscriptionId;
            this.subject = quota.subject;
        }
    }

    /**
     * Construct a quota
     * @param id
     * @param object
     * @param name
     * @param softLimit
     * @param hardLimit
     * @param unit
     * @param usage
     * @param subscriptionId
     * @param subject
     */
    public Quota(Integer id,
                 @NotNull @NotEmpty String object,
                 @NotNull @NotEmpty String name,
                 @NotNull Integer softLimit,
                 @NotNull Integer hardLimit,
                 Integer usage,
                 @NotNull @NotEmpty String unit,
                 Integer subscriptionId,
                 String subject) {
        if ( id != null ) {
            if ( ! id.toString().equals("") ) {
                this.id = id;
            }
        }
        this.object = object;
        this.name = name;
        this.softLimit = softLimit;
        this.hardLimit = hardLimit;
        this.unit = unit;
        this.usage = usage;
        this.subscriptionId = subscriptionId;
        this.subject = subject;
    }

    /**
     * Get the quota id
     * @return
     */
    @JsonProperty
    public Integer getId() {
        return id;
    }

    /**
     * Set the quota id
     * @param id
     */
    @JsonProperty
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the quota object type
     * @return object
     */
    @JsonProperty
    public String getObject() {
        return object;
    }

    /**
     * Set the quota object type
     * @param object
     */
    @JsonProperty
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Get the quota name
     * @return name
     */
    @JsonProperty
    public String getName() {
        return name;
    }

    /**
     * Set the quota name
     * @param name
     */
    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the quota soft limit
     * @return softLimit
     */
    @JsonProperty
    public @NotNull Integer getSoftLimit() {
        return softLimit;
    }

    /**
     * Set the quota soft limit
     * @param softLimit
     */
    @JsonProperty
    public void setSoftLimit(@NotNull Integer softLimit) {
        this.softLimit = softLimit;
    }

    /**
     * Get the quota hard limit
     * @return hardLimit
     */
    @JsonProperty
    public @NotNull Integer getHardLimit() {
        return hardLimit;
    }

    /**
     * Set the quota hard limit
     * @param hardLimit
     */
    @JsonProperty
    public void setHardLimit(@NotNull Integer hardLimit) {
        this.hardLimit = hardLimit;
    }


    /**
     * Get the quota usage
     * @return usage
     */
    @JsonProperty
    public Integer getUsage() {
        return usage;
    }

    /**
     * Set the quota usage
     * @param usage
     */
    @JsonProperty
    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    /**
     * Get the quota unit
     * @return unit
     */
    @JsonProperty
    public String getUnit() {
        return unit;
    }

    /**
     * Set the quota unit
     * @param unit
     */
    @JsonProperty
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Get the subscription id
     * @return subscriptionId
     */
    @JsonProperty
    public Integer getSubscriptionId() {
        return subscriptionId;
    }

    /**
     * Set the subscription id
     * @param subscriptionId
     */
    @JsonProperty
    public void setSubscriptionId(Integer subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    /**
     * Get the subject
     * @return subject
     */
    @JsonProperty
    public String getSubject() {
        return subject;
    }

    /**
     * Set the subject
     * @param subject
     */
    @JsonProperty
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Determine object equality based on the equality of all fields
     * @param o the object to be compared
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quota quota = (Quota) o;
        return Objects.equals(getId(), quota.getId()) &&
            Objects.equals(getObject(), quota.getObject()) &&
            Objects.equals(getName(), quota.getName()) &&
            Objects.equals(getSoftLimit(), quota.getSoftLimit()) &&
            Objects.equals(getHardLimit(), quota.getHardLimit()) &&
            Objects.equals(getUsage(), quota.getUsage()) &&
            Objects.equals(getUnit(), quota.getUnit()) &&
            Objects.equals(getSubscriptionId(), quota.getSubscriptionId()) &&
            Objects.equals(getSubject(), quota.getSubject());
    }

    /**
     * Calculate a hash based on all fields
     * @return hashcode
     */
    @Override
    public int hashCode() {

        return Objects.hash(getId(), getObject(), getName(), getSoftLimit(),
            getHardLimit(), getUsage(), getUnit(), getSubscriptionId(), getSubject());
    }
}
