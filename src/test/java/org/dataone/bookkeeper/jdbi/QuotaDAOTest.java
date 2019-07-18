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

        assertTrue(quotaDAO.findQuotaById(1).size() == 1);
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
            "(?, ?, ?, ?, ?, ?)", "quota", "test_customer_quota", 12345, 123450, "megabyte", 500);
        handle.execute("INSERT INTO quotas " +
            "(object, name, softLimit, hardLimit, unit, customerId) " +
            "VALUES " +
            "(?, ?, ?, ?, ?, ?)", "quota", "test_customer_quota", 54321, 543210, "megabyte", 500);
        });
        assertTrue(quotaDAO.findQuotasByCustomerId(500).size() == 2);
        assertThat(quotaDAO.findQuotasByCustomerId(0).isEmpty());
    }

    @Test
    @DisplayName("Test inserting a quota")
    public void testInsert() {
        quotaDAO.insert("quota", "test_quota", 12345,
            123450, "byte", 500);
        assertThat(quotaDAO.findQuotasByCustomerId(500).size() > 0);

    }

    @Test
    @DisplayName("Test updating a quota")
    public void testUpdate() {
        quotaDAO.insert("quota", "test_storage_quota", 12345,
            123450, "megabyte", 400);
        Quota quota = quotaDAO.findQuotasByCustomerId(400).get(0);
        Integer quotaId = quota.getId();
        quotaDAO.update(quotaId, "quota", "test_storage_quota_2", 56789,
            567890, "megabyte", 400);
        assertThat(quotaDAO.findQuotasByCustomerId(400).get(0).getName() == "test_quota_2");
        assertThat(quotaDAO.findQuotasByCustomerId(400).get(0).getSoftLimit() == 56789);
        assertThat(quotaDAO.findQuotasByCustomerId(400).get(0).getHardLimit() == 567890);

    }
}
