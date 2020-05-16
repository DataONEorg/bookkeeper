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
            "u.quotaId, " +
            "u.instanceId, " +
            "u.quantity, " +
            "u.status " +
        "FROM usages u ";

    /** The full query */
    String SELECT_ALL = SELECT_CLAUSE;

    /** Select by identifer */
    String SELECT_BY_ID = SELECT_CLAUSE +
            "WHERE u.id = :id";

    /** Select by instance identifier */
    String SELECT_ONE = SELECT_CLAUSE + "WHERE u.instanceId = :instanceId";

    /** Select by name and instance identifer */
    String SELECT_BY_OBJECT_AND_INSTANCE_ID = SELECT_CLAUSE +
        "WHERE u.instanceId = :instanceId " +
        "AND u.object = :object";

    /**
     * List all usages
     * @return usages the list of usages
     */
    @SqlQuery(SELECT_ALL)
    List<Usage> listUsages();

    /**
     * Find usages by usages identifier
     * @param id the usages identifier
     * @return usage the usage for the identifier
     */
    @SqlQuery(SELECT_ONE)
    Usage getUsage(@Bind("id") Integer id);

    /**
     * Find usages by sequence identifier.
     * @param id the object ("portal", "storage", ...)
     * @return usage the usage for object and instanceId
     */
    @SqlQuery(SELECT_BY_ID)
    Usage findUsageById(@Bind("id") Integer id);

    /**
     * Find usages by sequence identifier.
     * @param object the object ("portal", "storage", ...)
     * @param instanceId the instance identifier
     * @return usage the usage for object and instanceId
     */
    @SqlQuery(SELECT_BY_OBJECT_AND_INSTANCE_ID)
    Usage findUsageByQuotaNameAndInstanceId(@Bind("object") String object, @Bind("instanceId") String instanceId);

    /**
     * Insert a usage with a given Usage instance
     * @param usage the usage to insert
     */
    @SqlUpdate("INSERT INTO usages " +
            "(:object, " +
            ":quotaId, " +
            ":instanceId, " +
            ":quantity, " +
            ":status, " +
            "RETURNING id")
    @GetGeneratedKeys
    Integer insert(@BindBean Usage usage);

    /**
     * Update a usage
     * @param quotaId the quotaId of the usage to update
     * @param quantity the quality to add or subtract
     */
    @SqlUpdate("UPDATE usages SET " +
            "quantity = :quantity " +
            "WHERE quotaId = :quotaId")
    @GetGeneratedKeys
    Usage update(@Bind("quotaId") Integer quotaId, @Bind("quantity") Double quantity);

    /**
     * Delete a usage
     * @param id the usage id to delete
     */
    @SqlUpdate("DELETE FROM usages WHERE id = :id")
    void delete(@Bind("id") Integer id);

}
