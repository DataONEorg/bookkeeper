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

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.setup.Environment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.config.DataONEConfiguration;
import org.dataone.bookkeeper.jdbi.CustomerStore;
import org.dataone.client.auth.AuthTokenSession;
import org.dataone.client.v2.CNode;
import org.dataone.client.v2.itk.D1Client;
import org.dataone.service.exceptions.BaseException;
import org.dataone.service.exceptions.NotImplemented;
import org.dataone.service.exceptions.ServiceFailure;
import org.dataone.service.types.v1.Group;
import org.dataone.service.types.v1.Person;
import org.dataone.service.types.v1.Subject;
import org.dataone.service.types.v1.SubjectInfo;
import org.eclipse.jetty.server.Authentication;
import org.jdbi.v3.core.Jdbi;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A delegate used to connect with DataONE services
 */
public class DataONEAuthHelper {

    /* A logger for the class */
    private Log log = LogFactory.getLog(DataONEAuthHelper.class);

    /* The Coordinating Node base URL */
    private String cnBaseUrl;

    /* The Identity API endpoint */
    private String cnIdentityServiceEndpoint;

    private CustomerStore customerStore;

    /* The application environment */
    private Environment environment;

    /* The DataONE application configuration */
    private DataONEConfiguration configuration;

    /* The Coordinating Node instance */
    private CNode cn;

    /* The Coordinating Node public key used for verifying tokens */
    private RSAPublicKey cnPublicKey;

    /**
     * Construct an empty DataONEAuthHelper
     */
    public DataONEAuthHelper() {
    }

    /**
     * Construct a DataONEAuthHelper
     *
     * @param environment  the application environment
     * @param database  the JDBI database instance
     * @param configuration  the DataONE configuration instance
     */
    public DataONEAuthHelper (Environment environment, Jdbi database, DataONEConfiguration configuration) {
        this.environment = environment;
        this.customerStore = database.onDemand(CustomerStore.class);
        this.configuration = configuration;
    }

    /**
     * Get the CN base URL
     * @return cnBaseUrl  the CN base URL
     */
    public String getCnBaseUrl() {
        return cnBaseUrl;
    }

    /**
     * Set the CN base URL
     * @param cnBaseUrl  the CN base URL
     */
    public void setCnBaseUrl(String cnBaseUrl) {
        this.cnBaseUrl = cnBaseUrl;
    }

    /**
     * Get the CN identity service endpoint
     * @return  the identity service endpoint
     */
    public String getCnIdentityServiceEndpoint() {
        return cnIdentityServiceEndpoint;
    }

    /**
     * Set the identity service endpoint
     * @param cnIdentityServiceEndpoint  the identity service endpoint
     */
    public void setCnIdentityServiceEndpoint(String cnIdentityServiceEndpoint) {
        this.cnIdentityServiceEndpoint = cnIdentityServiceEndpoint;
    }

    /**
     * Get the customer store
     * @return customerStore  the customer store
     */
    public CustomerStore getCustomerStore() {
        return customerStore;
    }

    /**
     * Get the customer store
     * @param customerStore  the customer store
     */
    public void setCustomerStore(CustomerStore customerStore) {
        this.customerStore = customerStore;
    }

