/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2019
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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
    static {
        MAPPER.setSerializationInclusion(Include.NON_NULL);
        MAPPER.setSerializationInclusion(Include.NON_EMPTY);
    }
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
    @DisplayName("Test Address model deserialization")
    public void deserializesFromJSON() throws Exception {
        // Build the Address instance
        final Address address = new Address(LINE1, LINE2, CITY, STATE, POSTALCODE, COUNTRY);

        // Test the Address instance
        final Address deserializedAddress =
        MAPPER.readValue(fixture("fixtures/address.json"), Address.class);
        assertThat(deserializedAddress).isEqualTo(address);
    }

}