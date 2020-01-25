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

package org.dataone.bookkeeper;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.api.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Test functionality of the embedded PostgeSQL database
 */
public class EmbeddedPostgresqlTest extends BaseTestCase {

    /**
     * Test that we can query the embedded database
     */
    @Test
    public void testEmbeddedConnectivity() {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT 1");
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt(1));
            assertFalse(resultSet.next());

        } catch (SQLException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test the quota table integrity
     */
    @DisplayName("Test Preset Quota Count")
    @Test
    public void testSelectPresetQuotaCount() {
        try {

            // Check for 11 preset quotas
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT count(*) FROM quotas;");
            assertTrue(resultSet.next());
            assertEquals(3, resultSet.getInt(1));
            assertFalse(resultSet.next());

            // Check for
            resultSet = statement.executeQuery("SELECT count(*) FROM quotas WHERE subscriptionId IS NULL;");
            assertTrue(resultSet.next());
            assertEquals(3, resultSet.getInt(1));

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            fail();
        }
    }

    /**
     * Test reading a specific quota
     */
    @DisplayName("Test 1GB Quota")
    @Test
    public void testSelect1GBQuota() {
        try {

            // Check for 3 preset quotas
            Statement statement = connection.createStatement();
            ResultSet resultSet =
                statement.executeQuery("SELECT * FROM quotas WHERE softLimit = 1073741824.0;");
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getRow());
            assertFalse(resultSet.next());

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            fail();
        }
    }

    /**
     * Test inserting a customer
     */
    @DisplayName("Test Customer Insert")
    @Test
    public void testInsertCustomer() {
        try {
            Statement statement = connection.createStatement();
            String sql =
                "INSERT INTO customers " +
                    "(object, subject, balance, address, created, currency, delinquent, " +
                    "description, discount, email, invoicePrefix, invoiceSettings, " +
                    "metadata, givenName, surName, phone) " +
                "VALUES " +
                    "('customer', " +
                    "'http://orcid.org/0000-0002-8121-2341', " +
                    "0, " +
                    "'{\"line1\": \"735 State Street\", " +
                    " \"line2\": \"Suite 300\", " +
                    " \"city\": \"Santa Barbara\", " +
                    " \"state\": \"CA\", " +
                    " \"postalCode\": \"93106\", " +
                    " \"country\": \"USA\"}',' " +
                    Instant.now().toString() +
                    "', " +
                    "'USD', " +
                    "false, " +
                    "'No description', " +
                    "NULL, " +
                    "'cjones@nceas.ucsb.edu'," +
                    "NULL, " +
                    "NULL, " +
                    "NULL, " +
                    "'Christopher'," +
                    "'Jones', " +
                    "'805-893-2500'" +
                    ");";
            statement.execute(sql);

            sql = "SELECT * FROM customers;";
            ResultSet resultSet = statement.executeQuery(sql);
            assertTrue(resultSet.next());
            assertEquals(1,resultSet.getInt(1));
            assertEquals("Christopher", resultSet.getString("givenName"));
            assertEquals("Jones", resultSet.getString("surName"));
            String addressJSON = resultSet.getString("address");
            ObjectMapper mapper = Jackson.newObjectMapper();
            Address address = mapper.readValue(addressJSON, Address.class);
            assertEquals("Santa Barbara", address.getCity());
            assertFalse(resultSet.next());

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
