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

package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.api.Product;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.api.Membership;
import org.dataone.bookkeeper.jdbi.mappers.MembershipMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * The membership data access interfaces used to create, read, update, and delete
 * memberships from the database
 */
public interface MembershipStore {

    /** The query used to find all memberships with their quotas */
    String SELECT_CLAUSE =
        "SELECT " +
            "s.id AS s_id, " +
            "s.object AS s_object, " +
            "date_part('epoch', s.canceledAt)::int AS s_canceledAt, " +
            "s.collectionMethod AS s_collectionMethod, " +
            "date_part('epoch', s.created)::int AS s_created, " +
            "s.customerId AS s_customerId, " +
            "c.subject AS c_subject, " +
            "s.metadata::json AS s_metadata, " +
            "s.productId AS s_productId, " +
            "p.id AS p_id, " +
            "p.object AS p_object, " +
            "p.active AS p_active, " +
            "p.amount AS p_amount, " +
            "p.caption AS p_caption, " +
            "date_part('epoch', p.created)::int AS p_created, " +
            "p.currency AS p_currency, " +
            "p.description AS p_description, " +
            "p.interval AS p_interval, " +
            "p.name AS p_name, " +
            "p.statementDescriptor AS p_statementDescriptor, " +
            "p.type AS p_type, " +
            "p.unitLabel AS p_unitLabel, " +
            "p.url AS p_url, " +
            "p.metadata::json AS p_metadata, " +
            "s.quantity AS s_quantity, " +
            "date_part('epoch', s.startDate)::int AS s_startDate, " +
            "s.status AS s_status, " +
            "date_part('epoch', s.trialEnd)::int AS s_trialEnd, " +
            "date_part('epoch', s.trialStart)::int  AS s_trialStart, " +
            "q.id AS q_id, " +
            "q.object AS q_object, " +
            "q.quotaType AS q_quotaType, " +
            "q.softLimit AS q_softLimit, " +
            "q.hardLimit AS q_hardLimit, " +
            "q.totalUsage AS q_totalUsage, " +
            "q.unit AS q_unit, " +
            "q.membershipId AS q_membershipId, " +
            "q.owner AS q_owner " +
        "FROM memberships s " +
        "LEFT JOIN quotas q ON s.id = q.membershipId " +
        "LEFT JOIN customers c ON s.customerId = c.id " +
        "LEFT JOIN products p ON s.productId = p.id ";

    /** Clause to order listed results */
    String ORDER_CLAUSE = "ORDER BY s.created ";

    /** The full ordered query */
    String SELECT_ALL = SELECT_CLAUSE + ORDER_CLAUSE;

    /** The query used to find an individual membership */
    String SELECT_ONE = SELECT_CLAUSE + "WHERE s.id = :id";

    /** The query used to find a membership by owner identifier */
    String SELECT_OWNER = SELECT_CLAUSE + "WHERE c.subject = :owner";

    /** The query used to find a membership by owner identifier */
    String SELECT_OWNERS = SELECT_CLAUSE + "WHERE c.subject IN (<owners>)";

    /**
     * List all memberships with their quotas
     * @return memberships The list of memberships
     */
    @SqlQuery(SELECT_ALL)
    @RegisterBeanMapper(value = Quota.class, prefix = "q")
    @RegisterBeanMapper(value = Product.class, prefix = "p")
    @RegisterRowMapper(value = MembershipMapper.class)
    @UseRowReducer(MembershipQuotasReducer.class)
    List<Membership> listMemberships();

    /**
     * Get an individual membership
     * @param id the membership identifier
     * @return membership The individual membership
     */
    @SqlQuery(SELECT_ONE)
    @RegisterBeanMapper(value = Quota.class, prefix = "q")
    @RegisterBeanMapper(value = Product.class, prefix = "p")
    @RegisterRowMapper(value = MembershipMapper.class)
    @UseRowReducer(MembershipQuotasReducer.class)
    Membership getMembership(@Bind("id") Integer id);

