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

/**
 * A base list type with start, count, and total properties
 */
public class BaseList {

    /* The start index in the list */
    private Integer start;

    /* The subset count of the list */
    private Integer count;

    /* The total elements in the list */
    private Integer total;

    /**
     * Construct a base list
     */
    public BaseList() {

    }

    /**
     * Get the start index
     * @return start the start paging index
     */
    @JsonProperty
    public Integer getStart() {
        return start;
    }

    /**
     * Set the start index
     * @param start the start paging index
     */
    @JsonProperty
    public void setStart(Integer start) {
        this.start = start;
    }

    /**
     * Get the return count
     * @return count the count of items to be returned
     */
    @JsonProperty
    public Integer getCount() {
        return count;
    }

    /**
     * Set the return count
     * @param count the count of items to be returned
     */
    @JsonProperty
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * Get the total
     * @return total the total items in the list
     */
    @JsonProperty
    public Integer getTotal() {
        return total;
    }

    /**
     * Set the total
     * @param total the total items in the list
     */
    @JsonProperty
    public void setTotal(Integer total) {
        this.total = total;
    }
}
