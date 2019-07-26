package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.BaseTestCase;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test the Quota data access object
 */
public class QuotaDAOTest extends BaseTestCase {

    // The QuotaDAO to test
    private QuotaDAO quotaDAO;

    // A list of quota ids used in testing
    private List<Integer> quotaIds = new ArrayList<Integer>();

    // A list of customer ids used in testing
    private List<Integer> customerIds = new ArrayList<Integer>();

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
    }

    /**
     * Test getting the full Quota list
     */
    @Test
    @DisplayName("Test listing the quotas")
    public void testListQuotas() {
        assertTrue(quotaDAO.listQuotas().size() >= 11);
    }

    /**
     * Test getting a single quota by ID
     */
    @Test
    @DisplayName("Test get quota by quota ID")
    public void testGetQuotaById() {

        assertTrue(quotaDAO.findQuotasById(1).size() == 1);
    }

    /**
     * Test getting quotas by customer ID
     */
    @Test
    @DisplayName("Test get quota by customer ID")
    public void testGetQuotasByCustomerId() {

        try {
            Integer customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
            this.customerIds.add(customerId); // To be deleted
            Integer quotaId = QuotaHelper.insertTestQuotaWithCustomer(DAOHelper.getRandomId(), customerId);
            this.quotaIds.add(quotaId); // To be deleted
            assertTrue(quotaDAO.findQuotasByCustomerId(customerId).size() == 1);
            assertThat(quotaDAO.findQuotasByCustomerId(0).isEmpty());

        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Test inserting a Quota instance
     */
    @Test
    @DisplayName("Test inserting a Quota instance")
    public void testInsertWithQuota() {
        try {
            Integer quotaId = DAOHelper.getRandomId();
            Integer customerId = null;
            Quota quota = QuotaHelper.createTestStorageQuota(quotaId, customerId);
            this.quotaIds.add(quotaId);
            quotaDAO.insert(quota);
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
            Integer customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
            this.customerIds.add(customerId); // Clean up
            Integer quotaId = QuotaHelper.insertTestQuotaWithCustomer(DAOHelper.getRandomId(), customerId);
            this.quotaIds.add(quotaId); // Clean up
            Quota quota = new Quota();
            quota.setId(quotaId);
            quota.setObject("quota");
            String quotaName = "test_quota_" + DAOHelper.getRandomId().toString();
            quota.setName(quotaName);
            quota.setSoftLimit(56789);
            quota.setHardLimit(567890);
            quota.setUnit("megabyte");
            quota.setCustomerId(customerId);
            quotaDAO.update(quota);
            assertThat(QuotaHelper.getQuotaById(quotaId).getName() == quotaName);
            assertThat(QuotaHelper.getQuotaById(quotaId).getSoftLimit() == 56789);
            assertThat(QuotaHelper.getQuotaById(quotaId).getHardLimit() == 567890);
        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Test deleting a quota
     */
    @Test
    @DisplayName("Test deleting a quota")
    public void testDelete() {
        Integer customerId;
        Integer quotaId = null;
        try {
            customerId = CustomerHelper.insertTestCustomer(DAOHelper.getRandomId());
            this.customerIds.add(customerId); // Clean up
             quotaId = QuotaHelper.insertTestQuotaWithCustomer(DAOHelper.getRandomId(), customerId);
            quotaDAO.delete(quotaId);
            assertThat(QuotaHelper.getQuotaCountById(quotaId) == 0);
        } catch (SQLException e) {
            this.quotaIds.add(quotaId); // Clean up on fail
            fail();
        }

    }
}
