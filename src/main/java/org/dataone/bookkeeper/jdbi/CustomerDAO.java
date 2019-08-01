package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.jdbi.mappers.CustomerMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowReducer;

import java.util.List;

/**
 * The customer data access interfaces used to create, read, update, and delete
 * customers from the database
 */
public interface CustomerDAO {

    /** The query used to find all customers with their quotas */
    String SELECT_CLAUSE =
        "SELECT " +
            "c.id AS c_id, c.object AS c_object, c.orcid AS c_orcid, c.balance AS c_balance, " +
            "c.address AS c_address, date_part('epoch', c.created)::int AS c_created, " +
            "c.currency AS c_currency, c.delinquent AS c_delinquent, " +
            "c.description AS c_description, c.discount::json AS c_discount, " +
            "c.email AS c_email, c.invoicePrefix AS c_invoicePrefix, " +
            "c.invoiceSettings::json AS c_invoiceSettings, " +
            "c.metadata::json AS c_metadata, c.givenName AS c_givenName, " +
            "c.surName AS c_surName, c.phone AS c_phone, " +
            "q.id, q.object, q.name, q.softLimit, q.hardLimit, q.unit, q.customerId " +
        "FROM customers c " +
        "LEFT JOIN quotas q " +
        "ON c.id = q.customerId ";

    /** Clause to order listed results */
    String ORDER_CLAUSE = "ORDER BY c.surName, c.givenName, q.name ";

    /** The full ordered query */
    String SELECT_ALL = SELECT_CLAUSE + ORDER_CLAUSE;

    /** The query used to find an individual customer */
    String SELECT_ONE = SELECT_CLAUSE + "WHERE c.id = :id";

    /** The query used to find a customer by ORCID identifier */
    String SELECT_ORCID = SELECT_CLAUSE + "WHERE c.orcid = :orcid";

    /** The query used to find a customer by email */
    String SELECT_EMAIL = SELECT_CLAUSE + "WHERE c.email = :email";

    /**
     * List all customers with their quotas
     * @return customers The list of customers
     */
    @SqlQuery(SELECT_ALL)
    @RegisterRowMapper(CustomerMapper.class)
    @RegisterBeanMapper(value = Quota.class)
    @UseRowReducer(CustomerQuotasReducer.class)
    List<Customer> listCustomers();

    /**
     * Get an individual customer
     * @param id the customer identifier
     * @return customer The individual customer
     */
    @SqlQuery(SELECT_ONE)
    @RegisterRowMapper(CustomerMapper.class)
    @RegisterBeanMapper(value = Quota.class)
    @UseRowReducer(CustomerQuotasReducer.class)
    Customer getCustomer(@Bind("id") Integer id);

    /**
     * Get a customer by ORCID identifier
     * @param orcid the customer ORCID identifier
     * @return customer the customer with the given ORCID identifier
     */
    @SqlQuery(SELECT_ORCID)
    @RegisterRowMapper(CustomerMapper.class)
    @RegisterBeanMapper(value = Quota.class)
    @UseRowReducer(CustomerQuotasReducer.class)
    Customer findCustomerByOrcid(@Bind("orcid") String orcid);

    /**
     * Get a customer by email
     * @param email the customer email
     * @return customer the customer with the given email
     */
    @SqlQuery(SELECT_EMAIL)
    @RegisterRowMapper(CustomerMapper.class)
    @RegisterBeanMapper(value = Quota.class)
    @UseRowReducer(CustomerQuotasReducer.class)
    Customer findCustomerByEmail(@Bind("email") String email);

    /**
     * Insert a customer
     * @param customer the customer to insert
     */
    @SqlUpdate("INSERT INTO customers " +
        "(id, object, orcid, balance, address, created, currency, delinquent, " +
        "description, discount, email, invoicePrefix, invoiceSettings, " +
        "metadata, givenName, surName, phone) " +
        "VALUES (" +
        ":getId, " +
        ":getObject, " +
        ":getOrcid, " +
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
        ":getPhone)")
    void insert(@BindMethods Customer customer);

    /**
     * Update a customer
     * @param customer the customer to update
     */
    @SqlUpdate("UPDATE customers SET " +
        "object = :getObject, " +
        "orcid = :getOrcid, " +
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
        "phone = :getPhone")
    void update(@BindMethods Customer customer);

    /**
     * Delete a customer
     * @param id
     */
    @SqlUpdate("DELETE FROM customers WHERE id = :id")
    void delete(@Bind("id") Integer id);
}
