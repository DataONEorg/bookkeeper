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
    private Integer instanceId;

    /* The identifier of the quota usage */
    @NotNull
    private Integer quantity;


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
    public Integer getInstanceId() {
        return instanceId;
    }

    /**
     * Set the quota usage instance identifier
     * @param instanceId the quota usage instance identifier
     */
    public void setInstanceId(Integer instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * Get the quota usage quantity
     * @return quantity  the quota usage quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Set the quota usage quantity
     * @param quantity  the quota usage quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

