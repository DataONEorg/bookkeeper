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

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jdbi3.strategies.TimedAnnotationNameStrategy;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.util.component.LifeCycle;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * A base class for initializing an embedded database for testing
 */
public class BaseTestCase {

    /* The embedded database reference */
    private static EmbeddedPostgres pg;

    /* A connection to the database */
    static Connection connection;

    /* The Flyway database migrator used to manage database schema integrity */
    private static Flyway flyway;

    /* The metrics registry for testing */
    private static MetricRegistry metricRegistry = new MetricRegistry();

    /* The Dropwizard environment used in tests */
    private static Environment environment;

    /* The data source factory used to get a postgresql datasource */
    private static DataSourceFactory dataSourceFactory = new DataSourceFactory();

    /* The JDBI instance */
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
                .locations("filesystem:helm/db/migrations")
                .load();
            flyway.migrate();

            // Create a Dropwizard environment for testing
            environment = new Environment("bookkeeper");

            // Set up a PostgreSQL datasource for testing (Stores)

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

}
