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
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.jdbi.CustomerStore;
import org.jdbi.v3.sqlobject.CreateSqlObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Objects;

/**
 * CustomerService provides an abstraction of the CustomerService store to hide store
 * implementation details.
 */
public abstract class CustomerService {

    /**
     * Register a logger
     */
    public Log log = LogFactory.getLog(CustomerService.class);

    /**
     * Create a CustomerStore instance
     * @return customerStore  the customer store instance
     */
    @CreateSqlObject
    abstract CustomerStore customerStore();

    /**
     * List all customers
     * @return customers the customer list
     */
    public List<Customer> listCustomers() {
        List<Customer> customers = null;
        try {
            customers = customerStore().listCustomers();
        } catch (Exception e) {
            log.error("Customer listing failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return customers;
    }

    /**
     * Get the customer by id
     * @param id  the customer id
     * @return customer  the customer
     */
    public Customer getCustomer(Integer id) {
        Customer customer = null;

        // Do we have a valid id?
        if (Objects.isNull(id) || id.intValue() < 0) {
            throw new WebApplicationException(
                "Please provide a valid id", Status.EXPECTATION_FAILED);
        }

        try {
            customer = customerStore().getCustomer(id);
        } catch (Exception e) {
            log.error("Customer search by id failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return customer;
    }

    /**
     * Get the customer by subject
     * @param subject  the customer subject
     * @return customer  the customer
     */
    public Customer findCustomerBySubject(String subject) {
        Customer customer;

        // Do we have a valid id?
        if (Objects.isNull(subject) || subject.equals("")) {
            throw new WebApplicationException(
                "Please provide a valid customer subject", Status.EXPECTATION_FAILED);
        }

        try {
            customer = customerStore().findCustomerBySubject(subject);
        } catch (Exception e) {
            log.error("Customer search by customer subject failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return customer;
    }


    /**
     * Get the customer by email
     * @param email  the customer email
     * @return customer  the customer
     */
    public Customer findCustomerByEmail(String email) {
        Customer customer;

        // Do we have a valid id?
        if (Objects.isNull(email) || email.equals("")) {
            throw new WebApplicationException(
                "Please provide a valid customer email", Status.EXPECTATION_FAILED);
        }

        try {
            customer = customerStore().findCustomerByEmail(email);
        } catch (Exception e) {
            log.error("Customer search by customer email failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return customer;
    }

    /**
     * Insert the customer
     * @param customer  the customer
     * @return customer  the customer
     */
    public Customer insert(Customer customer){

        // Do we have a valid customer?
        if (Objects.isNull(customer)) {
            log.error("The customer insert failed for " + customer.getId());
            throw new WebApplicationException("Please provide a valid customer.", Status.NOT_MODIFIED);
        }

        // Insert it
        try {
            customerStore().insert(customer);
        } catch (Exception e) {
            log.error("The customer insert failed for " + customer.getId());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.NOT_MODIFIED);
        }
        return customer;
    }

    /**
     * Update the customer
     * @param customer  the customer
     * @return customer  the customer
     */
    public Customer update(Customer customer) {
        // Do we have a valid customer?
        if (Objects.isNull(customer)) {
            log.error("The customer update failed for " + customer.getId());
            throw new WebApplicationException("Please provide a valid customer.", Status.NOT_MODIFIED);
        }

        // Update it
        try {
            customerStore().update(customer);
        } catch (Exception e) {
            log.error("The customer update failed for " + customer.getId());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.NOT_MODIFIED);
        }
        return customer;
    }

    /**
     * Delete the customer by id
     * @param id  the customer id
     * @return deleted  true if the customer was deleted
     */
    public Boolean delete(Integer id) {
        Boolean deleted = false;
        try {
            customerStore().delete(id);
            deleted = true;
        } catch (Exception e) {
            log.error("The customer delete failed for " + id);
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.NOT_MODIFIED);
        }
        return deleted;
    }

    // TODO: Add health check
}
