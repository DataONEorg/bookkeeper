package org.dataone.bookkeeper.jdbi.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.api.Address;
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.Quota;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

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
        Quota quota = null;
        List<Quota> quotas = new LinkedList<Quota>();
        ObjectMapper mapper = Jackson.newObjectMapper();

        try {

            // If we have quota fields in the resultset, build and add a quota
            if (new Integer(rs.getInt("id")) != null ) {
                quota = new Quota(
                    new Integer(rs.getInt("id")),
                    rs.getString("object"),
                    rs.getString("name"),
                    new Integer(rs.getInt("softLimit")),
                    new Integer(rs.getInt("hardLimit")),
                    rs.getString("unit"),
                    new Integer(rs.getInt("c_id"))
                    );
                quotas.add(quota);
            }

            // Create a customer instance from the resultset
            customer = new Customer(
                new Integer(rs.getInt("c_id")),
                rs.getString("c_object"),
                rs.getString("c_orcid"),
                new Integer(rs.getInt("c_balance")),
                mapper.readValue(rs.getString("c_address"), Address.class),
                new Integer(rs.getInt("c_created")),
                rs.getString("c_currency"),
                rs.getBoolean("c_delinquent"),
                rs.getString("c_description"),
                (ObjectNode) mapper.readTree(rs.getString("c_discount")),
                rs.getString("c_email"),
                rs.getString("c_invoicePrefix"),
                (ObjectNode) mapper.readTree(rs.getString("c_invoiceSettings")),
                (ObjectNode) mapper.readTree(rs.getString("c_metadata")),
                rs.getString("c_givenName"),
                rs.getString("c_surName"),
                rs.getString("c_phone"),
                quotas
            );
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return customer;
    }
}