    /**
     * Get the application environment
     * @return  environment  the application environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Set the application environment
     * @param environment  the application environment
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Get the DataONE configuration
     * @return configuration the DataONE configuration
     */
    public DataONEConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Set the DataONE configuration
     * @param configuration  the DataONE configuration
     */
    public void setConfiguration(DataONEConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Verify the token's validity and expiry
     * @param token  the token to be verified
     * @return true  if the token is verified
     */
    public boolean verify(String token) throws AuthenticationException {
        boolean verified = false;
        String message;

        // Fetch the CN certificate
        try {
            D1Client.setCN(this.configuration.getCnBaseUrl());
            URL cnBaseUrl = new URL(D1Client.getCN().getNodeBaseServiceUrl());
            HttpsURLConnection connection = (HttpsURLConnection) cnBaseUrl.openConnection();
            connection.connect();
            Certificate cnCertificate = connection.getServerCertificates()[0];
            if ( cnCertificate != null ) {
                log.debug("Verifying token with CN certificate: " + cnCertificate.toString());
                this.cnPublicKey = (RSAPublicKey) cnCertificate.getPublicKey();

                // Parse then verify the signed token
                SignedJWT signedJWT = SignedJWT.parse(token);
                JWSVerifier jwsVerifier = new RSASSAVerifier(this.cnPublicKey);
                if ( ! signedJWT.verify(jwsVerifier) ) {
                    log.debug("Verifying token with public key: " + this.cnPublicKey);
                    log.warn("Couldn't verify token with CN public key: " + token);
                    return verified;
                    //throw new AuthenticationException("Couldn't verify token.");
                }

                // If verified, check the expiration dates
                ZonedDateTime now = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("UTC"));
                log.debug(now);
                ZonedDateTime expiration =
                    ZonedDateTime.ofInstant(
                        signedJWT.getJWTClaimsSet()
                            .getExpirationTime()
                            .toInstant(),
                        ZoneId.of("UTC")
                    );
                log.debug(expiration);

                if ( now.isAfter(expiration) ) {
                    log.warn("The token has expired: " + expiration);
                    // throw new AuthenticationException("The token has expired: " + expiration);
                } else {
                    verified = true;
                }
            } else {
                log.error("Couldn't verify token.  The CN certificate is null.");
            }

        } catch (NotImplemented notImplemented) {
            message = "Couldn't verify the token. " +
                 "The CN returned a NotImplemented: " + notImplemented.getMessage();
            log.warn(message);
            throw new AuthenticationException(message);
        } catch (ServiceFailure serviceFailure) {
             message = "Couldn't verify the token. " +
                 "The CN returned a ServiceFailure: " + serviceFailure.getMessage();
            log.warn(message);
            throw new AuthenticationException(message);
        } catch (MalformedURLException mue) {
             message = "Couldn't verify the token. " +
                 "The CN URL is malformed: " + mue.getMessage();
            log.warn(message);
            throw new AuthenticationException(message);
        } catch (IOException ioe) {
             message = "Couldn't verify the token. " +
                 "The CN returned connection failed: " + ioe.getMessage();
            log.warn(message);
            throw new AuthenticationException(message);
        } catch (ParseException e) {
             message = "Couldn't verify the token. " +
                 "The JWT library returned a parse exception: " + e.getMessage();
            log.warn(message);
            throw new AuthenticationException(message);
        } catch (JOSEException je) {
             message = "Couldn't verify the token. " +
                 "The JWT library returned an exception: " + je.getMessage();
            log.warn(message);
            throw new AuthenticationException(message);
        }
        log.debug("Token is verified: " + verified);
        return verified;
    }

    /**
     * Get the subject information (groups, etc.) for the given token's subject
     * from the DataONE Identity Service
     * @param token  the token of the user
     * @return subjectInfo  the subject information for the subject
     */
    public SubjectInfo getSubjectInfo(String token, String subject) throws BaseException {
        SubjectInfo subjectInfo = null;
        AuthTokenSession session = new AuthTokenSession(token);
        Subject d1Subject = new Subject();
        d1Subject.setValue(subject);
        session.setSubject(d1Subject);
        D1Client.setCN(this.configuration.getCnBaseUrl());
        this.cn = D1Client.getCN();
        subjectInfo = this.cn.getSubjectInfo(session, session.getSubject());

        return subjectInfo;
    }

    /**
     * Return the subject of the given token
     * @param token the token to be parsed
     * @return subject  the subject of the token
     * @throws ParseException a parsing exception
     */
    public String getTokenSubject(String token) throws ParseException {
        // Parse then verify the signed token
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getSubject();
    }

    /**
     * Return a customer instance with included subjectInfo, if available
     * @param subject
     * @return customer  A customer instance for the input subject
     * @throws AuthenticationException  a token parsing exception
     */
    public Customer createCustomerFromSubject(String subject) throws AuthenticationException {

        Customer customer = null;
        String errorMessage = "Couldn't get subject information from the Coordinating Node: ";
        customer = new Customer();
        customer.setSubject(subject);

        SubjectInfo subjectInfo = null;
        try {
            subjectInfo = getSubjectInfo(null, customer.getSubject());
        } catch (BaseException e) {
            errorMessage = "Couldn't retrieve subject from DataONE: " + "'" + customer.getSubject() + "'.";
            AuthenticationException ae = new AuthenticationException(errorMessage);
            ae.initCause(e);
            throw ae;
        }

        customer.setSubjectInfo(subjectInfo);
        return customer;
    }

