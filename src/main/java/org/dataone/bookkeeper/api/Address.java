package org.dataone.bookkeeper.api;

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
        this.city = city ;
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
}
