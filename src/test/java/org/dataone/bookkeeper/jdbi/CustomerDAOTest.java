package org.dataone.bookkeeper.jdbi;


import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.helpers.CustomerHelper;
import org.dataone.bookkeeper.helpers.DAOHelper;
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
 * Test the product data access object
 */
public class CustomerDAOTest extends BaseTestCase {

    /* The CustomerDAO to test */
    private CustomerDAO customerDAO;

    // A list of customer ids used in testing
    private List<Integer> customerIds = new ArrayList<Integer>();

    /**
     * Set up the DAO for testing
     */
    @BeforeEach
    public void init() {
        customerDAO = dbi.onDemand(CustomerDAO.class);
    }

    /**
     * Tear down resources
     */
    @AfterEach
    public void tearDown() {
        // Remove test customer entries
        for (Integer customerId : this.customerIds) {
            try {
                CustomerHelper.removeTestCustomer(customerId);
            } catch (SQLException e) {
                fail(e);
            }
        }
    }

    @Test
    @DisplayName("Test listing the customers")
    public void testListProducts() {
        // Insert a new product
        Integer customerId = null;
        try {
            customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
        } catch (SQLException e) {
            fail(e);
        }
        this.customerIds.add(customerId);
        assertTrue(customerDAO.listCustomers().size() >= 1);
    }
}
