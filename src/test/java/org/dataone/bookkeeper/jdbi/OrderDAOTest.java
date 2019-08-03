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
}
