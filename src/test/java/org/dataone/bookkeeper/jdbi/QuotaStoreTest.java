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
import org.dataone.bookkeeper.helpers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test the Quota data access object
 */
public class QuotaStoreTest extends BaseTestCase {

    // The QuotaStore to test
    private QuotaStore quotaStore;

    // A list of quota ids used in testing
    private List<Integer> quotaIds = new ArrayList<Integer>();

    // A list of customer ids used in testing
    private List<Integer> customerIds = new ArrayList<Integer>();

    // A list of product ids used in testing
    private List<Integer> productIds = new ArrayList<Integer>();

    // A list of membership ids used in testing
    private List<Integer> membershipIds = new ArrayList<Integer>();

    /**
     * Set up the Store for testing
     */
    @BeforeEach
    public void init() {
        quotaStore = dbi.onDemand(QuotaStore.class);
    }

    /**
     * Tear down resources
     */
    @AfterEach
    public void tearDown() {
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

        // Remove test membership entries
        for (Integer membershipId : this.membershipIds) {
            try {
                MembershipHelper.removeTestMembership(membershipId);
            } catch (SQLException e) {
                fail();
            }
        }

    }

    /**
     * Test getting the full Quota list
     */
    @Test
    @DisplayName("Test listing the quotas")
    public void testListQuotas() {
        assertThat(quotaStore.listQuotas().size() >= 3);
    }

    /**
     * Test getting a single quota by ID
     */
    @Test
    @DisplayName("Test get quota")
    public void testGetQuota() {
        Quota quota = quotaStore.getQuota(3);
        int identifier = (int) quota.getId();
        assertEquals(3, identifier);
    }

