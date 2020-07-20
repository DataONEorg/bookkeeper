/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2029
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
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the usagestatus model
 */
class UsageStatusTest {
    private final static ObjectMapper MAPPER = Jackson.newObjectMapper();
    static {
        MAPPER.setSerializationInclusion(Include.NON_NULL);
        MAPPER.setSerializationInclusion(Include.NON_EMPTY);
    }
    private final static String USAGESTATUS_JSON = "fixtures/usagestatus.json";
    private final static String OBJECT = "usagestatus";
    private static final String STATUS = "active";


    /**
     * Test serialization to JSON
     */
    @Test
    @Ignore
    @DisplayName("Test UsageStatus model serialization")
    public void serializesToJSON() throws Exception {
        // Build the UsageStatus instance
        final UsageStatus usagestatus = new UsageStatus(OBJECT, STATUS);
        // Test the UsageStatus instance
        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture(USAGESTATUS_JSON), UsageStatus.class));
        assertThat(MAPPER.writeValueAsString(usagestatus)).isEqualTo(expected);
    }

    /**
     * Test deserialization from JSON
     */
    @Test
    @Ignore
    @DisplayName("Test UsageStatus model deserialization")
    public void deserializesFromJSON() throws Exception {
        // Build the UsageStatus instance
        final UsageStatus usagestatus = new UsageStatus(OBJECT, STATUS);
        // Test the UsageStatus instance
        final UsageStatus deserializedUsageStatus =
                MAPPER.readValue(fixture(USAGESTATUS_JSON), UsageStatus.class);
        assertThat(deserializedUsageStatus).isEqualTo(usagestatus);
    }
}
