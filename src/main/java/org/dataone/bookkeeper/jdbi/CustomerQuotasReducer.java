package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.Quota;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;

import java.util.LinkedList;
import java.util.Map;

/**
 * Row reducer that accumulates multiple quotas associated
 * with a customer into a list based on a SQL join between
 * the customers and quotas tables.
 */
@RegisterBeanMapper(value = Quota.class)
public class CustomerQuotasReducer implements LinkedHashMapRowReducer<Integer, Customer> {

    /**
     * Accumulate quotas into a list in the customer instance
     * @param map The map of customer id to customer instances
     * @param rowView The view of the result set row from the joined tables
     */
    @Override
    public void accumulate(Map<Integer, Customer> map, RowView rowView) {
        // Build a customer from the resultset if one isn't in the map given the id
        Customer customer =
            map.computeIfAbsent(rowView.getColumn("c_id", Integer.class),
            id -> rowView.getRow(Customer.class));

        // Otherwise, for the same customer id, add quotas to the quota list
        if ( rowView.getColumn("id", Integer.class) != null ) {
            if ( customer.getQuotas() == null ) {
                customer.setQuotas(new LinkedList<Quota>());
            }
            customer.getQuotas().add(rowView.getRow(Quota.class));
        }
    }
}
