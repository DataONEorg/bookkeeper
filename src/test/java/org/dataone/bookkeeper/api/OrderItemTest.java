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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the OrderItem model
 */
public class OrderItemTest {
    private final static ObjectMapper MAPPER = Jackson.newObjectMapper();
    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
    private static final String OBJECT = "order_item";
    private static final Integer AMOUNT = 500;
    private static final String CURRENCY = "USD";
    private static final String DESCRIPTION = "DataONE Individual Membership";
    private static final Integer PARENT = 12345;
    private static final Integer QUANTITY = 1;
    private static final String TYPE = "sku";

    @Test
    @DisplayName("Test OrderItem model serialization")
    public void serializesToJSON() throws Exception {
        // Build the OrderItem instance
        final OrderItem orderItem =
            new OrderItem(OBJECT, AMOUNT, CURRENCY, DESCRIPTION, PARENT, QUANTITY, TYPE);

        // Test the OrderItem instance
        final String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture("fixtures/orderItem.json"), OrderItem.class));
        assertThat(MAPPER.writeValueAsString(orderItem)).isEqualTo(expected);
    }

    /**
     * Test deserialization from JSON
     */
    @Test
    @DisplayName("Test OrderItem model deserialization")
    public void deserializesFromJSON() throws Exception {
        // Build the OrderItem instance
        final OrderItem orderItem =
            new OrderItem(OBJECT, AMOUNT, CURRENCY, DESCRIPTION, PARENT, QUANTITY, TYPE);

        // Test the OrderItem instance
        final OrderItem deserializedOrderItem =
            MAPPER.readValue(fixture("fixtures/orderItem.json"), OrderItem.class);
        assertThat(deserializedOrderItem).isEqualTo(orderItem);
    }

}
