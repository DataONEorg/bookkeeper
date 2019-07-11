package org.dataone.bookkeeper.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the Customer model
 */
@DisplayName("Customer model test")
class CustomerTest {
    private final static ObjectMapper MAPPER = Jackson.newObjectMapper();
    private final static String PRODUCT_JSON = "fixtures/customer.json";
    private final static String ID = "BEAAE3CD-7FA9-4B41-A337-2F5692D5F14E";
    private final static String OBJECT = "customer";
    private final String ORCID = "http://orcid.org/0000-0002-8121-2341";
    private final int BALANCE = 0;
    private final Address ADDRESS = new Address(
        "735 State Street",
        "Suite 300",
        "Santa Barbara",
        "CA",
        "93106",
        "USA"
    );
    private final int CREATED = 1562866734;
    private final String CURRENCY = "USD";
    private final boolean DELINQUENT = false;
    private final String DESCRIPTION = null;
    private final JsonNode DISCOUNT = null;
    private final String EMAIL = "cjones@nceas.ucsb.edu";
    private final String INVOICEPREFIX = null;
    private final JsonNode INVOICESETTINGS = null;
    private final JsonNode METADATA = null;
    private final String GIVENNAME = "Christopher";
    private final String SURNAME = "Jones";
    private final String PHONE = "805-893-2500";

    /**
     * Test serialization to JSON
     */
    @Test
    @DisplayName("Test Customer model serialization")
    public void serializesToJSON() throws Exception {
        // Build the Customer instance
        final Customer customer = new Customer(ID, OBJECT, ORCID, BALANCE, ADDRESS, CREATED,
            CURRENCY, DELINQUENT, DESCRIPTION, DISCOUNT, EMAIL, INVOICEPREFIX, INVOICESETTINGS,
            METADATA, GIVENNAME, SURNAME, PHONE
        );

        // Test the Customer instance
        final String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture("fixtures/customer.json"), Customer.class));
        assertThat(MAPPER.writeValueAsString(customer)).isEqualTo(expected);

    }
}