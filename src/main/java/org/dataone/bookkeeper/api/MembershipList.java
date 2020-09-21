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

package org.dataone.bookkeeper.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * A list of memberships used as a representation response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MembershipList extends BaseList {

    private List<Membership> memberships;

    /**
     * Construct an empty membership list
     */
    public MembershipList() {

    }

    /**
     * Construct a membership list
     */
    public MembershipList(List<Membership> memberships) {
        this.memberships = memberships;
    }

    /**
     * Get the memberships list
     * @return memberships  the memberships list
     */
    @JsonProperty
    public List<Membership> getMemberships() {
        return memberships;
    }

    /**
     * Set the memberships list
     * @param memberships  the memberships list
     */
    @JsonProperty
    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships;
    }
}