    /**
     * Test getting quotas by membership ID
     */
    @Test
    @DisplayName("Test getting quotas by membership ID")
    public void testGetQuotasByMemberId() {

        try {
            // Insert a customer
            Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
            this.customerIds.add(customerId); // To be deleted

            // Insert a membership
            Integer membershipId =
                MembershipHelper.insertTestMembership(StoreHelper.getRandomId(), customerId);
            this.membershipIds.add(membershipId); // To be deleted
            Integer quotaId = QuotaHelper.insertTestQuotaWithMembership(StoreHelper.getRandomId(), membershipId);
            this.quotaIds.add(quotaId); // To be deleted
            assertEquals(1, quotaStore.findQuotasByMembershipId(membershipId).size());
            assertThat(quotaStore.findQuotasByMembershipId(0).isEmpty());

        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Test getting quotas by owner
     */
    @Test
    @DisplayName("Test getting quotas by subscriber")
    public void testGetQuotasByOwner() {
        try {
            // Insert a customer
            Customer customer = CustomerHelper.insertTestCustomer(
                CustomerHelper.createCustomer(StoreHelper.getRandomId()));
            this.customerIds.add(customer.getId()); // To be deleted

            // Insert a membership
            Integer membershipId =
                MembershipHelper.insertTestMembership(
                    StoreHelper.getRandomId(), customer.getId());
            this.membershipIds.add(membershipId); // To be deleted


            // Insert another customer
            Customer customerTwo = CustomerHelper.insertTestCustomer(
                    CustomerHelper.createCustomer(StoreHelper.getRandomId()));
            this.customerIds.add(customerTwo.getId()); // To be deleted

            // Insert a membership
            Integer membershipIdTwo =
                    MembershipHelper.insertTestMembership(
                            StoreHelper.getRandomId(), customerTwo.getId());
            this.membershipIds.add(membershipIdTwo); // To be deleted

            // Insert two quotas with separate owners, maintaining unique membershipId + quotaType
            Integer quotaOneId = QuotaHelper.insertTestQuotaWithOwner(
                StoreHelper.getRandomId(), membershipId, customer.getSubject());
            this.quotaIds.add(quotaOneId);
            Integer quotaTwoId = QuotaHelper.insertTestQuotaWithOwner(
                StoreHelper.getRandomId(), membershipIdTwo, customer.getSubject());
            this.quotaIds.add(quotaTwoId);
            assertEquals(2, quotaStore.findQuotasByOwner(customer.getSubject()).size());
        } catch (SQLException e) {
            fail(e);
        } catch (JsonProcessingException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Test list quotas by owners")
    public void testListQuotasByOwners() {

        Customer customer;
        Integer membershipId;
        Customer customerTwo;
        Integer membershipIdTwo;
        try {
            // Insert a customer
            customer = CustomerHelper.insertTestCustomer(
                CustomerHelper.createCustomer(StoreHelper.getRandomId()));
            this.customerIds.add(customer.getId()); // To be deleted

            // Insert a membership
            membershipId =
                MembershipHelper.insertTestMembership(
                    StoreHelper.getRandomId(), customer.getId());
            this.membershipIds.add(membershipId); // To be deleted

            // Insert a customer
            customerTwo = CustomerHelper.insertTestCustomer(
                    CustomerHelper.createCustomer(StoreHelper.getRandomId()));
            this.customerIds.add(customerTwo.getId()); // To be deleted

            // Insert a membership
            membershipIdTwo =
                    MembershipHelper.insertTestMembership(
                            StoreHelper.getRandomId(), customerTwo.getId());
            this.membershipIds.add(membershipIdTwo); // To be deleted

            // Insert two quotas with separate owners, maintaining unique membershipId + quotaType
            Integer quotaOneId = QuotaHelper.insertTestQuotaWithOwner(
                StoreHelper.getRandomId(), membershipId, customer.getSubject());
            this.quotaIds.add(quotaOneId);

            String groupSubject = "CN=some-group,DC=dataone,DC=org";
            Integer quotaTwoId = QuotaHelper.insertTestQuotaWithOwner(
                StoreHelper.getRandomId(), membershipIdTwo, groupSubject);
            this.quotaIds.add(quotaTwoId);

            List<String> owners = new ArrayList<String>();
            owners.add(customer.getSubject());
            owners.add(groupSubject);
            assertEquals(2, quotaStore.findQuotasByOwners(owners).size());

        } catch (SQLException e) {
            fail(e);
        } catch (JsonProcessingException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Test list quotas by type and owners")
    public void testListQuotasByNameAndOwners() {

        Customer customer;
        Integer membershipId;
        Customer customerTwo;
        Integer membershipIdTwo;
        try {
            // Insert a customer
            customer = CustomerHelper.insertTestCustomer(
                    CustomerHelper.createCustomer(StoreHelper.getRandomId()));
            this.customerIds.add(customer.getId()); // To be deleted

            // Insert a membership
            membershipId =
                    MembershipHelper.insertTestMembership(
                            StoreHelper.getRandomId(), customer.getId());
            this.membershipIds.add(membershipId); // To be deleted


            // Insert a customer
            customerTwo = CustomerHelper.insertTestCustomer(
                    CustomerHelper.createCustomer(StoreHelper.getRandomId()));
            this.customerIds.add(customerTwo.getId()); // To be deleted

            // Insert a membership
            membershipIdTwo =
                    MembershipHelper.insertTestMembership(
                            StoreHelper.getRandomId(), customerTwo.getId());
            this.membershipIds.add(membershipIdTwo); // To be deleted

            // Insert two quotas with separate owners, maintaining unique subscriptionId + quotaType
            Integer quotaOneId = QuotaHelper.insertTestQuotaWithOwner(
                    StoreHelper.getRandomId(), membershipId, customer.getSubject());
            this.quotaIds.add(quotaOneId);

            String groupSubject = "CN=some-group,DC=dataone,DC=org";
            Integer quotaTwoId = QuotaHelper.insertTestQuotaWithOwner(
                    StoreHelper.getRandomId(), membershipIdTwo, groupSubject);
            this.quotaIds.add(quotaTwoId);

            List<String> owners = new ArrayList<String>();
            owners.add(customer.getSubject());
            owners.add(groupSubject);
            assertEquals(2, quotaStore.findQuotasByNameAndOwners("portal", owners).size());

        } catch (SQLException e) {
            fail(e);
        } catch (JsonProcessingException e) {
            fail(e);
        }
    }

    /**
     * Test inserting a Quota instance
     */
    @Test
    @DisplayName("Test inserting a Quota instance")
    public void testInsertWithQuota() {
        try {
            Integer quotaId = StoreHelper.getRandomId();
            Integer customerId = null;
            Quota quota = QuotaHelper.createTestStorageQuota(quotaId, customerId);
            this.quotaIds.add(quotaId);
            quotaStore.insert(quota);
            assertThat(QuotaHelper.getQuotaCountById(quotaId) == 1);
        } catch (Exception e) {
            fail();
        }

    }

    /**
     * Test updating a quota
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

            Integer membershipId = MembershipHelper.insertTestMembership(
                MembershipHelper.createMembership(
                    StoreHelper.getRandomId(), customerId, productId
                )
            ).getId();
            this.membershipIds.add(membershipId); // Clean up
            Integer quotaId = QuotaHelper.insertTestQuotaWithMembership(
                StoreHelper.getRandomId(), membershipId
            );
            this.quotaIds.add(quotaId); // Clean up

            Quota quota = new Quota();
            quota.setId(quotaId);
            quota.setObject("quota");
            quota.setQuotaType("portal");
            quota.setSoftLimit(10.0);
            quota.setHardLimit(15.0);
            quota.setUnit("portal");
            quota.setMembershipId(membershipId);
            quotaStore.update(quota);
            assertEquals(QuotaHelper.getQuotaById(quotaId).getQuotaType(), quota.getQuotaType());
            assertThat(QuotaHelper.getQuotaById(quotaId).getSoftLimit() == 10.0);
            assertThat(QuotaHelper.getQuotaById(quotaId).getHardLimit() == 15.0);
        } catch (SQLException e) {
            fail();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test deleting a quota
     */
    @Test
    @DisplayName("Test deleting a quota")
    public void testDelete() {
        Integer customerId;
        Integer productId;
        Integer quotaId = null;
        try {
            // Add a test customer
            customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
            this.customerIds.add(customerId); // Clean up

            // Mint a productId
            productId = StoreHelper.getRandomId();

            // Add a test membership
            Integer membershipId = MembershipHelper.insertTestMembership(
                MembershipHelper.createMembership(
                    StoreHelper.getRandomId(), customerId, productId)
            ).getId();
            this.membershipIds.add(membershipId); // Clean up

            // Add a test quota
            quotaId = QuotaHelper.insertTestQuotaWithMembership(
                StoreHelper.getRandomId(), membershipId
            );
            this.quotaIds.add(quotaId);

            // Delete the quota
            quotaStore.delete(quotaId);
            assertThat(QuotaHelper.getQuotaCountById(quotaId) == 0);
        } catch (SQLException e) {
            this.quotaIds.add(quotaId); // Clean up on fail
            fail();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
