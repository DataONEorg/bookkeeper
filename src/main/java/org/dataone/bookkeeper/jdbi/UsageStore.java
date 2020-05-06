/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2020. All rights reserved.
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

import org.dataone.bookkeeper.api.Usage;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;

@RegisterBeanMapper(Usage.class)
public interface UsageStore {

    /** The query used to find all usages */
    String SELECT_CLAUSE =
        "SELECT " +
            "u.id, " +
            "u.object, " +
            "u.quotaId, " +
            "u.instanceId, " +
            "u.quantity, " +
            "u.status " +
        "FROM usages u " +
        "INNER JOIN quotas q ON u.quotaId = q.id ";

    /** The full query */
    String SELECT_ALL = SELECT_CLAUSE;

    /** Select by identifier */
    String SELECT_ONE = SELECT_CLAUSE + "WHERE u.id = :id";

    /** Select by name and instance identifer */
    String SELECT_BY_NAME_AND_INSTANCE_ID = SELECT_CLAUSE +
        "WHERE u.instanceId = :instanceId " +
        "AND q.name = :quotaName";

    // TODO: Define interfaces

}
