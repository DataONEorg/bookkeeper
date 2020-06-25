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
     * Insert a test quota with a given id and subscription id
     * @param quotaId
     * @param subscriptionId
     * @return
     */
    public static Integer insertTestQuotaWithSubscription(Integer quotaId, Integer subscriptionId)  throws SQLException {
        BaseTestCase.dbi.useHandle(handle ->
            handle.execute(
                "INSERT INTO quotas " +
                    "(id, " +
                    "object, " +
                    "quotaType, " +
                    "softLimit, " +
                    "hardLimit, " +
                    "usage, " +
                    "unit, " +
                    "subscriptionId, " +
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
                subscriptionId,
                null)
        );
        return quotaId;
    }

    /**
     * Insert a test quota with a given id, subscription id, and subject
     * @param quotaId
     * @param subscriptionId
     * @param subject
     * @return
     * @throws SQLException
     */
    public static Integer insertTestQuotaWithSubject(
        Integer quotaId, Integer subscriptionId, String subject) throws SQLException {
        BaseTestCase.dbi.useHandle(handle ->
            handle.execute("INSERT INTO quotas " +
                "(id, object, quotaType, softLimit, hardLimit, usage, unit, subscriptionId, subject) " +
                "VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                quotaId,
                "quota",
                "portal",
                5.0,
                5.0,
                null,
                "portal",
                subscriptionId,
                subject)
        );
        return quotaId;
    }

    /**
     * Insert test storage and portal quotas into the quotas table  for the given subscription id
     * @param storageQuotaId
     * @param portalQuotaId
     * @param subscriptionId
     * @return
     * @throws SQLException
     */
    public static Map<Integer, Quota> insertTestStorageAndPortalQuotasWithSubscription(
        Integer storageQuotaId, Integer portalQuotaId, Integer subscriptionId)
        throws SQLException {
        // Create storage and portal quotas
        Map<Integer, Quota> quotas = new HashMap<Integer, Quota>();
        quotas.put(storageQuotaId, QuotaHelper.createTestStorageQuota(storageQuotaId, subscriptionId));
        quotas.put(portalQuotaId, QuotaHelper.createTestPortalQuota(portalQuotaId, subscriptionId));

        String insertStatement = "INSERT INTO quotas " +
            "(id, object, quotaType, softLimit, hardLimit, usage, unit, subscriptionId, subject) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // Insert them into the database for the given subscription id
        BaseTestCase.dbi.useHandle(handle -> {
            handle.execute(insertStatement,
                storageQuotaId,
                quotas.get(storageQuotaId).getObject(),
                quotas.get(storageQuotaId).getQuotaType(),
                quotas.get(storageQuotaId).getSoftLimit(),
                quotas.get(storageQuotaId).getHardLimit(),
                quotas.get(storageQuotaId).getUsage(),
                quotas.get(storageQuotaId).getUnit(),
                quotas.get(storageQuotaId).getSubscriptionId(),
                quotas.get(storageQuotaId).getSubject()
            );
            handle.execute(insertStatement,
                portalQuotaId,
                quotas.get(portalQuotaId).getObject(),
                quotas.get(portalQuotaId).getQuotaType(),
                quotas.get(portalQuotaId).getSoftLimit(),
                quotas.get(portalQuotaId).getHardLimit(),
                quotas.get(portalQuotaId).getUsage(),
                quotas.get(portalQuotaId).getUnit(),
                quotas.get(portalQuotaId).getSubscriptionId(),
                quotas.get(portalQuotaId).getSubject()
            );
        });
        return quotas;
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
     * Create a test Quota instance given the quotaId and subscriptionId
     * @param quotaId
     * @param subscriptionId
     * @return
     */
    public static Quota createTestStorageQuota(@NotNull Integer quotaId, Integer subscriptionId) {
        Quota quota = new Quota();
        quota.setId(quotaId);
        quota.setObject("quota");
        quota.setQuotaType("storage");
        quota.setSoftLimit(4000000.0);
        quota.setHardLimit(5000000.0);
        quota.setUsage(null);
        quota.setUnit("megabyte");
        quota.setSubscriptionId(subscriptionId);
        quota.setSubject("http://orcid.org/0000-0000-0000-0000");
        return quota;
    }

    public static Quota createTestPortalQuota(@NotNull Integer quotaId, Integer subscriptionId) {
        Quota quota = new Quota();
        quota.setId(quotaId);
        quota.setObject("quota");
        quota.setQuotaType("portal");
        quota.setSoftLimit(3.0);
        quota.setHardLimit(3.0);
        quota.setUsage(null);
        quota.setUnit("portal");
        quota.setSubscriptionId(subscriptionId);
        quota.setSubject("http://orcid.org/0000-0000-0000-0000");
        return quota;
    }
    /**
     * Return the number of quotas in the database for the given quota type
     * @param quotaType
     * @return
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
     * Return the quota id for the given quota type
     * @param quotaType
     * @return
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
     * @param quotaId
     * @return
     */
    public static Quota getQuotaById(Integer quotaId) {
        Quota quota = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT id, object, quotaType, softLimit, hardLimit, usage, unit, " +
                "subscriptionId, subject " +
                "FROM quotas WHERE id = :id")
                .bind("id", quotaId)
                .mapToBean(Quota.class)
                .one()
        );
        return quota;
    }
}
