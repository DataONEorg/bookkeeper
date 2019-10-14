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

package org.dataone.bookkeeper.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Address;
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.jdbi.mappers.CustomerMapper;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * A delegate class with helper methods for manipulating the customers table for testing
 */
public class CustomerHelper {

    /**
     * Insert a test customer with the given customer id
     * @param customerId
     * @return
     */
    public static Integer insertTestCustomer(Integer customerId) throws SQLException {

        BaseTestCase.dbi.useHandle(handle -> {
            handle.execute("INSERT INTO customers " +
                    "(id, object, subject, balance, address, created, currency, delinquent, " +
                    "description, discount, email, invoicePrefix, invoiceSettings, " +
                    "metadata, givenName, surName, phone) " +
                    "VALUES (?, ?, ?, ?, ?::json, to_timestamp(?), ?, ?, " +
                    "?, ?::json, ?, ?, ?::json, ?::json, ?, ?, ?)",
                customerId,
                "customer",
                "http://orcid.org/0000-0002-8121-2341",
                0,
                "{}",
                1562866734,
                "USD",
                false,
                null,
                "{}",
                "cjones@nceas.ucsb.edu",
                null,
                "{}",
                "{}",
                "Christopher",
                "Jones",
                "805-893-2500");
        });

        return customerId;
    }

    /**
     * Insert a test customer given the Customer instance
     * @param customer
     * @return customer the inserted customer
     * @throws SQLException
     * @throws JsonProcessingException
     */
    public static Customer insertTestCustomer(Customer customer)
        throws SQLException, JsonProcessingException {
        BaseTestCase.dbi.useHandle(handle ->
            handle.execute("INSERT INTO customers " +
                "(id, object, subject, balance, address, created, currency, delinquent, " +
                "description, discount, email, invoicePrefix, invoiceSettings, " +
                "metadata, givenName, surName, phone) " +
                "VALUES (?, ?, ?, ?, ?::json, to_timestamp(?), ?, ?, " +
                "?, ?::json, ?, ?, ?::json, ?::json, ?, ?, ?)",
                customer.getId(),
                customer.getObject(),
                customer.getSubject(),
                customer.getBalance(),
                Jackson.newObjectMapper().writeValueAsString(customer.getAddress()),
                customer.getCreated(),
                customer.getCurrency(),
                customer.isDelinquent(),
                customer.getDescription(),
                Jackson.newObjectMapper().writeValueAsString(customer.getDiscount()),
                customer.getEmail(),
                customer.getInvoicePrefix(),
                Jackson.newObjectMapper().writeValueAsString(customer.getInvoiceSettings()),
                Jackson.newObjectMapper().writeValueAsString(customer.getMetadata()),
                customer.getGivenName(),
                customer.getSurName(),
                customer.getPhone()
                )
        );
        return customer;
    }

    /**
     * Create a customer for unit tests
     * @param customerId the customer id
     * @return customer the Customer instance
     */
    public static Customer createCustomer(Integer customerId) {
        Customer customer = new Customer(
            customerId,
            "customer",
            "http://orcid.org/0000-0002-8121-2341",
            0,
            new Address(
                "735 State Street",
                "Suite 300",
                "Santa Barbara",
                "CA",
                "93106",
                "USA"
            ),
            1562866734,
            "USD",
            false,
            "",
            Jackson.newObjectMapper().createObjectNode(),
            "cjones@nceas.ucsb.edu",
            "",
            Jackson.newObjectMapper().createObjectNode(),
            Jackson.newObjectMapper().createObjectNode(),
            "Christopher",
            "Jones",
            "805-893-2500"
        );
        return customer;
    }
    /**
     * Remove a test customer given its id
     * @param customerId
     */
    public static void removeTestCustomer(Integer customerId) throws SQLException {

        BaseTestCase.dbi.useHandle(handle ->
            handle.execute("DELETE FROM customers WHERE id = ?", customerId)
        );
    }


    /**
     * Get a customer given its id
     * @param customerId
     * @return customer the customer of the given id
     */
    public static Customer getCustomerById(Integer customerId) {
        Customer customer = null;
            customer = BaseTestCase.dbi.withHandle(handle ->
                handle.createQuery(
                    "SELECT " +
                    "c.id AS c_id, " +
                    "c.object AS c_object, " +
                    "c.subject AS c_subject, " +
                    "c.balance AS c_balance, " +
                    "c.address AS c_address, " +
                    "date_part('epoch', c.created)::int AS c_created, " +
                    "c.currency AS c_currency, " +
                    "c.delinquent AS c_delinquent, " +
                    "c.description AS c_description, " +
                    "c.discount::json AS c_discount, " +
                    "c.email AS c_email, " +
                    "c.invoicePrefix AS c_invoicePrefix, " +
                    "c.invoiceSettings::json AS c_invoiceSettings, " +
                    "c.metadata::json AS c_metadata, " +
                    "c.givenName AS c_givenName, " +
                    "c.surName AS c_surName, " +
                    "c.phone AS c_phone " +
                    "FROM customers c WHERE c.id = :id")
                    .bind("id", customerId)
                    .map(new CustomerMapper())
                    .one()
            );
        return customer;
    }

    /**
     * Return the number of customers given the customer id
     * @param customerId the id of the customer
     * @return count the number of customers
     */
    public static Integer getCustomerCountById(Integer customerId) {
        Integer count = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT count(*) FROM customers WHERE id = :id")
                .bind("id", customerId)
                .mapTo(Integer.class)
                .one()
        );
        return count;
    }
}
