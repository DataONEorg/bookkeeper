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

package org.dataone.bookkeeper.jdbi.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.api.Address;
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.Quota;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Maps customer rows to Customer instances. We use this since the JSON
 * fields within a Customer instance are not handled by BeanMapper
 */
public class CustomerMapper implements RowMapper<Customer> {

    /**
     * Construct a CustomerMapper
     */
    public CustomerMapper() {
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
    public Customer map(ResultSet rs, StatementContext ctx) throws SQLException {
        Customer customer = null;
        ObjectMapper mapper = Jackson.newObjectMapper();

        try {
            // Create a customer instance from the resultset
            customer = new Customer(
                Integer.valueOf(rs.getInt("c_id")),
                rs.getString("c_object"),
                rs.getString("c_subject"),
                Integer.valueOf(rs.getInt("c_balance")),
                mapper.readValue(rs.getString("c_address"), Address.class),
                Integer.valueOf(rs.getInt("c_created")),
                rs.getString("c_currency"),
                rs.getBoolean("c_delinquent"),
                rs.getString("c_description"),
                rs.getString("c_discount") != null ?
                    (ObjectNode) mapper.readTree(rs.getString("c_discount")) : null,
                rs.getString("c_email"),
                rs.getString("c_invoicePrefix"),
                rs.getString("c_invoiceSettings") != null ?
                    (ObjectNode) mapper.readTree(rs.getString("c_invoiceSettings")) : null,
                rs.getString("c_metadata") != null ?
                    (ObjectNode) mapper.readTree(rs.getString("c_metadata")) : null,
                rs.getString("c_givenName"),
                rs.getString("c_surName"),
                rs.getString("c_phone")
            );
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return customer;
    }
}
