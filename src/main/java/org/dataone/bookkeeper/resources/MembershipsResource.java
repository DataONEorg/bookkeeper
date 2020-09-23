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
import org.dataone.bookkeeper.jdbi.MembershipStore;
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
 * The entry point to the memberships collection
 */
@Timed
@Path("/memberships")
@Produces(MediaType.APPLICATION_JSON)

public class MembershipsResource extends BaseResource {

    /* The logging facility for this class */
    private final Log log = LogFactory.getLog(MembershipsResource.class);

    /* The membership store for database calls */
    private final MembershipStore membershipStore;

    /* The customer store for database calls */
    private final CustomerStore customerStore;

    /* An instance of the DataONE authn and authz delegate */
    private final DataONEAuthHelper dataoneAuthHelper;

    /**
     * Construct a quota collection
     * @param database  the jdbi database access reference
     */
    public MembershipsResource(Jdbi database, DataONEAuthHelper dataoneAuthHelper) {
        this.membershipStore = database.onDemand(MembershipStore.class);
        this.customerStore = database.onDemand(CustomerStore.class);
        this.dataoneAuthHelper = dataoneAuthHelper;

    }

    /**
     * List quotas, optionally by membershipId or owner.
     * Use start and count to get paginated results
     * @param start  the paging start index
     * @param count  the paging size count
     * @param owners the list of owners to fetch the membership for
     * @param requestor the DataONE subject to make the request as
     * @return memberships the memberships list
     */
    @Timed
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public MembershipList listMemberships(
            @Context SecurityContext context,
            @QueryParam("start") @DefaultValue("0") Integer start,
            @QueryParam("count") @DefaultValue("1000") Integer count,
            @QueryParam("owner") Set<String> owners,
            @QueryParam("requestor") String requestor) throws WebApplicationException {

        // The calling user injected in the security context via authentication
        Customer caller = (Customer) context.getUserPrincipal();
        List<Membership> memberships = new ArrayList<>();
        boolean isAdmin = this.dataoneAuthHelper.isAdmin(caller.getSubject())
                || this.dataoneAuthHelper.isBookkeeperAdmin(caller.getSubject());

        Set<String> associatedOwners;
        List<String> approvedOwners = new ArrayList<>();
        Boolean isProxy = isAdmin && requestor != null;

        // Admin users can make request as another user
        if (requestor != null) {
            if (isAdmin) {
                // Create a new Customer based on the 'requestor' parameter - don't update the subject directly in the
                // context, which is cached.
                try {
                    caller = this.dataoneAuthHelper.createCustomerFromSubject(requestor);
                } catch (io.dropwizard.auth.AuthenticationException dae) {
                    String message = "The requested memberships couldn't be listed: " + dae.getMessage();
                    throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
                }
            } else {
                throw new WebApplicationException(caller.getSubject() + " does not have admin privilege needed to set 'requestor'. ", Response.Status.FORBIDDEN);
            }
        }

        /* Determine if the caller is allowed to retrieve memberships for the specified owners */
        if (owners != null && owners.size() > 0) {
            // Filter out non-associated owners if not an admin
            if (!isAdmin || isProxy) {
                associatedOwners =
                        this.dataoneAuthHelper.filterByAssociatedSubjects(caller, owners);
                if (associatedOwners.size() > 0) {
                    approvedOwners.addAll(associatedOwners);
                }

                /* Caller is not admin and is not associated with any of the specified owners. */
                if (approvedOwners.size() == 0) {
                    throw new WebApplicationException("The requested owners don't exist or requestor doesn't have privilege to view them.", Response.Status.FORBIDDEN);
                }
            } else {
                /* Admin caller, so can see quotas for all requested owners */
                approvedOwners.addAll(owners);
            }
        } else {
            /** No owners specified and caller is not admin, so caller is allowed to
             view any membership for owners with which they are associated.
             If the caller is admin, then don't set subject, so that memberships for all owners requested can be viewed.
             */
            if (!isAdmin || isProxy) {
                if (approvedOwners.size() == 0) {
                    approvedOwners = new ArrayList(this.dataoneAuthHelper.getAssociatedSubjects(caller));
                }
            }
        }

        if (approvedOwners.size() > 0) {
            memberships = membershipStore.findMembershipsByOwners(approvedOwners);
        } else {
            memberships = membershipStore.listMemberships();
        }

        if (memberships == null || memberships.size() == 0) {
            if (! isAdmin || isProxy) {
                // If not an admin user or is a proxy user, we have no way to determine if they didn't have enough
                // privilege or if the quotas don't exist.
                throw new WebApplicationException("The requested memberships were not found or requestor does not have privilege to view them.", Response.Status.NOT_FOUND);
            } else {
                // Admin user can see any existing quota, so can't be a priv issue.
                throw new WebApplicationException("The requested memberships were not found.", Response.Status.NOT_FOUND);
            }
        }

        // TODO: Incorporate paging params - new MembershipList(start, count, total, quotas)
        return new MembershipList(memberships);
    }

    /**
     * Get the membership given an id
     * @param membershipId the membership id
     * @return  the membership for the id
     */
    @Timed
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{membershipId}")
    public Membership retrieve(
            @Context SecurityContext context,
            @PathParam("membershipId") @NotNull Integer membershipId)
            throws WebApplicationException {

        Customer caller = (Customer) context.getUserPrincipal();
        Membership membership = null;

        boolean isAdmin = this.dataoneAuthHelper.isAdmin(caller.getSubject())
                || this.dataoneAuthHelper.isBookkeeperAdmin(caller.getSubject());

        /* Admin user is making this request as another subject */
        if (!isAdmin) {
            throw new WebApplicationException("Admin privilege is required to retrieve a membership, " + caller.getSubject() + " is not authorized.", Response.Status.FORBIDDEN);
        }

        // Get the membership from the store
        try {
            membership = membershipStore.getMembership(membershipId);

            if ( isAdmin) {
                return membership;
            } else {
                // Ensure the caller is asssociated with the quota owner
                // TODO: add customer 'subject' to membership object
                Customer customer = customerStore.getCustomer(membership.getCustomer().getId());
                String owner = customer.getSubject();

                Set<String> owners = new HashSet<String>();
                owners.add(owner);
                Set<String> associatedOwners =
                    this.dataoneAuthHelper.filterByAssociatedSubjects(caller, owners);
                if (associatedOwners.size() > 0) {
                    return membership;
                } else {
                    throw new WebApplicationException(caller.getSubject() + " is not associated with this membership.", Response.Status.FORBIDDEN);
                }
            }
        } catch (Exception e) {
            String message = "Couldn't get the membership: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.NOT_FOUND);
        }
    }
}
