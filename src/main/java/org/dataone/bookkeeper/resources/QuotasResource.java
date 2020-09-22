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
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.api.QuotaList;
import org.dataone.bookkeeper.jdbi.QuotaStore;
import org.dataone.bookkeeper.security.DataONEAuthHelper;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The entry point to the quotas collection
 */
@Timed
@Path("/quotas")
@Produces(MediaType.APPLICATION_JSON)
public class QuotasResource extends BaseResource {

    /* The logging facility for this class */
    private final Log log = LogFactory.getLog(QuotasResource.class);

    /* The quota store for database calls */
    private final QuotaStore quotaStore;

    /* An instance of the DataONE authn and authz delegate */
    private final DataONEAuthHelper dataoneAuthHelper;

    /**
     * Construct a quota collection
     * @param database  the jdbi database access reference
     */
    public QuotasResource(Jdbi database, DataONEAuthHelper dataoneAuthHelper) {
        this.quotaStore = database.onDemand(QuotaStore.class);
        this.dataoneAuthHelper = dataoneAuthHelper;

    }

    /**
     * List quotas, optionally by membershipId or owner.
     * Use start and count to get paginated results
     * @param start  the paging start index
     * @param count  the paging size count
     * @param owners the quota owners (repeatable and treated as a list)
     * @param quotaType the quota type (e.g. "portal", "storage", ...)
     * @param requestor the DataONE subject to make the request as
     * @return quotas  the quota list
     */
    @Timed
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public QuotaList listQuotas(
        @Context SecurityContext context,
        @QueryParam("start") @DefaultValue("0") Integer start,
        @QueryParam("count") @DefaultValue("1000") Integer count,
        @QueryParam("quotaType") String quotaType,
        @QueryParam("owner") Set<String> owners,
        @QueryParam("requestor") String requestor) throws WebApplicationException {

        // The calling user injected in the security context via authentication
        Customer caller = (Customer) context.getUserPrincipal();
        boolean isAdmin = this.dataoneAuthHelper.isAdmin(caller.getSubject());

        List<Quota> quotas;
        Set<String> associatedOwners;
        List<String> approvedOwners = new ArrayList<>();
        /* Admin user is making this request as another subject */
        Boolean isProxy = isAdmin && requestor != null;

        // Admin users can make request as another user
        if (requestor != null) {
            if (isAdmin) {
                // Create a new Customer based on the 'requestor' parameter - don't update the subject directly in the
                // context, which is cached.
                try {
                    caller = this.dataoneAuthHelper.createCustomerFromSubject(requestor);
                } catch (io.dropwizard.auth.AuthenticationException dae) {
                    String message = "The requested quotas couldn't be listed: " + dae.getMessage();
                    throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
                }
            } else {
                throw new WebApplicationException(caller.getSubject() + " does not have admin privilege needed to set 'requestor'. ", Response.Status.FORBIDDEN);
            }
        }

        /* Determine if the caller is allowed to retrieve quotas for the specified owners */
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
                    throw new WebApplicationException("The requested owners don't exist or " +
                        "requestor doesn't have privilege to view them.", Response.Status.FORBIDDEN);
                }
            } else {
                /* Admin caller, so can see quotas for all requested owners */
                approvedOwners.addAll(owners);
            }
        } else {
            /* No owners specified and caller is not admin, so caller is allowed to
             view any quota for owners with which they are associated.
             If the caller is admin, then don't set subject, as they will be able to view all owners.
             */
            if (!isAdmin || isProxy) {
                if (approvedOwners.size() == 0) {
                    approvedOwners = new ArrayList(this.dataoneAuthHelper.getAssociatedSubjects(caller));
                }
            }
        }

        if (quotaType != null) {
            if (approvedOwners.size() > 0) {
                quotas = quotaStore.findQuotasByNameAndOwners(quotaType, approvedOwners);
            } else {
                /* Not sure if this is useful or practical, i.e. admin user can view all quotas for a quota type. */
                quotas = quotaStore.findQuotasByType(quotaType);
            }
        } else {
            if (approvedOwners.size() > 0) {
                quotas = quotaStore.findQuotasByOwners(approvedOwners);
            } else {
                quotas = quotaStore.listQuotas();
            }
        }

        if (quotas == null || quotas.size() == 0) {
            if (! isAdmin || isProxy) {
                // If not an admin user or is a proxy user, we have no way to determine if they didn't have enough
                // privilege or if the quotas don't exist.
                throw new WebApplicationException("The requested quotas were not found or requestor does not have privilege to view them.", Response.Status.NOT_FOUND);
            } else {
                // Admin user can see any existing quota, so can't be a priv issue.
                throw new WebApplicationException("The requested quotas were not found.", Response.Status.NOT_FOUND);
            }
        }

        // TODO: Incorporate paging params - new QuotaList(start, count, total, quotas)
        return new QuotaList(quotas);
    }

    /**
     * Create the given quota
     * @param quota  the quota to create
     * @return quota  the created quota
     */
    @Timed
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    public Quota create(
        @Context SecurityContext context,
        @NotNull @Valid Quota quota) throws WebApplicationException {

        Customer caller = (Customer) context.getUserPrincipal();
        if ( ! this.dataoneAuthHelper.isBookkeeperAdmin(caller.getSubject())) {
            throw new WebApplicationException("Bookkeepr admin privilege is required to create a quota, " + caller.getSubject() + " is not authorized.", Response.Status.FORBIDDEN);
        }

        // Insert the quota after it is validated
        try {
            Integer id = quotaStore.insert(quota);
            quota = quotaStore.getQuota(id);
        } catch (Exception e) {
            String message = "Couldn't insert the quota: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return quota;
    }

    /**
     * Get the quota given an id
     * @param quotaId  the quota id
     * @return  the quota for the id
     */
    @Timed
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{quotaId}")
    public Quota retrieve(
        @Context SecurityContext context,
        @PathParam("quotaId") @NotNull Integer quotaId)
        throws WebApplicationException {

        Customer caller = (Customer) context.getUserPrincipal();

        Quota quota;
        // Get the quota from the store
        try {
            quota = quotaStore.getQuota(quotaId);

            if (quota == null) {
                throw new WebApplicationException("The quota was not found.", Response.Status.NOT_FOUND);
            } else {
                if (this.dataoneAuthHelper.isAdmin(caller.getSubject())) {
                    return quota;
                } else {
                    // Ensure the caller is asssociated with the quota owner
                    String quotaOwner = quota.getOwner();
                    Set<String> owners = new HashSet<String>();
                    owners.add(quotaOwner);
                    Set<String> associatedOwners =
                            this.dataoneAuthHelper.filterByAssociatedSubjects(caller, owners);
                    if (associatedOwners.size() > 0) {
                        return quota;
                    } else {
                        throw new WebApplicationException(caller.getSubject() + " is not associated with this quota.", Response.Status.FORBIDDEN);
                    }
                }
            }
        } catch (Exception e) {
            String message = "The requested quota could not be retrieved: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.NOT_FOUND);
        }
    }

    /**
     * Update the quota
     * @param quota  the quota to update
     * @return  the updated quota
     * @throws WebApplicationException  a web app exception
     */
    @Timed
    @PUT
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{quotaId}")
    public Quota update(
        @Context SecurityContext context,
        @NotNull @Valid Quota quota) throws WebApplicationException {

        Customer caller = (Customer) context.getUserPrincipal();
        if ( ! this.dataoneAuthHelper.isBookkeeperAdmin(caller.getSubject())) {
            throw new WebApplicationException("Bookkeeper admin privilege is required to update a quota, " + caller.getSubject() + " is not authorized.", Response.Status.FORBIDDEN);
        }

        Quota updatedQuota;
        // Update the quota after validation
        try {
            updatedQuota = quotaStore.update(quota);
        } catch (Exception e) {
            String message = "Couldn't update the quota: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return updatedQuota;
    }

    /**
     * Delete the quota
     * @param quotaId  the quota id
     * @return  response 200 if deleted
     * @throws WebApplicationException  a web app exception
     */
    @Timed
    @DELETE
    @PermitAll
    @Path("{quotaId}")
    public Response delete(
        @Context SecurityContext context,
        @PathParam("quotaId") @Valid Integer quotaId) throws WebApplicationException {

        Customer caller = (Customer) context.getUserPrincipal();
        if ( ! this.dataoneAuthHelper.isBookkeeperAdmin(caller.getSubject())) {
            throw new WebApplicationException("Bookkeeper admin privilege is required to delete a quota, " + caller.getSubject() + " is not authorized.", Response.Status.FORBIDDEN);
        }

        String message = "The quotaId cannot be null.";
        if (quotaId == null) {
            throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
        }
        try {
            quotaStore.delete(quotaId);
        } catch (Exception e) {
            message = "Deleting the quota with id " + quotaId + " failed: " + e.getMessage();
            log.error(message);
            e.printStackTrace();
            throw e;
        }
        return Response.ok().build();
    }
}
