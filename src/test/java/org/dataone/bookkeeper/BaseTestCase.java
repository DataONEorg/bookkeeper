package org.dataone.bookkeeper;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jdbi3.strategies.TimedAnnotationNameStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.setup.Environment;
import org.dataone.bookkeeper.api.Quota;
import org.eclipse.jetty.util.component.LifeCycle;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * A base class for initializing an embedded database for testing
 */
public class BaseTestCase {

    // The embedded database reference
    public static EmbeddedPostgres pg = EmbeddedPostgresqlExtension.pg;

    // A connection to the database
    public static Connection connection = EmbeddedPostgresqlExtension.connection;

    // The Flyway database migrator used to manage database schema integrity
    public static Flyway flyway = EmbeddedPostgresqlExtension.flyway;

    // The metrics registry for testing
    public static MetricRegistry metricRegistry = new MetricRegistry();

    // The Dropwizard environment used in tests
    public static Environment environment;

    // The data source factory used to get a postgresql datasource
    public static DataSourceFactory dataSourceFactory = new DataSourceFactory();

    // The JDBI instance used in testing
    public static Jdbi dbi;

    /**
     * Initialize test resources - start an embedded PostgreSQL database
     */

    @BeforeAll
    public static void initAll() {
        try {

            // Try to optimize the PG database for testing with anti-persistence
            // options (fsync, full_page_writes)
            pg = EmbeddedPostgres.builder()
                .setPort(5432)
                .setServerConfig("shared_buffers", "1024MB")
                .setServerConfig("work_mem", "25MB")
                .setServerConfig("fsync", "off")
                .setServerConfig("full_page_writes", "off")
                .start();

            // Make a connection available to tests
            connection = pg.getPostgresDatabase().getConnection();

            // Run the production database migrations
            flyway = Flyway.configure()
                .dataSource(pg.getPostgresDatabase())
                .locations("db/migrations")
                .load();
            flyway.migrate();

            // Create a Dropwizard environment for testing
            environment = new Environment("bookkeeper",
                new ObjectMapper(), Validators.newValidator(), metricRegistry,
                ClassLoader.getSystemClassLoader());

            // Set up a PostgreSQL datasource for testing (DAOs)

            dataSourceFactory.setUrl("jdbc:postgresql://localhost:5432/postgres");
            dataSourceFactory.setUser("postgres");
            dataSourceFactory.setPassword("postgres");
            dataSourceFactory.setDriverClass("org.postgresql.Driver");
            dataSourceFactory.asSingleConnectionPool();

            // Initialize a dbi instance for tests to use
            dbi = new JdbiFactory(new TimedAnnotationNameStrategy())
                .build(environment, dataSourceFactory, "postgresql");

            // Start all managed objects in the environment
/*
            for (LifeCycle lifeCycle : environment.lifecycle().getManagedObjects() ) {
                lifeCycle.start();
            }
*/

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Clean up after all tests as needed
     */
    @AfterAll
    public static void tearDownAll() {

        try {

            // Stop all managed objects in the environment
            for (LifeCycle lifeCycle : environment.lifecycle().getManagedObjects() ) {
                lifeCycle.stop();
            }

            // Clean the database
            flyway.clean();

            // Close the database
            pg.close();

        } catch (IOException e) {
            e.printStackTrace();
            fail();

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Insert a test customer with the given customer id
     * @param customerId
     * @return
     */
    public Integer insertTestCustomer(Integer customerId) throws SQLException {


        dbi.useHandle(handle -> {
            handle.execute("INSERT INTO customers " +
                "(id, object, orcid, balance, address, created, currency, delinquent, " +
                "description, discount, email, invoicePrefix, invoiceSettings, " +
                "metadata, givenName, surName, phone) " +
                "VALUES (?, ?, ?, ?, to_json(?), to_timestamp(?), ?, ?, " +
                "?, to_json(?), ?, ?, to_json(?), " +
                "to_json(?), ?, ?, ?)",
                customerId,
                "customer",
                "http://orcid.org/0000-0002-8121-2341",
                0,
                "{}",
                1562866734,
                "USD",
                false,
                null,
                "{}",
                "cjones@nceas.ucsb.edu",
                null,
                "{}",
                "{}",
                "Christopher",
                "Jones",
                "805-893-2500");
        });

        return customerId;
    }

    /**
     * Insert a test quota with a given id and customer id
     * @param quotaId
     * @param customerId
     * @return
     */
    public Integer insertTestQuotaWithCustomer(Integer quotaId, Integer customerId)  throws SQLException {
        dbi.useHandle(handle ->
            handle.execute("INSERT INTO quotas " +
                "(id, object, name, softLimit, hardLimit, unit, customerId) " +
                "VALUES " +
                "(?, ?, ?, ?, ?, ?, ?)",
                quotaId,
                "quota",
                "test_customer_quota",
                12345,
                123450,
                "megabyte", customerId)
        );
        return quotaId;
    }

    /**
     * Remove a test quota given its id
     * @param quotaId
     */
    public void removeTestQuota(Integer quotaId) throws SQLException {

        dbi.useHandle(handle ->
            handle.execute("DELETE FROM quotas WHERE id = ?", quotaId)
        );
    }

    /**
     * Remove a test quota given its id
     * @param customerId
     */
    public void removeTestCustomer(Integer customerId) throws SQLException {

        dbi.useHandle(handle ->
            handle.execute("DELETE FROM customers WHERE id = ?", customerId)
        );
    }

    /**
     * Generate a random Integer for use as test case identifiers
     * @return
     */
    public Integer getRandomId() {
        int randomInt = ThreadLocalRandom.current().nextInt();
        return new Integer(randomInt);
    }

    /**
     * Return the number of quotas in the database for the given quota name
     * @param quotaName
     * @return
     */
    public Integer getQuotaCountByName(String quotaName) throws SQLException {

        Integer count = dbi.withHandle(handle ->
            handle.createQuery("SELECT count(*) FROM quotas WHERE name = :quotaName")
                .bind("quotaName", quotaName)
                .mapTo(Integer.class)
                .one()
        );

        return count;
    }

    public Integer getQuotaCountById(Integer quotaId) {
        Integer count = dbi.withHandle(handle ->
            handle.createQuery("SELECT count(*) FROM quotas WHERE id = :id")
                .bind("id", quotaId)
                .mapTo(Integer.class)
                .one()
            );
        return count;
    }
    /**
     * Return the quota id for the given quota name
     * @param quotaName
     * @return
     */
    public Integer getQuotaIdByName(String quotaName) throws SQLException {
        Integer quotaId = dbi.withHandle(handle ->
            handle.createQuery("SELECT id FROM quotas WHERE name = :quotaName")
                .bind("quotaName", quotaName)
                .mapTo(Integer.class)
                .one()
        );
        return quotaId;
    }

    /**
     * Return a quota instance given a quota id
     * @param quotaId
     * @return
     */
    public Quota getQuotaById(Integer quotaId) {
        Quota quota = dbi.withHandle(handle ->
            handle.createQuery("SELECT id, object, name, softLimit, hardLimit, unit " +
                "FROM quotas WHERE id = :id")
                .bind("id", quotaId)
                .mapToBean(Quota.class)
                .one()
        );
        return quota;
    }
}
