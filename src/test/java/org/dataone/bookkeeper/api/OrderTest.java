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
import org.dataone.bookkeeper.helpers.OrderHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.dataone.bookkeeper.helpers.OrderHelper.*;

/**
 * Test the Order model
 */
public class OrderTest {
    private final static ObjectMapper MAPPER = Jackson.newObjectMapper();
    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
    private final static String ORDER_JSON = "fixtures/order.json";

    /**
     * Test serialization to JSON
     * @throws Exception
     */
    @Test
    @DisplayName("Test Order model serialization")
    public void serializesToJSON() throws Exception {
        // Build a Order instance
        final Order order = createTestOrder(1, 2, 3, 4);
        final String actual = MAPPER.writeValueAsString(order);

        // Test the Order instance
        final String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture(ORDER_JSON), Order.class));
        assertThat(actual).isEqualTo(expected);
    }

    /**
     * Test deserialization from JSON
     */
    @Test
    @DisplayName("Test Order model deserialization")
    public void deserializesFromJSON() throws Exception {
        // Build the Order instance
        final Order order = OrderHelper.createTestOrder(1,2,3,4);

        // Test the Order instance
        final Order deserializedOrder =
            MAPPER.readValue(fixture(ORDER_JSON), Order.class);
        assertThat(deserializedOrder).isEqualTo(order);

    }

}
