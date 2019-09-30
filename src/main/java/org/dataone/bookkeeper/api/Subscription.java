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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Subscriptions represent a Product purchased by a Customer, charged for periodically.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
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
        super();
    }

    /**
     * Construct a subscription
     * @param object
     * @param billingCycleAnchor
     * @param canceledAt
     * @param collectionMethod
     * @param created
     * @param currentPeriodEnd
     * @param currentPeriodStart
     * @param customer
     * @param daysUntilDue
     * @param discount
     * @param endedAt
     * @param latestInvoice
     * @param metadata
     * @param product
     * @param quantity
     * @param startDate
     * @param status
     * @param trialEnd
     * @param trialStart
     * @param quotas
     */
    public Subscription(
        @NotNull @NotEmpty @Pattern(regexp = "subscription") String object,
        @NotNull @NotEmpty Integer billingCycleAnchor, Integer canceledAt,
        @NotNull @NotEmpty @Pattern(regexp = "charge_automatically|send_invoice") String collectionMethod,
        @NotNull @NotEmpty Integer created, Integer currentPeriodEnd, Integer currentPeriodStart,
        @NotNull @NotEmpty Integer customer, Integer daysUntilDue, ObjectNode discount, Integer endedAt,
        Integer latestInvoice, ObjectNode metadata, @NotNull @NotEmpty Product product,
        @NotNull @NotEmpty @Min(1) Integer quantity, Integer startDate,
        @NotNull @NotEmpty @Pattern(regexp = "incomplete|incomplete_expired|trialing|active|past_due|canceled|unpaid") String status,
        Integer trialEnd, Integer trialStart, List<Quota> quotas) {
        this.object = object;
        this.billingCycleAnchor = billingCycleAnchor;
        this.canceledAt = canceledAt;
        this.collectionMethod = collectionMethod;
        this.created = created;
        this.currentPeriodEnd = currentPeriodEnd;
        this.currentPeriodStart = currentPeriodStart;
        this.customer = customer;
        this.daysUntilDue = daysUntilDue;
        this.discount = discount;
        this.endedAt = endedAt;
        this.latestInvoice = latestInvoice;
        this.metadata = metadata;
        this.product = product;
        this.quantity = quantity;
        this.startDate = startDate;
        this.status = status;
        this.trialEnd = trialEnd;
        this.trialStart = trialStart;
        this.quotas = quotas;
    }
}
