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

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.Subscription;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.helpers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test the product data access object
 */
public class SubscriptionStoreTest extends BaseTestCase {

    /* The SubscriptionStore to test */
    private SubscriptionStore subscriptionStore;

    // A list of subscription ids used in testing
    private List<Integer> subscriptionIds = new ArrayList<Integer>();

    // A list of customer ids used in testing
    private List<Integer> customerIds = new ArrayList<Integer>();

    // A list of product ids used in testing
    private List<Integer> productIds = new ArrayList<Integer>();

    /**
     * Set up the Store for testing
     */
    @BeforeEach
    public void init() {
        subscriptionStore = dbi.onDemand(SubscriptionStore.class);
    }

    /**
     * Tear down resources
     */
    @AfterEach
    public void tearDown() {
        // Remove test subscription entries
        for (Integer subscriptionId : this.subscriptionIds) {
            try {
                SubscriptionHelper.removeTestSubscription(subscriptionId);
            } catch (SQLException e) {
                fail(e);
            }
        }

        // Remove test customer entries
        for (Integer customerId : this.customerIds) {
            try {
                CustomerHelper.removeTestCustomer(customerId);
            } catch (SQLException e) {
                fail(e);
            }
        }

        // Remove test product entries
        for (Integer productId : this.productIds) {
            try {
                ProductHelper.removeTestProduct(productId);
            } catch (SQLException e) {
                fail(e);
            }
        }
    }

    /**
     * Test listing subscriptions with empty quotas
     */
    @Test
    @DisplayName("Test listing the subscriptions")
    public void testListSubscriptions() {
        Integer subscriptionId = null;
        try {
            // Insert a new customer
            Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
            this.customerIds.add(customerId);
            // Insert a new subscription
            subscriptionId = SubscriptionHelper.insertTestSubscription(StoreHelper.getRandomId(), customerId);
            this.subscriptionIds.add(subscriptionId);

        } catch (SQLException e) {
            fail(e);
        }
        assertTrue(subscriptionStore.listSubscriptions().size() >= 1);
    }

    /**
     * Test listing a subscription with storage and portal quotas
     */
    @Test
    @DisplayName("Test listing subscriptions with associated storage and portal quotas")
    public void testListSubscriptionsWithStorageAndPortalQuotas() {

        try {
            // Insert a new customer
            Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
            this.customerIds.add(customerId);

            // Mint a new product id
            Integer productId = StoreHelper.getRandomId();
            this.productIds.add(productId);

            // Insert a subscription
            Subscription subscription = SubscriptionHelper.insertTestSubscription(
                SubscriptionHelper.createSubscription(StoreHelper.getRandomId(), customerId, productId)
            );
            this.subscriptionIds.add(subscription.getId());

            // Insert a storage and portal quotas for the subscription
            final Map<Integer, Quota> quotas =
                QuotaHelper.insertTestStorageAndPortalQuotasWithSubscription(
                    StoreHelper.getRandomId(), StoreHelper.getRandomId(), subscription.getId());
            List<Subscription> subscriptions = subscriptionStore.listSubscriptions();

            // Ensure we get a subscription returned
            assertTrue(subscriptions.size() >= 1);
                subscriptions.forEach(subscription2 -> {

                    // Ensure the quota count matches
                    assertTrue(subscription2.getQuotas().size() == quotas.size());

                    // Ensure the quota content matches
                    subscription2.getQuotas()
                        .forEach(quota -> {
                            Quota expectedQuota = quotas.get(quota.getId());
                            assertTrue(quota.getId().equals(expectedQuota.getId()));
                            assertTrue(quota.getObject().equals(expectedQuota.getObject()));
                            assertTrue(quota.getName().equals(expectedQuota.getName()));
                            assertTrue(quota.getSoftLimit().equals(expectedQuota.getSoftLimit()));
                            assertTrue(quota.getHardLimit().equals(expectedQuota.getHardLimit()));
                            assertTrue(quota.getUnit().equals(expectedQuota.getUnit()));
                            assertTrue(quota.getSubscriptionId().equals(expectedQuota.getSubscriptionId()));
                        });
                });
        } catch (SQLException e) {
            fail(e);
        } catch (JsonProcessingException e) {
            fail(e);
        }
    }

    /**
     * Test getting a subscription by id
     * @throws SQLException
     */
    @Test
    @DisplayName("Test getting a subscription by id")
    public void testGetSubscriptionById() throws SQLException {
        // Insert a new customer
        Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
        this.customerIds.add(customerId);

        // Insert a subscription
        final Integer subscriptionId = SubscriptionHelper.insertTestSubscription(
            StoreHelper.getRandomId(), customerId
        );
        this.subscriptionIds.add(subscriptionId);

        // Get the subscription
        Subscription subscription = subscriptionStore.getSubscription(subscriptionId);
        assertTrue(subscription.getId().equals(subscriptionId));
    }

