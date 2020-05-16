/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2020. All rights reserved.
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

import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Usage;
import org.dataone.bookkeeper.helpers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UsageStoreTest extends BaseTestCase {

    // The UsageStore to test
    private UsageStore usageStore;

    // A list of usage ids used in testing
    private List<Integer> usageIds = new ArrayList<Integer>();

    // A list of quota ids used in testing
    private List<Integer> quotaIds = new ArrayList<Integer>();

    // A list of customer ids used in testing
    private List<Integer> customerIds = new ArrayList<Integer>();

    // A list of product ids used in testing
    private List<Integer> productIds = new ArrayList<Integer>();

    // A list of subscription ids used in testing
    private List<Integer> subscriptionIds = new ArrayList<Integer>();

    /**
     * Set up the Store for testing
     */
    @BeforeEach
    public void init() {
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
     * Test getting usage by usage ID
     */
    @Test
    @DisplayName("Test getting usage by usage ID")
    public void testGetQuotasByUsageId() {

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

            Integer usageId = StoreHelper.getRandomId();
            UsageHelper.insertTestUsageWithQuota(usageId, quotaId);
            this.usageIds.add(usageId); // To be deleted

            Usage usage = usageStore.findUsageById(usageId);
            assertEquals(usage.getQuotaId(), quotaId);
            assertEquals(usage.getId(), usageId);
        } catch (SQLException e) {
            fail();
        }
    }
}
