/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2020. All rights reserved.
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

package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.api.Usage;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterBeanMapper(Usage.class)
public interface UsageStore {

    /** The query used to find all usages */
    String SELECT_CLAUSE =
        "SELECT " +
            "u.id, " +
            "u.object, " +
            "u.quotaid, " +
            "u.instanceid, " +
            "u.quantity, " +
            "u.status " +
        "FROM usages u ";

    /** The full query */
    String SELECT_ALL = SELECT_CLAUSE;

    /** Select by identifer */
    String SELECT_BY_ID = SELECT_CLAUSE +
            "WHERE u.id = :id";

    /** Select by instance identifier */
    String SELECT_BY_INSTANCE_ID = SELECT_CLAUSE + "WHERE u.instanceId = :instanceId";

    /** Select by instance identifier and subjects */
    String SELECT_BY_INSTANCE_ID_AND_SUBJECTS = SELECT_CLAUSE +
            "INNER JOIN quotas q ON q.id = u.quotaid " +
            "WHERE u.instanceid = :instanceId " +
            "AND q.subject IN (<subscribers>)";

    /** Select by quota type */
    String SELECT_BY_QUOTA_TYPE = SELECT_CLAUSE +
            "INNER JOIN quotas q ON q.id = u.quotaid " +
            "WHERE q.name = :quotaType";

    /** Select by quota subjects */
    String SELECT_BY_QUOTA_SUBJECTS = SELECT_CLAUSE +
            "INNER JOIN quotas q ON q.id = u.quotaid " +
            "WHERE q.subject IN (<subscribers>)";

    /** Select by quota type and subject */
    String SELECT_BY_QUOTA_TYPE_AND_SUBJECTS = SELECT_CLAUSE +
            "INNER JOIN quotas q ON q.id = u.quotaid " +
            "WHERE q.name = :quotaType " +
            "AND q.subject IN (<subscribers>)";

    /**
     * List all usages
     * @return usages the list of usages
     */
    @SqlQuery(SELECT_ALL)
    List<Usage> listUsages();

    /**
     * Find usages by usage identifier
     * @param id the usage identifier
     * @return usage the usage for the identifier
     */
    @SqlQuery(SELECT_BY_ID)
    Usage getUsage(@Bind("id") Integer id);

    /**
     * Find usages by instance identifier
     * @param instanceId the usage instance id
     * @return usage the usage for the instanceId and subject
     */
    @SqlQuery(SELECT_BY_INSTANCE_ID)
    Usage findUsageByInstanceId(@Bind("instanceId") String instanceId);

    /**
     * Find usages by instance identifier and subject.
     * @param instanceId the usage instance id
     * @param subscribers list of subscribers (quota subjects)
     * @return usage the usage for the instanceId and subjects
     */
    @SqlQuery(SELECT_BY_INSTANCE_ID_AND_SUBJECTS)
    Usage findUsageByInstanceIdAndSubjects(@Bind("instanceId") String instanceId, @BindList("subscribers") List<String> subscribers);

    /**
     * Find usages by quota type.
     * @param quotaType quota type (e.g. "portal", "storage", ...)
     * @return the list of usages for the quota type
     */
    @SqlQuery(SELECT_BY_QUOTA_TYPE)
    List<Usage> findUsagesByQuotaType(@Bind("quotaType") String quotaType);

    /**
     * Find usages by quota subscribers (subjects).
     * @param subscribers
     * @return the list of usages for the quota subjects
     */
    @SqlQuery(SELECT_BY_QUOTA_SUBJECTS)
    List<Usage> findUsagesByQuotaSubjects(@BindList("subscribers") List<String> subscribers);

    /**
     * Find usages by name and subjects.
     * @param quotaType the object ("portal", "storage", ...)
     * @param subscribers the list of quota subject
     * @return usages the list of usages for quota type and subjects
     */
    @SqlQuery(SELECT_BY_QUOTA_TYPE_AND_SUBJECTS)
    List<Usage> findUsagesByQuotaTypeAndSubjects(@Bind("quotaType") String quotaType, @BindList("subscribers") List<String> subscribers);

    /**
     * Insert a usage with a given Usage instance
     * @param usage the usage to insert
     */
    @SqlUpdate("INSERT INTO usages " +
            "(object, " +
            "quotaId, " +
            "instanceId, " +
            "quantity, " +
            "status) " +
            "VALUES " +
            "(:object, " +
            ":quotaId, " +
            ":instanceId, " +
            ":quantity, " +
            ":status) " +
            "RETURNING id")
    @GetGeneratedKeys
    Integer insert(@BindBean Usage usage);

    /**
     * Update a usage for a given id
     * @param usage the usage to update
     */
    @SqlUpdate("UPDATE usages " +
            "SET object = :object, " +
            "quotaId = :quotaId, " +
            "instanceId = :instanceId, " +
            "quantity = :quantity, " +
            "status = :status " +
            "RETURNING id")
    @GetGeneratedKeys
    Usage update(@BindBean Usage usage);

    /**
     * Delete a usage
     * @param id the usage id to delete
     */
    @SqlUpdate("DELETE FROM usages WHERE id = :id")
    void delete(@Bind("id") Integer id);

}
