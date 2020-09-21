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
import org.dataone.bookkeeper.security.DataONEAuthHelper;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.Instant;
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

    /* An instance of the DataONE authn and authz delegate */
    private final DataONEAuthHelper dataoneAuthHelper;

    /**
     * Construct a customer collection
     * @param database  the jdbi database access reference
     */
    public CustomersResource(Jdbi database, DataONEAuthHelper dataoneAuthHelper) {
        this.customerStore = database.onDemand(CustomerStore.class);
        this.dataoneAuthHelper = dataoneAuthHelper;
    }

    /**
     * List customers, optionally by membershipId or subject.
     * Use start and count to get paginated results
     * @param start  the paging start index
     * @param count  the paging size count
     * @return customers  the customer list
     */
    @Timed
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public CustomerList listCustomers(
        @Context SecurityContext context,
        @QueryParam("start") @DefaultValue("0") Integer start,
        @QueryParam("count") @DefaultValue("1000") Integer count,
        @QueryParam("email") @Email String email,
        @QueryParam("subject") String subject)
        throws WebApplicationException {

        Customer caller = (Customer) context.getUserPrincipal();
        Boolean isBkAdmin = this.dataoneAuthHelper.isBookkeeperAdmin(caller.getSubject());

        List<Customer> customers = new ArrayList<Customer>();
        Customer customer;
        // List customers, checking privilege status before returning
        if ( subject != null && ! subject.isEmpty() ) {
            customer = customerStore.findCustomerBySubject(subject);
            // Before returning, throw NotAuthorized if customer.subject != token.sub
            if ((customer.getSubject().compareToIgnoreCase(caller.getSubject()) != 0) && ! isBkAdmin) {
                throw new WebApplicationException("Bookkeeper admin privilege is required list a customer other than the requestor's, " + caller.getSubject() + " is not authorized.", Response.Status.FORBIDDEN);
            }
            customers.add(customer);
        } else if ( email != null && ! email.isEmpty() ) {
            customer = customerStore.findCustomerByEmail(email);
            // Before returning, throw NotAuthorized if customer.subject != token.sub
            if ((customer.getSubject().compareToIgnoreCase(caller.getSubject()) != 0) && ! isBkAdmin) {
                throw new WebApplicationException("Bookkeeper admin privilege is required list a customer other than the requestor's, " + caller.getSubject() + " is not authorized.", Response.Status.FORBIDDEN);
            }
            customers.add(customer);
        } else {
            // If authenticated as an bookkeeper admin, list all customers
            if ( ! isBkAdmin ) {
                throw new WebApplicationException("Bookkeeper admin privilege is required list all customers, " + caller.getSubject() + " is not authorized.", Response.Status.FORBIDDEN);
            }
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
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Customer create(@Context SecurityContext context,
        @NotNull @Valid Customer customer) throws WebApplicationException {

        // Insert the customer after it is validated
        Customer caller = (Customer) context.getUserPrincipal();
        boolean isAdmin = this.dataoneAuthHelper.isAdmin(caller.getSubject());
        // Ensure the caller is the customer being created, except for admins
        if ( ! isAdmin ) {
            customer.setSubject(caller.getSubject());
        }
        Customer existing = null;
        try {
            // Ensure the email and subject are unique
            existing = customerStore.findCustomerByEmail(customer.getEmail());
            if ( existing != null ) {
                throw new Exception("A customer exists with the given email.");
            }
            existing = customerStore.findCustomerBySubject(customer.getSubject());
            if ( existing != null ) {
                throw new Exception("A customer exists with the given subject.");
            }

            // Create the customer
            customer.setCreated(new Integer((int) Instant.now().getEpochSecond()));
            Integer id = customerStore.insert(customer);
            customer = customerStore.getCustomer(id);
        } catch (Exception e) {
            String message = "Couldn't insert the customer: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.INTERNAL_SERVER_ERROR);
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
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{customerId: [0-9]+}")
    public Customer retrieve(@Context SecurityContext context,
        @PathParam("customerId") Integer customerId)
        throws WebApplicationException {

        Customer caller = (Customer) context.getUserPrincipal();
        boolean isAdmin = this.dataoneAuthHelper.isAdmin(caller.getSubject());
        Customer customer = null;
        // Get the customer from the store
        try {
            if ( customerId != null ) {
                customer = customerStore.getCustomer(customerId);
            }
            // Ensure the caller is the customer being retrieved, except for admins
            if ( ! isAdmin ) {
                if ( customer != null && ! customer.getSubject().equals(caller.getSubject()) ) {
                    throw new Exception("The caller and customer subject don't match.");
                }
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
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{customerId}")
    public Customer update(@Context SecurityContext context,
        @NotNull @Valid Customer customer) throws WebApplicationException {

        // Update the customer after validation
        Customer caller = (Customer) context.getUserPrincipal();
        boolean isAdmin = this.dataoneAuthHelper.isAdmin(caller.getSubject());

        try {
            Customer existing = customerStore.getCustomer(customer.getId());

            // Ensure the caller is the customer being updated, except for admins
            if ( ! isAdmin ) {
                if ( existing != null && ! existing.getSubject().equals(caller.getSubject()) ) {
                    throw new Exception("The caller and customer subject don't match.");
                }
            }

            // Vet fields to maintain server-maintained values
            assert existing != null;
            customer.setCreated(existing.getCreated());
            customer.setBalance(existing.getBalance());
            customer.setDelinquent(existing.isDelinquent());
            customer.setSubject(existing.getSubject());
            customer.setSubjectInfo(null);
            if (! isAdmin ) {
                customer.setDiscount(existing.getDiscount());
            }
            // Then update the customer
            Integer id = customerStore.update(customer);
        } catch (Exception e) {
            String message = "Couldn't update the customer: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.INTERNAL_SERVER_ERROR);
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
    @PermitAll
    @Path("{customerId}")
    public Response delete(
            @Context SecurityContext context,
            @PathParam("customerId") @Valid Integer customerId) throws WebApplicationException {

        Customer caller = (Customer) context.getUserPrincipal();
        if ( ! this.dataoneAuthHelper.isBookkeeperAdmin(caller.getSubject())) {
            throw new WebApplicationException("Bookkeeper admin privilege is required to delete a customer, " + caller.getSubject() + " is not authorized.", Response.Status.FORBIDDEN);
        }

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
