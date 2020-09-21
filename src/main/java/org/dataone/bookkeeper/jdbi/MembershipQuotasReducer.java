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

import org.dataone.bookkeeper.api.Membership;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.jdbi.mappers.MembershipMapper;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;

import java.util.LinkedList;
import java.util.Map;

/**
 * Row reducer that accumulates multiple quotas associated
 * with a membership into a list based on a SQL join between
 * the memberships and quotas tables.
 */
@RegisterBeanMapper(value = Quota.class)
@RegisterRowMapper(value = MembershipMapper.class)
public class MembershipQuotasReducer implements LinkedHashMapRowReducer<Integer, Membership> {

    /**
     * Accumulate quotas into a list in the membership instance
     * @param map The map of membership id to membership instances
     * @param rowView The view of the result set row from the joined tables
     */
    @Override
    public void accumulate(Map<Integer, Membership> map, RowView rowView) {
        // Build a membership from the resultset if one isn't in the map given the id
        Membership membership =
            map.computeIfAbsent(rowView.getColumn("s_id", Integer.class),
            id -> rowView.getRow(Membership.class));

        // Otherwise, for the same membership id, add quotas to the quota list
        if ( rowView.getColumn("s_id", Integer.class) != null ) {
            if ( membership.getQuotas() == null ) {
                membership.setQuotas(new LinkedList<Quota>());
            }
            membership.getQuotas().add(rowView.getRow(Quota.class));
        }
    }
}
