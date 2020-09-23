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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * Memberships represent a Product purchased by a Customer, charged for periodically.
 */
@JsonIgnoreProperties({"metadataJSON"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Membership {

    /* The membership unique identifier */
    @NotNull
    @NotEmpty
    private Integer id;

    /* The membership object type */
    @NotNull
    @NotEmpty
    @Pattern(regexp = "membership")
    private String object;

    /* The membership billing cycle anchor timestamp (seconds since the epoch)
     * Determines the date of the first full invoice.
     * For products with month or year intervals,
     * the day of the month for subsequent invoices
    @NotNull
    @NotEmpty
    private Integer billingCycleAnchor;
    */

    /* The membership cancellation timestamp (seconds since the epoch) */
    private Integer canceledAt;

    /* The membership collection method */
    @NotNull
    @NotEmpty
    @Pattern(regexp = "charge_automatically|send_invoice")
    private String collectionMethod;

    /* The membership creation timestamp (seconds since the epoch) */
    private Integer created;

    /* The membership current period end timestamp (seconds since the epoch)
    private Integer currentPeriodEnd;
    */

    /* The membership current period start timestamp (seconds since the epoch)
    private Integer currentPeriodStart;
    */

    /* The membership customer */
    @NotNull
    @NotEmpty
    private Customer customer;

    /* The membership days until due
    private Integer daysUntilDue;
    */

    /* The membership discount
    private ObjectNode discount;
    */

    /* The membership end timestamp (seconds since the epoch)
    private Integer endedAt;
    */

    /* The membership latest invoice identifier
    private Integer latestInvoice;
    */

    /* The membership metadata object */
    private ObjectNode metadata;

    /* The purchased product */
    @NotNull
    @NotEmpty
    private Product product;

    /* The membership quantity of the product */
    @NotNull
    @NotEmpty
    @Min(1)
    private Integer quantity;

    /* The membership start date timestamp (seconds since the epoch) */
    private Integer startDate;

    /* The membership status
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

    /* The membership trial end timestamp (seconds since the epoch) */
    private Integer trialEnd;

    /* The membership trial start timestamp (seconds since the epoch) */
    private Integer trialStart;

    /* The quotas associated with the product, if any */
    private List<Quota> quotas;

    /**
     * Construct an empty membership.
     */
    public Membership() {
        super();
    }

    /**
     * Construct a membership
     * @param object the type of object ("membership")
     * @param canceledAt when the membership was canceled
     * @param collectionMethod the payment collection method
     * @param created when the membership was created
     * @param customer the associated customer
     * @param metadata the associated metadata
     * @param product the associated product
     * @param quantity the quantity of the associated product
     * @param startDate when the membership started
     * @param status the membership status
     * @param trialEnd the membership trial end date (epoch seconds)
     * @param trialStart the membership trial start date (epoch seconds)
     * @param quotas the quotas associated with the membership
     */
    public Membership(
        Integer id,
        @NotNull @NotEmpty @Pattern(regexp = "membership") String object,
        Integer canceledAt,
        @NotNull @NotEmpty @Pattern(regexp = "charge_automatically|send_invoice") String collectionMethod,
        Integer created,
        @NotNull Customer customer,
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
        this.customer = customer;
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
     * Get the membership identifier
     * @return id the membership identifier
     */
    @JsonProperty
    public Integer getId() {
        return id;
    }

    /**
     * Get the membership identifier
     * @param id the membership identifier
     */
    @JsonProperty
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the membership object type
     * @return object the membership object type
     */
    @JsonProperty
    public String getObject() {
        return object;
    }

    /**
     * Set the membership object type
     * @param object the membership object type
     */
    @JsonProperty
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Get the membership canceled timestamp (seconds since the epoch)
     * @return canceledAt the membership canceled timestamp
     */
    @JsonProperty
    public Integer getCanceledAt() {
        return canceledAt;
    }

    /**
     * Set the membership canceled timestamp (seconds since the epoch)
     * @param canceledAt the membership canceled timestamp
     */
    @JsonProperty
    public void setCanceledAt(Integer canceledAt) {
        this.canceledAt = canceledAt;
    }

    /**
     * Get the canceled at date as an ISO 8601 timestamp string
     * @return the membership canceled timestamp
     */
    public String getCanceledAtTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date((long) getCanceledAt().intValue() * 1000));
    }

    /**
     * Get the membership collection method
     * @return collectionMethod the membership collection metho
     */
    @JsonProperty
    public String getCollectionMethod() {
        return collectionMethod;
    }

    /**
     * Set the membership collection method
     * @param collectionMethod the membership collection metho
     */
    @JsonProperty
    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    /**
     * Get the membership creation timestamp (seconds since the epoch)
     * @return created the membership creation timestamp
     */
    @JsonProperty
    public Integer getCreated() {
        return created;
    }

    /**
     * Set the membership creation timestamp (seconds since the epoch)
     * @param created the membership creation timestamp
     */
    @JsonProperty
    public void setCreated(Integer created) {
        this.created = created;
    }

    /**
     * Get the creation date as an ISO 8601 timestamp string
     * @return timestamp the membership creation timestamp
     */
    public String getCreatedTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date((long) getCreated().intValue() * 1000));
    }

    /**
     * Get the customer id
     * @return customerId the customer id
     */
    public Integer getCustomerId() {
        Integer customerId = null;
        if ( this.customer != null ) {
            customerId = this.customer.getId();
        }
        return customerId;
    }
    /**
     * Get the membership customer
     * @return customer the membership customer
     */
    @JsonProperty
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Set the membership customer
     * @param customer the membership customer
     */
    @JsonProperty
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Get the membership metadata
     * @return metadata the membership metadata
     */
    @JsonProperty
    public ObjectNode getMetadata() {
        return metadata;
    }

    /**
     * Set the membership metadata
     * @param metadata the membership metadata
     */
    @JsonProperty
    public void setMetadata(ObjectNode metadata) {
        this.metadata = metadata;
    }

    /**
     * Return the metadata hash as a JSON string
     * @return metadata the metadata object hash
     * @throws JsonProcessingException a JSON processing exception
     */

    public String getMetadataJSON() throws JsonProcessingException {
        if ( metadata != null ) {
            return Jackson.newObjectMapper().writeValueAsString(getMetadata());
        } else {
            return "{}";
        }
    }

    /**
     * Get the membership product
     * @return product the membership product
     */
    @JsonProperty
    public Product getProduct() {
        return product;
    }

    /**
     * Set the membership product
     * @param product the membership product
     */
    @JsonProperty
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Get the product id
     * @return productId the product id
     */
    public Integer getProductId() {
        Integer productId = null;
        if ( this.product != null ) {
            productId = this.product.getId();
        }
        return productId;
    }

    /**
     * Get the membership quantity of the product
     * @return quantity the membership quantity of the product
     */
    @JsonProperty
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Set the membership quantity of the product
     * @param quantity the membership quantity of the product
     */
    @JsonProperty
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Get the membership start timestamp (seconds since the epoch)
     * @return startDate the membership start timestamp
     */
    @JsonProperty
    public Integer getStartDate() {
        return startDate;
    }

    /**
     * Set the membership start timestamp (seconds since the epoch)
     * @param startDate the membership start date  in seconds since the epoch)
     */
    @JsonProperty
    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the start date as an ISO 8601 timestamp string
     * @return startDateTimestamp the start date as an ISO 8601 timestamp
     */
    public String getStartDateTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date((long) getStartDate().intValue() * 1000));
    }

    /**
     * Get the membership status
     * @return status the membership status
     */
    @JsonProperty
    public String getStatus() {
        return status;
    }

    /**
     * Set the membership status
     * @param status the membership status
     */
    @JsonProperty
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the membership trial end timestamp (seconds since the epoch)
     * @return trialEnd the membership trial end timestamp
     */
    @JsonProperty
    public Integer getTrialEnd() {
        return trialEnd;
    }

    /**
     * Set the membership trial end timestamp (seconds since the epoch)
     * @param trialEnd the membership trial end in seconds since the epoch
     */
    @JsonProperty
    public void setTrialEnd(Integer trialEnd) {
        this.trialEnd = trialEnd;
    }

    /**
     * Get the trial end date as an ISO 8601 timestamp string
     * @return trailDateTimestamp the trial end date as an ISO 8601 timestamp
     */
    public String getTrialEndTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date((long) getTrialEnd().intValue() * 1000));
    }

    /**
     * Get the membership trial start timestamp (seconds since the epoch)
     * @return trialStart the membership trial start timestamp (seconds since the epoch)
     */
    @JsonProperty
    public Integer getTrialStart() {
        return trialStart;
    }

    /**
     * Set the membership trial start timestamp (seconds since the epoch)
     * @param trialStart the membership trial start timestamp (seconds since the epoch)
     */
    @JsonProperty
    public void setTrialStart(Integer trialStart) {
        this.trialStart = trialStart;
    }

    /**
     * Get the trial start date as an ISO 8601 timestamp string
     * @return trialStartTimestamp the trial start date as an ISO 8601 timestamp
     */
    public String getTrailStartTimestamp() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return formatter.format(new Date((long) getTrialStart().intValue() * 1000));
    }

    /**
     * Get the membership quotas
     * @return quotas the membership quotas
     */
    @JsonProperty
    public List<Quota> getQuotas() {
        return quotas;
    }

    /**
     * Set the membership quotas
     * @param quotas the membership quotas
     */
    @JsonProperty
    public void setQuotas(List<Quota> quotas) {
        this.quotas = quotas;
    }

    /**
     * Determine object equality based on the equality of all fields
     * @param o the object to compare
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Membership that = (Membership) o;
        return Objects.equals(getId(), that.getId()) &&
            Objects.equals(getObject(), that.getObject()) &&
            Objects.equals(getCanceledAt(), that.getCanceledAt()) &&
            Objects.equals(getCollectionMethod(), that.getCollectionMethod()) &&
            Objects.equals(getCreated(), that.getCreated()) &&
            Objects.equals(getCustomer(), that.getCustomer()) &&
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
     * @return hashcode the hashcode of the object
     */
    @Override
    public int hashCode() {

        return Objects.hash(getId(), getObject(), getCanceledAt(), getCollectionMethod(),
            getCreated(), getCustomer(), getMetadata(), getProduct(), getQuantity(),
            getStartDate(), getStatus(), getTrialEnd(), getTrialStart(), getQuotas());
    }
}
