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

package org.dataone.bookkeeper.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.*;
import org.dataone.bookkeeper.jdbi.SubscriptionQuotasReducer;
import org.dataone.bookkeeper.jdbi.mappers.ProductMapper;
import org.dataone.bookkeeper.jdbi.mappers.SubscriptionMapper;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * A delegate class with helper methods for manipulating the subscriptions table for testing
 */
public class SubscriptionHelper {

    /**
     * Insert a test subscription with the given subscription id
     * @param subscriptionId
     * @return
     */
    public static Integer insertTestSubscription(Integer subscriptionId, Integer customerId) throws SQLException {

        BaseTestCase.dbi.useHandle(handle -> {
            handle.execute("INSERT INTO subscriptions (" +
                "id, " +
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
                    "?, " +
                    "?, " +
                    "to_timestamp(?), " +
                    "?, " +
                    "to_timestamp(?), " +
                    "?, " +
                    "?::json, " +
                    "?, " +
                    "?, " +
                    "to_timestamp(?), " +
                    "?, " +
                    "to_timestamp(?), " +
                    "to_timestamp(?) " +
                ")",
                subscriptionId,
                "subscription",
                null,
                "send_invoice",
                1570486366,
                customerId,
                "{}",
                2,
                1,
                1570486366,
                "paid",
                1573078366,
                1570486366
            );
        });

        return subscriptionId;
    }

    /**
     * Insert a test subscription given the Subscription instance
     * @param subscription
     * @return subscription the inserted subscription
     * @throws SQLException
     * @throws JsonProcessingException
     */
    public static Subscription insertTestSubscription(Subscription subscription)
        throws SQLException, JsonProcessingException {

        if (subscription.getProduct() != null &&
            subscription.getProduct().getId() != null &&
            subscription.getCustomerId() != null) {
            BaseTestCase.dbi.useHandle(handle ->
                handle.execute("INSERT INTO subscriptions (" +
                    "id, " +
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
                        "?, " +
                        "?, " +
                        "to_timestamp(?), " +
                        "?, " +
                        "to_timestamp(?), " +
                        "?, " +
                        "?::json, " +
                        "?, " +
                        "?, " +
                        "to_timestamp(?), " +
                        "?, " +
                        "to_timestamp(?), " +
                        "to_timestamp(?) " +
                    ")",
                    subscription.getId(),
                    subscription.getObject(),
                    subscription.getCanceledAt(),
                    subscription.getCollectionMethod(),
                    subscription.getCreated(),
                    subscription.getCustomerId(),
                    Jackson.newObjectMapper().writeValueAsString(subscription.getMetadata()),
                    subscription.getProduct().getId(),
                    subscription.getQuantity(),
                    subscription.getStartDate(),
                    subscription.getStatus(),
                    subscription.getTrialEnd(),
                    subscription.getTrialStart()
                )
            );
        } else {
            throw new SQLException("Subscriptions must include a valid product and customer id.");
        }
        return subscription;
    }

    /**
     * Create a subscription for unit tests
     * @param subscriptionId the subscription id
     * @return subscription the Subscription instance
     */
    public static Subscription createSubscription(Integer subscriptionId, Integer customerId, Integer productId) {

        // Create and insert a product to subscribe to
        Product product = ProductHelper.createTestProduct(productId);
        ProductHelper.insertTestProduct(product);

        // Create a customer to subscribe
        Customer customer = CustomerHelper.createCustomer(customerId);

        // Extract quotas from the product features
        List<Quota> quotas = new LinkedList<Quota>();
        ArrayNode features = (ArrayNode) product.getMetadata().get("features");
        Feature feature;
        for (JsonNode jsonNode : features) {
            feature = Jackson.newObjectMapper().convertValue(jsonNode, Feature.class);
            Quota quota = feature.getQuota();
            if ( quota != null ) {
                quota.setSubscriptionId(subscriptionId);
                quota.setSubject(customer.getSubject());
                quota.setUsage(0.0);
            }
            quotas.add(quota);
        }

        // Build the subscription
        Subscription subscription = new Subscription(
            subscriptionId,
            "subscription",
            null,
            "send_invoice",
            new Integer(1570486366),
            customer.getId(),
            Jackson.newObjectMapper().createObjectNode(),
            product,
            new Integer(1),
            new Integer(1570486366),
            "trialing",
            new Integer(1573078366),
            new Integer(1570486366),
            quotas
        );
        return subscription;
    }

    /**
     * Remove a test subscription given its id
     * @param subscriptionId
     */
    public static void removeTestSubscription(Integer subscriptionId) throws SQLException {

        BaseTestCase.dbi.useHandle(handle ->
            handle.execute("DELETE FROM subscriptions WHERE id = ?", subscriptionId)
        );
    }

    /**
     * Get a subscription given its id
     * @param subscriptionId
     * @return subscription the subscription of the given id
     */
    public static Subscription getSubscriptionById(Integer subscriptionId) {
        Subscription subscription = null;
        Product product = null;
        subscription = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery(
                "SELECT " +
                    "s.id AS s_id, " +
                    "s.object AS s_object, " +
                    "date_part('epoch', s.canceledAt)::int AS s_canceledAt, " +
                    "s.collectionMethod AS s_collectionMethod, " +
                    "date_part('epoch', s.created)::int AS s_created, " +
                    "s.customerId AS s_customerId, " +
                    "s.metadata::json AS s_metadata, " +
                    "s.productId AS s_productId, " +
                    "p.id AS p_id, " +
                    "p.object AS p_object, " +
                    "p.active AS p_active, " +
                    "p.name AS p_name, " +
                    "p.caption AS p_caption, " +
                    "p.description AS p_description, " +
                    "p.created AS p_created, " +
                    "p.statementDescriptor AS p_statementDescriptor, " +
                    "p.type AS p_type, " +
                    "p.unitLabel AS p_unitLabel, " +
                    "p.url AS p_url, " +
                    "p.metadata AS p_metadata, " +
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
                "LEFT JOIN products p ON s.productId = p.id " +
                "WHERE s.id = :id")
                .bind("id", subscriptionId)
                .registerRowMapper(new SubscriptionMapper())
                .registerRowMapper(new ProductMapper())
                .registerRowMapper(BeanMapper.factory(Quota.class, "q"))
                .reduceRows(new SubscriptionQuotasReducer())
                .findFirst()
                .get()
                //.map(new SubscriptionMapper()).
                //.one()
        );
        return subscription;
    }

    /**
     * Return the number of subscriptions given the customer id
     * @param subscriptionId the id of the customer
     * @return count the number of subscriptions
     */
    public static Integer getSubscriptionCountById(Integer subscriptionId) {
        Integer count = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT count(*) FROM subscriptions WHERE id = :id")
                .bind("id", subscriptionId)
                .mapTo(Integer.class)
                .one()
        );
        return count;
    }
}
