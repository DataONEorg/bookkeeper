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
