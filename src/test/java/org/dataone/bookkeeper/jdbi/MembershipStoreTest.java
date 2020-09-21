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
import org.dataone.bookkeeper.api.Membership;
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
public class MembershipStoreTest extends BaseTestCase {

    /* The MembershipStore to test */
    private MembershipStore membershipStore;

    // A list of membership ids used in testing
    private List<Integer> membershipIds = new ArrayList<Integer>();

    // A list of customer ids used in testing
    private List<Integer> customerIds = new ArrayList<Integer>();

    // A list of product ids used in testing
    private List<Integer> productIds = new ArrayList<Integer>();

    /**
     * Set up the Store for testing
     */
    @BeforeEach
    public void init() {
        membershipStore = dbi.onDemand(MembershipStore.class);
    }

    /**
     * Tear down resources
     */
    @AfterEach
    public void tearDown() {
        // Remove test membership entries
        for (Integer membershipId : this.membershipIds) {
            try {
                MembershipHelper.removeTestMembership(membershipId);
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
     * Test listing memberships with empty quotas
     */
    @Test
    @DisplayName("Test listing the memberships")
    public void testListMemberships() {
        Integer membershipId;
        try {
            // Insert a new customer
            Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
            this.customerIds.add(customerId);
            // Insert a new membership
            membershipId = MembershipHelper.insertTestMembership(StoreHelper.getRandomId(), customerId);
            this.membershipIds.add(membershipId);

        } catch (SQLException e) {
            fail(e);
        }
        assertTrue(membershipStore.listMemberships().size() >= 1);
    }

    /**
     * Test listing a membership with storage and portal quotas
     */
    @Test
    @DisplayName("Test listing memberships with associated storage and portal quotas")
    public void testListMembershipsWithStorageAndPortalQuotas() {

        try {
            // Insert a new customer
            Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
            this.customerIds.add(customerId);

            // Mint a new product id
            Integer productId = StoreHelper.getRandomId();
            this.productIds.add(productId);

            // Insert a membership
            Membership membership = MembershipHelper.insertTestMembership(
                MembershipHelper.createMembership(StoreHelper.getRandomId(), customerId, productId)
            );
            this.membershipIds.add(membership.getId());

            // Insert a storage and portal quotas for the membership
            final Map<Integer, Quota> quotas =
                QuotaHelper.insertTestStorageAndPortalQuotasWithMembership(
                    StoreHelper.getRandomId(), StoreHelper.getRandomId(), membership.getId());
            List<Membership> memberships = membershipStore.listMemberships();

            // Ensure we get a membership returned
            assertTrue(memberships.size() >= 1);
                memberships.forEach(membership2 -> {

                    // Ensure the quota count matches
                    assertTrue(membership2.getQuotas().size() == quotas.size());

                    // Ensure the quota content matches
                    membership2.getQuotas()
                        .forEach(quota -> {
                            Quota expectedQuota = quotas.get(quota.getId());
                            assertTrue(quota.getId().equals(expectedQuota.getId()));
                            assertTrue(quota.getObject().equals(expectedQuota.getObject()));
                            assertTrue(quota.getQuotaType().equals(expectedQuota.getQuotaType()));
                            assertTrue(quota.getSoftLimit().equals(expectedQuota.getSoftLimit()));
                            assertTrue(quota.getHardLimit().equals(expectedQuota.getHardLimit()));
                            assertTrue(quota.getUnit().equals(expectedQuota.getUnit()));
                            assertTrue(quota.getMembershipId().equals(expectedQuota.getMembershipId()));
                        });
                });
        } catch (SQLException e) {
            fail(e);
        } catch (JsonProcessingException e) {
            fail(e);
        }
    }

    /**
     * Test getting a membership by id
     * @throws SQLException
     */
    @Test
    @DisplayName("Test getting a membership by id")
    public void testGetMembershipById() throws SQLException {
        // Insert a new customer
        Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
        this.customerIds.add(customerId);

        // Insert a membership
        final Integer membershipId = MembershipHelper.insertTestMembership(
            StoreHelper.getRandomId(), customerId
        );
        this.membershipIds.add(membershipId);

        // Get the membership
        Membership membership = membershipStore.getMembership(membershipId);
        assertTrue(membership.getId().equals(membershipId));
    }

    /**
     * Test getting a membership by subject identifier
     * @throws SQLException
     */
    @Test
    @DisplayName("Test finding a membership by subject identifier")
    public void testFindMembershipBySubject() throws SQLException, JsonProcessingException {
        // Insert a new customer
        Customer customer = CustomerHelper.insertTestCustomer(
            CustomerHelper.createCustomer(StoreHelper.getRandomId())
        );
        this.customerIds.add(customer.getId());

        // Mint a new product id
        Integer productId = StoreHelper.getRandomId();
        this.productIds.add(productId);

        // Insert a membership
        final Membership expectedMembership = MembershipHelper.insertTestMembership(
            MembershipHelper.createMembership(
                StoreHelper.getRandomId(), customer.getId(), productId)
        );
        this.membershipIds.add(expectedMembership.getId());

        // Get the subscription
        Membership membership = membershipStore.findMembershipByOwner(customer.getSubject());
        assertTrue(membership.getId().equals(expectedMembership.getId()));
    }

    /**
     * Test inserting a membership
     */
    @Test
    @DisplayName("Test inserting a membership")
    public void testInsert() {
        Customer customer;
        try {
            // Insert a new customer
            customer = CustomerHelper.insertTestCustomer(
                CustomerHelper.createCustomer(StoreHelper.getRandomId())
            );
            this.customerIds.add(customer.getId());

            // Mint a new product id
            Integer productId = StoreHelper.getRandomId();
            this.productIds.add(productId);

            // Create a Membership to insert
            Membership expectedMembership = MembershipHelper.createMembership(
                StoreHelper.getRandomId(), customer.getId(), productId
            );
            this.membershipIds.add(expectedMembership.getId());

            // Insert the membership
            Integer id = membershipStore.insert(expectedMembership);
            expectedMembership.setId(id);

            // Then get the membership to ensure it was inserted
            Membership membership = MembershipHelper.getMembershipById(expectedMembership.getId());

            assertTrue(membership.getId().equals(expectedMembership.getId()));
        } catch (SQLException e) {
            fail(e);
        } catch (JsonProcessingException jpe) {
            fail(jpe);
        }
    }

    @Test
    @DisplayName("Test updating a membership")
    public void testUpdate() throws SQLException, JsonProcessingException {
        // Insert a new customer
        Customer customer = CustomerHelper.insertTestCustomer(
            CustomerHelper.createCustomer(StoreHelper.getRandomId())
        );
        this.customerIds.add(customer.getId());

        // Mint a new product id
        Integer productId = StoreHelper.getRandomId();
        this.productIds.add(productId);

        // Insert a new test membership
        Membership expectedMembership = MembershipHelper.insertTestMembership(
            MembershipHelper.createMembership(
                StoreHelper.getRandomId(), customer.getId(), productId));
        this.membershipIds.add(expectedMembership.getId());

        // Now update the membership locally
        expectedMembership.setCanceledAt(new Integer(1570912264));
        expectedMembership.setStatus("canceled");


        // Push the changes to the database
        membershipStore.update(expectedMembership);

        // Get the updated membership from the database
        Membership updatedMembership = MembershipHelper.getMembershipById(expectedMembership.getId());

        assertTrue(updatedMembership.getCanceledAt().intValue() ==
            expectedMembership.getCanceledAt().intValue());
        assertTrue(updatedMembership.getStatus().equals(expectedMembership.getStatus()));

    }

    @Test
    @DisplayName("Test deleting a membership")
    public void testDelete() throws SQLException, JsonProcessingException {
        // Insert a new customer
        Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
        this.customerIds.add(customerId);

        // Mint a new product id
        Integer productId = StoreHelper.getRandomId();
        this.productIds.add(productId);

        // Insert a new membership
        Membership membership = MembershipHelper.insertTestMembership(
            MembershipHelper.createMembership(
                StoreHelper.getRandomId(), customerId, productId));
        this.membershipIds.add(membership.getId());

        // Delete it
        membershipStore.delete(membership.getId());

        // It's gone, right?
        assertThat(MembershipHelper.getMembershipCountById(membership.getId()) == 0);
    }
}
