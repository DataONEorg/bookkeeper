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
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.api.QuotaList;
import org.dataone.bookkeeper.jdbi.QuotaStore;
import org.jdbi.v3.core.Jdbi;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;

/**
 * The entry point to the quotas collection
 */
@Timed
@Path("/quotas")
@Produces(MediaType.APPLICATION_JSON)
public class QuotasResource extends BaseResource {

    /* The logging facility for this class */
    private Log log = LogFactory.getLog(QuotasResource.class);

    /* The quota store for database calls */
    private final QuotaStore quotaStore;

    /**
     * Construct a quota collection
     * @param database  the jdbi database access reference
     */
    public QuotasResource(Jdbi database) {
        this.quotaStore = database.onDemand(QuotaStore.class);
    }

    /**
     * List quotas, optionally by subscriptionId or subject.
     * Use start and count to get paginated results
     * @param start  the paging start index
     * @param count  the paging size count
     * @param subscriptionId  the subscriptionId
     * @param subject  the quota subject
     * @return quotas  the quota list
     */
    @Timed
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public QuotaList listQuotas(
        @QueryParam("start") @DefaultValue("0") Integer start,
        @QueryParam("count") @DefaultValue("1000") Integer count,
        @QueryParam("subscriptionId") Integer subscriptionId,
        @QueryParam("subject") String subject) throws WebApplicationException {

        List<Quota> quotas;
        try {
            if (subject != null) {
                quotas = quotaStore.findQuotasBySubject(subject);
            } else if (subscriptionId != null) {
                quotas = quotaStore.findQuotasBySubscriptionId(subscriptionId);
            // TODO: If authenticated as an admin, list all quotas
            // } else if ( // determine admin role) {
                // quotas = quotaStore.listQuotas();
            } else {
                quotas = quotaStore.listUnassignedQuotas();
            }
        } catch (Exception e) {
            String message = "Couldn't list quotas: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.EXPECTATION_FAILED);
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Quota create(@NotNull @Valid Quota quota) throws WebApplicationException {
        // Insert the quota after it is validated
        try {
            Integer id = quotaStore.insert(quota);
            quota = quotaStore.getQuota(id);
        } catch (Exception e) {
            String message = "Couldn't insert the quota: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.EXPECTATION_FAILED);
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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{quotaId}")
    public Quota retrieve(@PathParam("quotaId") @NotNull Integer quotaId)
        throws WebApplicationException {

        Quota quota = null;
        // Get the quota from the store
        try {
            quota = quotaStore.getQuota(quotaId);
        } catch (Exception e) {
            String message = "Couldn't get the quota: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.NOT_FOUND);
        }
        return quota;
    }

    /**
     * Update the quota
     * @param quota  the quota to update
     * @return  the updated quota
     * @throws WebApplicationException  a web app exception
     */
    @Timed
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{quotaId}")
    public Quota update(@NotNull @Valid Quota quota) throws WebApplicationException {
        // Update the quota after validation
        try {
            quotaStore.update(quota);
        } catch (Exception e) {
            String message = "Couldn't update the quota: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.EXPECTATION_FAILED);
        }
        return quota;
    }

    /**
     * Delete the quota
     * @param quotaId  the quota id
     * @return  response 200 if deleted
     * @throws WebApplicationException  a web app exception
     */
    @Timed
    @DELETE
    @Path("{quotaId}")
    public Response delete(@PathParam("quotaId") @Valid Integer quotaId) throws WebApplicationException {
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
