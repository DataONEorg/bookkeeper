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
        /** The order to return */
        Order order;
        /** The list of order items */
        List<OrderItem> items = new LinkedList<OrderItem>();
        /** The list of items as a JSON array */
        ArrayNode itemsArray;

        try {
            itemsArray =
                (ArrayNode) Jackson.newObjectMapper().readTree(rs.getString("items"));

            Iterator<JsonNode> iterator = itemsArray.elements();
            while ( iterator.hasNext() ) {
                ObjectNode item = (ObjectNode) iterator.next();
                items.add(mapper.readValue(item.toString(), OrderItem.class));
            }
            order = new Order(
                new Integer(rs.getInt("id")),
                rs.getString("object"),
                new Integer(rs.getInt("amount")),
                new Integer(rs.getInt("amountReturned")),
                (ObjectNode) mapper.readTree(rs.getString("charge")),
                new Integer(rs.getInt("created")),
                rs.getString("currency"),
                new Integer(rs.getInt("customer")),
                rs.getString("email"),
                items,
                (ObjectNode) mapper.readTree(rs.getString("metadata")),
                rs.getString("status"),
                (ObjectNode) mapper.readTree(rs.getString("statusTransitions")),
                new Integer(rs.getInt("updated"))
            );
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return order;
    }
}