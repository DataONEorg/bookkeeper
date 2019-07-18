package org.dataone.bookkeeper.api;


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the quota model
 */
class QuotaTest {
    private final static ObjectMapper MAPPER = Jackson.newObjectMapper();
    static {
        MAPPER.setSerializationInclusion(Include.NON_NULL);
        MAPPER.setSerializationInclusion(Include.NON_EMPTY);
    }
    private final static String QUOTA_JSON = "fixtures/quota.json";
    private static final Integer ID = 1;
    private static final String OBJECT = "quota";
    private static final String NAME = "custom_portal_count";
    private static final Integer SOFTLIMIT = 3;
    private static final Integer HARDLIMIT = 3;
    private static final String UNIT =  "portal";
    private static final Integer CUSTOMER_ID = null;

    /**
     * Test serialization to JSON
     */
    @Test
    @DisplayName("Test Quota model serialization")
    public void serializesToJSON() throws Exception {
        // Build the Customer instance
        final Quota quota = new Quota(ID, OBJECT, NAME, SOFTLIMIT, HARDLIMIT, UNIT, CUSTOMER_ID);
        quota.getCustomerId();
        // Test the Customer instance
        final String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture("fixtures/quota.json"), Quota.class));
        assertThat(MAPPER.writeValueAsString(quota)).isEqualTo(expected);

    }

}