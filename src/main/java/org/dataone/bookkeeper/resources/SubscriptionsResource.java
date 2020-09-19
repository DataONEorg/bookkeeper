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
import org.dataone.bookkeeper.api.*;
import org.dataone.bookkeeper.jdbi.CustomerStore;
import org.dataone.bookkeeper.jdbi.SubscriptionStore;
import org.dataone.bookkeeper.security.DataONEAuthHelper;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.PermitAll;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The entry point to the subscriptions collection
 */
@Timed
@Path("/subscriptions")
@Produces(MediaType.APPLICATION_JSON)

public class SubscriptionsResource extends BaseResource {

    /* The logging facility for this class */
    private Log log = LogFactory.getLog(SubscriptionsResource.class);

    /* The subscriptio store for database calls */
    private final SubscriptionStore subscriptionStore;

    /* The customer store for database calls */
    private final CustomerStore customerStore;

    /* An instance of the DataONE authn and authz delegate */
    private final DataONEAuthHelper dataoneAuthHelper;

    /**
     * Construct a quota collection
     * @param database  the jdbi database access reference
     */
    public SubscriptionsResource(Jdbi database, DataONEAuthHelper dataoneAuthHelper) {
        this.subscriptionStore = database.onDemand(SubscriptionStore.class);
        this.customerStore = database.onDemand(CustomerStore.class);
        this.dataoneAuthHelper = dataoneAuthHelper;

    }

    /**
     * List quotas, optionally by subscriptionId or subscriber.
     * Use start and count to get paginated results
     * @param start  the paging start index
     * @param count  the paging size count
     * @param subscribers the list of subscribers to fetch the subscription for
     * @param requestor the DataONE subject to make the request as
     * @return subscriptions the subscriptions list
     */
    @Timed
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public SubscriptionList listSubscriptions(
            @Context SecurityContext context,
            @QueryParam("start") @DefaultValue("0") Integer start,
            @QueryParam("count") @DefaultValue("1000") Integer count,
            @QueryParam("subscriber") Set<String> subscribers,
            @QueryParam("requestor") String requestor) throws WebApplicationException {

        // The calling user injected in the security context via authentication
        Customer caller = (Customer) context.getUserPrincipal();
        List<Subscription> subscriptions = new ArrayList<>();
        boolean isAdmin = this.dataoneAuthHelper.isAdmin(caller.getSubject())
                || this.dataoneAuthHelper.isBookkeeperAdmin(caller.getSubject());

        Set<String> associatedSubscribers;
        List<String> approvedSubscribers = new ArrayList<>();
        Boolean isProxy = isAdmin && requestor != null;

        // Admin users can make request as another user
        if (requestor != null) {
            if (isAdmin) {
                // Create a new Customer based on the 'requestor' parameter - don't update the subject directly in the
                // context, which is cached.
                try {
                    caller = this.dataoneAuthHelper.createCustomerFromSubject(requestor);
                } catch (io.dropwizard.auth.AuthenticationException dae) {
                    String message = "The requested subscriptions couldn't be listed: " + dae.getMessage();
                    throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
                }
            } else {
                throw new WebApplicationException(caller.getSubject() + " does not have admin privilege needed to set 'requestor'. ", Response.Status.FORBIDDEN);
            }
        }

        /* Determine if the caller is allowed to retrieve subscriptions for the specified subscribers */
        if (subscribers != null && subscribers.size() > 0) {
            // Filter out non-associated subscribers if not an admin
            if (!isAdmin || isProxy) {
                associatedSubscribers =
                        this.dataoneAuthHelper.filterByAssociatedSubjects(caller, subscribers);
                if (associatedSubscribers.size() > 0) {
                    approvedSubscribers.addAll(associatedSubscribers);
                }

                /* Caller is not admin and is not associated with any of the specified subscribers. */
                if (approvedSubscribers.size() == 0) {
                    throw new WebApplicationException("The requested subscribers don't exist or requestor doesn't have privilege to view them.", Response.Status.FORBIDDEN);
                }
            } else {
                /* Admin caller, so can see quotas for all requested subscribers */
                approvedSubscribers.addAll(subscribers);
            }
        } else {
            /** No subscribers specified and caller is not admin, so caller is allowed to
             view any subscription for subscribers with which they are associated.
             If the caller is admin, then don't set subject, so that subscriptions for all subscribers requested can be viewed.
             */
            if (!isAdmin || isProxy) {
                if (approvedSubscribers.size() == 0) {
                    approvedSubscribers = new ArrayList(this.dataoneAuthHelper.getAssociatedSubjects(caller));
                }
            }
        }

        if (approvedSubscribers.size() > 0) {
            subscriptions = subscriptionStore.findSubscriptionsBySubscribers(approvedSubscribers);
        } else {
            subscriptions = subscriptionStore.listSubscriptions();
        }

        if (subscriptions == null || subscriptions.size() == 0) {
            if (! isAdmin || isProxy) {
                // If not an admin user or is a proxy user, we have no way to determine if they didn't have enough
                // privilege or if the quotas don't exist.
                throw new WebApplicationException("The requested subscriptions were not found or requestor does not have privilege to view them.", Response.Status.NOT_FOUND);
            } else {
                // Admin user can see any existing quota, so can't be a priv issue.
                throw new WebApplicationException("The requested subscriptions were not found.", Response.Status.NOT_FOUND);
            }
        }

        // TODO: Incorporate paging params - new SubscriptionList(start, count, total, quotas)
        return new SubscriptionList(subscriptions);
    }

    /**
     * Get the subscription given an id
     * @param subscriptionId the subscription id
     * @return  the subscription for the id
     */
    @Timed
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{subscriptionId}")
    public Subscription retrieve(
            @Context SecurityContext context,
            @PathParam("subscriptionId") @NotNull Integer subscriptionId)
            throws WebApplicationException {

        Customer caller = (Customer) context.getUserPrincipal();
        Subscription subscription = null;

        boolean isAdmin = this.dataoneAuthHelper.isAdmin(caller.getSubject())
                || this.dataoneAuthHelper.isBookkeeperAdmin(caller.getSubject());

        /* Admin user is making this request as another subject */
        if (!isAdmin) {
            throw new WebApplicationException("Admin privilege is required to retrieve a subscription, " + caller.getSubject() + " is not authorized.", Response.Status.FORBIDDEN);
        }

        // Get the subscription from the store
        try {
            subscription = subscriptionStore.getSubscription(subscriptionId);

            if ( isAdmin) {
                return subscription;
            } else {
                // Ensure the caller is asssociated with the quota subscriber
                // TODO: add customer 'subject' to subscription object
                Customer customer = customerStore.getCustomer(subscription.getCustomerId());
                String subscriber = customer.getSubject();

                Set<String> subscribers = new HashSet<String>();
                subscribers.add(subscriber);
                Set<String> associatedSubscribers =
                    this.dataoneAuthHelper.filterByAssociatedSubjects(caller, subscribers);
                if (associatedSubscribers.size() > 0) {
                    return subscription;
                } else {
                    throw new WebApplicationException(caller.getSubject() + " is not associated with this subscription.", Response.Status.FORBIDDEN);
                }
            }
        } catch (Exception e) {
            String message = "Couldn't get the subscription: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.NOT_FOUND);
        }
    }
}
