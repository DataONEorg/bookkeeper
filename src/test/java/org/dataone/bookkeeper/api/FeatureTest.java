package org.dataone.bookkeeper.api;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the Feature model
 */
@DisplayName("Feature model test")
class FeatureTest {
    private final static ObjectMapper MAPPER = Jackson.newObjectMapper();
    static {
        MAPPER.setSerializationInclusion(Include.NON_NULL);
        MAPPER.setSerializationInclusion(Include.NON_EMPTY);
    }
    private final static String FEATURE_JSON = "fixtures/feature.json";
    private final static String NAME = "custom_portal";
    private final static String LABEL = "Branded Portals";
    private final static String DESCRIPTION = "Showcase your research, data, results, " +
        "and usage metrics by building a custom web portal.";
    private final static Quota QUOTA = new Quota(
        null,
        "quota",
        "custom_portal_count",
        3L,
        3L,
        "portal",
        null
    );

    /**
     * Test serialization to JSON
     */
    @Test
    @DisplayName("test Customer model serialization")
    public void serializesToJSON() throws Exception {
        // Build the Customer instance
        final Feature feature = new Feature(NAME, LABEL, DESCRIPTION, QUOTA);

        // Test the Feature instance
        final String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture(FEATURE_JSON), Feature.class));
        assertThat(MAPPER.writeValueAsString(feature)).isEqualTo(expected);
    }
}