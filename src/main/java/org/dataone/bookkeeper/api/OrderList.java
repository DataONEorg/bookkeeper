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
 * A list of orders used as a representation response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderList extends BaseList {

    private List<Order> orders;

    /**
     * Construct an empty order list
     */
    public OrderList(List<Order> orders) {
        this.orders = orders;
    }

    /**
     * Get the orders list
     * @return orders  the orders list
     */
    @JsonProperty
    public List<Order> getOrders() {
        return orders;
    }

    /**
     * Set the orders list
     * @param orders  the orders list
     */
    @JsonProperty
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
