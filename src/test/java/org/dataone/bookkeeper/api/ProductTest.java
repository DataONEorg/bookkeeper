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
import org.dataone.bookkeeper.helpers.ProductHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static io.dropwizard.testing.FixtureHelpers.fixture;

/**
 * Test the Product model
 */
@DisplayName("Product model test")
public class ProductTest {
    private final static ObjectMapper MAPPER = Jackson.newObjectMapper();
    static {
        MAPPER.setSerializationInclusion(Include.NON_NULL);
        MAPPER.setSerializationInclusion(Include.NON_EMPTY);
    }
    private final static String PRODUCT_JSON = "fixtures/product.json";

    /**
     * Test serialization to JSON
     * @throws Exception
     */
    @Test
    @DisplayName("Test Product model serialization")
    public void serializesToJSON() throws Exception {
        // Build a Product instance
        final Product product = ProductHelper.createTestProduct(1);
        final String actual = MAPPER.writeValueAsString(product);

        // Test the Product instance
        final String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture(PRODUCT_JSON), Product.class));
        assertThat(actual).isEqualTo(expected);
    }

    /**
     * Test deserialization from JSON
     */
    @Test
    @DisplayName("Test Product model deserialization")
    public void deserializesFromJSON() throws Exception {
        // Build the Product instance
        final Product product = ProductHelper.createTestProduct(1);

        // Test the Product instance
        final Product deserializedProduct =
            MAPPER.readValue(fixture(PRODUCT_JSON), Product.class);
        assertThat(deserializedProduct).isEqualTo(product);

    }

}
