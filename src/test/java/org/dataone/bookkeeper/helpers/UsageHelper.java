package org.dataone.bookkeeper.helpers;

import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Usage;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UsageHelper {

    /**
     * Insert a test quota with a given id and subscription id
     * @param quotaId
     * @return
     */
    public static Integer insertTestUsageWithQuota(Integer usageId, Integer quotaId)  throws SQLException {
        BaseTestCase.dbi.useHandle(handle ->
                handle.execute(
                        "INSERT INTO usages " +
                                "(id, " +
                                "object, " +
                                "quotaId, " +
                                "instanceId, " +
                                "quantity, " +
                                "status)" +
                                "VALUES " +
                                "(?, ?, ?, ?, ?, ?)",
                        usageId,
                        "portal",
                        quotaId,
                        "urn:uuid:F773C70F-90D3-4EBD-AA84-6B81C79C6F06",
                        10.0,
                        "active")
        );
        return usageId;
    }
    /**
     * Insert test storage and portal usages into the usages table  for the given quota ids
     * @param storageUsageId
     * @param portalUsageId
     * @param storageQuotaId
     * @param portalQuotaId
     * @return
     * @throws SQLException
     */
    public static Map<Integer, Usage> insertTestStorageAndPortalUsages(
            Integer storageUsageId, Integer portalUsageId,
            Integer storageQuotaId, Integer portalQuotaId)
            throws SQLException {
        // Create storage and portal usages
        Map<Integer, Usage> usages = new HashMap<Integer, Usage>();
        usages.put(storageUsageId, UsageHelper.createTestStorageUsage(storageUsageId, storageQuotaId));
        usages.put(portalUsageId, UsageHelper.createTestPortalUsage(portalUsageId, portalUsageId));

        String insertStatement = "INSERT INTO usages " +
                "(object, quota, instanceId, quantity, status) " +
                "VALUES (?, ?, ?, ?, ?)";
        // Insert them into the database for the given subscription id
        BaseTestCase.dbi.useHandle(handle -> {
            handle.execute(insertStatement,
                    storageUsageId,
                    usages.get(storageUsageId).getObject(),
                    usages.get(storageUsageId).getQuotaId(),
                    usages.get(storageUsageId).getInstanceId(),
                    usages.get(storageUsageId).getQuantity(),
                    usages.get(storageUsageId).getStatus()
            );
            handle.execute(insertStatement,
                    portalUsageId,
                    usages.get(portalUsageId).getObject(),
                    usages.get(portalUsageId).getQuotaId(),
                    usages.get(portalUsageId).getInstanceId(),
                    usages.get(portalUsageId).getQuantity(),
                    usages.get(portalUsageId).getStatus()
            );
        });
        return usages;
    }

    /**
     * Remove a test quota given its id
     * @param usageId
     */
    public static void removeTestUsage(Integer usageId) throws SQLException {

        BaseTestCase.dbi.useHandle(handle ->
                handle.execute("DELETE FROM usages WHERE id = ?", usageId)
        );
    }

    /**
     * Create a test storage Usage instance given the usageId and quotaId
     * @param quotaId
     * @param quotaId
     * @return
     */
    public static Usage createTestStorageUsage(@NotNull Integer usageId, Integer quotaId) {
        Usage usage = new Usage();
        usage.setId(usageId);
        usage.setObject("storage");
        usage.setQuotaId(quotaId);
        usage.setInstanceId("urn:node:ABC");
        usage.setQuantity(5000000.0);
        usage.setStatus("active");
        return usage;
    }

    /**
     * Create a test portal Usage instance given the usageId and quotaId
     * @param quotaId
     * @param quotaId
     * @return
     */
    public static Usage createTestPortalUsage(@NotNull Integer usageId, Integer quotaId) {
        Usage usage = new Usage();
        usage.setId(usageId);
        usage.setObject("portal");
        usage.setQuotaId(quotaId);
        usage.setInstanceId("urn:node:B2B3E90E-E462-4321-A0EF-BC728A4BAAEC");
        usage.setQuantity(10.0);
        usage.setStatus("active");
        return usage;
    }
}
