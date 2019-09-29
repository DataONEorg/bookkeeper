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
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * The quota data access interfaces used to create, read, update, and delete
 * quotas from the database
 */
@RegisterBeanMapper(Quota.class)
public interface QuotaStore {

    /**
     * List all quotas
     */
    @SqlQuery(  "SELECT " +
        "q.id, q.object, q.name, q.softLimit, q.hardLimit, q.usage, q.unit, q.customerId, q.subject " +
        "FROM quotas q")
    List<Quota> listQuotas();

    /**
     * Find quotas by quota identifier
     * @param id
     */
    @SqlQuery("SELECT " +
        "q.id, q.object, q.name, q.softLimit, q.hardLimit, q.usage, q.unit, q.customerId, q.subject " +
        "FROM quotas q " +
        "WHERE q.id = :id")
    Quota getQuota(@Bind("id") Integer id);

    /**
     * Find quotas by customer identifier.
     *
     * Pass a null customerId to list all product-associated quotas (i.e. not bound to a customer).
     * @param customerId
     */
    @SqlQuery("SELECT " +
        "q.id, q.object, q.name, q.softLimit, q.hardLimit, q.usage, q.unit, q.customerId, q.subject " +
        "FROM quotas q " +
        "WHERE q.customerId = :customerId")
    List<Quota> findQuotasByCustomerId(@Bind("customerId") Integer customerId);


    /**
     * Find quotas by subject identifier
     *
     * @param subject the subject identifier (such as an ORCID identifier)
     * @return quotas the list of quotas for the subject
     */
    @SqlQuery("SELECT " +
        "q.id, q.object, q.name, q.softLimit, q.hardLimit, q.usage, q.unit, q.customerId, q.subject " +
        "FROM quotas q " +
        "WHERE q.subject = :subject")
    List<Quota> findQuotasBySubject(@Bind("subject") String subject);

    /**
     * Insert a quota with a given Quota instance
     * @param quota
     */
    @SqlUpdate("INSERT INTO quotas " +
        "(object, name, softLimit, hardLimit, usage, unit, customerId, subject) " +
        "VALUES " +
        "(:object, :name, :softLimit, :hardLimit, :usage, :unit, :customerId, :subject)")
    void insert(@BindBean Quota quota);

    /**
     * Update a quota for a given id
     * @param quota
     */
   @SqlUpdate("UPDATE quotas " +
        "SET object = :object, " +
        "name = :name, " +
        "softLimit = :softLimit, " +
        "hardLimit = :hardLimit, " +
       "unit = :unit, " +
        "usage = :usage, " +
       "customerId = :customerId, " +
        "subject = :subject " +
        "WHERE id = :id")
    void update(@BindBean Quota quota);

    /**
     * Delete a quota given the quota id
     * @param id
     */
    @SqlUpdate("DELETE FROM quotas WHERE id = :id")
    void delete(@Bind("id") Integer id);
}
