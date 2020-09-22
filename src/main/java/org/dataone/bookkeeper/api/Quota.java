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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.dropwizard.jackson.Jackson;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.Objects;

/**
 * Quotas represent limits placed on services resources (storage, etc.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Quota {
    /* The quota id (assigned by db layer) */
    private Integer id;

    /* The quota object type */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "quota")
    private String object;

    /* The quota type */
    @NotEmpty
    @Pattern(regexp = "portal|storage|repository_storage")
    @NotNull
    private String quotaType;

    /* The quota soft limit */
    private @NotNull Double softLimit;

    /* The quota hard limit */
    private @NotNull Double hardLimit;

    /* The total usage of the quota */
    private @NotNull Double totalUsage = 0.0;

    /* The quota unit */
    @NotEmpty
    @NotNull
    private String unit;

    /* The quota membership id */
    private Integer membershipId;

    /* The quota owner id */
    private String owner;

    /**
     * Construct an empty Quota
     */
    public Quota() {
        super();
    }

    /**
     * Construct a Quota from a JSON string
     * @param json the JSON quota object
     * @throws IOException when an I/O exception occurs
     */
    public Quota(String json) throws IOException {
        super();

        // Return an empty Quota instance when the JSON object is empty
        if ( ! json.equals("{}") ) {

            // Otherwise try to build the Quota
            Quota quota = Jackson.newObjectMapper().readValue(json, Quota.class);
            this.id = quota.id;
            this.object = quota.object;
            this.quotaType = quota.quotaType;
            this.softLimit = quota.softLimit;
            this.hardLimit = quota.hardLimit;
            this.totalUsage = quota.totalUsage;
            this.unit = quota.unit;
            this.membershipId = quota.membershipId;
            this.owner = quota.owner;
        }
    }

    /**
     * Construct a quota
     * @param id            the quota identifier
     * @param object        the quota object type
     * @param quotaType     the quota type
     * @param softLimit     the quota soft limit
     * @param hardLimit     the quota hard limit
     * @param unit          the quota unit
     * @param totalUsage    the quota total usage
     * @param membershipId  the quota membership identifier
     * @param owner         the quota owner
     */
    public Quota(Integer id,
                 @NotNull @NotEmpty String object,
                 @NotNull @NotEmpty String quotaType,
                 @NotNull Double softLimit,
                 @NotNull Double hardLimit,
                 @NotNull Double totalUsage,
                 @NotNull @NotEmpty String unit,
                 Integer membershipId,
                 String owner) {
        if ( id != null ) {
            if ( ! id.toString().equals("") ) {
                this.id = id;
            }
        }
        this.object = object;
        this.quotaType = quotaType;
        this.softLimit = softLimit;
        this.hardLimit = hardLimit;
        this.unit = unit;
        this.totalUsage = totalUsage;
        this.membershipId = membershipId;
        this.owner = owner;
    }

    /**
     * Get the quota id
     * @return  id  the quota identifier
     */
    @JsonProperty
    public Integer getId() {
        return id;
    }

    /**
     * Set the quota id
     * @param id the quota identifier
     */
    @JsonProperty
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the quota object type
     * @return object  the quota object type
     */
    @JsonProperty
    public String getObject() {
        return object;
    }

    /**
     * Set the quota object type
     * @param object  the quota object type
     */
    @JsonProperty
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Get the quota type
     * @return quotaType the quota type
     */
    @JsonProperty
    public String getQuotaType() {
        return quotaType;
    }

    /**
     * Set the quota type
     * @param quotaType the quota type
     */
    @JsonProperty
    public void setQuotaType(String quotaType) {
        this.quotaType = quotaType;
    }

    /**
     * Get the quota soft limit
     * @return softLimit  the quota soft limit
     */
    @JsonProperty
    public @NotNull Double getSoftLimit() {
        return softLimit;
    }

    /**
     * Set the quota soft limit
     * @param softLimit the quota soft limit
     */
    @JsonProperty
    public void setSoftLimit(@NotNull Double softLimit) {
        this.softLimit = softLimit;
    }

    /**
     * Get the quota hard limit
     * @return hardLimit  the quota hard limit
     */
    @JsonProperty
    public @NotNull Double getHardLimit() {
        return hardLimit;
    }

    /**
     * Set the quota hard limit
     * @param hardLimit  the quota hard limit
     */
    @JsonProperty
    public void setHardLimit(@NotNull Double hardLimit) {
        this.hardLimit = hardLimit;
    }

    /**
     * Get the quota total usage
     * @return totalUsage  the quota total usage
     */
    @JsonProperty
    public Double getTotalUsage() {
        if (totalUsage == null) {
            totalUsage = 0.0;
        }
        return totalUsage;
    }

    /**
     * Set the quota total usage
     * @param totalUsage the quota total usage
     */
    @JsonProperty
    public void setTotalUsage(Double totalUsage) {
        this.totalUsage = totalUsage;
    }

    /**
     * Get the quota unit
     * @return unit  the quota unit
     */
    @JsonProperty
    public String getUnit() {
        return unit;
    }

    /**
     * Set the quota unit
     * @param unit  the quota unit
     */
    @JsonProperty
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Get the membership id
     * @return membershipId  the quota membership identifier
     */
    @JsonProperty
    public Integer getMembershipId() {
        return membershipId;
    }

    /**
     * Set the membership id
     * @param membershipId  the quota membership identifier
     */
    @JsonProperty
    public void setMembershipId(Integer membershipId) {
        this.membershipId = membershipId;
    }

    /**
     * Get the owner
     * @return owner  the quota owner
     */
    @JsonProperty
    public String getOwner() {
        return owner;
    }

    /**
     * Set the owner
     * @param owner  the quota owner
     */
    @JsonProperty
    public void setOwner(String owner) {
        this.owner = owner;
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
        Quota quota = (Quota) o;
        return Objects.equals(getId(), quota.getId()) &&
            Objects.equals(getObject(), quota.getObject()) &&
            Objects.equals(getQuotaType(), quota.getQuotaType()) &&
            Objects.equals(getSoftLimit(), quota.getSoftLimit()) &&
            Objects.equals(getHardLimit(), quota.getHardLimit()) &&
            Objects.equals(getTotalUsage(), quota.getTotalUsage()) &&
            Objects.equals(getUnit(), quota.getUnit()) &&
            Objects.equals(getMembershipId(), quota.getMembershipId()) &&
            Objects.equals(getOwner(), quota.getOwner());
    }

    /**
     * Calculate a hash based on all fields
     * @return hashcode  the hashcode of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getObject(), getQuotaType(), getSoftLimit(),
            getHardLimit(), getTotalUsage(), getUnit(), getMembershipId(), getOwner());
    }
}
