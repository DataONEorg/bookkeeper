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

package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.api.Order;
import org.dataone.bookkeeper.jdbi.mappers.OrderMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

/**
 * The order data access interfaces used to create, read, update, and delete
 * orders from the database
 */
public interface OrderStore {

    /** The query used to find all orders */
    String SELECT_CLAUSE = "SELECT " +
        "o.id, " +
        "o.object, " +
        "o.amount, " +
        "o.amountReturned, " +
        "o.charge::json AS charge, " +
        "date_part('epoch', o.created)::int AS created, " +
        "o.currency, " +
        "o.customer, " +
        "o.email, " +
        "o.items::json AS items, " +
        "o.metadata::json AS metadata, " +
        "o.status, " +
        "o.statusTransitions::json AS statusTransitions, " +
        "date_part('epoch', o.updated)::int AS updated " +
        "FROM orders o ";

    /** Clause to order listed results */
    String ORDER_CLAUSE = "ORDER BY o.id, o.created, o.updated ";

    /** The full ordered query */
    String SELECT_ALL = SELECT_CLAUSE + ORDER_CLAUSE;

    String SELECT_ONE = SELECT_CLAUSE + "WHERE o.id = :id";

    String SELECT_CUSTOMER = SELECT_CLAUSE + "WHERE customer = :customer";

    /**
     * List all orders
     * @return
     */
    @SqlQuery(SELECT_ALL)
    @RegisterRowMapper(OrderMapper.class)
    @UseRowMapper(OrderMapper.class)
    List<Order> listOrders();

    /**
     * Get an order by order id
     * @param id the order id
     * @return
     */
    @SqlQuery(SELECT_ONE)
    @RegisterRowMapper(OrderMapper.class)
    @UseRowMapper(OrderMapper.class)
    Order getOrder(@Bind("id") Integer id);

    /**
     * Find orders by customer id
     * @param customerId the id of the customer
     * @return
     */
    @SqlQuery(SELECT_CUSTOMER)
    @RegisterRowMapper(OrderMapper.class)
    @UseRowMapper(OrderMapper.class)
    List<Order> findOrdersByCustomerId(@Bind("customer") Integer customerId);

    /**
     * Insert an order
     * @param order the order to insert
     */
    @SqlUpdate("INSERT INTO orders (" +
        "object, " +
        "amount, " +
        "amountReturned, " +
        "charge, " +
        "created, " +
        "currency, " +
        "customer, " +
        "email, " +
        "items, " +
        "metadata, " +
        "status, " +
        "statusTransitions, " +
        "updated " +
        ") VALUES (" +
        ":getObject, " +
        ":getAmount, " +
        ":getAmountReturned, " +
        ":getChargeJSON::json, " +
        "to_timestamp(:getCreated), " +
        ":getCurrency, " +
        ":getCustomer, " +
        ":getEmail, " +
        ":getItemsJSON::json, " +
        ":getMetadataJSON::json, " +
        ":getStatus, " +
        ":getStatusTransitionsJSON::json, " +
        "to_timestamp(:getUpdated))")
    void insert(@BindMethods Order order);

    /**
     * Update an order
     * @param order the order to update
     */
    @SqlUpdate("UPDATE orders SET " +
        "object = :getObject, " +
        "amount = :getAmount, " +
        "amountReturned = :getAmountReturned, " +
        "charge = :getChargeJSON::json, " +
        "created = to_timestamp(:getCreated), " +
        "currency = :getCurrency, " +
        "customer = :getCustomer, " +
        "email = :getEmail, " +
        "items = :getItemsJSON::json, " +
        "metadata = :getMetadataJSON::json, " +
        "status = :getStatus, " +
        "statusTransitions = :getStatusTransitionsJSON::json, " +
        "updated = to_timestamp(:getUpdated) " +
        "WHERE id = :getId")
    void update(@BindMethods Order order);

    /**
     * Delete an order
     * @param id the order id to delete
     */
    @SqlUpdate("DELETE FROM orders WHERE id = :id")
    void delete(@Bind("id") Integer id);
}
