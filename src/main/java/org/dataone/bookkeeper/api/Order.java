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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Orders represent a list of purchased products by customers
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

    /* The order unique id */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;

    /* The order object type */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "order")
    private String object;

    /* The order amount (in the smallest unit of the currency) */
    @NotNull
    private Integer amount;

    /* The order amount returned */
    private Integer amountReturned;

    /* The order payment charge details */
    private ObjectNode charge;

    /* The order creation date (seconds since the epoch) */
    private Integer created;

    /* The order currency id */
    @NotEmpty
    @NotNull
    private String currency;

    /* The order customer id */
    @NotNull
    private Integer customer;

    /* The order customer's email */
    @NotEmpty
    @NotNull
    private String email;

    /* The order item list of products */
    private List<OrderItem> items;

    /* The order metadata */
    private ObjectNode metadata;

    /* The order status */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "created|paid|canceled|fulfilled|returned")
    private String status;

    /* The order status transitions (history of status/timestamp key/value pairs*/
    private ObjectNode statusTransitions;

    /* The order update date (seconds since the epoch) */
    private Integer updated;

    /**
     * Construct an empty order
     */
    public Order() {
        super();
    }

    /**
     * Construct an order
     * @param id
     * @param object
     * @param amount
     * @param amountReturned
     * @param charge
     * @param created
     * @param currency
     * @param customer
     * @param email
     * @param items
     * @param metadata
     * @param status
     * @param statusTransitions
     * @param updated
     */
    public Order(
        Integer id,
        @NotEmpty @NotNull @Pattern(regexp = "order") String object,
        @NotNull Integer amount,
        Integer amountReturned,
        ObjectNode charge,
        Integer created,
        @NotEmpty @NotNull String currency,
        @NotNull Integer customer,
        @NotEmpty @NotNull String email,
        List<OrderItem> items,
        ObjectNode metadata,
        @NotEmpty @NotNull @Pattern(regexp = "created|paid|canceled|fulfilled|returned") String status,
        ObjectNode statusTransitions,
        Integer updated) {
        super();
        this.id = id;
        this.object = object;
        this.amount = amount;
        this.amountReturned = amountReturned;
        this.charge = charge;
        this.created = created;
        this.currency = currency;
        this.customer = customer;
        this.email = email;
        this.items = items;
        this.metadata = metadata;
        this.status = status;
        this.statusTransitions = statusTransitions;
        this.updated = updated;
    }

    /**
     * Get the order id
     * @return id the order identifier
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the order id
     * @param id the order identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the order object type
     * @return object the order object type
     */
    public String getObject() {
        return object;
    }

    /**
     * Set the order object type
     * @param object the order object type ("order")
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Get the order amount
     * @return amount the order amount in the smallest unit of the currency
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Set the order amount
     * @param amount the order amount in the smallest unit of the currency
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * Get the order amount returned
     * @return the order amount returned
     */
    public Integer getAmountReturned() {
        return amountReturned;
    }

    /**
     * Set the order amount returned
     * @param amountReturned the order amount returned
     */
    public void setAmountReturned(Integer amountReturned) {
        this.amountReturned = amountReturned;
    }

    /**
     * Get the order payment charge details
     * @return charge the order charge details
     */
    public ObjectNode getCharge() {
        return charge;
    }

    /**
     * Set the order payment charge details
     * @param charge the order payment charge details
     */
    public void setCharge(ObjectNode charge) {
        this.charge = charge;
    }

    /**
     * Get the order creation date
     * @return created the order creation date in seconds since the epoch
     */
    public Integer getCreated() {
        return created;
    }

    /**
     * Set the order creation date
     * @param created the order creation date in seconds since the epoch
     */
    public void setCreated(Integer created) {
        this.created = created;
    }

    /**
     * Get the order currency code
     * @return currency the order currency code
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Set the order currency code
     * @param currency the order currency code
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Get the order customer
     * @return customer the order customer
     */
    public Integer getCustomer() {
        return customer;
    }

    /**
     * Set the order customer id
     * @param customer the order customer id
     */
    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    /**
     * Get the order email
     * @return email the order email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the order email
     * @param email the order email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the order items
     * @return the list of order items
     */
    public List<OrderItem> getItems() {
        return items;
    }

    /**
     * Set the order items
     * @param items the list of order items
     */
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    /**
     * Get the order metadata
     * @return metadata the JSON metadata associated with the order
     */
    public ObjectNode getMetadata() {
        return metadata;
    }

    /**
     * Set the order metadata
     * @param metadata the JSON metadata associated with the order
     */
    public void setMetadata(ObjectNode metadata) {
        this.metadata = metadata;
    }

    /**
     * Get the order status
     * @return status the status of the order
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the order status
     * @param status the status of the order
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the order status transitions
     * @return statusTransitions the JSON object of status transitions
     */
    public ObjectNode getStatusTransitions() {
        return statusTransitions;
    }

    /**
     * Set the order status transitions
     * @param statusTransitions the JSON object of status transitions
     */
    public void setStatusTransitions(ObjectNode statusTransitions) {
        this.statusTransitions = statusTransitions;
    }

    /**
     * Get the order updated date
     * @return updated the order updated date
     */
    public Integer getUpdated() {
        return updated;
    }

    /**
     * Set the order updated date
     * @param updated the order updated date in seconds since the epoch
     */
    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    /**
     * Return the charge hash as a JSON string
     * @return charge the charge hash as a JSON string
     * @throws JsonProcessingException
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
     * @throws IOException
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
     * Return the charge hash as a JSON string
     * @return charge the charge hash as a JSON string
     * @throws JsonProcessingException
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
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(getId(), order.getId()) &&
            Objects.equals(getObject(), order.getObject()) &&
            Objects.equals(getAmount(), order.getAmount()) &&
            Objects.equals(getAmountReturned(), order.getAmountReturned()) &&
            Objects.equals(getCharge(), order.getCharge()) &&
            Objects.equals(getCreated(), order.getCreated()) &&
            Objects.equals(getCurrency(), order.getCurrency()) &&
            Objects.equals(getCustomer(), order.getCustomer()) &&
            Objects.equals(getEmail(), order.getEmail()) &&
            Objects.equals(getItems(), order.getItems()) &&
            Objects.equals(getMetadata(), order.getMetadata()) &&
            Objects.equals(getStatus(), order.getStatus()) &&
            Objects.equals(getStatusTransitions(), order.getStatusTransitions()) &&
            Objects.equals(getUpdated(), order.getUpdated());
    }

    /**
     * Generate an order hash code
     * @return
     */
    @Override
    public int hashCode() {

        return Objects.hash(getId(), getObject(), getAmount(), getAmountReturned(),
            getCharge(), getCreated(), getCurrency(), getCustomer(), getEmail(), getItems(),
            getMetadata(), getStatus(), getStatusTransitions(), getUpdated());
    }
}
