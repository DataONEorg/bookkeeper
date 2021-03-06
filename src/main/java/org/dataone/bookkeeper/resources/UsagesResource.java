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
import org.dataone.bookkeeper.api.Usage;
import org.dataone.bookkeeper.api.UsageList;
import org.dataone.bookkeeper.api.UsageStatus;
import org.dataone.bookkeeper.jdbi.QuotaStore;
import org.dataone.bookkeeper.jdbi.UsageStore;
import org.dataone.bookkeeper.security.DataONEAuthHelper;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.*;
import java.util.stream.Collectors;


/**
 * The entry point to the usages collection
 */
@Timed
@Path("/usages")
@Produces(MediaType.APPLICATION_JSON)
public class UsagesResource {

    /* The logging facility for this class */
    private Log log = LogFactory.getLog(UsagesResource.class);

    /* The quotas store for database calls */
    private final QuotaStore quotaStore;

    /* The usages store for database calls */
    private final UsageStore usageStore;

    /* An instance of the DataONE authn and authz delegate */
    private final DataONEAuthHelper dataoneAuthHelper;

    /**
     * Construct a quota collection
     * @param database  the jdbi database access reference
     */
    public UsagesResource(Jdbi database, DataONEAuthHelper dataoneAuthHelper) {
        this.quotaStore = database.onDemand(QuotaStore.class);
        this.usageStore = database.onDemand(UsageStore.class);
        this.dataoneAuthHelper = dataoneAuthHelper;
    }

