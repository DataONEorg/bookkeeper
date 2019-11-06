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

package org.dataone.bookkeeper.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.jdbi.QuotaStore;
import org.jdbi.v3.sqlobject.CreateSqlObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Objects;

/**
 * QuotaService provides an abstraction of the quotas store to hide store
 * implementation details.
 */
public abstract class QuotaService {

    /**
     * Register a logger
     */
    private Log log = LogFactory.getLog(QuotaService.class);

    /**
     * Create a QuotaStore instance
     * @return quotaStore - the quota store instance
     */
    @CreateSqlObject
    abstract QuotaStore quotaStore();

    /**
     * List all quotas
     * @return quotas the quota list
     */
    public List<Quota> listQuotas() throws WebApplicationException {
        List<Quota> quotas = null;
        try {
            quotas = quotaStore().listQuotas();
        } catch (Exception e) {
            log.error("Quota listing failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return quotas;
    }

    /**
     * Get the quota by id
     * @param id the quota id
     * @return quota  the quota
     */
    public Quota getQuota(Integer id) throws WebApplicationException {
        Quota quota = null;

        // Do we have a valid id?
        if (Objects.isNull(id) || id.intValue() < 0) {
            throw new WebApplicationException(
                "Please provide a valid id", Status.EXPECTATION_FAILED);
        }

        try {
            quota = quotaStore().getQuota(id);
        } catch (Exception e) {
            log.error("Quota search by id failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return quota;
    }


    /**
     * Get the quota by subject
     * @param subject the quota id
     * @return quota  the quota
     */
    public List<Quota> findQuotaBySubject(String subject) throws WebApplicationException {
        List<Quota> quotas;

        // Do we have a valid id?
        if (Objects.isNull(subject) || subject.equals("")) {
            throw new WebApplicationException(
                "Please provide a valid customer subject", Status.EXPECTATION_FAILED);
        }

        try {
            quotas = quotaStore().findQuotasBySubject(subject);
        } catch (Exception e) {
            log.error("Quota search by customer subject failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return quotas;
    }

    /**
     * Insert the quota
     * @param quota  the quota
     * @return quota  the quota
     */
    public Quota insert(Quota quota) throws WebApplicationException {

        // Do we have a valid quota?
        if (Objects.isNull(quota)) {
            log.error("The quota to insert was null");
            throw new WebApplicationException("Please provide a valid quota.", Status.NOT_MODIFIED);
        }

        // Insert it
        try {
            quotaStore().insert(quota);
        } catch (Exception e) {
            log.error("The quota insert failed for " + quota.getId());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.NOT_MODIFIED);
        }
        return quota;
    }

    /**
     * Update the quota
     * @param quota  the quota
     * @return quota  the quota
     */
    public Quota update(Quota quota) throws WebApplicationException {
        // Do we have a valid quota?
        if (Objects.isNull(quota)) {
            log.error("The quota to update was null");
            throw new WebApplicationException("Please provide a valid quota.", Status.NOT_MODIFIED);
        }

        // Update it
        try {
            quotaStore().update(quota);
        } catch (Exception e) {
            log.error("The quota update failed for " + quota.getId());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.NOT_MODIFIED);
        }
        return quota;
    }

    /**
     * Delete the quota by id
     * @param id  the quota id
     * @return deleted  true if the quota was deleted
     */
    public Boolean delete(Integer id) throws WebApplicationException {
        boolean deleted = false;
        try {
            quotaStore().delete(id);
            deleted = true;
        } catch (Exception e) {
            log.error("The quota delete failed for " + id);
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.NOT_MODIFIED);
        }
        return deleted;
    }

    // TODO: Add health check
}
