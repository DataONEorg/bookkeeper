package org.dataone.bookkeeper.api;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.util.Objects;

/**
 * Addresses are a part of Customers, storing their mailing address information.
 */
public class Address {

    /* The address line 1*/
    private String line1;

    /* The address line 2*/
    private String line2;

    /* The address city */
    private String city;

    /* The address state */
    private String state;

    /* The address postal code */
    private String postalCode;

    /* The address country */
    private String country;

    /**
     * Construct an empty address
     */
    public Address() {
        super();
    }

    /**
     * Construct an Address from a JSON string
     * @param json
     */
    public Address(String json) throws IOException {
        super();

        // Return an empty Address instance when the JSON object is empty
        if ( ! json.equals("{}") ) {

            // Otherwise try to build the Address
            Address address = Jackson.newObjectMapper().readValue(json, Address.class);
            this.line1 = address.line1;
            this.line2 = address.line2;
            this.city = address.city;
            this.state = address.state;
            this.postalCode = address.postalCode;
            this.country = address.country;
        }
    }

    /**
     * Construct an address
     * @param line1
     * @param line2
     * @param city
     * @param state
     * @param postalCode
     * @param country
     */
    public Address(String line1, String line2, String city,
                   String state, String postalCode, String country) {
        super();
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    /**
     * Get the address line 1
     * @return line1
     */
    public String getLine1() {
        return line1;
    }

    /**
     * Set the address line 1
     * @param line1
     */
    public void setLine1(String line1) {
        this.line1 = line1;
    }

    /**
     * Get the address line 2
     * @return line2
     */
    public String getLine2() {
        return line2;
    }

    /**
     * Set the address line 2
     * @param line2
     */
    public void setLine2(String line2) {
        this.line2 = line2;
    }

    /**
     * Get the address city
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the address city
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get the address state
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * Set the address state
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Get the address postal code
     * @return postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Set the address postal code
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Get the address country
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set the address country
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Determine object equality based on the equality of all fields
     * @param o
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getLine1(), address.getLine1()) &&
            Objects.equals(getLine2(), address.getLine2()) &&
            Objects.equals(getCity(), address.getCity()) &&
            Objects.equals(getState(), address.getState()) &&
            Objects.equals(getPostalCode(), address.getPostalCode()) &&
            Objects.equals(getCountry(), address.getCountry());
    }

    /**
     * Calculate a hash based on all fields
     * @return hash
     */
    @Override
    public int hashCode() {

        return Objects.hash(getLine1(), getLine2(), getCity(), getState(), getPostalCode(), getCountry());
    }
}