    /**
     * Get a membership by owner identifier
     * @param owner the customer owner identifier
     * @return membership the membership with the given owner identifier
     */
    @SqlQuery(SELECT_OWNER)
    @RegisterBeanMapper(value = Quota.class, prefix = "q")
    @RegisterBeanMapper(value = Product.class, prefix = "p")
    @RegisterRowMapper(value = MembershipMapper.class)
    @UseRowReducer(MembershipQuotasReducer.class)
    Membership findMembershipByOwner(@Bind("owner") String owner);

    /**
     * Get memberships by owner identifiers
     * @param owners the owner identifiers
     * @return memberships the memberships matching the requested identifiers
     */
    @SqlQuery(SELECT_OWNERS)
    @RegisterBeanMapper(value = Quota.class, prefix = "q")
    @RegisterBeanMapper(value = Product.class, prefix = "p")
    @RegisterRowMapper(value = MembershipMapper.class)
    @UseRowReducer(MembershipQuotasReducer.class)
    List<Membership> findMembershipsByOwners(@BindList("owners") List<String> owners);

    /**
     * Insert a membership
     * @param membership the membership to insert
     */
    @SqlUpdate(
        "INSERT INTO memberships (" +
            "object, " +
            "canceledAt, " +
            "collectionMethod, " +
            "created, " +
            "customerId, " +
            "metadata, " +
            "productId, " +
            "quantity, " +
            "startDate, " +
            "status, " +
            "trialEnd, " +
            "trialStart " +
        ") VALUES (" +
            ":getObject, " +
            "to_timestamp(:getCanceledAt), " +
            ":getCollectionMethod, " +
            "to_timestamp(:getCreated), " +
            ":getCustomerId, " +
            ":getMetadataJSON::json, " +
            ":getProductId, " +
            ":getQuantity, " +
            "to_timestamp(:getStartDate), " +
            ":getStatus, " +
            "to_timestamp(:getTrialEnd), " +
            "to_timestamp(:getTrialStart) " +
        ") RETURNING id"
    )
    @GetGeneratedKeys
    Integer insert(@BindMethods Membership membership);

    /**
     * Insert a membership and its quotas in a transaction
     * @param membership the membership to insert
     * @param quotas the quotas to insert
     * @return id the id of the membership
     */
    @Transaction
    default Integer insertWithQuotas(Membership membership, @NotNull @Valid Collection<Quota> quotas) {
        Integer id = insert(membership);
        for ( Quota quota : quotas) {
            quota.setMembershipId(id);
            Integer quotaId = insertQuota(quota);
        }
        return id;
    }

    /**
     * Insert a quota with a given Quota instance
     * @param quota the quota to insert
     */
    @SqlUpdate("INSERT INTO quotas " +
        "(object, quotaType, softLimit, hardLimit, totalUsage, unit, membershipId, owner) " +
        "VALUES " +
        "(:object, :quotaType, :softLimit, :hardLimit, :totalUsage, :unit, :membershipId, :owner) " +
        "RETURNING id")
    @GetGeneratedKeys
    Integer insertQuota(@BindBean Quota quota);

    /**
     * Update a membership
     * @param membership the membership to update
     */
    @SqlUpdate(
        "UPDATE memberships SET " +
            "id = :getId, " +
            "object = :getObject, " +
            "canceledAt = to_timestamp(:getCanceledAt), " +
            "collectionMethod = :getCollectionMethod, " +
            "created = to_timestamp(:getCreated), " +
            "customerId = :getCustomerId, " +
            "metadata = :getMetadataJSON::json, " +
            "productId = :getProductId, " +
            "quantity = :getQuantity, " +
            "startDate = to_timestamp(:getStartDate), " +
            "status = :getStatus, " +
            "trialEnd = to_timestamp(:getTrialEnd), " +
            "trialStart = to_timestamp(:getTrialStart) " +
            "RETURNING id"
    )
    @GetGeneratedKeys
    Integer update(@BindMethods Membership membership);

    /**
     * Delete a membership
     * @param id
     */
    @SqlUpdate("DELETE FROM memberships WHERE id = :id")
    void delete(@Bind("id") Integer id);
}