    /**
     * Get the usage for a given instance identifier and quota type
     * @param context  the security context of the authenticated user
     * @param instanceId the instance identifier of the usage
     * @param quotaType name of the quota being used
     * @param subjects the quota subject
     * @param requestor the subject to make the request as, instead of caller's subject
     * @return usage the usage object for the given instance identifier
     */
    @Timed
    @GET
    @PermitAll
    public UsageList listUsages(@Context SecurityContext context,
                                @QueryParam("start") @DefaultValue("0") Integer start,
                                @QueryParam("count") @DefaultValue("1000") Integer count,
                                @QueryParam("quotaId") Integer quotaId,
                                @QueryParam("quotaType") String quotaType,
                                @QueryParam("instanceId") String instanceId,
                                @QueryParam("status") String status,
                                @QueryParam("subject") Set<String> subjects,
                                @QueryParam("requestor") String requestor) {

        List<Usage> usages = null;
        Usage usage = null;
        // The calling user injected in the security context via authentication
        Customer caller = (Customer) context.getUserPrincipal();
        boolean isAdmin = this.dataoneAuthHelper.isAdmin(caller.getSubject());
        Set<String> associatedSubjects;
        List<String> approvedSubjects = new ArrayList<>();
        Boolean isProxy = isAdmin && requestor != null;

        // Admin users can make request as another user
        if(requestor != null) {
            if(isAdmin) {
                // Create a new Customer based on the 'requestor' parameter - don't update the subject directly in the
                // context, which is cached.
                try {
                    caller = this.dataoneAuthHelper.createCustomerFromSubject(requestor);
                } catch (io.dropwizard.auth.AuthenticationException dae) {
                    String message = "The requested usages couldn't be listed: " + dae.getMessage();
                    throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
                }
            } else {
                // Note: this exception will be caught by the outer block.
                throw new WebApplicationException(caller.getSubject() +
                    " does not have admin privilege needed to set 'requestor'. ",
                    Response.Status.FORBIDDEN);
            }
        }

        /*  Check the list of requested subjects and if not an admin user, filter them based on
         * the subjects the requestor has the privilege to view info for.
         *
         */
        if (subjects != null && subjects.size() > 0) {
            // Filter out non-associated subjects if not an admin, or if the admin user has requested a proxy requestor
            if (! isAdmin || isProxy) {
                associatedSubjects = this.dataoneAuthHelper.filterByAssociatedSubjects(caller, subjects);
                if ( associatedSubjects.size() > 0 ) {
                    approvedSubjects.addAll(associatedSubjects);
                }

                /* Caller is not admin and is not associated with any of the specified subjects. */
                if (approvedSubjects.size() == 0) {
                    throw new WebApplicationException("The requested subjects don't exist or " +
                        "requestor doesn't have privilege to view them.", Response.Status.FORBIDDEN);
                }
            } else {
                /* Admin caller, so can see quotas for all requested subjects */
                approvedSubjects.addAll(subjects);
            }
        } else {
            /* No subjects specified and caller is not admin, so caller is allowed to
               view any quota for subjects with which they are associated.
               If the caller is admin, then don't set subject, as they will be able to view all subjects.
            */
            if (! isAdmin || isProxy) {
                if (approvedSubjects.size() == 0) {
                    approvedSubjects = new ArrayList(this.dataoneAuthHelper.getAssociatedSubjects(caller));
                }
            }
        }

        /* Test for all combinations of query params that are supported.
            Note that Admin users may not have a subject set, as they have privilege to see usages for
            any subject.
            Non-admin users will always have at least one subject set (their own).
            At this point, the subject list for non-admin users is vetted.
         */
        /* The "instanceid + quotaId" combination is unique among all usages, so only one usage should be returned. */
        if (instanceId != null && quotaId != null) {
            if (approvedSubjects.size() == 0) {
                usage = usageStore.findUsageByInstanceIdAndQuotaId(instanceId, quotaId);
            } else {
                /* Non-admin users can only retrieve instanceIds+quotaId for an subject that they are associated with,
                 * so constrain results by subjects, i.e. if the instanceId+quotaId they are trying to access must
                 * belong to an subject they can access.
                 * Note: it's not possible to filter usage entries by subject after they are retrieved, as the
                 * usage object doesn't contain the quota subject, therefore, the UsageStore has to do the
                 * filtering by subject.
                 */
                usage = usageStore.findUsageByInstanceIdQuotaIdAndSubjects(instanceId, quotaId, approvedSubjects);
            }
            if (usage == null) {
                usages = null;
            } else {
                usages = new ArrayList<>();
                usages.add(usage);
            }
        } else if (instanceId != null && quotaType != null) {
            usage = usageStore.findUsageByInstanceIdAndQuotaType(instanceId, quotaType);
            if (usage == null) {
                usages = null;
            } else {
                usages = new ArrayList<>();
                usages.add(usage);
            }
        } else if (instanceId != null) {
            if(approvedSubjects.size() == 0) {
                usages = usageStore.findUsagesByInstanceId(instanceId);
            } else {
                usages = usageStore.findUsagesByInstanceIdAndSubjects(instanceId, approvedSubjects);
            }
        } else if (quotaId != null) {
            if(approvedSubjects.size() == 0) {
                usages = usageStore.findUsagesByQuotaId(quotaId);
            } else {
                /* Non-admin users can only retrieve instanceIds for an subject that they are associated with,
                 * so constrain results by subjects, i.e. if the instanceId they are trying to access must
                  * belong to an subject they can access. */
                usages = usageStore.findUsagesByQuotaIdAndSubjects(quotaId, approvedSubjects);
            }
        } else if (quotaType != null) {
            if(approvedSubjects.size() == 0) {
                /* subject is null, quotaType is not */
                usages = usageStore.findUsagesByQuotaType(quotaType);
            } else {
                usages = usageStore.findUsagesByQuotaTypeAndSubjects(quotaType, approvedSubjects);
            }
        } else if (approvedSubjects.size() > 0) {
            // quotaId, quotaType, instanceId not set
            usages = usageStore.findUsagesByQuotaSubjects(approvedSubjects);
        } else {
            /* Must be admin user, so list all usages */
            usages = usageStore.listUsages();
        }

        if (usages == null || usages.size() == 0) {
            if (! isAdmin || isProxy) {
                // If not an admin user or is a proxy user, we have no way to determine if they didn't have enough
                // privilege or if the usage doesn't exist.
                throw new WebApplicationException("The requested usages were not found or requestor does not have privilege to view them.", Response.Status.NOT_FOUND);
            } else {
                // Admin user can see any existing usage, so can't be a priv issue.
                throw new WebApplicationException("The requested usage was not found.", Response.Status.NOT_FOUND);
            }
        } else {
            // Filter by status, if requested.
            if (status != null) {
                List<Usage> filteredUsages = usages
                    .stream()
                    .filter(u -> u.getStatus().compareToIgnoreCase(status) == 0)
                    .collect(Collectors.toList());
                if (filteredUsages.size() > 0) {
                    usages = filteredUsages;
                } else {
                    throw new WebApplicationException("No requested usages found with status = " + status, Response.Status.NOT_FOUND);
                }
            }
        }
        return new UsageList(usages);
    }

    /**
     * Create the given Usage
     * @param usage the usage to create
     * @return usage the created usage
     */
    @Timed
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Usage create (
            @Context SecurityContext context,
            @NotNull @Valid Usage usage) throws WebApplicationException {
        String DEFAULT_STATUS = "active";
        // Insert the usage after it is validated
        // The calling user injected in the security context via authentication
        Customer caller = (Customer) context.getUserPrincipal();
        boolean isAdmin = this.dataoneAuthHelper.isAdmin(caller.getSubject());

        if ( isAdmin ) {
            try {
                // On create, status is always set to 'active'
                if (usage.getStatus().compareToIgnoreCase(DEFAULT_STATUS) != 0) {
                    usage.setStatus(DEFAULT_STATUS);
                }
                Integer id = usageStore.insert(usage);
                usage = usageStore.getUsage(id);
            } catch (Exception e) {
                String message = "Couldn't insert the usage: " + e.getMessage();
                throw new WebApplicationException(message, Response.Status.INTERNAL_SERVER_ERROR);
            }
            return usage;
        } else {
            // Throw an exception, this is an admin-only call
            throw new WebApplicationException("Admin privilege is required to create a usage, " + caller.getSubject() + " is not authorized.", Response.Status.FORBIDDEN);
        }
    }

