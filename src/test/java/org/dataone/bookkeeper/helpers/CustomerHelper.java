package org.dataone.bookkeeper.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Address;
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.Quota;

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
                    "(id, object, orcid, balance, address, created, currency, delinquent, " +
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
                "(id, object, orcid, balance, address, created, currency, delinquent, " +
                "description, discount, email, invoicePrefix, invoiceSettings, " +
                "metadata, givenName, surName, phone) " +
                "VALUES (?, ?, ?, ?, ?::json, to_timestamp(?), ?, ?, " +
                "?, ?::json, ?, ?, ?::json, ?::json, ?, ?, ?)",
                customer.getId(),
                customer.getObject(),
                customer.getOrcid(),
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
            "805-893-2500",
            new LinkedList<Quota>()
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


}
