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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.api.Order;
import org.dataone.bookkeeper.api.OrderItem;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OrderMapper implements RowMapper<Order> {

    /**
     * Construct an OrderMapper
     */
    public OrderMapper() {

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
    public Order map(ResultSet rs, StatementContext ctx) throws SQLException {

        ObjectMapper mapper = Jackson.newObjectMapper();
        /* The order to return */
        Order order;
        /* The list of order items */
        List<OrderItem> items = new LinkedList<OrderItem>();
        /* The list of items as a JSON array */
        ArrayNode itemsArray;

        try {
            itemsArray =
                (ArrayNode) mapper.readTree(rs.getString("o_items"));

            Iterator<JsonNode> iterator = itemsArray.elements();
            while ( iterator.hasNext() ) {
                ObjectNode item = (ObjectNode) iterator.next();
                items.add(mapper.readValue(item.toString(), OrderItem.class));
            }
            order = new Order(
                new Integer(rs.getInt("o_id")),
                rs.getString("o_object"),
                new Integer(rs.getInt("o_amount")),
                new Integer(rs.getInt("o_amountReturned")),
                rs.getString("o_charge") != null ?
                    (ObjectNode) mapper.readTree(rs.getString("o_charge")) : null,
                new Integer(rs.getInt("o_created")),
                rs.getString("o_currency"),
                rs.getString("o_subject"),
                new Integer(rs.getInt("o_customer")),
                rs.getString("o_email"),
                items,
                rs.getString("o_metadata") != null ?
                    (ObjectNode) mapper.readTree(rs.getString("o_metadata")) : null,
                rs.getString("o_name"),
                rs.getString("o_status"),
                rs.getString("o_statusTransitions") != null ?
                    (ObjectNode) mapper.readTree(rs.getString("o_statusTransitions")) : null,
                new Integer(rs.getInt("o_updated")),
                rs.getString("o_seriesId"),
                new Integer(rs.getInt("o_startDate")),
                new Integer(rs.getInt("o_endDate")),
                null // let the row reducer populate quotas

            );
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return order;
    }
}
