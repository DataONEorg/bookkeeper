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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    /**
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

    /**
     * Get the subscription identifier
     * @return
     */
    @JsonProperty
    public String getId() {
        return id;
    }

    /**
     * Get the subscription identifier
     * @param id
     */
    @JsonProperty
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the subscription object type
     * @return
     */
    @JsonProperty
    public String getObject() {
        return object;
    }

    /**
     * Set the subscription object type
     * @param object
     */
    @JsonProperty
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Get the subscription billing cycle anchor timestamp (seconds since the epoch)
     * @return
     */
    @JsonProperty
    public Integer getBillingCycleAnchor() {
        return billingCycleAnchor;
    }

    /**
     * Set the subscription billing cycle anchor timestamp (seconds since the epoch)
     * @param billingCycleAnchor
     */
    @JsonProperty
    public void setBillingCycleAnchor(Integer billingCycleAnchor) {
        this.billingCycleAnchor = billingCycleAnchor;
    }

    /**
     * Get the billing cycle anchor date as an ISO 8601 timestamp string
     * @return
     */
    public String getBillingCycleAnchorTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date(getBillingCycleAnchor().intValue() * 1000));
    }

    /**
     * Get the subscription canceled timestamp (seconds since the epoch)
     * @return
     */
    @JsonProperty
    public Integer getCanceledAt() {
        return canceledAt;
    }

    /**
     * Set the subscription canceled timestamp (seconds since the epoch)
     * @param canceledAt
     */
    @JsonProperty
    public void setCanceledAt(Integer canceledAt) {
        this.canceledAt = canceledAt;
    }

    /**
     * Get the canceled at date as an ISO 8601 timestamp string
     * @return
     */
    public String getCanceledAtTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date(getCanceledAt().intValue() * 1000));
    }

    /**
     * Get the subscription collection method
     * @return
     */
    @JsonProperty
    public String getCollectionMethod() {
        return collectionMethod;
    }

    /**
     * Set the subscription collection method
     * @param collectionMethod
     */
    @JsonProperty
    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    /**
     * Get the subscription creation timestamp (seconds since the epoch)
     * @return
     */
    @JsonProperty
    public Integer getCreated() {
        return created;
    }

    /**
     * Set the subscription creation timestamp (seconds since the epoch)
     * @param created
     */
    @JsonProperty
    public void setCreated(Integer created) {
        this.created = created;
    }

    /**
     * Get the creation date as an ISO 8601 timestamp string
     * @return
     */
    public String getCreatedTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date(getCreated().intValue() * 1000));
    }

    /**
     * Get the subscription period end timestamp (seconds since the epoch)
     * @return
     */
    @JsonProperty
    public Integer getCurrentPeriodEnd() {
        return currentPeriodEnd;
    }

    /**
     * Set the subscription period end timestamp (seconds since the epoch)
     * @param currentPeriodEnd
     */
    @JsonProperty
    public void setCurrentPeriodEnd(Integer currentPeriodEnd) {
        this.currentPeriodEnd = currentPeriodEnd;
    }

    /**
     * Get the currentperiod end date as an ISO 8601 timestamp string
     * @return
     */
    public String getCurrentPeriodEndTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date(getCurrentPeriodEnd().intValue() * 1000));
    }

    /**
     * Get the subscription period start timestamp (seconds since the epoch)
     * @return
     */
    @JsonProperty
    public Integer getCurrentPeriodStart() {
        return currentPeriodStart;
    }

    /**
     * Set the subscription period start timestamp (seconds since the epoch)
     * @param currentPeriodStart
     */
    @JsonProperty
    public void setCurrentPeriodStart(Integer currentPeriodStart) {
        this.currentPeriodStart = currentPeriodStart;
    }

    /**
     * Get the creation date as an ISO 8601 timestamp string
     * @return
     */
    public String getCurrentPeriodStartTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date(getCurrentPeriodStart().intValue() * 1000));
    }

    /**
     * Get the subscription customer identifier
     * @return
     */
    @JsonProperty
    public Integer getCustomer() {
        return customer;
    }

    /**
     * Set the subscription customer identifier
     * @param customer
     */
    @JsonProperty
    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    /**
     * Get the subscription days until due
     * @return
     */
    @JsonProperty
    public Integer getDaysUntilDue() {
        return daysUntilDue;
    }

    /**
     * Set the subscription days until due
     * @param daysUntilDue
     */
    @JsonProperty
    public void setDaysUntilDue(Integer daysUntilDue) {
        this.daysUntilDue = daysUntilDue;
    }

    /**
     * Get the subscription discount
     * @return
     */
    @JsonProperty
    public ObjectNode getDiscount() {
        return discount;
    }

    /**
     * Set the subscription discount
     * @param discount
     */
    @JsonProperty
    public void setDiscount(ObjectNode discount) {
        this.discount = discount;
    }

    /**
     * Return the discount hash as a JSON string
     * @return discount the discount object hash
     */
    public String getDiscountJSON() throws JsonProcessingException {
        if ( discount != null ) {
            return Jackson.newObjectMapper().writeValueAsString(getDiscount());
        } else {
            return "{}";
        }
    }

    /**
     * Get the subscription end timestamp (seconds since the epoch)
     * @return
     */
    @JsonProperty
    public Integer getEndedAt() {
        return endedAt;
    }

    /**
     * Set the subscription end timestamp (seconds since the epoch)
     * @param endedAt
     */
    @JsonProperty
    public void setEndedAt(Integer endedAt) {
        this.endedAt = endedAt;
    }

    /**
     * Get the ended at date as an ISO 8601 timestamp string
     * @return
     */
    public String getEndedAtTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date(getEndedAt().intValue() * 1000));
    }

    /**
     * Get the subscription latest invoice identifier
     * @return
     */
    @JsonProperty
    public Integer getLatestInvoice() {
        return latestInvoice;
    }

    /**
     * Set the subscription latest invoice identifier
     * @param latestInvoice
     */
    @JsonProperty
    public void setLatestInvoice(Integer latestInvoice) {
        this.latestInvoice = latestInvoice;
    }

    /**
     * Get the subscription metadata
     * @return
     */
    @JsonProperty
    public ObjectNode getMetadata() {
        return metadata;
    }

    /**
     * Set the subscription metadata
     * @param metadata
     */
    @JsonProperty
    public void setMetadata(ObjectNode metadata) {
        this.metadata = metadata;
    }

    /**
     * Return the metadata hash as a JSON string
     * @return metadata the metadata object hash
     * @throws JsonProcessingException
     */

    public String getMetadataJSON() throws JsonProcessingException {
        if ( metadata != null ) {
            return Jackson.newObjectMapper().writeValueAsString(getMetadata());
        } else {
            return "{}";
        }
    }

    /**
     * Get the subscription product
     * @return
     */
    @JsonProperty
    public Product getProduct() {
        return product;
    }

    /**
     * Set the subscription product
     * @param product
     */
    @JsonProperty
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Get the subscription quantity of the product
     * @return
     */
    @JsonProperty
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Set the subscription quantity of the product
     * @param quantity
     */
    @JsonProperty
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Get the subscription start timestamp (seconds since the epoch)
     * @return
     */
    @JsonProperty
    public Integer getStartDate() {
        return startDate;
    }

    /**
     * Set the subscription start timestamp (seconds since the epoch)
     * @param startDate
     */
    @JsonProperty
    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the start date as an ISO 8601 timestamp string
     * @return
     */
    public String getStartDateTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date(getStartDate().intValue() * 1000));
    }

    /**
     * Get the subscription status
     * @return
     */
    @JsonProperty
    public String getStatus() {
        return status;
    }

    /**
     * Set the subscription status
     * @param status
     */
    @JsonProperty
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the subscription trial end timestamp (seconds since the epoch)
     * @return
     */
    @JsonProperty
    public Integer getTrialEnd() {
        return trialEnd;
    }

    /**
     * Set the subscription trial end timestamp (seconds since the epoch)
     * @param trialEnd
     */
    @JsonProperty
    public void setTrialEnd(Integer trialEnd) {
        this.trialEnd = trialEnd;
    }

    /**
     * Get the trial end date as an ISO 8601 timestamp string
     * @return
     */
    public String getTrialEndTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date(getTrialEnd().intValue() * 1000));
    }

    /**
     * Get the subscription trial start timestamp (seconds since the epoch)
     * @return
     */
    @JsonProperty
    public Integer getTrialStart() {
        return trialStart;
    }

    /**
     * Set the subscription trial start timestamp (seconds since the epoch)
     * @param trialStart
     */
    @JsonProperty
    public void setTrialStart(Integer trialStart) {
        this.trialStart = trialStart;
    }

    /**
     * Get the trial start date as an ISO 8601 timestamp string
     * @return
     */
    public String getTrailStartTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date(getTrialStart().intValue() * 1000));
    }

    /**
     * Get the subscription quotas
     * @return
     */
    @JsonProperty
    public List<Quota> getQuotas() {
        return quotas;
    }

    /**
     * Set the subscription quotas
     * @param quotas
     */
    @JsonProperty
    public void setQuotas(List<Quota> quotas) {
        this.quotas = quotas;
    }

    /**
     * Determine object equality based on the equality of all fields
     * @param o the object to compare
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(getId(), that.getId()) &&
            Objects.equals(getObject(), that.getObject()) &&
            Objects.equals(getBillingCycleAnchor(), that.getBillingCycleAnchor()) &&
            Objects.equals(getCanceledAt(), that.getCanceledAt()) &&
            Objects.equals(getCollectionMethod(), that.getCollectionMethod()) &&
            Objects.equals(getCreated(), that.getCreated()) &&
            Objects.equals(getCurrentPeriodEnd(), that.getCurrentPeriodEnd()) &&
            Objects.equals(getCurrentPeriodStart(), that.getCurrentPeriodStart()) &&
            Objects.equals(getCustomer(), that.getCustomer()) &&
            Objects.equals(getDaysUntilDue(), that.getDaysUntilDue()) &&
            Objects.equals(getDiscount(), that.getDiscount()) &&
            Objects.equals(getEndedAt(), that.getEndedAt()) &&
            Objects.equals(getLatestInvoice(), that.getLatestInvoice()) &&
            Objects.equals(getMetadata(), that.getMetadata()) &&
            Objects.equals(getProduct(), that.getProduct()) &&
            Objects.equals(getQuantity(), that.getQuantity()) &&
            Objects.equals(getStartDate(), that.getStartDate()) &&
            Objects.equals(getStatus(), that.getStatus()) &&
            Objects.equals(getTrialEnd(), that.getTrialEnd()) &&
            Objects.equals(getTrialStart(), that.getTrialStart()) &&
            Objects.equals(getQuotas(), that.getQuotas());
    }

    /**
     * Calculate a hash based on all fields
     * @return
     */
    @Override
    public int hashCode() {

        return Objects.hash(getId(), getObject(), getBillingCycleAnchor(), getCanceledAt(),
            getCollectionMethod(), getCreated(), getCurrentPeriodEnd(), getCurrentPeriodStart(),
            getCustomer(), getDaysUntilDue(), getDiscount(), getEndedAt(), getLatestInvoice(),
            getMetadata(), getProduct(), getQuantity(), getStartDate(), getStatus(),
            getTrialEnd(), getTrialStart(), getQuotas());
    }
}