    /**
     * Get the usage for a given identifier
     * @param context  the security context of the authenticated user
     * @param id the id of the usage to retrieve
     * @return usage  the usage object for the given usage identifier
     */
    @Timed
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Usage retrieve(
        @Context SecurityContext context,
        @PathParam("id") @NotNull Integer id)
        throws WebApplicationException {

        Usage usage = null;
        // The calling user injected in the security context via authentication
        Customer caller = (Customer) context.getUserPrincipal();
        usage = usageStore.getUsage(id);
        if (usage == null) {
            throw new WebApplicationException("The requested usage was not found.", Response.Status.NOT_FOUND);
        }

        if ( this.dataoneAuthHelper.isAdmin(caller.getSubject()) ) {
            return usage;
        }

        // Ensure the caller is asssociated with the quota subject
        // The quota subject is not in the usages table, it must be retrieved from the quota table.
        Integer quotaId = usage.getQuotaId();
        Quota quota = quotaStore.getQuota(quotaId);
        Set<String> subjects = new HashSet<String>();
        subjects.add(quota.getSubject());

        Set<String> associatedSubjects =
            this.dataoneAuthHelper.filterByAssociatedSubjects(caller, subjects);

        if ( associatedSubjects.size() > 0 ) {
            return usage;
        } else {
            throw new WebApplicationException(caller.getSubject() +
                " is not associated with this usage.", Response.Status.FORBIDDEN);
        }
    }

    /**
     * Update the given usage, adding or update the usage row in the
     * usages table.  Requires administrative authorization.
     * @param context  the security context of the authenticated user
     * @param usageId the usage identifier
     * @return usage the usage object with the updated usage
     * @throws WebApplicationException  if adjusting the usage fails
     */
    @Timed
    @PUT
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{usageId}")
    public Usage update(
            @Context SecurityContext context,
            @NotNull @Valid Usage usage,
            @PathParam("usageId") @NotNull @Positive Integer usageId
    ) throws WebApplicationException {
        Usage updatedUsage;

        // The calling user injected in the security context via authentication
        Customer caller = (Customer) context.getUserPrincipal();
        boolean isAdmin = this.dataoneAuthHelper.isAdmin(caller.getSubject());

        if ( isAdmin ) {
            try {
                updatedUsage = usageStore.update(usage);
            } catch (Exception e) {
                String message = "Couldn't update the usage: " + e.getMessage();
                throw new WebApplicationException(message, Response.Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new WebApplicationException("Admin privilege is required to update a usage, " + caller.getSubject() + " is not authorized.", Response.Status.FORBIDDEN);
        }
        return updatedUsage;
    }

    /**
     * Delete the usage
     * @param usageId the usage id
     * @return response 200 if deleted
     * @throws WebApplicationException  a web app exception
     */
    @Timed
    @DELETE
    @PermitAll
    @Path("{usageId}")
    public Response delete(
            @Context SecurityContext context,
            @PathParam("usageId") @Valid Integer usageId) throws WebApplicationException {
        String message = "The usageId cannot be null.";

        if (usageId == null) {
            throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
        }

        // Only Admin users can delete a usage
        Customer caller = (Customer) context.getUserPrincipal();
        if ( ! this.dataoneAuthHelper.isAdmin(caller.getSubject())) {
            throw new WebApplicationException("Admin privilege is required to delete a usage, " + caller.getSubject() + " is not authorized.", Response.Status.FORBIDDEN);
        }

        try {
            usageStore.delete(usageId);
        } catch (Exception e) {
            message = "Deleting the usage with id " + usageId + " failed: " + e.getMessage();
            log.error(message);
            e.printStackTrace();
            throw new WebApplicationException(message, Response.Status.INTERNAL_SERVER_ERROR);
        }
        return Response.ok().build();
    }
    /**
     * Get a usage status
     * @param quotaType the usage quota type ("portal" | "storage" | ...)
     * @param instanceId the usage instance id type
     * @return response 200 if deleted
     * @throws WebApplicationException  a web app exception
     */
    @Timed
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public UsageStatus getStatus(
            @QueryParam("quotaType") String quotaType,
            @QueryParam("instanceId") String instanceId) throws WebApplicationException {

        log.debug("Usage status");
        Usage usage = null;
        UsageStatus usageStatus = null;

        String message = null;
        if (quotaType == null || instanceId == null)  {
            message = "Both quotaType and instanceId must be specified.";
            throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
        }

        try {
            log.debug("Usage status query");
            usage = usageStore.findUsageByInstanceIdAndQuotaType(instanceId, quotaType);
        } catch (Exception e) {
            message = "Retrieving the usage with instanceId: " + instanceId + ", quotaType: " + quotaType
                + " failed: " + e.getMessage();
            log.error(message);
            throw new WebApplicationException(message, Response.Status.INTERNAL_SERVER_ERROR);
        }

        // If a usage was returned from the store, construct a UsageStatus from it and return success
        if (usage != null) {
            usageStatus = new UsageStatus("usagestatus", usage.getStatus());
        } else {
            throw new WebApplicationException("The requested usage status was not found.", Response.Status.NOT_FOUND);
        }

        return usageStatus;
    }


}
