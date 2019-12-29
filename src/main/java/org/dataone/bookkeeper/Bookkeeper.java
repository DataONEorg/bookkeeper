/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2019. All rights reserved.
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

import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jdbi3.bundles.JdbiExceptionsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.dataone.bookkeeper.resources.CustomersResource;
import org.dataone.bookkeeper.resources.ProductsResource;
import org.dataone.bookkeeper.resources.QuotasResource;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.WebApplicationException;

/**
 * The main Bookkeeper REST service application
 */
public class Bookkeeper extends Application<BookkeeperConfiguration> {

    private Bootstrap<BookkeeperConfiguration> bootstrap;

    /**
     * Return the application name
     *
     * @return name The name of the application
     */
    @Override
    public String getName() {
        return "DataONE Bookkeeper";
    }

    /**
     * Initialize the application
     */
    @Override
    public void initialize(Bootstrap<BookkeeperConfiguration> bootstrap) {

        // Enable SSL reloading without restarting.
        // TODO: Configure a LetsEncrypt hook to call POST https://localhost:8081/tasks/ssl-reload
        // bootstrap.addBundle(new SslReloadBundle());

        // Use the JDBI database exception bundle for better logging
        bootstrap.addBundle(new JdbiExceptionsBundle());

    }

    /**
     * Run the Bookkeeper application
     * @param configuration The configuration used to bootstrap the application
     * @param environment The application environment
     * @throws WebApplicationException  a web app exception
     */
    @Override
    public void run(BookkeeperConfiguration configuration,
                    Environment environment) throws WebApplicationException {

        // Set up managed database access
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi database = factory.build(environment,
            configuration.getDataSourceFactory(), "postgresql");

        // TODO: Do we need to enable CORS, or let the ingress controller handle it?
        // https://stackoverflow.com/questions/25775364/enabling-cors-in-dropwizard-not-working#25801822

        // Register the products resource
        environment.jersey().register(new ProductsResource(database));

        // Register the quotas resource
        environment.jersey().register(new QuotasResource(database));

        // Register the customers resource
        environment.jersey().register(new CustomersResource(database));
    }

    /**
     * The Bookkeeper application entrypoint
     * @param args  arguments for the app
     * @throws Exception  all application exceptions
     */
    public static void main(String[] args) throws Exception {
        // Spin up the application
        new Bookkeeper().run(args);
    }
}
