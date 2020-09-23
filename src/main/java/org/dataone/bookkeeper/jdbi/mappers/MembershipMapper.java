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

package org.dataone.bookkeeper.jdbi.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.Membership;
import org.dataone.bookkeeper.api.Product;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps membership rows to Membership instances. We use this since the JSON
 * fields within a Membership instance are not handled by BeanMapper
 */
public class MembershipMapper implements RowMapper<Membership> {

    /**
     * Construct a MembershipMapper
     */
    public MembershipMapper() {
    }

    /**
     * Map the current row of the result set.
     * This method should not cause the result set to advance; allow Jdbi to do that, please.
     *
     * @param rs  the result set being iterated
     * @param ctx the statement context
     * @return the value to produce for this row
     * @throws SQLException if anything goes wrong go ahead and let this percolate; Jdbi will handle it
     */
    @Override
    public Membership map(ResultSet rs, StatementContext ctx) throws SQLException {
        Membership membership = null;
        Product product = null;
        Customer customer = null;
        ObjectMapper mapper = Jackson.newObjectMapper();

        try {
            product = new ProductMapper().map(rs, ctx);

        } catch (PSQLException psqle) {
            // There are no products in the result, continue
        }

        try {
            customer = new CustomerMapper().map(rs, ctx);

        } catch (PSQLException psqle) {
            // There are no customers in the result, continue
        }

        try {
            // Create a membership instance from the resultset
            membership = new Membership(
                new Integer(rs.getInt("s_id")),
                rs.getString("s_object"),
                new Integer(rs.getInt("s_canceledAt")),
                rs.getString("s_collectionMethod"),
                new Integer(rs.getInt("s_created")),
                customer,
                rs.getString("s_metadata") != null ?
                    (ObjectNode) mapper.readTree(rs.getString("s_metadata")) : null,
                product,
                new Integer(rs.getInt("s_quantity")),
                new Integer(rs.getInt("s_startDate")),
                rs.getString("s_status"),
                new Integer(rs.getInt("s_trialEnd")),
                new Integer(rs.getInt("s_trialStart")),
                null // let the row reducer populate quotas
            );
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return membership;
    }
}
