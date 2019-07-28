package org.dataone.bookkeeper.jdbi.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.api.Address;
import org.dataone.bookkeeper.api.Customer;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    @RegisterBeanMapper(value = Customer.class, prefix = "c") // TODO: Does this work?
    public Customer map(ResultSet rs, StatementContext ctx) throws SQLException {
        Customer customer;
        ObjectMapper mapper = Jackson.newObjectMapper();
        try {
            customer = new Customer(
                new Integer(rs.getInt("id")),
                rs.getString("object"),
                rs.getString("orcid"),
                new Integer(rs.getInt("balance")),
                (Address) mapper.readValue(rs.getString("address"), Address.class),
                new Integer(rs.getInt("created")),
                rs.getString("currency"),
                rs.getBoolean("delinquent"),
                rs.getString("description"),
                (ObjectNode) mapper.readTree(rs.getString("discount")),
                rs.getString("email"),
                rs.getString("invoiceprefix"),
                (ObjectNode) mapper.readTree(rs.getString("invoicesettings")),
                (ObjectNode) mapper.readTree(rs.getString("metadata")),
                rs.getString("givenname"),
                rs.getString("surname"),
                rs.getString("phone"),
                (ObjectNode) mapper.readTree(rs.getString("quotas"))
            );
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return customer;
    }
}
