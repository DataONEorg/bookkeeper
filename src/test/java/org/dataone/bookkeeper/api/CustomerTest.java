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
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the Customer model
 */
@DisplayName("Customer model test")
class CustomerTest {
    private final static ObjectMapper MAPPER = Jackson.newObjectMapper();
    static {
        MAPPER.setSerializationInclusion(Include.NON_NULL);
        MAPPER.setSerializationInclusion(Include.NON_EMPTY);
    }
    private final static String PRODUCT_JSON = "fixtures/customer.json";
    private final static  Integer ID = 1;
    private final static String OBJECT = "customer";
    private final String SUBJECT = "http://orcid.org/0000-0002-8121-2341";
    private final Integer BALANCE = 0;
    private final Address ADDRESS = new Address(
        "735 State Street",
        "Suite 300",
        "Santa Barbara",
        "CA",
        "93106",
        "USA"
    );
    private final Integer CREATED = 1562866734;
    private final String CURRENCY = "USD";
    private final boolean DELINQUENT = false;
    private final String DESCRIPTION = "";
    private final ObjectNode DISCOUNT = MAPPER.createObjectNode();
    private final String EMAIL = "cjones@nceas.ucsb.edu";
    private final String INVOICEPREFIX = "";
    private final ObjectNode INVOICESETTINGS = MAPPER.createObjectNode();
    private final ObjectNode METADATA = MAPPER.createObjectNode();
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
        final Customer customer = new Customer(ID, OBJECT, SUBJECT, BALANCE, ADDRESS, CREATED,
            CURRENCY, DELINQUENT, DESCRIPTION, DISCOUNT, EMAIL, INVOICEPREFIX, INVOICESETTINGS,
            METADATA, GIVENNAME, SURNAME, PHONE);

        // Test the Customer instance
        final String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture("fixtures/customer.json"), Customer.class));
        assertThat(MAPPER.writeValueAsString(customer)).isEqualTo(expected);

    }

    /**
     * Test deserialization from JSON
     */
    @Test
    @DisplayName("Test Customer model deserialization")
    public void deserializesFromJSON() throws Exception {
        // Build the Customer instance
        final Customer customer = new Customer(ID, OBJECT, SUBJECT, BALANCE, ADDRESS, CREATED,
            CURRENCY, DELINQUENT, DESCRIPTION, DISCOUNT, EMAIL, INVOICEPREFIX, INVOICESETTINGS,
            METADATA, GIVENNAME, SURNAME, PHONE);

        // Test the Customer instance
        final Customer deserializedCustomer =
            MAPPER.readValue(fixture("fixtures/customer.json"), Customer.class);
        assertThat(deserializedCustomer).isEqualTo(customer);
    }
}