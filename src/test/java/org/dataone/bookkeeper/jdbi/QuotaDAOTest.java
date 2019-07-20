package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.BaseTestCase;
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
                removeTestQuota(quotaId);
            } catch (SQLException e) {
                fail();
            }
        }

        // Remove test customer entries
        for (Integer customerId : this.customerIds) {
            try {
                removeTestCustomer(customerId);
            } catch (SQLException e) {
                fail();
            }
        }
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

        try {
            Integer customerId = insertTestCustomer(getRandomId());
            this.customerIds.add(customerId); // To be deleted
            Integer quotaId = insertTestQuotaWithCustomer(getRandomId(), customerId);
            this.quotaIds.add(quotaId); // To be deleted
            assertTrue(quotaDAO.findQuotasByCustomerId(customerId).size() == 1);
            assertThat(quotaDAO.findQuotasByCustomerId(0).isEmpty());

        } catch (SQLException e) {
            fail();
        }
    }

    /**
     * Test inserting a quota
     */
    @Test
    @DisplayName("Test inserting a quota")
    public void testInsert() {
        try {
            Integer customerId = insertTestCustomer(getRandomId());
            this.customerIds.add(customerId); // To be deleted
            String quotaName = "test_quota_" + getRandomId().toString();
            quotaDAO.insert("quota", quotaName, 12345,
                123450, "byte", null);
            assertThat(getQuotaCountByName(quotaName) > 0);
            Integer quotaId = getQuotaIdByName(quotaName);
            this.quotaIds.add(quotaId); // To be deleted
        } catch (SQLException e) {
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
            Integer customerId = insertTestCustomer(getRandomId());
            this.customerIds.add(customerId); // To be deleted
            Integer quotaId = insertTestQuotaWithCustomer(getRandomId(), customerId);
            this.quotaIds.add(quotaId); // To be deleted
            String quotaName = "test_quota_" + getRandomId().toString();
            quotaDAO.update(quotaId, "quota", quotaName, 56789,
                567890, "megabyte", customerId);
            assertThat(getQuotaById(quotaId).getName() == "test_storage_quota_2");
            assertThat(getQuotaById(quotaId).getSoftLimit() == 56789);
            assertThat(getQuotaById(quotaId).getHardLimit() == 567890);
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
        try {
            Integer customerId = insertTestCustomer(getRandomId());
            this.customerIds.add(customerId); // To be deleted
            Integer quotaId = insertTestQuotaWithCustomer(getRandomId(), customerId);
            quotaDAO.delete(quotaId);
            assertThat(getQuotaCountById(quotaId) == 0);
            // this.quotaIds.add(quotaId); // Already deleted
        } catch (SQLException e) {
            fail();
        }

    }
}
