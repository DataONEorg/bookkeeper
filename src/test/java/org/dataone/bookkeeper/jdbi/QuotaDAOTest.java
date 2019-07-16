package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Quota;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

public class QuotaDAOTest extends BaseTestCase {

    // The QuotaDAO to test
    private QuotaDAO quotaDAO;

    /**
     * Set up the DAO for testing
     */
    @BeforeEach
    public void init() {
        quotaDAO = dbi.onDemand(QuotaDAO.class);
    }

    /**
     * Tear down resources
     */
    @AfterEach
    public void tearDown() {
        // Nothing yet
    }

    /**
     * Test getting the full Quota list
     */
    @Test
    @DisplayName("Test get quota by ID")
    public void testListQuotas() {
        assertTrue(quotaDAO.listQuotas().size() >= 11);
    }

    /**
     * Test getting a single quota by ID
     */
    @Test
    @DisplayName("Test get quota by quota ID")
    public void testGetQuotaById() {

        assertTrue(quotaDAO.findQuotaById(1L).size() == 1);
    }

    /**
     * Test getting quotas by customer ID
     */
    @Test
    @DisplayName("Test get quota by customer ID")
    public void testGetQuotaByCustomerId() {

        dbi.useHandle(handle ->
        {handle.execute("INSERT INTO quotas " +
            "(object, name, softLimit, hardLimit, unit, customerId) " +
            "VALUES " +
            "(?, ?, ?, ?, ?, ?)", "quota", "test_customer_quota", 12345, 123450, "bytes", 500);
        handle.execute("INSERT INTO quotas " +
            "(object, name, softLimit, hardLimit, unit, customerId) " +
            "VALUES " +
            "(?, ?, ?, ?, ?, ?)", "quota", "test_customer_quota", 54321, 543210, "bytes", 500);
        });
        assertTrue(quotaDAO.findQuotasByCustomerId(500L).size() == 2);
        assertThat(quotaDAO.findQuotasByCustomerId(0L).isEmpty());
    }

}
