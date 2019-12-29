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
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * Customers represent individuals that order products.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {
    /* The customer unique id */
    private Integer id;

    /* The customer object type (must be "customer" */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "customer")
    private String object;

    /* The customer unique subject id as the full http or https URL */
    @NotEmpty
    @NotNull
    private String subject;

    /* The customer account balance */
    private Integer balance;

    /* The customer address */
    private Address address;

    /* The customer creation date, in seconds from the unix epoch */
    private Integer created;

    /* The customer default currency code */
    @Pattern(regexp = "[A-Z]{3}")
    private String currency;

    /* The customer invoice delinquency status */
    private boolean delinquent;

    /* The customer description, can be null */
    private String description;

    /* The customer discount settings as a JSON object */
    private ObjectNode discount;

    /* The customer email address */
    @NotEmpty
    @NotNull
    @Email
    private String email;

    /* The customer invoice prefix to generate unique invoice numbers */
    private String invoicePrefix;

    /* The customer invoice settings */
    private ObjectNode invoiceSettings;

    /* The customer metadata (extended information as needed) */
    private ObjectNode metadata;

    /* The customer given name */
    @NotEmpty
    @NotNull
    private String givenName;

    /* The customer surname */
    @NotEmpty
    @NotNull
    private String surName;

    /* The customer phone number */
    private String phone;

    /**
     * Construct an empty Customer
     */
    public Customer() {
        super();
    }

    /**
     * Construct a Customer
     * @param id
     * @param object
     * @param subject
     * @param balance
     * @param address
     * @param created
     * @param currency
     * @param delinquent
     * @param description
     * @param discount
     * @param email
     * @param invoicePrefix
     * @param invoiceSettings
     * @param metadata
     * @param givenName
     * @param surName
     * @param phone
     */
    public Customer(Integer id,
                    @NotEmpty @NotNull @Pattern(regexp = "customer") String object,
                    @NotEmpty @NotNull String subject,
                    Integer balance,
                    Address address,
                    Integer created,
                    @Pattern(regexp = "[A-Z]{3}") String currency,
                    @NotNull boolean delinquent,
                    @NotEmpty String description,
                    ObjectNode discount,
                    @NotEmpty @NotNull String email,
                    String invoicePrefix,
                    ObjectNode invoiceSettings,
                    ObjectNode metadata,
                    @NotEmpty @NotNull String givenName,
                    @NotEmpty @NotNull String surName,
                    String phone) {
        super();
        this.id = id;
        this.object = object;
        this.subject = subject;
        this.balance = balance;
        this.address = address;
        this.created = created;
        this.currency = currency;
        this.delinquent = delinquent;
        this.description = description;
        this.discount = discount;
        this.email = email;
        this.invoicePrefix = invoicePrefix;
        this.invoiceSettings = invoiceSettings;
        this.metadata = metadata;
        this.givenName = givenName;
        this.surName = surName;
        this.phone = phone;
    }

    /**
     * Get the customer identifier
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the customer identifier
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the customer object type
     * @return object
     */
    public String getObject() {
        return object;
    }

    /**
     * G=Set the customer object type
     * @param object
     */
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Get the customer subject identifier
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set the customer subject identifier
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Get the customer identifier
     * @return balance
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     * Set the customer balance
     * @param balance
     */
    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    /**
     * Get the customer address
     * @return address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Set the customer address
     * @param address
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Get the customer creation date
     * @return created
     */
    public Integer getCreated() {
        return created;
    }

    /**
     * Set the customer creation date
     * @param created
     */
    public void setCreated(Integer created) {
        this.created = created;
    }

    /**
     * Get the customer currency
     * @return currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Set the customer currency
     * @param currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Get the customer delinquency status
     * @return delinquent
     */
    public boolean isDelinquent() {
        return delinquent;
    }

    /**
     * Set the customer delinquency status
     * @param delinquent
     */
    public void setDelinquent(boolean delinquent) {
        this.delinquent = delinquent;
    }

    /**
     * Get the customer description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the customer description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the customer discount
     * @return discount
     */
    public ObjectNode getDiscount() {
        return discount;
    }

    /**
     * Set the customer discount
     * @param discount
     */
    public void setDiscount(ObjectNode discount) {
        this.discount = discount;
    }

    /**
     * Get the customer email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the customer email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the customer invoice prefix
     * @return invoicePrefix
     */
    public String getInvoicePrefix() {
        return invoicePrefix;
    }

    /**
     * Set the customer invoicePrefix
     * @param invoicePrefix
     */
    public void setInvoicePrefix(String invoicePrefix) {
        this.invoicePrefix = invoicePrefix;
    }

    /**
     * Get the customer invoice settings
     * @return invoiceSettings
     */
    public ObjectNode getInvoiceSettings() {
        return invoiceSettings;
    }

    /**
     * Set the customer invoice settings
     * @param invoiceSettings
     */
    public void setInvoiceSettings(ObjectNode invoiceSettings) {
        this.invoiceSettings = invoiceSettings;
    }

    /**
     * Get the customer metadata
     * @return metadata
     */
    public ObjectNode getMetadata() {
        return metadata;
    }

    /**
     * Set the customer metadata
     * @param metadata
     */
    public void setMetadata(ObjectNode metadata) {
        this.metadata = metadata;
    }

    /**
     * Get the customer given name
     * @return givenName
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Set the customer given name
     * @param givenName
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * Get the customer surname
     * @return surName
     */
    public String getSurName() {
        return surName;
    }

    /**
     * Set the customer surname
     * @param surName
     */
    public void setSurName(String surName) {
        this.surName = surName;
    }

    /**
     * Get the customer phone number
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set the customer phone number
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Return the discount hash as a JSON string
     * @return discount the discount JSON string
     * @throws JsonProcessingException
     */
    public String getDiscountJSON() throws JsonProcessingException {
        if ( discount != null ) {
            return Jackson.newObjectMapper().writeValueAsString(getDiscount());
        } else {
            return "{}";
        }
    }

    /**
     * Return the address hash as a JSON string
     * @return address the address JSON string
     * @throws JsonProcessingException
     */
    public String getAddressJSON() throws JsonProcessingException {
        if ( address != null ) {
            return Jackson.newObjectMapper().writeValueAsString(getAddress());
        } else {
            return "{}";
        }
    }

    /**
     * Return the invoice settings hash as a JSON string
     * @return invoiceSettings the invoice settings JSON string
     * @throws JsonProcessingException
     */
    public String getInvoiceSettingsJSON() throws JsonProcessingException {
        if ( invoiceSettings != null ) {
            return Jackson.newObjectMapper().writeValueAsString(getInvoiceSettings());
        } else {
            return "{}";
        }
    }

    /**
     * Return the metadata hash as a JSON string
     * @return metadata the metadata JSON string
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
     * Determine object equality based on the equality of all fields
     * @param o the object to be compared
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;

        return isDelinquent() == customer.isDelinquent() &&
            Objects.equals(getId(), customer.getId()) &&
            Objects.equals(getBalance(), customer.getBalance()) &&
            Objects.equals(getCreated(), customer.getCreated()) &&
            Objects.equals(getObject(), customer.getObject()) &&
            Objects.equals(getSubject(), customer.getSubject()) &&
            Objects.equals(getAddress(), customer.getAddress()) &&
            Objects.equals(getCurrency(), customer.getCurrency()) &&
            Objects.equals(getDescription(), customer.getDescription()) &&
            Objects.equals(getDiscount(), customer.getDiscount()) &&
            Objects.equals(getEmail(), customer.getEmail()) &&
            Objects.equals(getInvoicePrefix(), customer.getInvoicePrefix()) &&
            Objects.equals(getInvoiceSettings(), customer.getInvoiceSettings()) &&
            Objects.equals(getMetadata(), customer.getMetadata()) &&
            Objects.equals(getGivenName(), customer.getGivenName()) &&
            Objects.equals(getSurName(), customer.getSurName()) &&
            Objects.equals(getPhone(), customer.getPhone());
    }

    /**
     * Calculate a hash based on all fields
     * @return
     */
    @Override
    public int hashCode() {

        return Objects.hash(getId(), getObject(), getSubject(), getBalance(), getAddress(),
            getCreated(), getCurrency(), isDelinquent(), getDescription(), getDiscount(),
            getEmail(), getInvoicePrefix(), getInvoiceSettings(), getMetadata(),
            getGivenName(), getSurName(), getPhone());
    }
}
