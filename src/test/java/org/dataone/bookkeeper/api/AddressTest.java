package org.dataone.bookkeeper.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the Address model
 */
class AddressTest {
    private final static ObjectMapper MAPPER = Jackson.newObjectMapper();
    private final static String ADDRESS_JSON = "fixtures/address.json";
    private static final String LINE1 = "735 State Street";
    private static final String LINE2 = "Suite 300";
    private static final String CITY = "Santa Barbara";
    private static final String STATE = "CA";
    private static final String POSTALCODE = "93106";
    private static final String COUNTRY = "USA";

    /**
     * Test serialization to JSON
     */
    @Test
    @DisplayName("Test Address model serialization")
    public void serializesToJSON() throws Exception {
        // Build the Address instance
        final Address address = new Address(LINE1, LINE2, CITY, STATE, POSTALCODE, COUNTRY);

        // Test the Address instance
        final String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture("fixtures/address.json"), Address.class));
        assertThat(MAPPER.writeValueAsString(address)).isEqualTo(expected);

    }

    /**
     * Test deserialization from JSON
     */
    @Test
    @DisplayName("Test Address model desrialization")
    public void deserializesFromJSON() throws Exception {
        // Build the Address instance
        final Address address = new Address(LINE1, LINE2, CITY, STATE, POSTALCODE, COUNTRY);

        // Test the Address instance
        final Address deserializedAddress =
        MAPPER.readValue(fixture("fixtures/address.json"), Address.class);
        assertThat(deserializedAddress).isEqualTo(address);
    }

}