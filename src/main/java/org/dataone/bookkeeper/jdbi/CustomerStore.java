/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2019. All rights reserved.
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

package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.jdbi.mappers.CustomerMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * The customer data access interfaces used to create, read, update, and delete
 * customers from the database
 */
public interface CustomerStore {

    /** The query used to find all customers */
    String SELECT_CLAUSE =
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
        "FROM customers c ";

    /** Clause to order listed results */
    String ORDER_CLAUSE = "ORDER BY c.surName, c.givenName ";

    /** The full ordered query */
    String SELECT_ALL = SELECT_CLAUSE + ORDER_CLAUSE;

    /** The query used to find an individual customer */
    String SELECT_ONE = SELECT_CLAUSE + "WHERE c.id = :id";

    /** The query used to find a customer by subject identifier */
    String SELECT_SUBJECT = SELECT_CLAUSE + "WHERE c.subject = :subject";

    /** The query used to find a customer by email */
    String SELECT_EMAIL = SELECT_CLAUSE + "WHERE c.email = :email";

    /**
     * List all customers
     * @return customers The list of customers
     */
    @SqlQuery(SELECT_ALL)
    @RegisterRowMapper(CustomerMapper.class)
    List<Customer> listCustomers();

    /**
     * Get an individual customer
     * @param id the customer identifier
     * @return customer The individual customer
     */
    @SqlQuery(SELECT_ONE)
    @RegisterRowMapper(CustomerMapper.class)
    Customer getCustomer(@Bind("id") Integer id);

    /**
     * Get a customer by subject identifier
     * @param subject the customer subject identifier
     * @return customer the customer with the given subject identifier
     */
    @SqlQuery(SELECT_SUBJECT)
    @RegisterRowMapper(CustomerMapper.class)
    Customer findCustomerBySubject(@Bind("subject") String subject);

    /**
     * Get a customer by email
     * @param email the customer email
     * @return customer the customer with the given email
     */
    @SqlQuery(SELECT_EMAIL)
    @RegisterRowMapper(CustomerMapper.class)
    Customer findCustomerByEmail(@Bind("email") String email);

    /**
     * Insert a customer
     * @param customer the customer to insert
     */
    @SqlUpdate(
        "INSERT INTO customers (" +
            "object, " +
            "subject, " +
            "balance, " +
            "address, " +
            "created, " +
            "currency, " +
            "delinquent, " +
            "description, " +
            "discount, " +
            "email, " +
            "invoicePrefix, " +
            "invoiceSettings, " +
            "metadata, " +
            "givenName, " +
            "surName, " +
            "phone" +
        ") VALUES (" +
            ":getObject, " +
            ":getSubject, " +
            ":getBalance, " +
            ":getAddressJSON::json, " +
            "to_timestamp(:getCreated), " +
            ":getCurrency, " +
            ":isDelinquent, " +
            ":getDescription, " +
            ":getDiscountJSON::json, " +
            ":getEmail, " +
            ":getInvoicePrefix, " +
            ":getInvoiceSettingsJSON::json, " +
            ":getMetadataJSON::json, " +
            ":getGivenName, " +
            ":getSurName, " +
            ":getPhone" +
        ") RETURNING id")
    @GetGeneratedKeys
    Integer insert(@BindMethods Customer customer);

    /**
     * Update a customer
     * @param customer the customer to update
     */
    @SqlUpdate("UPDATE customers SET " +
        "object = :getObject, " +
        "subject = :getSubject, " +
        "balance = :getBalance, " +
        "address = :getAddressJSON::json, " +
        "created = to_timestamp(:getCreated), " +
        "currency = :getCurrency, " +
        "delinquent = :isDelinquent, " +
        "description = :getDescription, " +
        "discount = :getDiscountJSON::json, " +
        "email = :getEmail, " +
        "invoicePrefix = :getInvoicePrefix, " +
        "invoiceSettings = :getInvoiceSettingsJSON::json, " +
        "metadata = :getMetadataJSON::json, " +
        "givenName = :getGivenName, " +
        "surName = :getSurName, " +
        "phone = :getPhone " +
        "RETURNING id")
    @GetGeneratedKeys
    Integer update(@BindMethods Customer customer);

    /**
     * Delete a customer
     * @param id
     */
    @SqlUpdate("DELETE FROM customers WHERE id = :id")
    void delete(@Bind("id") Integer id);
}
