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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Integer id;

    /* The subscription object type */
    @NotNull
    @NotEmpty
    @Pattern(regexp = "subscription")
    private String object;

    /* The subscription billing cycle anchor timestamp (seconds since the epoch)
     * Determines the date of the first full invoice.
     * For products with month or year intervals,
     * the day of the month for subsequent invoices
    @NotNull
    @NotEmpty
    private Integer billingCycleAnchor;
    */

    /* The subscription cancellation timestamp (seconds since the epoch) */
    private Integer canceledAt;

    /* The subscription collection method */
    @NotNull
    @NotEmpty
    @Pattern(regexp = "charge_automatically|send_invoice")
    private String collectionMethod;

    /* The subscription creation timestamp (seconds since the epoch) */
    private Integer created;

    /* The subscription current period end timestamp (seconds since the epoch)
    private Integer currentPeriodEnd;
    */

    /* The subscription current period start timestamp (seconds since the epoch)
    private Integer currentPeriodStart;
    */

    /* The subscription customer identifier */
    @NotNull
    @NotEmpty
    private Integer customerId;

    /* The subscription days until due
    private Integer daysUntilDue;
    */

    /* The subscription discount
    private ObjectNode discount;
    */

    /* The subscription end timestamp (seconds since the epoch)
    private Integer endedAt;
    */

    /* The subscription latest invoice identifier
    private Integer latestInvoice;
    */

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
     * @param canceledAt
     * @param collectionMethod
     * @param created
     * @param customerId
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
        Integer id,
        @NotNull @NotEmpty @Pattern(regexp = "subscription") String object,
        Integer canceledAt,
        @NotNull @NotEmpty @Pattern(regexp = "charge_automatically|send_invoice") String collectionMethod,
        Integer created,
        @NotNull Integer customerId,
        ObjectNode metadata,
        @NotNull @NotEmpty Product product,
        @NotNull @Min(1) Integer quantity,
        Integer startDate,
        @NotNull @NotEmpty @Pattern(regexp = "incomplete|incomplete_expired|trialing|active|past_due|canceled|unpaid") String status,
        Integer trialEnd,
        Integer trialStart,
        List<Quota> quotas) {
        this.id = id;
        this.object = object;
        this.canceledAt = canceledAt;
        this.collectionMethod = collectionMethod;
        this.created = created;
        this.customerId = customerId;
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
    public Integer getId() {
        return id;
    }

    /**
     * Get the subscription identifier
     * @param id
     */
    @JsonProperty
    public void setId(Integer id) {
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
        return formatter.format(new Date((long) getCanceledAt().intValue() * 1000));
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
        return formatter.format(new Date((long) getCreated().intValue() * 1000));
    }

    /**
     * Get the subscription customer identifier
     * @return
     */
    @JsonProperty
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * Set the subscription customer identifier
     * @param customerId
     */
    @JsonProperty
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
     * Get the product id
     * @return
     */
    public Integer getProductId() {
        Integer productId = null;
        if ( this.product != null ) {
            productId = this.product.getId();
        }
        return productId;
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
        return formatter.format(new Date((long) getStartDate().intValue() * 1000));
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
        return formatter.format(new Date((long) getTrialEnd().intValue() * 1000));
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
        return formatter.format(new Date((long) getTrialStart().intValue() * 1000));
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
            Objects.equals(getCanceledAt(), that.getCanceledAt()) &&
            Objects.equals(getCollectionMethod(), that.getCollectionMethod()) &&
            Objects.equals(getCreated(), that.getCreated()) &&
            Objects.equals(getCustomerId(), that.getCustomerId()) &&
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

        return Objects.hash(getId(), getObject(), getCanceledAt(), getCollectionMethod(),
            getCreated(), getCustomerId(), getMetadata(), getProduct(), getQuantity(),
            getStartDate(), getStatus(), getTrialEnd(), getTrialStart(), getQuotas());
    }
}
