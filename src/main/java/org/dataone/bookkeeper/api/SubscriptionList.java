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

import java.util.List;

/**
 * A list of subscriptions used as a representation response
 */
public class SubscriptionList extends BaseList {

    private List<Subscription> subscriptions;

    /**
     * Construct an empty subscription list
     */
    public SubscriptionList(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    /**
     * Get the subscriptions list
     * @return subscriptions  the subscriptions list
     */
    @JsonProperty
    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    /**
     * Set the subscriptions list
     * @param subscriptions  the subscriptions list
     */
    @JsonProperty
    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
