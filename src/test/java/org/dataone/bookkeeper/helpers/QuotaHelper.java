/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2019
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.dataone.bookkeeper.helpers;

import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Quota;

import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * A delegate class with helper methods for manipulating the quotas table during testing
 */
public class QuotaHelper {


    /**
     * Insert a test quota with a given id and membership id
     * @param quotaId the quota identifier
     * @param orderId the order identifier
     * @return quotaId the quota identifier
     */
    public static Integer insertTestQuotaWithOrder(Integer quotaId, Integer orderId)  throws SQLException {
        BaseTestCase.dbi.useHandle(handle ->
            handle.execute(
                "INSERT INTO quotas " +
                    "(id, " +
                    "object, " +
                    "quotaType, " +
                    "softLimit, " +
                    "hardLimit, " +
                    "totalUsage, " +
                    "unit, " +
                    "orderId, " +
                    "subject) " +
                "VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                quotaId,
                "quota",
                "portal",
                5.0,
                10.0,
                null,
                "portal",
                orderId,
                null)
        );
        return quotaId;
    }

    /**
     * Insert a test quota with a given id, membership id, and subject
     * @param quotaId the quota identifier
     * @param orderId the order identifier
     * @param subject the order subject
     * @return quotaId the quota identifier
     * @throws SQLException any SQL exception
     */
    public static Integer insertTestQuotaWithSubject(
        Integer quotaId, Integer orderId, String subject) throws SQLException {
        BaseTestCase.dbi.useHandle(handle ->
            handle.execute(
                "INSERT INTO quotas (" +
                    "id, " +
                    "object, " +
                    "quotaType, " +
                    "softLimit, " +
                    "hardLimit, " +
                    "totalUsage, " +
                    "unit, " +
                    "orderId, " +
                    "subject) " +
                "VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                quotaId,
                "quota",
                "portal",
                5.0,
                5.0,
                null,
                "portal",
                orderId,
                subject)
        );
        return quotaId;
    }

    /**
     * Insert test storage and portal quotas into the quotas table  for the given membership id
     * @param storageQuotaId the storage quota identifier
     * @param portalQuotaId the portal quota identifier
     * @param orderId the order identifier
     * @return quotas the inserted quotas
     * @throws SQLException any SQL exception
     */
    public static Map<Integer, Quota> insertTestStorageAndPortalQuotasWithOrder(
        Integer storageQuotaId, Integer portalQuotaId, Integer orderId)
        throws SQLException {
        // Create storage and portal quotas
        Map<Integer, Quota> quotas = new HashMap<Integer, Quota>();
        quotas.put(storageQuotaId, QuotaHelper.createTestStorageQuota(storageQuotaId, orderId));
        quotas.put(portalQuotaId, QuotaHelper.createTestPortalQuota(portalQuotaId, orderId));

        String insertStatement = "INSERT INTO quotas " +
            "(id, object, quotaType, softLimit, hardLimit, totalUsage, unit, orderId, subject) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // Insert them into the database for the given membership id
        BaseTestCase.dbi.useHandle(handle -> {
            handle.execute(insertStatement,
                storageQuotaId,
                quotas.get(storageQuotaId).getObject(),
                quotas.get(storageQuotaId).getQuotaType(),
                quotas.get(storageQuotaId).getSoftLimit(),
                quotas.get(storageQuotaId).getHardLimit(),
                quotas.get(storageQuotaId).getTotalUsage(),
                quotas.get(storageQuotaId).getUnit(),
                quotas.get(storageQuotaId).getOrderId(),
                quotas.get(storageQuotaId).getSubject()
            );
            handle.execute(insertStatement,
                portalQuotaId,
                quotas.get(portalQuotaId).getObject(),
                quotas.get(portalQuotaId).getQuotaType(),
                quotas.get(portalQuotaId).getSoftLimit(),
                quotas.get(portalQuotaId).getHardLimit(),
                quotas.get(portalQuotaId).getTotalUsage(),
                quotas.get(portalQuotaId).getUnit(),
                quotas.get(portalQuotaId).getOrderId(),
                quotas.get(portalQuotaId).getSubject()
            );
        });
        return quotas;
    }

    /**
     * Remove a test quota given its id
     * @param quotaId the quota identifier
     */
    public static void removeTestQuota(Integer quotaId) throws SQLException {

       BaseTestCase.dbi.useHandle(handle ->
            handle.execute("DELETE FROM quotas WHERE id = ?", quotaId)
        );
    }

    /**
     * Create a test Quota instance given the quotaId and orderId
     * @param quotaId the quota identifier
     * @param orderId the order identifier
     * @return quota the quota being created
     */
    public static Quota createTestStorageQuota(@NotNull Integer quotaId, Integer orderId) {
        Quota quota = new Quota();
        quota.setId(quotaId);
        quota.setObject("quota");
        quota.setQuotaType("storage");
        quota.setSoftLimit(4000000.0);
        quota.setHardLimit(5000000.0);
        quota.setTotalUsage(null);
        quota.setUnit("megabyte");
        quota.setOrderId(orderId);
        quota.setSubject("http://orcid.org/0000-0000-0000-0000");
        return quota;
    }

    /**
     * Create a test quota
     * @param quotaId the quota identifier
     * @param orderId the order identifier
     * @return quota the quota created
     */
    public static Quota createTestPortalQuota(@NotNull Integer quotaId, Integer orderId) {
        Quota quota = new Quota();
        quota.setId(quotaId);
        quota.setObject("quota");
        quota.setQuotaType("portal");
        quota.setSoftLimit(3.0);
        quota.setHardLimit(3.0);
        quota.setTotalUsage(null);
        quota.setUnit("portal");
        quota.setOrderId(orderId);
        quota.setSubject("http://orcid.org/0000-0000-0000-0000");
        return quota;
    }
    /**
     * Return the number of quotas in the database for the given quota type
     * @param quotaType the quota type
     * @return count the count of quotas by type
     */
    public static Integer getQuotaCountByType(String quotaType) throws SQLException {

        Integer count = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT count(*) FROM quotas WHERE quotaType = :quotaType")
                .bind("quotaType", quotaType)
                .mapTo(Integer.class)
                .one()
        );

        return count;
    }

    /**
     * Return the number of quotas for the given quota id
     * @param quotaId the quota identifier
     * @return count the quota count
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
     * Return the quota id for the given quota type
     * @param quotaType the quota type
     * @return quotaId the quota identifier
     */
    public static Integer getQuotaIdByType(String quotaType) throws SQLException {
        Integer quotaId = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT id FROM quotas WHERE quotaType = :quotaType")
                .bind("quotaType", quotaType)
                .mapTo(Integer.class)
                .one()
        );
        return quotaId;
    }

    /**
     * Return a quota instance given a quota id
     * @param quotaId the quota identifier
     * @return quota the returned quota
     */
    public static Quota getQuotaById(Integer quotaId) {
        Quota quota = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT id, object, quotaType, softLimit, hardLimit, totalUsage, unit, " +
                "orderId, subject " +
                "FROM quotas WHERE id = :id")
                .bind("id", quotaId)
                .mapToBean(Quota.class)
                .one()
        );
        return quota;
    }
}
