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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.*;
import org.dataone.bookkeeper.jdbi.MembershipQuotasReducer;
import org.dataone.bookkeeper.jdbi.mappers.ProductMapper;
import org.dataone.bookkeeper.jdbi.mappers.MembershipMapper;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * A delegate class with helper methods for manipulating the memberships table for testing
 */
public class MembershipHelper {

    /**
     * Insert a test membership with the given membership id
     * @param membershipId the membership id
     * @param customerId the customer id
     * @return membershipId the membership id
     */
    public static Integer insertTestMembership(Integer membershipId, Integer customerId) throws SQLException {

        BaseTestCase.dbi.useHandle(handle -> {
            handle.execute("INSERT INTO memberships (" +
                "id, " +
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
                    "?, " +
                    "?, " +
                    "to_timestamp(?), " +
                    "?, " +
                    "to_timestamp(?), " +
                    "?, " +
                    "?::json, " +
                    "?, " +
                    "?, " +
                    "to_timestamp(?), " +
                    "?, " +
                    "to_timestamp(?), " +
                    "to_timestamp(?) " +
                ")",
                membershipId,
                "membership",
                null,
                "send_invoice",
                1570486366,
                customerId,
                "{}",
                2,
                1,
                1570486366,
                "paid",
                1573078366,
                1570486366
            );
        });

        return membershipId;
    }

    /**
     * Insert a test membership given the Membership instance
     * @param membership
     * @return membership the inserted membership
     * @throws SQLException
     * @throws JsonProcessingException
     */
    public static Membership insertTestMembership(Membership membership)
        throws SQLException, JsonProcessingException {

        if (membership.getProduct() != null &&
            membership.getProduct().getId() != null &&
            membership.getCustomer() != null &&
            membership.getCustomer().getId() != null) {
            BaseTestCase.dbi.useHandle(handle ->
                handle.execute("INSERT INTO memberships (" +
                    "id, " +
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
                        "?, " +
                        "?, " +
                        "to_timestamp(?), " +
                        "?, " +
                        "to_timestamp(?), " +
                        "?, " +
                        "?::json, " +
                        "?, " +
                        "?, " +
                        "to_timestamp(?), " +
                        "?, " +
                        "to_timestamp(?), " +
                        "to_timestamp(?) " +
                    ")",
                    membership.getId(),
                    membership.getObject(),
                    membership.getCanceledAt(),
                    membership.getCollectionMethod(),
                    membership.getCreated(),
                    membership.getCustomer().getId(),
                    Jackson.newObjectMapper().writeValueAsString(membership.getMetadata()),
                    membership.getProduct().getId(),
                    membership.getQuantity(),
                    membership.getStartDate(),
                    membership.getStatus(),
                    membership.getTrialEnd(),
                    membership.getTrialStart()
                )
            );
        } else {
            throw new SQLException("Memberships must include a valid product and customer id.");
        }
        return membership;
    }

    /**
     * Create a membership for unit tests
     * @param membershipId the membership id
     * @return membership the Membership instance
     */
    public static Membership createMembership(Integer membershipId, Integer customerId, Integer productId) {

        // Create and insert a product to include in the membership
        Product product = ProductHelper.createTestProduct(productId);
        ProductHelper.insertTestProduct(product);

        // Create a customer for the membership
        Customer customer = CustomerHelper.createCustomer(customerId);

        // Extract quotas from the product features
        List<Quota> quotas = new LinkedList<Quota>();
        ArrayNode features = (ArrayNode) product.getMetadata().get("features");
        Feature feature;
        for (JsonNode jsonNode : features) {
            feature = Jackson.newObjectMapper().convertValue(jsonNode, Feature.class);
            Quota quota = feature.getQuota();
            if ( quota != null ) {
                quota.setMembershipId(membershipId);
                quota.setOwner(customer.getSubject());
                quota.setTotalUsage(0.0);
            }
            quotas.add(quota);
        }

        // Build the membership
        Membership membership = new Membership(
            membershipId,
            "membership",
            null,
            "send_invoice",
            new Integer(1570486366),
            customer,
            Jackson.newObjectMapper().createObjectNode(),
            product,
            new Integer(1),
            new Integer(1570486366),
            "trialing",
            new Integer(1573078366),
            new Integer(1570486366),
            quotas
        );
        return membership;
    }

    /**
     * Remove a test membership given its id
     * @param membershipId
     */
    public static void removeTestMembership(Integer membershipId) throws SQLException {

        BaseTestCase.dbi.useHandle(handle ->
            handle.execute("DELETE FROM memberships WHERE id = ?", membershipId)
        );
    }

    /**
     * Get a membership given its id
     * @param membershipId
     * @return membership the membership of the given id
     */
    public static Membership getMembershipById(Integer membershipId) {
        Membership membership = null;
        Product product = null;
        membership = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery(
                "SELECT " +
                    "s.id AS s_id, " +
                    "s.object AS s_object, " +
                    "date_part('epoch', s.canceledAt)::int AS s_canceledAt, " +
                    "s.collectionMethod AS s_collectionMethod, " +
                    "date_part('epoch', s.created)::int AS s_created, " +
                    "s.customerId AS s_customerId, " +
                    "s.metadata::json AS s_metadata, " +
                    "s.productId AS s_productId, " +
                    "p.id AS p_id, " +
                    "p.object AS p_object, " +
                    "p.active AS p_active, " +
                    "p.name AS p_name, " +
                    "p.caption AS p_caption, " +
                    "p.description AS p_description, " +
                    "p.created AS p_created, " +
                    "p.statementDescriptor AS p_statementDescriptor, " +
                    "p.type AS p_type, " +
                    "p.unitLabel AS p_unitLabel, " +
                    "p.url AS p_url, " +
                    "p.metadata AS p_metadata, " +
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
                "LEFT JOIN products p ON s.productId = p.id " +
                "WHERE s.id = :id")
                .bind("id", membershipId)
                .registerRowMapper(new MembershipMapper())
                .registerRowMapper(new ProductMapper())
                .registerRowMapper(BeanMapper.factory(Quota.class, "q"))
                .reduceRows(new MembershipQuotasReducer())
                .findFirst()
                .get()
                //.map(new MembershipMapper()).
                //.one()
        );
        return membership;
    }

    /**
     * Return the number of memberships given the customer id
     * @param membershipId the id of the customer
     * @return count the number of memberships
     */
    public static Integer getMembershipCountById(Integer membershipId) {
        Integer count = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT count(*) FROM memberships WHERE id = :id")
                .bind("id", membershipId)
                .mapTo(Integer.class)
                .one()
        );
        return count;
    }
}
