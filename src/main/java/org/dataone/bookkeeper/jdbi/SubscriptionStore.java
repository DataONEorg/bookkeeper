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

package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.api.Product;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.api.Subscription;
import org.dataone.bookkeeper.jdbi.mappers.SubscriptionMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;

import java.util.List;

/**
 * The subscription data access interfaces used to create, read, update, and delete
 * subscriptions from the database
 */
public interface SubscriptionStore {

    /** The query used to find all subscriptions with their quotas */
    String SELECT_CLAUSE =
        "SELECT " +
            "s.id AS s_id, " +
            "s.object AS s_object, " +
            "date_part('epoch', s.canceledAt)::int AS s_canceledAt, " +
            "s.collectionMethod AS s_collectionMethod, " +
            "date_part('epoch', s.created)::int AS s_created, " +
            "s.customerId AS s_customerId, " +
            "c.subject AS c_subject, " +
            "s.metadata::json AS s_metadata, " +
            "s.productId AS s_productId, " +
            "p.id AS p_id, " +
            "p.object AS p_object, " +
            "p.active AS p_active, " +
            "p.amount AS p_amount, " +
            "p.caption AS p_caption, " +
            "date_part('epoch', p.created)::int AS p_created, " +
            "p.currency AS p_currency, " +
            "p.description AS p_description, " +
            "p.interval AS p_interval, " +
            "p.name AS p_name, " +
            "p.statementDescriptor AS p_statementDescriptor, " +
            "p.type AS p_type, " +
            "p.unitLabel AS p_unitLabel, " +
            "p.url AS p_url, " +
            "p.metadata::json AS p_metadata, " +
            "s.quantity AS s_quantity, " +
            "date_part('epoch', s.startDate)::int AS s_startDate, " +
            "s.status AS s_status, " +
            "date_part('epoch', s.trialEnd)::int AS s_trialEnd, " +
            "date_part('epoch', s.trialStart)::int  AS s_trialStart, " +
            "q.id AS q_id, " +
            "q.object AS q_object, " +
            "q.name AS q_name, " +
            "q.softLimit AS q_softLimit, " +
            "q.hardLimit AS q_hardLimit, " +
            "q.usage AS q_usage, " +
            "q.unit AS q_unit, " +
            "q.subscriptionId AS q_subscriptionId, " +
            "q.subject AS q_subject " +
        "FROM subscriptions s " +
        "LEFT JOIN quotas q ON s.id = q.subscriptionId " +
        "LEFT JOIN customers c ON s.customerId = c.id " +
        "LEFT JOIN products p ON s.productId = p.id ";

    /** Clause to order listed results */
    String ORDER_CLAUSE = "ORDER BY s.created ";

    /** The full ordered query */
    String SELECT_ALL = SELECT_CLAUSE + ORDER_CLAUSE;

    /** The query used to find an individual subscription */
    String SELECT_ONE = SELECT_CLAUSE + "WHERE s.id = :id";

    /** The query used to find a subscription by subject identifier */
    String SELECT_SUBJECT = SELECT_CLAUSE + "WHERE c.subject = :subject";

    /**
     * List all subscriptions with their quotas
     * @return subscriptions The list of subscriptions
     */
    @SqlQuery(SELECT_ALL)
    @RegisterBeanMapper(value = Quota.class, prefix = "q")
    @RegisterBeanMapper(value = Product.class, prefix = "p")
    @RegisterRowMapper(value = SubscriptionMapper.class)
    @UseRowReducer(SubscriptionQuotasReducer.class)
    List<Subscription> listSubscriptions();

    /**
     * Get an individual subscription
     * @param id the subscription identifier
     * @return subscription The individual subscription
     */
    @SqlQuery(SELECT_ONE)
    @RegisterBeanMapper(value = Quota.class, prefix = "q")
    @RegisterBeanMapper(value = Product.class, prefix = "p")
    @RegisterRowMapper(value = SubscriptionMapper.class)
    @UseRowReducer(SubscriptionQuotasReducer.class)
    Subscription getSubscription(@Bind("id") Integer id);

    /**
     * Get a subscription by subject identifier
     * @param subject the customer subject identifier
     * @return subscription the subscription with the given subject identifier
     */
    @SqlQuery(SELECT_SUBJECT)
    @RegisterBeanMapper(value = Quota.class, prefix = "q")
    @RegisterBeanMapper(value = Product.class, prefix = "p")
    @RegisterRowMapper(value = SubscriptionMapper.class)
    @UseRowReducer(SubscriptionQuotasReducer.class)
    Subscription findSubscriptionBySubject(@Bind("subject") String subject);

    /**
     * Insert a subscription
     * @param subscription the subscription to insert
     */
    @SqlUpdate(
        "INSERT INTO subscriptions (" +
            "object, " +
            "canceledAt, " +
            "collectionMethod, " +
            "created, " +
            "customerId, " +
            "metadata, " +
            "productId, " +
            "quantity, " +
            "startDate, " +
            "status, " +
            "trialEnd, " +
            "trialStart " +
        ") VALUES (" +
            ":getObject, " +
            "to_timestamp(:getCanceledAt), " +
            ":getCollectionMethod, " +
            "to_timestamp(:getCreated), " +
            ":getCustomerId, " +
            ":getMetadataJSON::json, " +
            ":getProductId, " +
            ":getQuantity, " +
            "to_timestamp(:getStartDate), " +
            ":getStatus, " +
            "to_timestamp(:getTrialEnd), " +
            "to_timestamp(:getTrialStart) " +
        ")"
    )
    void insert(@BindMethods Subscription subscription);

    /**
     * Update a subscription
     * @param subscription the subscription to update
     */
    @SqlUpdate(
        "UPDATE subscriptions SET " +
            "id = :getId, " +
            "object = :getObject, " +
            "canceledAt = to_timestamp(:getCanceledAt), " +
            "collectionMethod = :getCollectionMethod, " +
            "created = to_timestamp(:getCreated), " +
            "customerId = :getCustomerId, " +
            "metadata = :getMetadataJSON::json, " +
            "productId = :getProductId, " +
            "quantity = :getQuantity, " +
            "startDate = to_timestamp(:getStartDate), " +
            "status = :getStatus, " +
            "trialEnd = to_timestamp(:getTrialEnd), " +
            "trialStart = to_timestamp(:getTrialStart) "
    )
    void update(@BindMethods Subscription subscription);

    /**
     * Delete a subscription
     * @param id
     */
    @SqlUpdate("DELETE FROM subscriptions WHERE id = :id")
    void delete(@Bind("id") Integer id);
}
