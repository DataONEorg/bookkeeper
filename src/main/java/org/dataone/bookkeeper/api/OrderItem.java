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

import io.dropwizard.jackson.Jackson;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.Objects;

/**
 * OrderItem represents a single line item of an order
 */
public class OrderItem {

    /* The order item object type */
    @NotNull
    @NotEmpty
    private String object;

    /* The order item amount */
    private Integer amount;

    /* The order item currency */
    private String currency;

    /* The order item description */
    private String description;

    /* The order item parent product id */
    @NotNull
    private Integer parent;

    /* The order item quantity */
    @NotNull
    private Integer quantity;

    /* The order item type */
    @NotNull
    @NotEmpty
    @Pattern(regexp = "sku|tax|shipping|discount")
    private String type;

    /**
     * Construct an empty order item
     */
    public OrderItem() {
        super();
    }

    /**
     * Construct an order item from a JSON string
     */
    public OrderItem(String json) throws IOException {
        super();
        // Return an empty Feature instance when the JSON object is empty
        if ( ! json.equals("{}") ) {

            // Otherwise try to build the Feature
            OrderItem orderItem = Jackson.newObjectMapper().readValue(json, OrderItem.class);
            this.object = orderItem.getObject();
            this.amount = orderItem.getAmount();
            this.currency = orderItem.getCurrency();
            this.description = orderItem.getDescription();
            this.parent = orderItem.getParent();
            this.quantity = orderItem.getQuantity();
            this.type = orderItem.getType();
        }
    }

    /**
     * Construct an order item
     * @param object
     * @param amount
     * @param currency
     * @param description
     * @param parent
     * @param quantity
     * @param type
     */
    public OrderItem(
        @NotNull @NotEmpty String object,
        @NotNull Integer amount,
        @NotNull @NotEmpty String currency,
        @NotNull @NotEmpty String description,
        Integer parent,
        @NotNull Integer quantity,
        @NotNull @NotEmpty @Pattern(regexp = "sku|tax|shipping|discount") String type) {
        super();
        this.object = object;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.parent = parent;
        this.quantity = quantity;
        this.type = type;
    }

    /**
     * Get the order item object type
     * @return
     */
    public String getObject() {
        return object;
    }

    /**
     * Set the order item object type
     * @param object
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Get the order item amount
     * @return
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Set the order item amount
     * @param amount
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * Get the order item currency
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Set the order item currency
     * @param currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Get the order item description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the order item description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the order item parent identifier
     * @return
     */
    public Integer getParent() {
        return parent;
    }

    /**
     * Set the order item parent identifier
     * @param parent
     */
    public void setParent(Integer parent) {
        this.parent = parent;
    }

    /**
     * Get the order item quantity
     * @return
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Set the order item quantity
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Get the order item type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Set the order item type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Determine order item equality
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(getObject(), orderItem.getObject()) &&
            Objects.equals(getAmount(), orderItem.getAmount()) &&
            Objects.equals(getCurrency(), orderItem.getCurrency()) &&
            Objects.equals(getDescription(), orderItem.getDescription()) &&
            Objects.equals(getParent(), orderItem.getParent()) &&
            Objects.equals(getQuantity(), orderItem.getQuantity()) &&
            Objects.equals(getType(), orderItem.getType());
    }

    /**
     * Generate an order item hash code
     * @return
     */
    @Override
    public int hashCode() {

        return Objects.hash(getObject(), getAmount(), getCurrency(),
            getDescription(), getParent(), getQuantity(), getType());
    }
}
