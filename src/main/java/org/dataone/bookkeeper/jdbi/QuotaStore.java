/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2019. All rights reserved.
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

import org.dataone.bookkeeper.api.Quota;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * The quota data access interfaces used to create, read, update, and delete
 * quotas from the database
 */
@RegisterBeanMapper(Quota.class)
public interface QuotaStore {

    /** The query used to find all quotas */
    String SELECT_CLAUSE =
        "SELECT " +
            "q.id, " +
            "q.object, " +
            "q.quotaType, " +
            "q.softLimit, " +
            "q.hardLimit, " +
            "q.totalUsage, " +
            "q.unit, " +
            "q.membershipId, " +
            "q.subject " +
            "FROM quotas q ";

    /** The full ordered query */
    String SELECT_ALL = SELECT_CLAUSE;

    /** The query used to find unassigned quotas (i.e. generic product quotas */
    String SELECT_UNASSIGNED = SELECT_CLAUSE + "WHERE membershipId IS NULL ";

    /** The query used to find an individual quota */
    String SELECT_ONE = SELECT_CLAUSE + "WHERE q.id = :id ";

    /** The query used to find quotas by name */
    String SELECT_BY_TYPE = SELECT_CLAUSE + "WHERE q.quotaType = :quotaType";

    /** The query used to find a quota by membership identifier */
    String SELECT_MEMBERSHIP = SELECT_CLAUSE + "WHERE q.membershipId = :membershipId ";

    /** The query used to find a quota by subject identifier */
    String SELECT_OWNER = SELECT_CLAUSE + "WHERE q.subject = :subject ";

    /** The query used to find quotas by multiple subject identifiers */
    String SELECT_OWNERS = SELECT_CLAUSE + "WHERE q.subject IN (<subjects>) ";

    /** The query used to find quotas by type and multiple subject identifiers */
    String SELECT_BY_NAME_AND_OWNERS = SELECT_CLAUSE + "WHERE q.quotaType = :quotaType AND q.subject IN (<subjects>) ";

    /**
     * List all quotas
     * @return quotas the list of quotas
     */
    @SqlQuery(SELECT_ALL)
    List<Quota> listQuotas();

    /**
     * List all unassigned quotas (no membershipId)
     * @return quotas the list of unassigned quotas
     */
    @SqlQuery(SELECT_UNASSIGNED)
    List<Quota> listUnassignedQuotas();

    /**
     * Find quotas by quota identifier
     * @param id the quota identifier
     * @return quota the quota for the identifier
     */
    @SqlQuery(SELECT_ONE)
    Quota getQuota(@Bind("id") Integer id);

    /**
     * Find quotas by membership identifier.
     *
     * Pass a null membershipId to list all product-associated quotas (i.e. not bound to a membership).
     * @param membershipId the membership identifier
     * @return quotas the quotas for the membershipId
     */
    @SqlQuery(SELECT_MEMBERSHIP)
    List<Quota> findQuotasByMembershipId(@Bind("membershipId") Integer membershipId);

    /**
     * Find quotas by quota type
     *
     * @param quotaType quota type
     * @return quotas the list of quotas for the quota type
     */
    @SqlQuery(SELECT_BY_TYPE)
    List<Quota> findQuotasByType(@Bind("quotaType") String quotaType);

    /**
     * Find quotas by subject identifier
     *
     * @param subject the subject identifier (such as an ORCID identifier)
     * @return quotas the list of quotas for the subject
     */
    @SqlQuery(SELECT_OWNER)
    List<Quota> findQuotasBySubject(@Bind("subject") String subject);

    /**
     * Find quotas by a list of subject identifiers
     *
     * @param subjects the subject identifiers list (such as an ORCID identifier)
     * @return quotas the list of quotas for the subject
     */
    @SqlQuery(SELECT_OWNERS)
    List<Quota> findQuotasBySubjects(@BindList("subjects") List<String> subjects);

    /**
     * Find quotas by a quota type and subjects
     *
     * @param quotaType the quota name (e.g. "portal", "storage")
     * @param subjects the subject identifiers (such as an ORCID identifier)
     * @return quotas the list of quotas for the subjects and names
     */
    @SqlQuery(SELECT_BY_NAME_AND_OWNERS)
    List<Quota> findQuotasByNameAndSubjects(@Bind("quotaType") String quotaType, @BindList("subjects") List<String> subjects);

    /**
     * Insert a quota with a given Quota instance
     * @param quota the quota to insert
     */
    @SqlUpdate("INSERT INTO quotas " +
        "(object, " +
        "quotaType, " +
        "softLimit, " +
        "hardLimit, " +
        "unit, " +
        "membershipId, " +
        "subject) " +
        "VALUES " +
        "(:object, " +
        ":quotaType, " +
        ":softLimit, " +
        ":hardLimit, " +
        ":unit, " +
        ":membershipId, " +
        ":subject) " +
        "RETURNING id")
    @GetGeneratedKeys
    Integer insert(@BindBean Quota quota);

    /**
     * Update a quota for a given id
     * @param quota the quota to update
     */
   @SqlUpdate("UPDATE quotas " +
       "SET object = :object, " +
       "quotaType = :quotaType, " +
       "softLimit = :softLimit, " +
       "hardLimit = :hardLimit, " +
       "unit = :unit, " +
       "membershipId = :membershipId, " +
       "subject = :subject " +
       "WHERE id = :id ")
   @GetGeneratedKeys
   Quota update(@BindBean Quota quota);

    /**
     * Delete a quota given the quota id
     * @param id the quota to delete
     */
    @SqlUpdate("DELETE FROM quotas WHERE id = :id")
    void delete(@Bind("id") Integer id);
}
