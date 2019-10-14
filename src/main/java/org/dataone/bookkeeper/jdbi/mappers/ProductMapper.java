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

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.api.Product;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps product rows to Product instances. We use this since the JSON
 * fields within a Product instance are not handled by BeanMapper
 */
public class ProductMapper implements RowMapper<Product> {

    /**
     * Construct a ProductMapper
     */
    public ProductMapper() {
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
    public Product map(ResultSet rs, StatementContext ctx) throws SQLException {
        Product product;
        try {
            product = new Product(
                new Integer(rs.getInt("id")),
                rs.getString("object"),
                rs.getBoolean("active"),
                rs.getInt("amount"),
                rs.getString("caption"),
                rs.getString("currency"),
                new Integer(rs.getInt("created")),
                rs.getString("description"),
                rs.getString("interval"),
                rs.getString("name"),
                rs.getString("statementDescriptor"),
                rs.getString("type"),
                rs.getString("unitLabel"),
                rs.getString("url"),
                (ObjectNode) Jackson.newObjectMapper().readTree(rs.getString("metadata"))
            );
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return product;
    }
}
