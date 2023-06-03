/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2023
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

import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.sql.Connection;
import org.eclipse.jetty.util.component.LifeCycle;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jdbi3.strategies.TimedAnnotationNameStrategy;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;


/**
 * A base class for initializing an embedded database for testing
 */
public class BaseTestCase {

    /* The embedded database reference */
    private static PostgreSQLContainer<?> pg;

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
     * Initialize test resources - start a PostgreSQL database container
     */

    @BeforeAll
    public static void initAll() {
        try {
            pg = new PostgreSQLContainer<>("postgres:14");
            pg.start();

            // Make a connection available to tests
            connection = pg.createConnection("?TC_DAEMON=true");

            // Run the production database migrations
            flyway = Flyway.configure()
                .dataSource(pg.getJdbcUrl(), pg.getUsername(), pg.getPassword())
                .locations("filesystem:helm/db/migrations")
                .cleanDisabled(false)
                .load();
            flyway.migrate();

            // Create a Dropwizard environment for testing
            environment = new Environment("bookkeeper");

            // Set up a PostgreSQL datasource for testing (Stores)

            dataSourceFactory.setUrl(pg.getJdbcUrl());
            dataSourceFactory.setUser(pg.getUsername());
            dataSourceFactory.setPassword(pg.getPassword());
            dataSourceFactory.setDriverClass(pg.getDriverClassName());
            dataSourceFactory.asSingleConnectionPool();

            // Initialize a dbi instance for tests to use
            dbi = new JdbiFactory(new TimedAnnotationNameStrategy())
                .build(environment, dataSourceFactory, "postgresql");

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
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