    /**
     * Return a customer instance with included subjectInfo, if available
     * @param token  the token representing the customer
     * @return customer  the customer represented by the token
     * @throws AuthenticationException  a token parsing exception
     */
    public Customer getCustomerWithSubjectInfo(String token) throws AuthenticationException {

        Customer customer = null;
        String errorMessage = "Couldn't get subject information from the Coordinating Node: ";
        String subject = null;
        try {
            subject = getTokenSubject(token);
            customer = getCustomerStore().findCustomerBySubject(subject);
            if ( customer == null ) {
                log.info("A customer record doesn't exist yet for " + subject +
                    ". Creating a new customer.");
                customer = new Customer();
                customer.setSubject(subject);
            }
        } catch (ParseException e) {
            errorMessage = "Couldn't parse the given token: ";
            throw new AuthenticationException(errorMessage + e.getMessage());
        }

        SubjectInfo subjectInfo = null;
        try {
            subjectInfo = getSubjectInfo(token, customer.getSubject());
        } catch (BaseException e) {
            errorMessage = "Couldn't retrieve subject from DataONE: " + "'" + customer.getSubject() + "'.";
            AuthenticationException ae = new AuthenticationException(errorMessage);
            ae.initCause(e);
            throw ae;
        }
        customer.setSubjectInfo(subjectInfo);
        return customer;
    }

    /**
     * Check if the given subject is an administrator
     * @param subject the subject to check
     * @return true if the subject is a  DataONE admin
     */
    public boolean isAdmin(String subject) {
        return getConfiguration().getAdminSubjects().contains(subject);
    }


    /**
     * Check if the given subject is an administrator
     * @param subject the subject to check
     * @return true if the subject is a  DataONE admin
     */
    public boolean isBookkeeperAdmin(String subject) { return getConfiguration().getBookkeeperAdminSubjects().contains(subject); }

    /**
     * For a given customer, return a filtered subject list with only associated subjects
     *
     * This method expands the subjects found in the customer subjectInfo list, and filters
     * out subjects in the subjects argument that are not found in the customer subjectInfo.  This
     * helps keep callers from getting database information not related to them.
     * @param customer  the calling customer
     * @param subjects  the list of subjects they want to get information about
     * @return subjects the list of subjects they are associated with
     */
    public Set<String> filterByAssociatedSubjects(Customer customer, Set<String> subjects) {

        SubjectInfo subjectInfo = customer.getSubjectInfo();
        Set<String> associatedSubjects = new HashSet<String>(); // no dupes with a Set

        if ( subjectInfo != null ) {
            List<Group> groups = subjectInfo.getGroupList();
            List<Person> persons = subjectInfo.getPersonList();

            for (String subject : subjects ) {
                // Check the groups list
                for (Group group : groups) {
                    if ( group.getSubject().getValue().equals(subject) ) {
                        associatedSubjects.add(subject);
                        break;
                    }
                }
                // Check the persons list (equivalent identities)
                for ( Person person : persons) {
                    if ( person.getSubject().getValue().equals(subject) ) {
                        associatedSubjects.add(subject);
                        break;
                    }
                }

            }
        }
        return associatedSubjects;
    }

    /**
     * For a given customer, return all associated subjects
     *
     * This method expands the subjects found in the customer subjectInfo list
     * @param customer  the calling customer
     * @return subjects the list of all subjects they are associated with
     */
    public Set<String> getAssociatedSubjects(Customer customer) {

        SubjectInfo subjectInfo = customer.getSubjectInfo();
        Set<String> associatedSubjects = new HashSet<String>(); // no dupes with a Set

        if ( subjectInfo != null ) {
            List<Group> groups = subjectInfo.getGroupList();
            List<Person> persons = subjectInfo.getPersonList();

                // Add all associated groups
                for (Group group : groups) {
                    associatedSubjects.add(group.getSubject().getValue());
                }
                // Add all equivalent identities
                for ( Person person : persons) {
                    associatedSubjects.add(person.getSubject().getValue());
                }
        }
        return associatedSubjects;
    }
}
