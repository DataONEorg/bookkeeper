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
            assertEquals(11, resultSet.getInt(1));
            assertFalse(resultSet.next());

            // Check for
            resultSet = statement.executeQuery("SELECT count(*) FROM quotas WHERE customerId IS NULL;");
            assertTrue(resultSet.next());
            assertEquals(11, resultSet.getInt(1));

        } catch (SQLException sqle) {
            sqle.printStackTrace();
            fail();
        }
    }

    /**
     * Test reading a specific quota
     */
    @DisplayName("Test 10GB Quota")
    @Test
    public void testSelect10GBQuota() {
        try {

            // Check for 11 preset quotas
            Statement statement = connection.createStatement();
            ResultSet resultSet =
                statement.executeQuery("SELECT * FROM quotas WHERE softLimit = 10995116277760;");
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
                    "(object, orcid, balance, address, created, currency, delinquent, " +
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
