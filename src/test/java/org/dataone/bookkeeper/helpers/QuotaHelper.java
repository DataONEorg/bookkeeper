package org.dataone.bookkeeper.helpers;

import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Quota;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;

/**
 * A delegate class with helper methods for manipulating the quotas table during testing
 */
public class QuotaHelper {


    /**
     * Insert a test quota with a given id and customer id
     * @param quotaId
     * @param customerId
     * @return
     */
    public static Integer insertTestQuotaWithCustomer(Integer quotaId, Integer customerId)  throws SQLException {
        BaseTestCase.dbi.useHandle(handle ->
            handle.execute("INSERT INTO quotas " +
                    "(id, object, name, softLimit, hardLimit, unit, customerId) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?)",
                quotaId,
                "quota",
                "test_customer_quota",
                12345,
                123450,
                "megabyte", customerId)
        );
        return quotaId;
    }

    /**
     * Remove a test quota given its id
     * @param quotaId
     */
    public static void removeTestQuota(Integer quotaId) throws SQLException {

       BaseTestCase.dbi.useHandle(handle ->
            handle.execute("DELETE FROM quotas WHERE id = ?", quotaId)
        );
    }

    /**
     * Create a test Quota instance given the quotaId and customerId
     * @param quotaId
     * @param customerId
     * @return
     */
    public static Quota createTestStorageQuota(@NotNull Integer quotaId, Integer customerId) {
        Quota quota = new Quota();
        quota.setId(quotaId);
        quota.setObject("quota");
        quota.setName("test_storage_quota_" + quotaId);
        quota.setSoftLimit(4000000);
        quota.setHardLimit(5000000);
        quota.setUnit("megabyte");
        quota.setCustomerId(customerId);
        return quota;
    }

    public static Quota createTestPortalQuota(@NotNull Integer quotaId, Integer customerId) {
        Quota quota = new Quota();
        quota.setId(quotaId);
        quota.setObject("quota");
        quota.setName("test_portal_quota_" + quotaId);
        quota.setSoftLimit(3);
        quota.setHardLimit(3);
        quota.setUnit("portal");
        quota.setCustomerId(customerId);
        return quota;
    }
    /**
     * Return the number of quotas in the database for the given quota name
     * @param quotaName
     * @return
     */
    public static Integer getQuotaCountByName(String quotaName) throws SQLException {

        Integer count = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT count(*) FROM quotas WHERE name = :quotaName")
                .bind("quotaName", quotaName)
                .mapTo(Integer.class)
                .one()
        );

        return count;
    }

    /**
     * Return the number of quotas for the given quota id
     * @param quotaId
     * @return
     */
    public static Integer getQuotaCountById(Integer quotaId) {
        Integer count = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT count(*) FROM quotas WHERE id = :id")
                .bind("id", quotaId)
                .mapTo(Integer.class)
                .one()
        );
        return count;
    }
    /**
     * Return the quota id for the given quota name
     * @param quotaName
     * @return
     */
    public static Integer getQuotaIdByName(String quotaName) throws SQLException {
        Integer quotaId = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT id FROM quotas WHERE name = :quotaName")
                .bind("quotaName", quotaName)
                .mapTo(Integer.class)
                .one()
        );
        return quotaId;
    }

    /**
     * Return a quota instance given a quota id
     * @param quotaId
     * @return
     */
    public static Quota getQuotaById(Integer quotaId) {
        Quota quota = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT id, object, name, softLimit, hardLimit, unit " +
                "FROM quotas WHERE id = :id")
                .bind("id", quotaId)
                .mapToBean(Quota.class)
                .one()
        );
        return quota;
    }
}
