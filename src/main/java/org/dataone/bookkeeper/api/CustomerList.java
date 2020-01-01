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
 * A list of customers used as a representation response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerList extends BaseList {

    private List<Customer> customers;

    /**
     * Construct an empty customer list
     */
    public CustomerList(List<Customer> customers) {
        this.customers = customers;
    }

    /**
     * Get the customers list
     * @return customers  the customers list
     */
    @JsonProperty
    public List<Customer> getCustomers() {
        return customers;
    }

    /**
     * Set the customers list
     * @param customers  the customers list
     */
    @JsonProperty
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
