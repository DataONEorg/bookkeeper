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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Products represent a given offering to be purchased
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Product {

    /* The product id */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Integer id;

    /* The product object type */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "product")
    private String object;

    /* The product visibility status (true or false) */
    @NotNull
    private boolean active;

    /* The product cost (in pence of the currency) */
    @NotNull
    @Min(0)
    private Integer amount;

    /* The product caption */
    @NotEmpty
    @NotNull
    @Length(max = 500)
    private String caption;

    /* The product currency code */
    @NotEmpty
    @NotNull
    @Length(max = 3)
    private String currency;

    /* The product creation timestamp (from the unix epoch in seconds)*/
    private Integer created;

    /* The product description */
    @NotEmpty
    @NotNull
    @Length(max = 1000)
    private String description;

    /* The product payment interval */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "day|week|month|year")
    private String interval;

    /* The product name */
    @NotEmpty
    @NotNull
    @Length(max = 250)
    private String name;

    /* The product statement descriptor shown on charge receipts */
    @Length(max = 100)
    private String statementDescriptor;

    /* The product type, either a good or service */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "good|service")
    private String type;

    /* The product unit label used on invoices and charge receipts for type=service*/
    private String unitLabel;

    /* The product URL that provides a product description */
    @Pattern(regexp = "http.*")
    private String url;

    /* The product metadata hash of product features and other metadata */
    private ObjectNode metadata;


    /**
     * Construct an empty product
     */
    public Product() {
        super();
    }

    public Product(
        Integer id,
        @NotEmpty @NotNull @Pattern(regexp = "product") String object,
        @NotNull boolean active,
        @NotNull @Min(0) Integer amount,
        @NotEmpty @NotNull @Length(max = 500) String caption,
        @NotEmpty @NotNull @Length(max = 3) String currency,
        Integer created,
        @NotEmpty @NotNull @Length(max = 1000) String description,
        @NotEmpty @NotNull @Pattern(regexp = "day|week|month|year") String interval,
        @NotEmpty @NotNull @Length(max = 250) String name,
        @Length(max = 100) String statementDescriptor,
        @NotEmpty @NotNull @Pattern(regexp = "good|service") String type,
        String unitLabel,
        @Pattern(regexp = "http.*") String url,
        ObjectNode metadata) {
        this.id = id;
        this.object = object;
        this.active = active;
        this.amount = amount;
        this.caption = caption;
        this.currency = currency;
        this.created = created;
        this.description = description;
        this.interval = interval;
        this.name = name;
        this.statementDescriptor = statementDescriptor;
        this.type = type;
        this.unitLabel = unitLabel;
        this.url = url;
        this.metadata = metadata;
    }

    /**
     * Get the product id
     * @return id
     */
    @JsonProperty
    public Integer getId() {
        return id;
    }

    /**
     * Get the id as a primitive int
     * @return
     */
    public int getIdAsInt() {
        return getId().intValue();
    }

    /**
     * Set the product id
     * @param id
     */
    @JsonProperty
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the product object type string
     * @return object
     */
    @JsonProperty
    public String getObject() {
        return object;
    }

    /**
     * Set the product object type string
     * @param object
     */
    @JsonProperty
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Get the product active status
     * @return active
     */
    @JsonProperty
    public boolean isActive() {
        return active;
    }

    /**
     * Set the product active status
     * @param active
     */
    @JsonProperty
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Get the product name
     * @return name
     */
    @JsonProperty
    public String getName() {
        return name;
    }

    /**
     * Set the product name
     * @param name
     */
    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the product caption
     * @return
     */
    @JsonProperty
    public String getCaption() {
        return caption;
    }

    /**
     * Set the product caption
     * @param caption
     */
    @JsonProperty
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Get the product description
     * @return
     */
    @JsonProperty
    public String getDescription() {
        return description;
    }

    /**
     * Set the product description
     * @param description
     */
    @JsonProperty
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the product creation timestamp
     * @return
     */
    @JsonProperty
    public Integer getCreated() {
        return created;
    }

    /**
     * Set the product creation timestamp
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
     * Get the product statement descriptor
     * @return
     */
    @JsonProperty
    public String getStatementDescriptor() {
        return statementDescriptor;
    }

    /**
     * Set the product statement descriptor
     * @param statementDescriptor
     */
    @JsonProperty
    public void setStatementDescriptor(String statementDescriptor) {
        this.statementDescriptor = statementDescriptor;
    }

    /**
     * Get the product type
     * @return
     */
    @JsonProperty
    public String getType() {
        return type;
    }

    /**
     * Set the product type
     * @param type
     */
    @JsonProperty
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the product unit label
     * @return
     */
    @JsonProperty
    public String getUnitLabel() {
        return unitLabel;
    }

    /**
     * Set the product unit label
     * @param unitLabel
     */
    @JsonProperty
    public void setUnitLabel(String unitLabel) {
        this.unitLabel = unitLabel;
    }

    /**
     * Get the product URL
     * @return
     */
    @JsonProperty
    public String getUrl() {
        return url;
    }

    /**
     * Set the product URL
     * @param url
     */
    @JsonProperty
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get the product metadata
     * @return
     */
    @JsonProperty
    public ObjectNode getMetadata() {
        return metadata;
    }

    /**
     * Set the product metadata
     * @param metadata
     */
    @JsonProperty
    public void setMetadata(ObjectNode metadata) {
        this.metadata = metadata;
    }

    /**
     * Return the metadata hash as a JSON string
     * @return
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
     * Get the product cost amount
     * @return
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Set the product cost amount
     * @param amount
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * Get the product currency code
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Set the product currency code
     * @param currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Get the product payment interval
     * @return
     */
    public String getInterval() {
        return interval;
    }

    /**
     * Set the product payment
     * @param interval
     */
    public void setInterval(String interval) {
        this.interval = interval;
    }

    /**
     * Determine object equality based on the equality of all fields
     * @param o the object to be compared
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return isActive() == product.isActive() &&
            Objects.equals(getId(), product.getId()) &&
            Objects.equals(getObject(), product.getObject()) &&
            Objects.equals(getAmount(), product.getAmount()) &&
            Objects.equals(getCaption(), product.getCaption()) &&
            Objects.equals(getCurrency(), product.getCurrency()) &&
            Objects.equals(getCreated(), product.getCreated()) &&
            Objects.equals(getDescription(), product.getDescription()) &&
            Objects.equals(getInterval(), product.getInterval()) &&
            Objects.equals(getName(), product.getName()) &&
            Objects.equals(getStatementDescriptor(), product.getStatementDescriptor()) &&
            Objects.equals(getType(), product.getType()) &&
            Objects.equals(getUnitLabel(), product.getUnitLabel()) &&
            Objects.equals(getUrl(), product.getUrl()) &&
            Objects.equals(getMetadata(), product.getMetadata());
    }


    /**
     * Calculate a hash based on all fields
     * @return hashcode
     */
    @Override
    public int hashCode() {

        return Objects.hash(getId(), getObject(), isActive(), getAmount(), getCaption(),
            getCurrency(), getCreated(), getDescription(), getInterval(), getName(),
            getStatementDescriptor(), getType(), getUnitLabel(), getUrl(), getMetadata());
    }
}
