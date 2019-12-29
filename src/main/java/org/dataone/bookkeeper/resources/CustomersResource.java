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

package org.dataone.bookkeeper.resources;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.CustomerList;
import org.dataone.bookkeeper.jdbi.CustomerStore;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * The entry point to the customers collection
 */
@Timed
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
public class CustomersResource extends BaseResource {

    /* The logging facility for this class */
    private Log log = LogFactory.getLog(CustomersResource.class);

    /* The customer store for database calls */
    private final CustomerStore customerStore;

    /**
     * Construct a customer collection
     * @param database  the jdbi database access reference
     */
    public CustomersResource(Jdbi database) {
        this.customerStore = database.onDemand(CustomerStore.class);
    }

    /**
     * List customers, optionally by subscriptionId or subject.
     * Use start and count to get paginated results
     * @param start  the paging start index
     * @param count  the paging size count
     * @return customers  the customer list
     */
    @Timed
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerList listCustomers(
        @QueryParam("start") @DefaultValue("0") Integer start,
        @QueryParam("count") @DefaultValue("1000") Integer count,
        @QueryParam("email") @Email String email,
        @QueryParam("subject") String subject)
        throws WebApplicationException {

        // TODO: Incorporate authentication
        List<Customer> customers = new ArrayList<Customer>();
        Customer customer;
        // TODO: If authenticated as an admin, list all customers
        if ( subject != null && ! subject.isEmpty() ) {
            customer = customerStore.findCustomerBySubject(subject);
            // TODO: Before returning, throw NotAuthorized if customer.subject != token.sub
            customers.add(customer);
        } else if ( email != null && ! email.isEmpty() ) {
            customer = customerStore.findCustomerByEmail(email);
            // TODO: Before returning, throw NotAuthorized if customer.subject != token.sub
            customers.add(customer);
        } else {
            // This should be admin access only
            customers = customerStore.listCustomers();
        }

        // TODO: Incorporate paging params - new CustomerList(start, count, total, customers)
        return new CustomerList(customers);
    }

    /**
     * Create the given customer
     * @param customer  the customer to create
     * @return customer  the created customer
     */
    @Timed
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Customer create(@NotNull @Valid Customer customer) throws WebApplicationException {
        // Insert the customer after it is validated
        Customer existing = null;
        try {
            // Ensure the email and subject are unique
            existing = customerStore.findCustomerByEmail(customer.getEmail());
            if ( existing != null ) {
                throw new Exception("A customer exists with the given email.");
            } else {
                existing = customerStore.findCustomerBySubject(customer.getSubject());
                if ( existing != null ) {
                    throw new Exception("A customer exists with the given subject.");
                }
            }
            Integer id = customerStore.insert(customer);
            customer = customerStore.getCustomer(id);
        } catch (Exception e) {
            String message = "Couldn't insert the customer: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.EXPECTATION_FAILED);
        }
        return customer;
    }

    /**
     * Get the customer given an id
     * @param customerId  the customer id
     * @return  the customer for the id
     */
    @Timed
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{customerId: [0-9]+}")
    public Customer retrieve(@PathParam("customerId") Integer customerId)
        throws WebApplicationException {

        Customer customer = null;
        // Get the customer from the store
        try {
            if ( customerId != null ) {
                customer = customerStore.getCustomer(customerId);
            }
        } catch (Exception e) {
            String message = "Couldn't get the customer: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.NOT_FOUND);
        }
        return customer;
    }
    
    /**
     * Update the customer
     * @param customer  the customer to update
     * @return  the updated customer
     * @throws WebApplicationException  a web app exception
     */
    @Timed
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{customerId}")
    public Customer update(@NotNull @Valid Customer customer) throws WebApplicationException {
        // Update the customer after validation
        try {
            // TODO: Vet fields to maintain server-maintained values
            Customer existing = customerStore.getCustomer(customer.getId());
            Integer id = customerStore.update(customer);
        } catch (Exception e) {
            String message = "Couldn't update the customer: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.EXPECTATION_FAILED);
        }
        return customer;
    }

    /**
     * Delete the customer
     * @param customerId  the customer id
     * @return  response 200 if deleted
     * @throws WebApplicationException  a web app exception
     */
    @Timed
    @DELETE
    @Path("{customerId}")
    public Response delete(@PathParam("customerId") @Valid Integer customerId) throws WebApplicationException {
        String message = "The customerId cannot be null.";
        if (customerId == null) {
            throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
        }
        try {
            customerStore.delete(customerId);
        } catch (Exception e) {
            message = "Deleting the customer with id " + customerId + " failed: " + e.getMessage();
            log.error(message);
            e.printStackTrace();
            throw e;
        }
        return Response.ok().build();
    }
}
