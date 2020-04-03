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
        "portal",
        3.0,
        3.0,
        null,
        "portal",
        null,
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

    /**
     * Test deserialization from JSON
     */
    @Test
    @DisplayName("Test Feature model deserialization")
    public void deserializesFromJSON() throws Exception {
        // Build the Feature instance
        final Feature feature = new Feature(NAME, LABEL, DESCRIPTION, QUOTA);

        // Test the Feature instance
        final Feature deserializedFeature =
            MAPPER.readValue(fixture("fixtures/feature.json"), Feature.class);
        assertThat(deserializedFeature).isEqualTo(feature);
    }
}