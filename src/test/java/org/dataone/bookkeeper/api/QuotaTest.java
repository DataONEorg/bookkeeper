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
    private static final Integer USAGE = null;
    private static final String UNIT =  "portal";
    private static final Integer SUBSCRIPTION_ID = null;
    private static final String SUBJECT = null;

    /**
     * Test serialization to JSON
     */
    @Test
    @DisplayName("Test Quota model serialization")
    public void serializesToJSON() throws Exception {
        // Build the Quota instance
        final Quota quota = new Quota(ID, OBJECT, NAME, SOFTLIMIT, HARDLIMIT,
            USAGE, UNIT, SUBSCRIPTION_ID, SUBJECT);
        // Test the Quota instance
        final String expected = MAPPER.writeValueAsString(
            MAPPER.readValue(fixture("fixtures/quota.json"), Quota.class));
        assertThat(MAPPER.writeValueAsString(quota)).isEqualTo(expected);

    }

    /**
     * Test deserialization from JSON
     */
    @Test
    @DisplayName("Test Quota model deserialization")
    public void deserializesFromJSON() throws Exception {
        // Build the Quota instance
        final Quota quota = new Quota(ID, OBJECT, NAME, SOFTLIMIT, HARDLIMIT,
            USAGE, UNIT, SUBSCRIPTION_ID, SUBJECT);

        // Test the Quota instance
        final Quota deserializedQuota =
            MAPPER.readValue(fixture("fixtures/quota.json"), Quota.class);
        assertThat(deserializedQuota).isEqualTo(quota);
    }
}