/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2019. All rights reserved.
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

import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

public class Subscription {

    /* The subscription unique identifier */
    @NotNull
    @NotEmpty
    private String id;

    /* The subscription object type */
    @NotNull
    @NotEmpty
    @Pattern(regexp = "subscription")
    private String object;

    /* The subscription billing cycle anchor timestamp (seconds since the epoch)
     * Determines the date of the first full invoice.
     * For products with month or year intervals,
     * the day of the month for subsequent invoices
     */
    @NotNull
    @NotEmpty
    private Integer billingCycleAnchor;

    /* The subscription cancellation timestamp (seconds since the epoch) */
    private Integer canceledAt;

    /* The subscription collection method */
    @NotNull
    @NotEmpty
    @Pattern(regexp = "charge_automatically|send_invoice")
    private String collectionMethod;

    /* The subscription creation timestamp (seconds since the epoch) */
    @NotNull
    @NotEmpty
    private Integer created;

    /* The subscription current period end timestamp (seconds since the epoch) */
    private Integer currentPeriodEnd;

    /* The subscription current period start timestamp (seconds since the epoch) */
    private Integer currentPeriodStart;

    /* The subscription customer identifier */
    @NotNull
    @NotEmpty
    private Integer customer;

    /* The subscription days until due */
    private Integer daysUntilDue;

    /* The subscription discount */
    private ObjectNode discount;

    /* The subscription end timestamp (seconds since the epoch) */
    private Integer endedAt;

    /* The subscription latest invoice identifier */
    private Integer latestInvoice;

    /* The subscription metadata object */
    private ObjectNode metadata;

    /* The subscribed product */
    @NotNull
    @NotEmpty
    private Product product;

    /* The subscription quantity of the product */
    @NotNull
    @NotEmpty
    @Min(1)
    private Integer quantity;

    /* The subscription start date timestamp (seconds since the epoch) */
    private Integer startDate;

    /* The subscription status
    * incomplete - attempt to collect automatically fails
    * incomplete_expired - first invoice is not paid in X days
    * trialing - in trial period
    * active - paid and out of trial period
    * past_due - latest invoice is not paid in X days
    * canceled - has been canceled (by customer or being unpaid)
    * unpaid - still unpaid after X past_due cycles
    */
    @NotNull
    @NotEmpty
    @Pattern(regexp = "incomplete|incomplete_expired|trialing|active|past_due|canceled|unpaid")
    private String status;

    /* The subscription trial end timestamp (seconds since the epoch) */
    private Integer trialEnd;

    /* The subscription trial start timestamp (seconds since the epoch) */
    private Integer trialStart;

    /* The quotas associated with the product, if any */
    private List<Quota> quotas;
    /*
     * Construct an empty subscription.
     */
    public Subscription() {

    }
}
