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

import javax.validation.constraints.NotNull;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test the usage model
 */
class UsageTest {
    private final static ObjectMapper MAPPER = Jackson.newObjectMapper();
    static {
        MAPPER.setSerializationInclusion(Include.NON_NULL);
        MAPPER.setSerializationInclusion(Include.NON_EMPTY);
    }
    private final static String USAGE_JSON = "fixtures/usage.json";
    private static final Integer ID = 54321;
    private static final String OBJECT = "usage";
    private static final Integer QUOTA_ID = 1;
    private static final String INSTANCE_ID = "urn:uuid:56925d4b-9e46-49ec-96ea-38dc9ed0a64c";
    private static final @NotNull Double QUANTITY = 1.0;
    private static final String STATUS = "active";


    /**
     * Test serialization to JSON
     */
    @Test
    @DisplayName("Test Usage model serialization")
    public void serializesToJSON() throws Exception {
        // Build the Usage instance
        final Usage usage = new Usage(ID, OBJECT, QUOTA_ID, INSTANCE_ID, QUANTITY, STATUS);
        // Test the Usage instance
        final String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture("fixtures/usage.json"), Usage.class));
        assertThat(MAPPER.writeValueAsString(usage)).isEqualTo(expected);

    }

    /**
     * Test deserialization from JSON
     */
    @Test
    @DisplayName("Test Usage model deserialization")
    public void deserializesFromJSON() throws Exception {
        // Build the Usage instance
        final Usage usage = new Usage(ID, OBJECT, QUOTA_ID, INSTANCE_ID, QUANTITY, STATUS);
        // Test the Usage instance
        final Usage deserializedUsage =
            MAPPER.readValue(fixture("fixtures/usage.json"), Usage.class);
        assertThat(deserializedUsage).isEqualTo(usage);
    }
}