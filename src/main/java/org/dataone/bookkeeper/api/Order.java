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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Orders represent a list of purchased products by customers
 */
@JsonIgnoreProperties({"chargeJSON", "metadataJSON", "itemsJSON", "statusTransitionsJSON"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

    /* The order unique id */
    private Integer id;

    /* The order object type */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "order")
    private String object;

    /* The order amount (in the smallest unit of the currency) */
    private Integer amount;

    /* The order amount returned */
    private Integer amountReturned;

    /* The order payment charge details */
    private ObjectNode charge;

    /* The order creation date (seconds since the epoch) */
    private Integer created;

    /* The order currency id */
    private String currency;

    /* The order subject identifier, likely an ORCID or DataONE group DN */
    private String subject;

    /* The order customer id */
    @NotNull
    private Integer customer;

    /* The order customer's email */
    private String email;

    /* The order item list of products */
    @NotEmpty
    @NotNull
    @Valid
    private List<OrderItem> items;

    /* The order metadata */
    private ObjectNode metadata;

    /* The name of the order, set by the customer */
    private String name;

    /* The order status */
    @Pattern(regexp = "active|created|paid|past_due|refunded|trialing|unpaid")
    private String status;

    /* The order status transitions (history of status/timestamp key/value pairs*/
    private ObjectNode statusTransitions;

    /* The order update date (seconds since the epoch) */
    private Integer updated;

    /*The order series identifier used to track renewals */
    private String seriesId;

    /* The start date for the order used to determine service expiry */
    private Integer startDate;

    /* The end date for the order used to determine service expiry */
    private Integer endDate;

    /* The quotas associated with the product, if any */
    private List<Quota> quotas;

    /**
     * Construct an empty order
     */
    public Order() {
        super();
    }

    /**
     * Construct an order
     * @param id  the order identifier
     * @param object the order object type
     * @param amount  the order amount
     * @param amountReturned  the order amount returned
     * @param charge  the charge associated with the order
     * @param created  the order create timestamp (seconds since the epoch)
     * @param currency  the order currency identifier
     * @param customer  the order customer identifier
     * @param email  the order customer email
     * @param items  the order items list
     * @param metadata  the metadata object associated with an order
     * @param name  the name of the order, set by the customer
     * @param status  the order status, one of active|created|paid|past_due|refunded|trialing|unpaid
     * @param statusTransitions  the object showing status transitions
     * @param updated  the order update timestamp (seconds since the epoch)
     * @param seriesId the order series identifier
     * @param startDate the ordered services start timestamp (seconds since the epoch)
     * @param endDate the ordered services end timestamp (seconds since the epoch)
     * @param quotas  the quotas associated with the order
     */
    public Order(
        Integer id,
        @NotEmpty @NotNull @Pattern(regexp = "order") String object,
        @NotNull Integer amount,
        Integer amountReturned,
        ObjectNode charge,
        Integer created,
        String currency,
        String subject,
        @NotNull Integer customer,
        String email,
        @NotEmpty @NotNull @Valid List<OrderItem> items,
        ObjectNode metadata,
        String name,
        @NotEmpty @NotNull @Pattern(regexp = "active|created|paid|past_due|refunded|trialing|unpaid") String status,
        ObjectNode statusTransitions,
        Integer updated,
        String seriesId,
        Integer startDate,
        Integer endDate,
        List<Quota> quotas) {
        super();
        this.id = id;
        this.object = object;
        this.amount = amount;
        this.amountReturned = amountReturned;
        this.charge = charge;
        this.created = created;
        this.currency = currency;
        this.subject = subject;
        this.customer = customer;
        this.email = email;
        this.items = items;
        this.metadata = metadata;
        this.name = name;
        this.status = status;
        this.statusTransitions = statusTransitions;
        this.updated = updated;
        this.seriesId = seriesId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quotas = quotas;
    }

    /**
     * Get the order id
     * @return id the order identifier
     */
    @JsonProperty
    public Integer getId() {
        return id;
    }

    /**
     * Set the order id
     * @param id the order identifier
     */
    @JsonProperty
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the order object type
     * @return object the order object type
     */
    @JsonProperty
    public String getObject() {
        return object;
    }

    /**
     * Set the order object type
     * @param object the order object type ("order")
     */
    @JsonProperty
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Get the order amount
     * @return amount the order amount in the smallest unit of the currency
     */
    @JsonProperty
    public Integer getAmount() {
        return amount;
    }

    /**
     * Set the order amount
     * @param amount the order amount in the smallest unit of the currency
     */
    @JsonProperty
    public void setAmount(Integer amount) {
        this.amount = amount;
    }


    @JsonProperty
    public Integer getTotalAmount() {
        Integer total = 0;

        // If we have an item list, total the items
        if ( ! getItems().isEmpty() ) {
            for (OrderItem item : getItems() ) {
                total = total + item.getAmount();
            }
        }
        return total;
    }
    /**
     * Get the order amount returned
     * @return the order amount returned
     */
    @JsonProperty
    public Integer getAmountReturned() {
        return amountReturned;
    }

    /**
     * Set the order amount returned
     * @param amountReturned the order amount returned
     */
    @JsonProperty
    public void setAmountReturned(Integer amountReturned) {
        this.amountReturned = amountReturned;
    }

    /**
     * Get the order payment charge details
     * @return charge the order charge details
     */
    @JsonProperty
    public ObjectNode getCharge() {
        return charge;
    }

    /**
     * Set the order payment charge details
     * @param charge the order payment charge details
     */
    @JsonProperty
    public void setCharge(ObjectNode charge) {
        this.charge = charge;
    }

    /**
     * Get the order creation date
     * @return created the order creation date in seconds since the epoch
     */
    @JsonProperty
    public Integer getCreated() {
        return created;
    }

    /**
     * Set the order creation date
     * @param created the order creation date in seconds since the epoch
     */
    @JsonProperty
    public void setCreated(Integer created) {
        this.created = created;
    }

    /**
     * Get the order currency code
     * @return currency the order currency code
     */
    @JsonProperty
    public String getCurrency() {
        return currency;
    }

    /**
     * Set the order currency code
     * @param currency the order currency code
     */
    @JsonProperty
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Get the order customer
     * @return customer the order customer
     */
    @JsonProperty
    public Integer getCustomer() {
        return customer;
    }

    /**
     * Set the order customer id
     * @param customer the order customer id
     */
    @JsonProperty
    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    /**
     * Get the order subject
     * @return the order subject
     */
    @JsonProperty
    public String getSubject() {
        return subject;
    }

    /**
     * Set the order subject
     * @param subject the order subject
     */
    @JsonProperty
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Get the order email
     * @return email the order email
     */
    @JsonProperty
    public String getEmail() {
        return email;
    }

    /**
     * Set the order email
     * @param email the order email
     */
    @JsonProperty
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the order items
     * @return the list of order items
     */
    @JsonProperty
    public List<OrderItem> getItems() {
        return items;
    }

    /**
     * Set the order items
     * @param items the list of order items
     */
    @JsonProperty
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    /**
     * Get the order metadata
     * @return metadata the JSON metadata associated with the order
     */
    @JsonProperty
    public ObjectNode getMetadata() {
        return metadata;
    }

    /**
     * Set the order metadata
     * @param metadata the JSON metadata associated with the order
     */
    @JsonProperty
    public void setMetadata(ObjectNode metadata) {
        this.metadata = metadata;
    }

    /**
     * Get the name of the order
     * @return the order name set by the customer
     */
    @JsonProperty
    public String getName() {
        return name;
    }

    /**
     * Set the name of the order
     * @param name the order name set by the customer
     */
    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the order status
     * @return status the status of the order
     */
    @JsonProperty
    public String getStatus() {
        return status;
    }

    /**
     * Set the order status
     * @param status the status of the order
     */
    @JsonProperty
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the order status transitions
     * @return statusTransitions the JSON object of status transitions
     */
    @JsonProperty
    public ObjectNode getStatusTransitions() {
        return statusTransitions;
    }

    /**
     * Set the order status transitions
     * @param statusTransitions the JSON object of status transitions
     */
    @JsonProperty
    public void setStatusTransitions(ObjectNode statusTransitions) {
        this.statusTransitions = statusTransitions;
    }

    /**
     * Get the order updated date
     * @return updated the order updated date
     */
    @JsonProperty
    public Integer getUpdated() {
        return updated;
    }

    /**
     * Set the order updated date
     * @param updated the order updated date in seconds since the epoch
     */
    @JsonProperty
    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    /**
     * Get the order series identifier
     * @return the order series identifier
     */
    @JsonProperty
    public String getSeriesId() {
        return seriesId;
    }

    /**
     * Set the order series identifier
     * @param seriesId the order series identifier
     */
    @JsonProperty
    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    /**
     * Get the order start date
     * @return the order start date
     */
    @JsonProperty
    public Integer getStartDate() {
        return startDate;
    }

    /**
     * Set the order start date
     * @param startDate the order start date
     */
    @JsonProperty
    public void setStartDate(Integer startDate) {
        this.startDate = startDate;
    }

    /**
     * Get the order end date
     * @return the order end date
     */
    @JsonProperty
    public Integer getEndDate() {
        return endDate;
    }

    /**
     * Set the order end date
     * @param endDate the order end date
     */
    @JsonProperty
    public void setEndDate(Integer endDate) {
        this.endDate = endDate;
    }

    /**
     * Get the order quotas
     * @return quotas the order quotas
     */
    @JsonProperty
    public List<Quota> getQuotas() {
        return quotas;
    }

    /**
     * Set the order quotas
     * @param quotas the order quotas
     */
    @JsonProperty
    public void setQuotas(List<Quota> quotas) {
        this.quotas = quotas;
    }

    /**
     * Return the charge hash as a JSON string
     * @return charge the charge hash as a JSON string
     * @throws JsonProcessingException a JSON processing exception
     */
    public String getChargeJSON() throws JsonProcessingException {
        if ( charge != null ) {
            return Jackson.newObjectMapper().writeValueAsString(getCharge());
        } else {
            return "{}";
        }
    }


    /**
     * Return the items list as a JSON array
     * @return items the order items list
     * @throws IOException an I/O exception
     */
    public String getItemsJSON() throws IOException {
        if ( items != null ) {
            ObjectMapper mapper = Jackson.newObjectMapper();
            ArrayNode itemsArray = mapper.createArrayNode();

            for (OrderItem item : items) {
                itemsArray.add(mapper.readTree(mapper.writeValueAsString(item)));
            }
            return itemsArray.toString();
        } else {
            return "[]";
        }
    }

    /**
     * Return the metadata hash as a JSON string
     * @return metadata the metadata hash as a JSON string
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
     * Return the charge hash as a JSON string
     * @return charge the charge hash as a JSON string
     * @throws JsonProcessingException a JSON processing exception
     */
    public String getStatusTransitionsJSON() throws JsonProcessingException {
        if ( statusTransitions != null ) {
            return Jackson.newObjectMapper().writeValueAsString(getStatusTransitions());
        } else {
            return "{}";
        }
    }

    /**
     * Determine equality of another order
     * @param o the object to compare
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId()) &&
            getObject().equals(order.getObject()) &&
            getAmount().equals(order.getAmount()) &&
            Objects.equals(getAmountReturned(), order.getAmountReturned()) &&
            Objects.equals(getCharge(), order.getCharge()) &&
            Objects.equals(getCreated(), order.getCreated()) &&
            Objects.equals(getCurrency(), order.getCurrency()) &&
            Objects.equals(getSubject(), order.getSubject()) &&
            getCustomer().equals(order.getCustomer()) &&
            Objects.equals(getEmail(), order.getEmail()) &&
            getItems().equals(order.getItems()) &&
            Objects.equals(getMetadata(), order.getMetadata()) &&
            Objects.equals(getName(), order.getName()) &&
            getStatus().equals(order.getStatus()) &&
            Objects.equals(getStatusTransitions(), order.getStatusTransitions()) &&
            Objects.equals(getUpdated(), order.getUpdated()) &&
            getSeriesId().equals(order.getSeriesId()) &&
            Objects.equals(getStartDate(), order.getStartDate()) &&
            Objects.equals(getEndDate(), order.getEndDate()) &&
            Objects.equals(getQuotas(), order.getQuotas());
    }

    /**
     * Generate an order hash code
     * @return hash the order hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getObject(), getAmount(), getAmountReturned(),
            getCharge(), getCreated(), getCurrency(), getSubject(), getCustomer(),
            getEmail(), getItems(), getMetadata(), getName(), getStatus(), getStatusTransitions(),
            getUpdated(), getSeriesId(), getStartDate(), getEndDate(), getQuotas());
    }
}
