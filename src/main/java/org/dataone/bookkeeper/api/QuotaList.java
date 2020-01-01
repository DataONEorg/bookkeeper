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

package org.dataone.bookkeeper.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * A list of quotas used as a representation response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuotaList extends BaseList {

    private List<Quota> quotas;

    /**
     * Construct an empty quota list
     */
    public QuotaList(List<Quota> quotas) {
        this.quotas = quotas;
    }

    /**
     * Get the quotas list
     * @return quotas  the quotas list
     */
    @JsonProperty
    public List<Quota> getQuotas() {
        return quotas;
    }

    /**
     * Set the quotas list
     * @param quotas  the quotas list
     */
    @JsonProperty
    public void setQuotas(List<Quota> quotas) {
        this.quotas = quotas;
    }
}
