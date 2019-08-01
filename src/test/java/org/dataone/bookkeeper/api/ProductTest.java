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
