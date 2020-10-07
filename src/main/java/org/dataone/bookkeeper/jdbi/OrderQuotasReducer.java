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

package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.api.Order;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.jdbi.mappers.OrderMapper;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;

import java.util.LinkedList;
import java.util.Map;

/**
 * Row reducer that accumulates multiple quotas associated
 * with an order into a list based on a SQL join between
 * the orders and quotas tables.
 */
@RegisterBeanMapper(value = Quota.class)
@RegisterRowMapper(value = OrderMapper.class)
public class OrderQuotasReducer implements LinkedHashMapRowReducer<Integer, Order> {

    /**
     * Accumulate quotas into a list in the order instance
     * @param map The map of order id to order instances
     * @param rowView The view of the result set row from the joined tables
     */
    @Override
    public void accumulate(Map<Integer, Order> map, RowView rowView) {
        // Build an order from the resultset if one isn't in the map given the id
        Order order =
            map.computeIfAbsent(rowView.getColumn("o_id", Integer.class),
            id -> rowView.getRow(Order.class));

        // Otherwise, for the same order id, add quotas to the quota list
        if ( rowView.getColumn("o_id", Integer.class) != null ) {
            // Ensure quotas are present before adding the list
            boolean hasQuotas = rowView.getColumn("q_id", Integer.class) != null;
            if ( hasQuotas && order.getQuotas() == null ) {
                order.setQuotas(new LinkedList<Quota>());
            }
            // When present, add the quota
            if ( hasQuotas ) {
                Quota quota = rowView.getRow(Quota.class);
                order.getQuotas().add(quota);
            }
        }
    }
}
