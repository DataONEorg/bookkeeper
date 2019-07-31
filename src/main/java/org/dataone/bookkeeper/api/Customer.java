package org.dataone.bookkeeper.api;

import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Customers represent individuals that order products.
 */
public class Customer {
    /* The customer unique id */
    private Integer id;

    /* The customer object type (must be "customer" */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "customer")
    private String object;

    /* The customer unique ORCID id as the full http or https URL */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "http?://orcid\\.org/[\\d]{4}-[\\d]{4}-[\\d]{4}-[\\d]{3}[0-9X]")
    private String orcid;

    /* The customer account balance */
    @NotEmpty
    @NotNull
    private Integer balance;

    /* The customer address */
    @NotEmpty
    @NotNull
    private Address address;

    /* The customer creation date, in seconds from the unix epoch */
    @NotEmpty
    @NotNull
    private Integer created;

    /* The customer default currency code */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "[A-Z]{3}")
    private String currency;

    /* The customer invoice delinquency status */
    @NotEmpty
    @NotNull
    private boolean delinquent;

    /* The customer description, can be null */
    @NotEmpty
    private String description;

    /* The customer discount settings as a JSON object */
    @NotEmpty
    private ObjectNode discount;

    /* The customer email address */
    @NotEmpty
    @NotNull
    private String email;

    /* The customer invoice prefix to generate unique invoice numbers */
    @NotEmpty
    private String invoicePrefix;

    /* The customer invoice settings */
    @NotEmpty
    private ObjectNode invoiceSettings;

    /* The customer metadata (extended information as needed) */
    @NotEmpty
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
    @NotEmpty
    private String phone;

    /* The customer quota list, if any */
    private List<Quota> quotas;
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
     * @param orcid
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
     * @param quotas
     */
    public Customer(Integer id,
                    @NotEmpty @NotNull @Pattern(regexp = "customer") String object,
                    @NotEmpty @NotNull @Pattern(regexp = "http?://orcid\\.org/[\\d]{4}-[\\d]{4}-[\\d]{4}-[\\d]{3}[0-9X]") String orcid,
                    @NotEmpty @NotNull Integer balance,
                    @NotEmpty @NotNull Address address,
                    @NotEmpty @NotNull Integer created,
                    @NotEmpty @NotNull @Pattern(regexp = "[A-Z]{3}") String currency,
                    @NotEmpty @NotNull boolean delinquent,
                    @NotEmpty String description,
                    @NotEmpty ObjectNode discount,
                    @NotEmpty @NotNull String email,
                    @NotEmpty String invoicePrefix,
                    @NotEmpty ObjectNode invoiceSettings,
                    @NotEmpty ObjectNode metadata,
                    @NotEmpty @NotNull String givenName,
                    @NotEmpty @NotNull String surName,
                    @NotEmpty String phone,
                    List<Quota> quotas) {
        super();
        this.id = id;
        this.object = object;
        this.orcid = orcid;
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
        this.quotas = quotas;
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
     * Get the customer ORCID identifier
     * @return orcid
     */
    public String getOrcid() {
        return orcid;
    }

    /**
     * Set the customer ORCID identifier
     * @param orcid
     */
    public void setOrcid(String orcid) {
        this.orcid = orcid;
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
 * Get the customer identifier
 */
    /**
     * Get the customer given name
     * @return givenName
     */
 String getGivenName() {
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
     * Get the customer quotas
     * @return
     */
    public List<Quota> getQuotas() {
        return quotas;
    }

    /**
     * Set the customer quotas
     * @param quotas
     */
    public void setQuotas(List<Quota> quotas) {
        this.quotas = quotas;
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
            Objects.equals(getOrcid(), customer.getOrcid()) &&
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
            Objects.equals(getPhone(), customer.getPhone()) &&
            Objects.equals(getQuotas(), customer.getQuotas());

    }

    /**
     * Calculate a hash based on all fields
     * @return
     */
    @Override
    public int hashCode() {

        return Objects.hash(getId(), getObject(), getOrcid(), getBalance(), getAddress(),
            getCreated(), getCurrency(), isDelinquent(), getDescription(), getDiscount(),
            getEmail(), getInvoicePrefix(), getInvoiceSettings(), getMetadata(),
            getGivenName(), getSurName(), getPhone(), getQuotas());
    }
}
