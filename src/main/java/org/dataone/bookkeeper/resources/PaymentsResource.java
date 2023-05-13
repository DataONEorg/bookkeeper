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

package org.dataone.bookkeeper.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataone.bookkeeper.security.DataONEAuthHelper;
import org.jdbi.v3.core.Jdbi;

import com.codahale.metrics.annotation.Timed;

/**
 * The entry point to the payments collection
 */
@Timed
@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentsResource extends BaseResource {

    /* The logging facility for this class */
    private final Log log = LogFactory.getLog(PaymentsResource.class);

    /**
     * Construct an order collection
     * 
     * @param database the jdbi database access reference
     * @param dataoneAuthHelper authentication adapter
     */
    public PaymentsResource(Jdbi database, DataONEAuthHelper dataoneAuthHelper) {
    }

    @Timed
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response record(String transact_json) {
        log.info("PAYMENT: " + transact_json);
        return Response.ok().build();
    }
}
