/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2020. All rights reserved.
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

package org.dataone.bookkeeper.security;

import io.dropwizard.auth.Authorizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataone.bookkeeper.api.Customer;

import javax.annotation.Nullable;
import javax.ws.rs.container.ContainerRequestContext;

/**
 * A delegate class used to authorize REST method calls
 */
public class DataONEAuthorizer implements Authorizer<Customer> {

    /* Set up a logger for the class */
    private Log log = LogFactory.getLog(DataONEAuthorizer.class);

    /* A delegate instance for DataONE authz and authn*/
    private final DataONEAuthHelper dataoneAuthHelper;

    /**
     * Construct a DataONEAuthorizer
     * @param dataoneAuthHelper  a DataONEAuthHelper instance
     */
    public DataONEAuthorizer(DataONEAuthHelper dataoneAuthHelper) {
        this.dataoneAuthHelper = dataoneAuthHelper;
    }

    /**
     * @param customer  the customer
     * @param role  the role
     * @deprecated
     */
    @Override
    public boolean authorize(Customer customer, String role) {

        return false;
    }

    /**
     * Authorize a request for the given principal and role
     * @param principal  the customer making the request
     * @param role  the role being requested
     * @param requestContext  the request context
     * @return  true if the customer is a member of the given role
     */
    @Override
    public boolean authorize(Customer principal, String role,
        @Nullable ContainerRequestContext requestContext) {
        // TODO: Implement this for @RolesAllowed method annotations
        log.warn("The authorize() method is unimplemented.");
        return false;
    }
}
