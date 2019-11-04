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

package org.dataone.bookkeeper.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataone.bookkeeper.api.Order;
import org.dataone.bookkeeper.jdbi.OrderStore;
import org.jdbi.v3.sqlobject.CreateSqlObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Objects;

/**
 * OrderService provides an abstraction of the OrderService store to hide store
 * implementation details.
 */
public abstract class OrderService {

    /**
     * Register a logger
     */
    public Log log = LogFactory.getLog(OrderService.class);

    /**
     * Create a OrderStore instance
     * @return orderStore - the order store instance
     */
    @CreateSqlObject
    abstract OrderStore orderStore();

    /**
     * List all orders
     * @return orders the order list
     */
    public List<Order> listOrders() {
        List<Order> orders = null;
        try {
            orders = orderStore().listOrders();
        } catch (Exception e) {
            log.error("Order listing failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return orders;
    }

    /**
     * Get the order by id
     * @param id the order id
     * @return order  the order
     */
    public Order getOrder(Integer id) {
        Order order = null;

        // Do we have a valid id?
        if (Objects.isNull(id) || id.intValue() < 0) {
            throw new WebApplicationException(
                "Please provide a valid id", Status.EXPECTATION_FAILED);
        }

        try {
            order = orderStore().getOrder(id);
        } catch (Exception e) {
            log.error("Order search by id failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return order;
    }


    /**
     * Get the order by id
     * @param customerId the order id
     * @return order  the order
     */
    public List<Order> findOrderByCustomerId(Integer customerId) {
        List<Order> orders;

        // Do we have a valid id?
        if (Objects.isNull(customerId) || customerId.intValue() < 0) {
            throw new WebApplicationException(
                "Please provide a valid customer id", Status.EXPECTATION_FAILED);
        }

        try {
            orders = orderStore().findOrdersByCustomerId(customerId);
        } catch (Exception e) {
            log.error("Order search by customer id failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return orders;
    }

    /**
     * Insert the order
     * @param order  the order
     * @return order  the order
     */
    public Order insert(Order order){

        // Do we have a valid order?
        if (Objects.isNull(order)) {
            log.error("The order insert failed for " + order.getId());
            throw new WebApplicationException("Please provide a valid order.", Status.NOT_MODIFIED);
        }

        // Insert it
        try {
            orderStore().insert(order);
        } catch (Exception e) {
            log.error("The order insert failed for " + order.getId());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.NOT_MODIFIED);
        }
        return order;
    }

    /**
     * Update the order
     * @param order  the order
     * @return order  the order
     */
    public Order update(Order order) {
        // Do we have a valid order?
        if (Objects.isNull(order)) {
            log.error("The order update failed for " + order.getId());
            throw new WebApplicationException("Please provide a valid order.", Status.NOT_MODIFIED);
        }

        // Update it
        try {
            orderStore().update(order);
        } catch (Exception e) {
            log.error("The order update failed for " + order.getId());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.NOT_MODIFIED);
        }
        return order;
    }

    /**
     * Delete the order by id
     * @param id  the order id
     * @return deleted  true if the order was deleted
     */
    public Boolean delete(Integer id) {
        Boolean deleted = false;
        try {
            orderStore().delete(id);
            deleted = true;
        } catch (Exception e) {
            log.error("The order delete failed for " + id);
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.NOT_MODIFIED);
        }
        return deleted;
    }

    // TODO: Add health check
}
