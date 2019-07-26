package org.dataone.bookkeeper.helpers;

import org.dataone.bookkeeper.BaseTestCase;

import java.sql.SQLException;

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
                    "VALUES (?, ?, ?, ?, to_json(?), to_timestamp(?), ?, ?, " +
                    "?, to_json(?), ?, ?, to_json(?), " +
                    "to_json(?), ?, ?, ?)",
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
     * Remove a test customer given its id
     * @param customerId
     */
    public static void removeTestCustomer(Integer customerId) throws SQLException {

        BaseTestCase.dbi.useHandle(handle ->
            handle.execute("DELETE FROM customers WHERE id = ?", customerId)
        );
    }


}
