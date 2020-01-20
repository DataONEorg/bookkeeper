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

package org.dataone.bookkeeper.security;


import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataone.bookkeeper.api.Customer;

import java.util.Optional;

/**
 * Authenticate a user given their DataONE JWT credentials as a string
 */
public class DataONEAuthenticator implements Authenticator<String, Customer> {

    /* Set up a logger */
    Log log = LogFactory.getLog(DataONEAuthenticator.class);

    /* A delegate instance for DataONE authz and authn*/
    private final DataONEAuthHelper dataONEAuthHelper;

    /**
     * Construct a DataONEAuthorizer
     * @param dataONEAuthHelper  a DataONEAuthHelper instance
     */
    public DataONEAuthenticator(DataONEAuthHelper dataONEAuthHelper) {
        this.dataONEAuthHelper = dataONEAuthHelper;
    }

    /**
     * Authenticate the user using DataONE services
     *
     * Note that we accept OAuth bearer tokens and verify them, but rely on the CN to issue the
     * calling client a token sometime before Bookkeeper calls are made.
     *
     * @param token the user's JWT bearer token
     * @return customer the Customer instance
     * @throws AuthenticationException  if the user is not authenticated
     */
    @Override
    public Optional<Customer> authenticate(String token) throws AuthenticationException {
        Customer customer = null;
        // Verify the token
        boolean verified = this.dataONEAuthHelper.verify(token);

        if ( verified ) {
            // Amend the Customer with SubjectInfo from the DataONE Identity service
            customer = this.dataONEAuthHelper.getCustomerWithSubjectInfo(token);
        }
        if ( customer != null ) {
            return Optional.of(customer);
        }
        return Optional.empty();
    }
}
