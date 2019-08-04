package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Order;
import org.dataone.bookkeeper.helpers.CustomerHelper;
import org.dataone.bookkeeper.helpers.DAOHelper;
import org.dataone.bookkeeper.helpers.OrderHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Test the order data access object
 */
public class OrderDAOTest extends BaseTestCase {

    // The OrderDAO to test
    private OrderDAO orderDAO;

    // A list of order ids used in testing
    private List<Integer> orderIds = new ArrayList<Integer>();

    // A list of customer ids used in testing
    private List<Integer> customerIds = new ArrayList<Integer>();

    /**
     * Set up the DAO for testing
     */
    @BeforeEach
    public void init() {
        orderDAO = dbi.onDemand(OrderDAO.class);
    }

    /**
     * Tear down resources
     */
    @AfterEach
    public void tearDown() {
        // Remove test order entries
        for (Integer orderId : this.orderIds) {
            try {
                OrderHelper.removeTestOrder(orderId);
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
    }

    /**
     * Test listing the orders
     */
    @Test
    @DisplayName("Test list the orders")
    public void testListOrders() {
        try {
            // Insert a new customer
            Integer customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
            this.customerIds.add(customerId);

            // Insert a new order for the customer
            Integer orderId = OrderHelper.insertTestOrder(DAOHelper.getRandomId(), customerId);

            // Test listing the orders
            assertTrue(orderDAO.listOrders().size() >= 1);
        } catch (SQLException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Test getting an order by id")
    public void testGetOrder() {
        try {
            // Insert a new customer
            Integer customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
            this.customerIds.add(customerId);

            // Create new order for the customer and insert it
            Order expected = OrderHelper.insertTestOrder(
                OrderHelper.createTestOrder(DAOHelper.getRandomId(),
                customerId, DAOHelper.getRandomId(), DAOHelper.getRandomId()));
            Order order = orderDAO.getOrder(expected.getId());

            assertTrue(order.equals(expected));
        } catch (SQLException sqle) {
            fail(sqle);
        }
    }

    /**
     * Test getting orders by customer id
     */
    @Test
    @DisplayName("Test getting orders by customer id")
    public void testFindOrdersByCustomerId() {
        try {
            // Insert a new customer
            Integer customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
            this.customerIds.add(customerId);

            // Create new order for the customer and insert it
            Order expected = OrderHelper.insertTestOrder(
                OrderHelper.createTestOrder(DAOHelper.getRandomId(),
                    customerId, DAOHelper.getRandomId(), DAOHelper.getRandomId()));
            this.orderIds.add(expected.getId());

            // Get orders for the given customer id
            List<Order> orders = orderDAO.findOrdersByCustomerId(customerId);
            assertTrue(orders.size() == 1);
            assertTrue(orders.get(0).getId().equals(expected.getId()));
        } catch (SQLException e) {
            fail(e);
        }
    }

    /**
     * Test inserting an order
     */
    @Test
    @DisplayName("Test inserting an order")
    public void testInsert() {
        Integer customerId = null;
        try {
            // Insert a new customer
            customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());

            // Create a new order for the customer
            Order expected = OrderHelper.createTestOrder(
                DAOHelper.getRandomId(), customerId, DAOHelper.getRandomId(), DAOHelper.getRandomId());

            // Insert the order
            orderDAO.insert(expected);

            // Fetch the order by id
            Integer count = OrderHelper.getTestOrderCountById(expected.getId());

            assertTrue(count == 1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.customerIds.add(customerId);
    }

    @Test
    @DisplayName("Test updating an order")
    public void testUpdate() {
        Integer customerId = null;
        try {
            // Insert a new customer
            customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
            this.customerIds.add(customerId);

            // Insert a new order for the customer
            Order expected =
                OrderHelper.insertTestOrder(
                    OrderHelper.createTestOrder(
                        DAOHelper.getRandomId(), customerId,
                        DAOHelper.getRandomId(), DAOHelper.getRandomId()
                    )
                );
            this.orderIds.add(expected.getId());

            // Update the order locally
            expected.setUpdated(
                new Integer((int) Instant.now().getEpochSecond())
            );
            expected.setStatus("canceled");
            expected.setEmail("you@me.com");
            expected.setCurrency("JPY");
            expected.setCreated(
                new Integer((int) Instant.now().getEpochSecond())
            );
            expected.setAmount(new Integer(60000));

            orderDAO.update(expected);

            Order updated = OrderHelper.getTestOrderById(expected.getId());

            assertTrue(updated.getUpdated().equals(expected.getUpdated()));
            assertTrue(updated.getStatus().equals(expected.getStatus()));
            assertTrue(updated.getEmail().equals(expected.getEmail()));
            assertTrue(updated.getCurrency().equals(expected.getCurrency()));
            assertTrue(updated.getCreated().equals(expected.getCreated()));
            assertTrue(updated.getAmount().equals(expected.getAmount()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test deleting an order
     */
    @Test
    @DisplayName("Test deleting an order")
    public void testDelete() {
        Integer customerId = null;
        try {
            // Insert a new customer
            customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
            this.customerIds.add(customerId);

            // Insert an order for the customer
            Integer orderId = OrderHelper.insertTestOrder(DAOHelper.getRandomId(), customerId);

            // Delete it
            orderDAO.delete(orderId);

            // Try to get a count of the orders with that id
            Integer count = OrderHelper.getTestOrderCountById(orderId);

            assertTrue(count == 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
