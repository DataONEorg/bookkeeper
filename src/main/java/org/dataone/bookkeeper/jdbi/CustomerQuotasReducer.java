package org.dataone.bookkeeper.jdbi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.jdbi.mappers.CustomerMapper;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.Map;

/**
 * Row reducer that accumulates multiple quotas associated
 * with a customer into a list based on a SQL join between
 * the customers and quotas tables.
 */
public class CustomerQuotasReducer implements LinkedHashMapRowReducer<Integer, Customer> {

    /**
     * Accumulate quotas into a list in the customer instance
     * @param map The map of customer id to customer instances
     * @param rowView The view of the result set row from the joined tables
     */
    @Override
    @UseRowMapper(CustomerMapper.class) // TODO: Does this work?
    @RegisterBeanMapper(value = Quota.class, prefix = "q")
    public void accumulate(Map<Integer, Customer> map, RowView rowView) {
        ObjectMapper mapper = Jackson.newObjectMapper();
        Customer customer =
            map.computeIfAbsent(rowView.getColumn("id", Integer.class),
            id -> rowView.getRow(Customer.class));

        if ( rowView.getColumn("q.id", Integer.class) != null ) {
            // TODO: Migrated to ObjectNode from List<Quota>, so fix this
            //customer.getQuotas().add(rowView.getRow(Quota.class));
            ObjectNode quotasObject = customer.getQuotas();
            if (quotasObject == null) {
                quotasObject = mapper.createObjectNode();
            }
            if (quotasObject.get("quotas") == null ) {
                quotasObject.set("quotas", mapper.createArrayNode());
            }
            ArrayNode quotas = (ArrayNode) customer.getQuotas().get("quotas");
            try {
                // TODO: Is converting the Quota to ObjectNode better here than string?
                quotas.add(mapper.writeValueAsString(rowView.getRow(Quota.class)));
            } catch (JsonProcessingException e) {
                e.printStackTrace(); // TODO: Decide how to throw or log
            }
        }
    }
}
