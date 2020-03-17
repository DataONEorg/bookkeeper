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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class Usage {

    /* The identifier of the quota usage */
    private Integer id;

    /* The identifier of the quota usage */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "usage")
    private String object;

    /* The identifier of the quota usage */
    @NotNull
    private Integer quotaId;

    /* The identifier of the quota usage */
    @NotNull
    private String instanceId;

    /* The identifier of the quota usage */
    private @NotNull Double quantity;


    /**
     * Construct an empty usage instance
     */
    public Usage() {
    }

    /**
     * Construct a Usage instance
     * @param id  the identifier of the quota usage instance
     * @param object the object type of the quota usage instance ("usage")
     * @param quotaId  the identifier of the associated quota
     * @param instanceId  the identifier of the instance object using a portion of the quota
     * @param quantity  the quantity of the quota used
     */
    public Usage(Integer id,
                 @NotEmpty @NotNull @Pattern(regexp = "usage") String object,
                 @NotNull Integer quotaId,
                 @NotNull String instanceId,
                 @NotNull Double quantity) {
        this.id = id;
        this.object = object;
        this.quotaId = quotaId;
        this.instanceId = instanceId;
        this.quantity = quantity;
    }

    /**
     * Get the quota usage identifier
     * @return id  the quota usage identifier
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the quota usage identifier
     * @param id  the quota usage identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the quota usage object type
     * @return object  the quota usage object type
     */
    public String getObject() {
        return object;
    }

    /**
     * Set the quota usage object type
     * @param object  the quota usage object type
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Get the quota identifier
     * @return quotaId  the quota identifier
     */
    public Integer getQuotaId() {
        return quotaId;
    }

    /**
     * Set the quota identifier
     * @param quotaId  the quota identifier
     */
    public void setQuotaId(Integer quotaId) {
        this.quotaId = quotaId;
    }

    /**
     * Get the quota usage instance identifier
     * @return instanceId  the quota usage instance identifier
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * Set the quota usage instance identifier
     * @param instanceId the quota usage instance identifier
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * Get the quota usage quantity
     * @return quantity  the quota usage quantity
     */
    public @NotNull Double getQuantity() {
        return quantity;
    }

    /**
     * Set the quota usage quantity
     * @param quantity  the quota usage quantity
     */
    public void setQuantity(@NotNull Double quantity) {
        this.quantity = quantity;
    }

    /**
     * Determine equality with the given object
     * @param o  the object to compare
     * @return  true if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usage usage = (Usage) o;
        return Objects.equals(getId(), usage.getId()) &&
            getObject().equals(usage.getObject()) &&
            getQuotaId().equals(usage.getQuotaId()) &&
            getInstanceId().equals(usage.getInstanceId()) &&
            getQuantity().equals(usage.getQuantity());
    }

    /**
     * Generate a hashcode for the object based on its members
     * @return  the object hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getObject(), getQuotaId(), getInstanceId(), getQuantity());
    }
}