    /**
     * Test getting a subscription by subject identifier
     * @throws SQLException
     */
    @Test
    @DisplayName("Test finding a subscription by subject identifier")
    public void testFindSubscriptionBySubject() throws SQLException, JsonProcessingException {
        // Insert a new customer
        Customer customer = CustomerHelper.insertTestCustomer(
            CustomerHelper.createCustomer(StoreHelper.getRandomId())
        );
        this.customerIds.add(customer.getId());

        // Mint a new product id
        Integer productId = StoreHelper.getRandomId();
        this.productIds.add(productId);

        // Insert a subscription
        final Subscription expectedSubscription = SubscriptionHelper.insertTestSubscription(
            SubscriptionHelper.createSubscription(
                StoreHelper.getRandomId(), customer.getId(), productId)
        );
        this.subscriptionIds.add(expectedSubscription.getId());

        // Get the subscription
        Subscription subscription = subscriptionStore.findSubscriptionBySubject(customer.getSubject());
        assertTrue(subscription.getId().equals(expectedSubscription.getId()));
    }

    /**
     * Test inserting a subscription
     */
    @Test
    @DisplayName("Test inserting a subscription")
    public void testInsert() {
        Customer customer = null;
        try {
            // Insert a new customer
            customer = CustomerHelper.insertTestCustomer(
                CustomerHelper.createCustomer(StoreHelper.getRandomId())
            );
            this.customerIds.add(customer.getId());

            // Mint a new product id
            Integer productId = StoreHelper.getRandomId();
            this.productIds.add(productId);

            // Create a Subscription to insert
            Subscription expectedSubscription = SubscriptionHelper.createSubscription(
                StoreHelper.getRandomId(), customer.getId(), productId
            );
            this.subscriptionIds.add(expectedSubscription.getId());

            // Insert the subscription
            subscriptionStore.insert(expectedSubscription);

            // Then get the subscription to ensure it was inserted
            Subscription subscription = SubscriptionHelper.getSubscriptionById(expectedSubscription.getId());

            assertTrue(subscription.getId().equals(expectedSubscription.getId()));
        } catch (SQLException e) {
            fail(e);
        } catch (JsonProcessingException jpe) {
            fail(jpe);
        }
    }

    @Test
    @DisplayName("Test updating a subscription")
    public void testUpdate() throws SQLException, JsonProcessingException {
        // Insert a new customer
        Customer customer = CustomerHelper.insertTestCustomer(
            CustomerHelper.createCustomer(StoreHelper.getRandomId())
        );
        this.customerIds.add(customer.getId());

        // Mint a new product id
        Integer productId = StoreHelper.getRandomId();
        this.productIds.add(productId);

        // Insert a new test subscription
        Subscription expectedSubscription = SubscriptionHelper.insertTestSubscription(
            SubscriptionHelper.createSubscription(
                StoreHelper.getRandomId(), customer.getId(), productId));
        this.subscriptionIds.add(expectedSubscription.getId());

        // Now update the subscription locally
        expectedSubscription.setCanceledAt(new Integer(1570912264));
        expectedSubscription.setStatus("canceled");


        // Push the changes to the database
        subscriptionStore.update(expectedSubscription);

        // Get the updated subscription from the database
        Subscription updatedSubscription = SubscriptionHelper.getSubscriptionById(expectedSubscription.getId());

        assertTrue(updatedSubscription.getCanceledAt().intValue() ==
            expectedSubscription.getCanceledAt().intValue());
        assertTrue(updatedSubscription.getStatus().equals(expectedSubscription.getStatus()));

    }

    @Test
    @DisplayName("Test deleting a subscription")
    public void testDelete() throws SQLException, JsonProcessingException {
        // Insert a new customer
        Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
        this.customerIds.add(customerId);

        // Mint a new product id
        Integer productId = StoreHelper.getRandomId();
        this.productIds.add(productId);

        // Insert a new subscription
        Subscription subscription = SubscriptionHelper.insertTestSubscription(
            SubscriptionHelper.createSubscription(
                StoreHelper.getRandomId(), customerId, productId));
        this.subscriptionIds.add(subscription.getId());

        // Delete it
        subscriptionStore.delete(subscription.getId());

        // It's gone, right?
        assertThat(SubscriptionHelper.getSubscriptionCountById(subscription.getId()) == 0);
    }
}
