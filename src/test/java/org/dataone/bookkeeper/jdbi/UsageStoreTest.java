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
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.api.Usage;
import org.dataone.bookkeeper.helpers.*;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class UsageStoreTest extends BaseTestCase {

    // The QuotaStore to test
    private QuotaStore quotaStore;

    // The UsageStore to test
    private UsageStore usageStore;

    // A list of quotaIds used in testing
    private List<Integer> quotaIds = new ArrayList<Integer>();

    // A list of usageIds used in testing
    private List<Integer> usageIds = new ArrayList<Integer>();

    // A list of customer ids used in testing
    private List<Integer> customerIds = new ArrayList<Integer>();

    // A list of product ids used in testing
    private List<Integer> productIds = new ArrayList<Integer>();

    // A list of subscription ids used in testing
    private List<Integer> subscriptionIds = new ArrayList<Integer>();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Set up the Store for testing
     */
    @BeforeEach
    public void init() {
        quotaStore = dbi.onDemand(QuotaStore.class);
        usageStore = dbi.onDemand(UsageStore.class);
    }

    /**
     * Tear down resources
     */
    @AfterEach
    public void tearDown() {
        // Remove test usage entries
        for (Integer usageId : this.usageIds) {
            try {
                UsageHelper.removeTestUsage(usageId);
            } catch (SQLException e) {
                fail();
            }
        }

        // Remove test quota entries
        for (Integer quotaId : this.quotaIds) {
            try {
                QuotaHelper.removeTestQuota(quotaId);
            } catch (SQLException e) {
                fail();
            }
        }

        // Remove test customer entries
        for (Integer customerId : this.customerIds) {
            try {
                CustomerHelper.removeTestCustomer(customerId);
            } catch (SQLException e) {
                fail();
            }
        }

        // Remove test product entries
        for (Integer productId : this.productIds) {
            try {
                ProductHelper.removeTestProduct(productId);
            } catch (SQLException e) {
                fail();
            }
        }

        // Remove test subscription entries
        for (Integer subscriptionId : this.subscriptionIds) {
            try {
                SubscriptionHelper.removeTestSubscription(subscriptionId);
            } catch (SQLException e) {
                fail();
            }
        }
    }

    /**
     * Test getting the full Quota list
     */
    @Test
    @DisplayName("Test listing the usages")
    public void testListUsages() {
        assertThat(usageStore.listUsages().size() >= 3);
    }

    /**
     * Test getting a single quota by ID
     */
    @Test
    @DisplayName("Test get usage")
    public void testGetQuota() {
        try {
            // Insert a customer
            Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
            this.customerIds.add(customerId); // To be deleted

            // Insert a subscription
            Integer subscriptionId =
                    SubscriptionHelper.insertTestSubscription(StoreHelper.getRandomId(), customerId);
            this.subscriptionIds.add(subscriptionId); // To be deleted
            Integer quotaId = QuotaHelper.insertTestQuotaWithSubscription(StoreHelper.getRandomId(), subscriptionId);
            this.quotaIds.add(quotaId); // To be deleted

            String instanceId = StoreHelper.getRandomId().toString();
            Quota quota = quotaStore.getQuota(quotaId);
            Integer usageId = UsageHelper.insertTestUsageInstanceId(StoreHelper.getRandomId(), quotaId, instanceId);
            this.usageIds.add(usageId);

            Usage usage = usageStore.getUsage(usageId);
            assertEquals(usageId, usage.getId());
        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Test getting quotas by subscription ID
     */
    @Test
    @DisplayName("Test getting usages by instance ID")
    public void testFindUsagesByInstanceId() {

        try {
            // Insert a customer
            Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
            this.customerIds.add(customerId); // To be deleted

            // Insert a subscription
            Integer subscriptionId =
                    SubscriptionHelper.insertTestSubscription(StoreHelper.getRandomId(), customerId);
            this.subscriptionIds.add(subscriptionId); // To be deleted
            Integer quotaId = QuotaHelper.insertTestQuotaWithSubscription(StoreHelper.getRandomId(), subscriptionId);
            this.quotaIds.add(quotaId); // To be deleted

            String instanceId = StoreHelper.getRandomId().toString();
            Quota quota = quotaStore.getQuota(quotaId);
            Integer usageId = UsageHelper.insertTestUsageInstanceId(StoreHelper.getRandomId(), quotaId, instanceId);
            this.usageIds.add(usageId);

            Usage usage = usageStore.findUsageByInstanceId(instanceId);
            assertEquals(usage.getInstanceId(), instanceId);

            assertEquals(usage.getQuotaId(), quota.getId());

        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Test getting usages by instance ID and subjects list
     */
    @Test
    @DisplayName("Test getting usages by instance ID and subjects")
    public void testFindUsagesByInstanceIdAndSubjects() {

        try {
            List<String> subjects = new ArrayList<>();
            // Insert a customer
            Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
            this.customerIds.add(customerId); // To be deleted

            // Insert a subscription
            Integer subscriptionId =
                    SubscriptionHelper.insertTestSubscription(StoreHelper.getRandomId(), customerId);
            this.subscriptionIds.add(subscriptionId); // To be deleted
            Integer quotaId = StoreHelper.getRandomId();
            QuotaHelper.insertTestQuotaWithSubject(quotaId, subscriptionId,"http://orcid.org/0000-0002-2192-403X");
            this.quotaIds.add(quotaId); // To be deleted

            Quota quota = quotaStore.getQuota(quotaId);
            String instanceId = quota.getSubject() + StoreHelper.getRandomId().toString();
            Integer usageId = UsageHelper.insertTestUsageInstanceId(StoreHelper.getRandomId(), quotaId, instanceId);
            this.usageIds.add(usageId);

            subjects.add(quota.getSubject());
            Usage usage = usageStore.findUsageByInstanceIdAndSubjects(instanceId, subjects);

            assertEquals(quota.getId(), usage.getQuotaId());
            assertEquals(usage.getInstanceId(), instanceId);

        } catch (SQLException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Test list quotas by subjects")
    public void testFindUsagesByQuotaSubjects() {

        Customer customer;
        Integer subscriptionId;
        try {
            // Insert a customer
            customer = CustomerHelper.insertTestCustomer(
                    CustomerHelper.createCustomer(StoreHelper.getRandomId()));
            this.customerIds.add(customer.getId()); // To be deleted

            // Insert a subscription
            subscriptionId =
                    SubscriptionHelper.insertTestSubscription(
                            StoreHelper.getRandomId(), customer.getId());
            this.subscriptionIds.add(subscriptionId); // To be deleted

            // Insert two quotas with separate subjects
            Integer quotaOneId = QuotaHelper.insertTestQuotaWithSubject(
                    StoreHelper.getRandomId(), subscriptionId, customer.getSubject());
            this.quotaIds.add(quotaOneId);

            String groupSubject = "CN=some-group,DC=dataone,DC=org";
            Integer quotaTwoId = QuotaHelper.insertTestQuotaWithSubject(
                    StoreHelper.getRandomId(), subscriptionId, groupSubject);
            this.quotaIds.add(quotaTwoId);

            List<String> subjects = new ArrayList<String>();
            subjects.add(customer.getSubject());
            subjects.add(groupSubject);

            Integer usageOneId = UsageHelper.insertTestUsage(StoreHelper.getRandomId(), quotaOneId, StoreHelper.getRandomId().toString(), 1.0,"active");
            this.usageIds.add(usageOneId);

            Integer usageTwoId = UsageHelper.insertTestUsage(StoreHelper.getRandomId(), quotaTwoId, StoreHelper.getRandomId().toString(), 1.0,"active");
            this.usageIds.add(usageTwoId);

            assertEquals(2, usageStore.findUsagesByQuotaSubjects(subjects).size());

        } catch (SQLException e) {
            fail(e);
        } catch (JsonProcessingException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Test find usages by quota type")
    public void testFindUsagesByQuotaType() {

        Customer customer;
        Integer subscriptionId;
        try {
            // Insert a customer
            customer = CustomerHelper.insertTestCustomer(
                    CustomerHelper.createCustomer(StoreHelper.getRandomId()));
            this.customerIds.add(customer.getId()); // To be deleted

            // Insert a subscription
            subscriptionId =
                    SubscriptionHelper.insertTestSubscription(
                            StoreHelper.getRandomId(), customer.getId());
            this.subscriptionIds.add(subscriptionId); // To be deleted

            Integer storageQuotaId = StoreHelper.getRandomId();
            Integer portalQuotaId = StoreHelper.getRandomId();

            QuotaHelper.insertTestStorageAndPortalQuotasWithSubscription(storageQuotaId, portalQuotaId, subscriptionId);
            this.quotaIds.add(storageQuotaId);
            this.quotaIds.add(portalQuotaId);

            Integer usageOneId = UsageHelper.insertTestUsage(StoreHelper.getRandomId(), storageQuotaId, StoreHelper.getRandomId().toString(), 3000.0,"active");
            this.usageIds.add(usageOneId);

            Integer usageTwoId = UsageHelper.insertTestUsage(StoreHelper.getRandomId(), portalQuotaId, StoreHelper.getRandomId().toString(), 3000.0,"active");
            this.usageIds.add(usageTwoId);

            assertEquals(usageStore.findUsagesByQuotaType("storage").size(), 1);
            assertEquals(usageStore.findUsagesByQuotaType("portal").size(), 1);

        } catch (SQLException e) {
            fail(e);
        } catch (JsonProcessingException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Test find usages by quota type and subjects")
    public void testFindUsagesByQuotaTypeAndSubjects() {

        Customer customer;
        Integer subscriptionId;
        try {
            // Insert a customer
            customer = CustomerHelper.insertTestCustomer(
                    CustomerHelper.createCustomer(StoreHelper.getRandomId()));
            this.customerIds.add(customer.getId()); // To be deleted

            // Insert a subscription
            subscriptionId =
                    SubscriptionHelper.insertTestSubscription(
                            StoreHelper.getRandomId(), customer.getId());
            this.subscriptionIds.add(subscriptionId); // To be deleted

            Integer storageQuotaId = StoreHelper.getRandomId();
            Integer portalQuotaId = StoreHelper.getRandomId();

            Map<Integer, Quota> quotas = QuotaHelper.insertTestStorageAndPortalQuotasWithSubscription(storageQuotaId, portalQuotaId, subscriptionId);
            this.quotaIds.add(storageQuotaId);
            this.quotaIds.add(portalQuotaId);

            Integer usageOneId = UsageHelper.insertTestUsage(StoreHelper.getRandomId(), storageQuotaId, StoreHelper.getRandomId().toString(), 3000.0,"active");
            this.usageIds.add(usageOneId);

            Integer usageTwoId = UsageHelper.insertTestUsage(StoreHelper.getRandomId(), portalQuotaId, StoreHelper.getRandomId().toString(), 3000.0,"active");
            this.usageIds.add(usageTwoId);

            List<String> subscribers = new ArrayList<>();
            subscribers.add(quotas.get(portalQuotaId).getSubject());

            String quotaTypePortal = quotas.get(portalQuotaId).getName();
            String quotaTypeStorage =  quotas.get(storageQuotaId).getName();

            List<Usage> usages = usageStore.findUsagesByQuotaTypeAndSubjects(quotaTypePortal, subscribers);
            assertEquals(usages.size(), 1);

            usages = usageStore.findUsagesByQuotaTypeAndSubjects(quotaTypeStorage, subscribers);
            assertEquals(usages.size(), 1);

        } catch (SQLException e) {
            fail(e);
        } catch (JsonProcessingException e) {
            fail(e);
        }
    }

    /**
     * Test inserting a Usage instance
     */
    @Test
    @DisplayName("Test inserting a Usage instance")
    public void testInsertWithUsage() {
        try {
            Customer customer;
            Integer subscriptionId;
            // Insert a customer
            customer = CustomerHelper.insertTestCustomer(
                    CustomerHelper.createCustomer(StoreHelper.getRandomId()));
            this.customerIds.add(customer.getId()); // To be deleted

            // Insert a subscription
            subscriptionId =
                    SubscriptionHelper.insertTestSubscription(
                            StoreHelper.getRandomId(), customer.getId());
            this.subscriptionIds.add(subscriptionId); // To be deleted

            Integer storageQuotaId = StoreHelper.getRandomId();
            Integer portalQuotaId = StoreHelper.getRandomId();

            Map<Integer, Quota> quotas = QuotaHelper.insertTestStorageAndPortalQuotasWithSubscription(storageQuotaId, portalQuotaId, subscriptionId);
            this.quotaIds.add(storageQuotaId);
            this.quotaIds.add(portalQuotaId);

            assertEquals(QuotaHelper.getQuotaCountById(storageQuotaId),1 ,"Portal quota not inserted." );

            Integer usageId = StoreHelper.getRandomId();
            String instanceId = StoreHelper.getRandomId().toString();
            Usage usage = UsageHelper.createTestStorageUsage(usageId, portalQuotaId, instanceId);
            usageStore.insert(usage);
            this.usageIds.add(usageId);

            assertThat(UsageHelper.getUsageCountById(usageId) == 1);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test inserting a duplicate Usage instance
     */
    @Test
    @DisplayName("Test inserting a duplicate Usage instance")
    public void testInsertDuplicateUsages() {
        try {
            Customer customer;
            Integer subscriptionId;
            // Insert a customer
            customer = CustomerHelper.insertTestCustomer(
                    CustomerHelper.createCustomer(StoreHelper.getRandomId()));
            this.customerIds.add(customer.getId()); // To be deleted

            // Insert a subscription
            subscriptionId =
                    SubscriptionHelper.insertTestSubscription(
                            StoreHelper.getRandomId(), customer.getId());
            this.subscriptionIds.add(subscriptionId); // To be deleted

            Integer storageQuotaId = StoreHelper.getRandomId();
            Integer portalQuotaId = StoreHelper.getRandomId();

            Map<Integer, Quota> quotas = QuotaHelper.insertTestStorageAndPortalQuotasWithSubscription(storageQuotaId, portalQuotaId, subscriptionId);
            this.quotaIds.add(storageQuotaId);
            this.quotaIds.add(portalQuotaId);

            assertEquals(QuotaHelper.getQuotaCountById(storageQuotaId),1 ,"Portal quota not inserted." );

            Integer usageId = StoreHelper.getRandomId();
            String instanceId = StoreHelper.getRandomId().toString();
            Usage usage = UsageHelper.createTestStorageUsage(usageId, portalQuotaId, instanceId);
            usageStore.insert(usage);
            this.usageIds.add(usageId);

            assertThat(UsageHelper.getUsageCountById(usageId) == 1);

            // Now attempt to insert a usage for the existing quotaId + instanceId
            Integer newUsageId = StoreHelper.getRandomId();
            Usage newUsage = UsageHelper.createTestStorageUsage(newUsageId, storageQuotaId, instanceId);
            this.usageIds.add(newUsageId);

            Exception exception = assertThrows(org.jdbi.v3.core.statement.UnableToExecuteStatementException.class, () -> {
                usageStore.insert(usage);
            });

            String expectedMessage = "duplicate key value violates unique constraint";
            String actualMessage = exception.getMessage();

            assertTrue(actualMessage.contains(expectedMessage));

        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test updating a usage
     */
    @Test
    @DisplayName("Test updating a quota")
    public void testUpdate() {
        try {

            // Add a test customer
            Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
            this.customerIds.add(customerId); // Clean up

            // Mint a test productId
            Integer productId = StoreHelper.getRandomId();
            this.productIds.add(productId);

            Integer subscriptionId = SubscriptionHelper.insertTestSubscription(
                    SubscriptionHelper.createSubscription(
                            StoreHelper.getRandomId(), customerId, productId
                    )
            ).getId();
            this.subscriptionIds.add(subscriptionId); // Clean up
            Integer quotaId = QuotaHelper.insertTestQuotaWithSubscription(
                    StoreHelper.getRandomId(), subscriptionId
            );
            this.quotaIds.add(quotaId); // Clean up

            String instanceId = subscriptionId.toString() + quotaId.toString();
            Integer usageId = UsageHelper.insertTestUsageInstanceId(StoreHelper.getRandomId(), quotaId, instanceId);
            this.usageIds.add(usageId); // Clean up

            assertThat(UsageHelper.getUsageById(usageId).getInstanceId().equals(instanceId));
        } catch (SQLException e) {
            fail();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test deleting a usage
     */
    @Test
    @DisplayName("Test deleting a usage")
    public void testDelete() {
        Integer customerId;
        Integer productId;
        Integer quotaId = null;
        Integer usageId = null;
        try {
            // Add a test customer
            customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
            this.customerIds.add(customerId); // Clean up

            // Mint a productId
            productId = StoreHelper.getRandomId();

            // Add a test subscription
            Integer subscriptionId = SubscriptionHelper.insertTestSubscription(
                    SubscriptionHelper.createSubscription(
                            StoreHelper.getRandomId(), customerId, productId)
            ).getId();
            this.subscriptionIds.add(subscriptionId); // Clean up

            // Add a test quota
            quotaId = QuotaHelper.insertTestQuotaWithSubscription(
                    StoreHelper.getRandomId(), subscriptionId
            );
            this.quotaIds.add(quotaId);
            Quota quota = QuotaHelper.getQuotaById(quotaId);

            String instanceId = quota.getSubject() + quotaId.toString();
            // Add a usage for the quota
            usageId = StoreHelper.getRandomId();
            UsageHelper.insertTestUsageInstanceId(usageId, quotaId, instanceId);

            assertThat(UsageHelper.getUsageCountById(usageId) == 1);
            // Delete the usage
            usageStore.delete(usageId);
            assertThat(UsageHelper.getUsageCountById(quotaId) == 0);
        } catch (SQLException e) {
            this.quotaIds.add(quotaId); // Clean up on fail
            fail();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
