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
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.jdbi.mappers.OrderMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;

import java.util.List;

/**
 * The order data access interfaces used to create, read, update, and delete
 * orders from the database
 */
public interface OrderStore {

    /** The query used to find all orders */
    String SELECT_CLAUSE = "SELECT " +
        "o.id AS o_id, " +
        "o.object AS o_object, " +
        "o.amount AS o_amount, " +
        "o.amountReturned AS o_amountReturned, " +
        "o.charge::json AS o_charge, " +
        "date_part('epoch', o.created)::int AS o_created, " +
        "o.currency AS o_currency, " +
        "o.customer AS o_customer, " +
        "o.subject AS o_subject, " +
        "o.email AS o_email, " +
        "o.items::json AS o_items, " +
        "o.metadata::json AS o_metadata, " +
        "o.name AS o_name, " +
        "o.status AS o_status, " +
        "o.statusTransitions::json AS o_statusTransitions, " +
        "date_part('epoch', o.updated)::int AS o_updated, " +
        "o.seriesId AS o_seriesId, " +
        "date_part('epoch', o.startDate)::int AS o_startDate, " +
        "date_part('epoch', o.endDate)::int AS o_endDate, " +
        "q.id AS q_id, " +
        "q.object AS q_object, " +
        "q.quotaType AS q_quotaType, " +
        "q.softLimit AS q_softLimit, " +
        "q.hardLimit AS q_hardLimit, " +
        "q.totalUsage AS q_totalUsage, " +
        "q.unit AS q_unit, " +
        "q.orderId AS q_orderId, " +
        "q.subject AS q_subject, " +
        "q.name AS q_name " +
        "FROM orders o " +
        "LEFT JOIN quotas q ON q.orderId = o.id ";
    /** Clause to order listed results */
    String ORDER_CLAUSE = "ORDER BY o.id, o.created, o.updated ";

    /** The full ordered query */
    String SELECT_ALL = SELECT_CLAUSE + ORDER_CLAUSE;

    String SELECT_ONE = SELECT_CLAUSE + "WHERE o.id = :id ";

    String SELECT_CUSTOMER = SELECT_CLAUSE + "WHERE customer = :customer ";

    String SELECT_SUBJECTS = SELECT_CLAUSE + "WHERE o.subject IN (<subjects>) " + ORDER_CLAUSE;

    /**
     * List all orders
     * @return the order list
     */
    @SqlQuery(SELECT_ALL)
    @RegisterBeanMapper(value = Quota.class, prefix = "q")
    @RegisterRowMapper(OrderMapper.class)
    @UseRowReducer(OrderQuotasReducer.class)
    List<Order> listOrders();

    /**
     * Get an order by order id
     * @param id the order id
     * @return the desired order
     */
    @SqlQuery(SELECT_ONE)
    @RegisterBeanMapper(value = Quota.class, prefix = "q")
    @RegisterRowMapper(OrderMapper.class)
    @UseRowReducer(OrderQuotasReducer.class)
    Order getOrder(@Bind("id") Integer id);

    /**
     * Find orders by customer id
     * @param customerId the id of the customer
     * @return the desired orders
     */
    @SqlQuery(SELECT_CUSTOMER)
    @RegisterBeanMapper(value = Quota.class, prefix = "q")
    @RegisterRowMapper(OrderMapper.class)
    @UseRowReducer(OrderQuotasReducer.class)
    List<Order> findOrdersByCustomerId(@Bind("customer") Integer customerId);

    /**
     * Find orders by subject
     * @param subjects the list of subjects for the desired orders
     * @return the desired orders
     */
    @SqlQuery(SELECT_SUBJECTS)
    @RegisterBeanMapper(value = Quota.class, prefix = "q")
    @RegisterRowMapper(OrderMapper.class)
    @UseRowReducer(OrderQuotasReducer.class)
    List<Order> findOrdersBySubjects(@BindList("subjects") List<String> subjects);
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
        "subject, " +
        "email, " +
        "items, " +
        "metadata, " +
        "name, " +
        "status, " +
        "statusTransitions, " +
        "updated, " +
        "seriesId, " +
        "startDate, " +
        "endDate " +
        ") VALUES (" +
        ":getObject, " +
        ":getAmount, " +
        ":getAmountReturned, " +
        ":getChargeJSON::json, " +
        "to_timestamp(:getCreated), " +
        ":getCurrency, " +
        ":getCustomer, " +
        ":getSubject, " +
        ":getEmail, " +
        ":getItemsJSON::json, " +
        ":getMetadataJSON::json, " +
        ":getName, " +
        ":getStatus, " +
        ":getStatusTransitionsJSON::json, " +
        "to_timestamp(:getUpdated), " +
        ":getSeriesId, " +
        "to_timestamp(:getStartDate), " +
        "to_timestamp(:getEndDate)) " +
        "RETURNING id")
    @GetGeneratedKeys
    Integer insert(@BindMethods Order order);

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
        "subject = :getSubject, " +
        "email = :getEmail, " +
        "items = :getItemsJSON::json, " +
        "metadata = :getMetadataJSON::json, " +
        "name = :getName, " +
        "status = :getStatus, " +
        "statusTransitions = :getStatusTransitionsJSON::json, " +
        "updated = to_timestamp(:getUpdated), " +
        "seriesId = :getSeriesId, " +
        "startDate = to_timestamp(:getStartDate), " +
        "endDate = to_timestamp(:getEndDate) " +
        "WHERE id = :getId " +
        "RETURNING id")
    @GetGeneratedKeys
    Integer update(@BindMethods Order order);

    /**
     * Delete an order
     * @param id the order id to delete
     */
    @SqlUpdate("DELETE FROM orders WHERE id = :id")
    void delete(@Bind("id") Integer id);
}
