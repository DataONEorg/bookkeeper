package org.dataone.bookkeeper.jdbi;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.helpers.CustomerHelper;
import org.dataone.bookkeeper.helpers.DAOHelper;
import org.dataone.bookkeeper.helpers.QuotaHelper;
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

    /**
     * Test listing customers with empty quotas
     */
    @Test
    @DisplayName("Test listing the customers")
    public void testListCustomers() {
        // Insert a new product
        Integer customerId = null;
        try {
            customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
            this.customerIds.add(customerId);

        } catch (SQLException e) {
            fail(e);
        }
        assertTrue(customerDAO.listCustomers().size() >= 1);
    }

    @Test
    @DisplayName("Test listing customers with associated portal quotas")
    public void testListCustomersWithPortalQuota() {

        try {
            // Insert a customer
            final Integer customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
            this.customerIds.add(customerId);

            // Insert a portal quota for the customer
            final Integer quotaId =
                QuotaHelper.insertTestQuotaWithCustomer(DAOHelper.getRandomId(), customerId);
                customerDAO.listCustomers().forEach(customer -> {
                    customer.getQuotas()
                        .forEach(quota -> {
                            assertTrue(quota.getId().equals(quotaId));
                            assertTrue(quota.getCustomerId().equals(customerId));
                        });
                });
        } catch (SQLException e) {
            fail(e);
        }

        // Add a portal quota for the customer


    }
}
