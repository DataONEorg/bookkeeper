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
                rs.getString("name"),
                rs.getString("caption"),
                rs.getString("description"),
                new Integer(rs.getInt("created")),
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
