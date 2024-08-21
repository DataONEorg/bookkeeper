/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2023
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.fail;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.Payment;
import org.dataone.bookkeeper.helpers.CustomerHelper;
import org.dataone.bookkeeper.helpers.OrderHelper;
import org.dataone.bookkeeper.helpers.StoreHelper;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

/**
 * Test class for PaymentStore
 */
public class PaymentStoreTest extends BaseTestCase {

    // The PaymentStore to test
    private PaymentStore paymentStore;

    // A list of paymentIds used in testing
    private List<Integer> paymentIds = new ArrayList<Integer>();

    // A list of customer ids used in testing
    private List<Integer> customerIds = new ArrayList<Integer>();

    // A list of order ids used in testing
    private List<Integer> orderIds = new ArrayList<Integer>();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * Set up the Store for testing with a customer and an order
     */
    @BeforeEach
    public void init() {
        paymentStore = dbi.onDemand(PaymentStore.class);
        try {
        Customer customer;
            // Insert a customer to be used in later tests
            customer = CustomerHelper.insertTestCustomer(
                        CustomerHelper.createCustomer(StoreHelper.getRandomId()));
            this.customerIds.add(customer.getId());

            // Insert an order
            Integer orderId;
            orderId = OrderHelper.insertTestOrder(StoreHelper.getRandomId(), customerIds.get(0));
            this.orderIds.add(orderId);
            
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test getting the full Payments list
     */
    @Test
    @DisplayName("Test listing all of the the payments")
    public void testListPayments() {
        try {
            // Insert three test payments to be listed and checked
            Integer orderId = orderIds.get(0);
            insertTestPayment(orderId);
            insertTestPayment(orderId);
            insertTestPayment(orderId);

            List<Payment> payments = paymentStore.listPayments();
            assertTrue(payments.size() == 3);
            assertTrue(payments.get(0).getOrderId().intValue() == orderId.intValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test getting a single quota by ID
     */
    // @Test
    // @DisplayName("Test get usage")
    // public void testGetQuota() {
    //     try {
    //         // Insert a customer
    //         Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
    //         this.customerIds.add(customerId); // To be deleted

    //         // Insert an order
    //         Integer orderId =
    //                 OrderHelper.insertTestOrder(StoreHelper.getRandomId(), customerId);
    //         this.orderIds.add(orderId); // To be deleted
    //         Integer quotaId = QuotaHelper.insertTestQuotaWithOrder(StoreHelper.getRandomId(), orderId);
    //         this.quotaIds.add(quotaId); // To be deleted

    //         String instanceId = StoreHelper.getRandomId().toString();
    //         Quota quota = quotaStore.getQuota(quotaId);
    //         Integer usageId = UsageHelper.insertTestUsageInstanceId(StoreHelper.getRandomId(), quotaId, instanceId);
    //         this.usageIds.add(usageId);

    //         Usage usage = usageStore.getUsage(usageId);
    //         assertEquals(usageId, usage.getId());
    //     } catch (SQLException e) {
    //         fail();
    //     }
    // }

    /**
     * Test getting usages by instance ID
     */
    // @Test
    // @DisplayName("Test getting usages by instance ID")
    // public void testFindUsagesByInstanceId() {

    //     try {
    //         // Insert a customer
    //         Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
    //         this.customerIds.add(customerId); // To be deleted

    //         // Insert an order
    //         Integer orderId =
    //                 OrderHelper.insertTestOrder(StoreHelper.getRandomId(), customerId);
    //         this.orderIds.add(orderId); // To be deleted
    //         Integer quotaId = QuotaHelper.insertTestQuotaWithOrder(StoreHelper.getRandomId(), orderId);
    //         this.quotaIds.add(quotaId); // To be deleted

    //         String instanceId = StoreHelper.getRandomId().toString();
    //         Quota quota = quotaStore.getQuota(quotaId);
    //         Integer usageId = UsageHelper.insertTestUsageInstanceId(StoreHelper.getRandomId(), quotaId, instanceId);
    //         this.usageIds.add(usageId);

    //         Usage usage = usageStore.findUsageByInstanceIdAndQuotaId(instanceId, quotaId);
    //         assertEquals(usage.getInstanceId(), instanceId);

    //         assertEquals(usage.getQuotaId(), quota.getId());

    //     } catch (SQLException e) {
    //         fail();
    //     }
    // }

    /**
     * Test inserting a Payment instance
     */
    @Test
    @DisplayName("Test inserting a Payment instance")
    public void testInsertWithPayment() {
        try {
            Integer paymentId = insertTestPayment(orderIds.get(0));
            assertTrue(paymentId != null);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Insert a payment into the paymentStore for the given orderId
     * @param orderId the order for which the payment applies
     * @return Integer identifier of the generated payment
     */
    private Integer insertTestPayment(Integer orderId) {
        String transactionId = StoreHelper.getRandomId().toString();
        Payment payment = createTestStoragePayment(transactionId, orderId);
        Integer paymentId = paymentStore.insert(payment);
        this.paymentIds.add(paymentId);
        return paymentId;
    }

    /**
     * Create a test payment
     * @param transactionId the transasctionId associated with the payment
     * @param orderId the orderId associated with the payment
     * @return the Payment instance that was created
     */
    private Payment createTestStoragePayment(String transactionId, Integer orderId) {
        Payment payment = new Payment();
        //payment.setPaymentId(paymentId); // Probably best to get rid of paymentId and just use transactionId
        payment.setTransactionId(transactionId);
        payment.setOrderId(orderId);
        // TODO: Set the other fields with test data
        return payment;
    }

    /**
     * Test inserting a duplicate Usage instance
     */
    // @Test
    // @DisplayName("Test inserting a duplicate Usage instance")
    // public void testInsertDuplicateUsages() {
    //     try {
    //         Customer customer;
    //         Integer orderId;
    //         // Insert a customer
    //         customer = CustomerHelper.insertTestCustomer(
    //                 CustomerHelper.createCustomer(StoreHelper.getRandomId()));
    //         this.customerIds.add(customer.getId()); // To be deleted

    //         // Insert an order
    //         orderId =
    //                 OrderHelper.insertTestOrder(
    //                         StoreHelper.getRandomId(), customer.getId());
    //         this.orderIds.add(orderId); // To be deleted

    //         Integer storageQuotaId = StoreHelper.getRandomId();
    //         Integer portalQuotaId = StoreHelper.getRandomId();

    //         Map<Integer, Quota> quotas =
    //             QuotaHelper.insertTestStorageAndPortalQuotasWithOrder(storageQuotaId, portalQuotaId, orderId);
    //         this.quotaIds.add(storageQuotaId);
    //         this.quotaIds.add(portalQuotaId);

    //         assertEquals(QuotaHelper.getQuotaCountById(storageQuotaId),1 ,"Portal quota not inserted." );

    //         Integer usageId = StoreHelper.getRandomId();
    //         String instanceId = StoreHelper.getRandomId().toString();
    //         Usage usage = UsageHelper.createTestStorageUsage(usageId, portalQuotaId, instanceId);
    //         usageStore.insert(usage);
    //         this.usageIds.add(usageId);

    //         assertThat(UsageHelper.getUsageCountById(usageId) == 1);

    //         // Now attempt to insert a usage for the existing quotaId + instanceId
    //         Integer newUsageId = StoreHelper.getRandomId();
    //         Usage newUsage = UsageHelper.createTestStorageUsage(newUsageId, portalQuotaId, instanceId);

    //         Exception exception = assertThrows(org.jdbi.v3.core.statement.UnableToExecuteStatementException.class, () -> {
    //             usageStore.insert(usage);
    //         });

    //         String expectedMessage = "duplicate key value violates unique constraint";
    //         String actualMessage = exception.getMessage();

    //         assertTrue(actualMessage.contains(expectedMessage));

    //     } catch (Exception e) {
    //         fail();
    //     }
    // }

    /**
     * Test updating a usage
     */
    // @Test
    // @DisplayName("Test updating a quota")
    // public void testUpdate() {
    //     try {

    //         // Add a test customer
    //         Integer customerId = CustomerHelper.insertTestCustomer(StoreHelper.getRandomId());
    //         this.customerIds.add(customerId); // Clean up

    //         // Mint a test productId
    //         Integer productId = StoreHelper.getRandomId();
    //         this.productIds.add(productId);

    //         // Mint a test chargeId
    //         Integer chargeId = StoreHelper.getRandomId();

    //         // Mint a test invoiceId
    //         Integer invoiceId = StoreHelper.getRandomId();

    //         Integer orderId = OrderHelper.insertTestOrder(
    //                 OrderHelper.createOrder(
    //                         StoreHelper.getRandomId(), customerId, chargeId, invoiceId, productId
    //                 )
    //         ).getId();
    //         this.orderIds.add(orderId); // Clean up
    //         Integer quotaId = QuotaHelper.insertTestQuotaWithOrder(
    //                 StoreHelper.getRandomId(), orderId
    //         );
    //         this.quotaIds.add(quotaId); // Clean up

    //         String instanceId = orderId.toString() + quotaId.toString();
    //         Integer usageId = UsageHelper.insertTestUsageInstanceId(StoreHelper.getRandomId(), quotaId, instanceId);
    //         this.usageIds.add(usageId); // Clean up

    //         assertThat(UsageHelper.getUsageById(usageId).getInstanceId().equals(instanceId));
    //     } catch (SQLException e) {
    //         fail();
    //     }
    // }

    /**
     * Test deleting a payment
     */
    @Test
    @DisplayName("Test deleting payments")
    public void testDelete() {
        try {
            Integer paymentId = insertTestPayment(orderIds.get(0));
            assertTrue(paymentId != null);
            int paymentCount = paymentStore.listPayments().size();
            boolean deleted = paymentStore.delete(paymentId.toString());
            assertTrue(deleted);
            int newCount = paymentStore.listPayments().size();
            assertTrue("Payment count was not decreased after deletion.", paymentCount - newCount == 1);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // And try deleting a non-existent payment identifier
        boolean deleted = paymentStore.delete("I-am-not-a-pipe");
        assertFalse("Deletion for non-existent id should have failed but did not.", deleted);
    }
}
