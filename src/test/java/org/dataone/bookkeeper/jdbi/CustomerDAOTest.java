package org.dataone.bookkeeper.jdbi;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Map;

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

    /**
     * Test listing a customer with storage and portal quotas
     */
    @Test
    @DisplayName("Test listing customers with associated storage and portal quotas")
    public void testListCustomersWithStorageAndPortalQuotas() {

        try {
            // Insert a customer
            final Integer customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
            this.customerIds.add(customerId);

            // Insert a storage and portal quotas for the customer
            final Map<Integer, Quota> quotas =
                QuotaHelper.insertTestStorageAndPortalQuotasWithCustomer(
                    DAOHelper.getRandomId(), DAOHelper.getRandomId(), customerId);
                customerDAO.listCustomers().forEach(customer -> {
                    customer.getQuotas()
                        .forEach(quota -> {
                            Quota expectedQuota = quotas.get(quota.getId());
                            assertTrue(quota.getId().equals(expectedQuota.getId()));
                            assertTrue(quota.getObject().equals(expectedQuota.getObject()));
                            assertTrue(quota.getName().equals(expectedQuota.getName()));
                            assertTrue(quota.getSoftLimit().equals(expectedQuota.getSoftLimit()));
                            assertTrue(quota.getHardLimit().equals(expectedQuota.getHardLimit()));
                            assertTrue(quota.getUnit().equals(expectedQuota.getUnit()));
                            assertTrue(quota.getCustomerId().equals(expectedQuota.getCustomerId()));
                        });
                });
        } catch (SQLException e) {
            fail(e);
        }
    }

    /**
     * Test getting a customer by id
     * @throws SQLException
     */
    @Test
    @DisplayName("Test getting a customer by id")
    public void testGetCustomerById() throws SQLException {
        // Insert a customer
        final Integer customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
        this.customerIds.add(customerId);

        // Get the customer
        Customer customer = customerDAO.getCustomer(customerId);
        assertTrue(((Customer) customer).getId().equals(customerId));
    }

    /**
     * Test getting a customer by ORCID identifier
     * @throws SQLException
     */
    @Test
    @DisplayName("Test finding a customer by ORCID identifier")
    public void testFindCustomerByOrcid() throws SQLException, JsonProcessingException {
        // Insert a customer
        final Customer expectedCustomer = CustomerHelper.insertTestCustomer(
            CustomerHelper.createCustomer(DAOHelper.getRandomId()));
        this.customerIds.add(expectedCustomer.getId());

        // Get the customer
        Customer customer = customerDAO.findCustomerByOrcid(expectedCustomer.getOrcid());
        assertTrue(((Customer) customer).getId().equals(expectedCustomer.getId()));
    }

    /**
     * Test getting a customer by email
     * @throws SQLException
     * @throws JsonProcessingException
     */
    @Test
    @DisplayName("Test finding a customer by email")
    public void testFindCustomerByEmail() throws SQLException, JsonProcessingException {
        // Insert a customer
        final Customer expectedCustomer = CustomerHelper.insertTestCustomer(
            CustomerHelper.createCustomer(DAOHelper.getRandomId()));
        this.customerIds.add(expectedCustomer.getId());

        // Get the customer
        Customer customer = customerDAO.findCustomerByEmail(expectedCustomer.getEmail());
        assertTrue(((Customer) customer).getId().equals(expectedCustomer.getId()));
    }

    /**
     * Test inserting a customer
     */
    @Test
    @DisplayName("Test inserting a customer")
    public void testInsert() {
        // Create a Customer to insert
        Customer expectedCustomer = CustomerHelper.createCustomer(DAOHelper.getRandomId());
        this.customerIds.add(expectedCustomer.getId());

        // Insert the customer
        customerDAO.insert(expectedCustomer);

        // Then get the customer to ensure it was inserted
        Customer customer = CustomerHelper.getCustomerById(expectedCustomer.getId());

        assertTrue(customer.getId().equals(expectedCustomer.getId()));
    }
}
