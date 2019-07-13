package org.dataone.bookkeeper.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
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
    private final static String PRODUCT_JSON = "fixtures/product.json";

    // Set fields for the Product instance to test
    private final static Long ID = 1L;
    private final static String OBJECT = "product";
    private final static boolean ACTIVE = true;
    private final static String NAME = "Organization";
    private final static String CAPTION = "Small institutions or groups";
    private final static String DESCRIPTION = "Create multiple portals for your work and projects. Help others understand and access your data.";
    private final static int CREATED = 1559768309;
    private final static String STATEMENT_DESCRIPTOR = "DataONE Membership Plan - Organization";
    private final static String TYPE = "service";
    private final static String UNIT_LABEL = "membership";
    private final static String URL = "https://dataone.org/memberships/organization";
    // Build up a feature list
    private final static Feature FEATURE1 = new Feature();
    private final static Feature FEATURE2 = new Feature();
    private final static Feature FEATURE3 = new Feature();
    private final static Feature FEATURE4 = new Feature();
    private final static Feature FEATURE5 = new Feature();
    private final static Feature FEATURE6 = new Feature();

    // Build a quota for feature 1
    private static final Long QUOTA_ID = 1L;
    private static final String QUOTA_TYPE = "quota";
    private static final String QUOTA_NAME = "custom_portal_count";
    private static final int QUOTA_SOFT_LIMIT = 3;
    private static final int QUOTA_HARD_LIMIT = 3;
    private static final String QUOTA_UNIT = "portal";

    private static final Quota QUOTA = new Quota(QUOTA_ID, QUOTA_TYPE, QUOTA_NAME,
        QUOTA_SOFT_LIMIT, QUOTA_HARD_LIMIT, QUOTA_UNIT);

    static {
        FEATURE1.setName("custom_portal");
        FEATURE1.setLabel("Branded Portals");
        FEATURE1.setDescription("Showcase your research, data, results, and usage metrics by building a custom web portal.");
        FEATURE1.setQuota(QUOTA);

        FEATURE2.setName("custom_search_filters");
        FEATURE2.setLabel("Custom Search Filters");
        FEATURE2.setDescription("Create custom search filters in your portal to allow scientists to search your holdings using filters appropriate to your field of science.");
        FEATURE2.setQuota(null);

        FEATURE3.setName("fair_data_assessment");
        FEATURE3.setLabel("FAIR Data Assessments");
        FEATURE3.setDescription("Access quality metric reports using the FAIR data suite of checks.");
        FEATURE3.setQuota(null);

        FEATURE4.setName("custom_quality_metrics");
        FEATURE4.setLabel("Custom Quality Metrics");
        FEATURE4.setDescription("Create a suite of custom quality metadata checks specific to your datasets.");
        FEATURE4.setQuota(null);

        FEATURE5.setName("aggregated_metrics");
        FEATURE5.setLabel("Aggregated Metrics");
        FEATURE5.setDescription("Access and share reports on aggregated usage metrics such as dataset views, data downloads, and dataset citations.");
        FEATURE5.setQuota(null);

        FEATURE6.setName("dataone_voting_member");
        FEATURE6.setLabel("DataONE Voting Member");
        FEATURE6.setDescription("Vote on the direction and priorities at DataONE Community meetings.");
        FEATURE6.setQuota(null);

    }

    // Add the feature list to the metadata object
    private final static ObjectNode METADATA = MAPPER.createObjectNode();
    static {
        METADATA.putArray("features")
            .addPOJO(FEATURE1)
            .addPOJO(FEATURE2)
            .addPOJO(FEATURE3)
            .addPOJO(FEATURE4)
            .addPOJO(FEATURE5)
            .addPOJO(FEATURE6);
    }
    /**
     * Test serialization to JSON
     * @throws Exception
     */
    @Test
    @DisplayName("Test Product model serialization")
    public void serializesToJSON() throws Exception {
        // Build a Product instance
        final Product product = new Product(ID, OBJECT, ACTIVE, NAME,
            CAPTION, DESCRIPTION, CREATED, STATEMENT_DESCRIPTOR, TYPE,
            UNIT_LABEL, URL, METADATA);

        // Test the Product instance
        final String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture("fixtures/product.json"), Product.class));
        assertThat(MAPPER.writeValueAsString(product)).isEqualTo(expected);
    }

}
