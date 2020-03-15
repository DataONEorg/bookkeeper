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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A list of quota usage instances as a representation response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsageList extends BaseList {

    private List<Usage> usages;

    /**
     * Construct an empty usage list
     */
    public UsageList(List<Usage> usages) {
        this.usages = usages;
    }

    /**
     * Get the usages list
     * @return usages  the usages list
     */
    @JsonProperty
    public List<Usage> getUsages() {
        return usages;
    }

    /**
     * Set the usages list
     * @param usages  the usages list
     */
    @JsonProperty
    public void setUsages(List<Usage> usages) {
        this.usages = usages;
    }
}
