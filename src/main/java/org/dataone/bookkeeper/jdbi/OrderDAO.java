package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.api.Order;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * The order data access interfaces used to create, read, update, and delete
 * orders from the database
 */
public interface OrderDAO {

    /**
     * List all orders
     * @return
     */
    List<Order> listOrders();

    /**
     * Get an order by order id
     * @param id the order id
     * @return
     */
    Order getOrder(@Bind("id") Integer id);

    /**
     * Find orders by customer id
     * @param customerId the id of the customer
     * @return
     */
    List<Order> findOrdersByCustomerId(@Bind("customer") Integer customerId);

    /**
     * Insert an order
     * @param order the order to insert
     */
    @SqlUpdate("INSERT INTO orders (" +
        "id, " +
        "object, " +
        "amount, " +
        "amountReturned, " +
        "charge, " +
        "created, " +
        "currency, " +
        "customer, " +
        "email, " +
        "items, " +
        "metadata, " +
        "status, " +
        "statusTransitions, " +
        "updated, " +
        ") VALUES (" +
        ":getId" +
        ":getObject, " +
        ":getAmount, " +
        ":getAmountReturned, " +
        ":getChargeJSON::json, " +
        "to_timestamp(:getCreated), " +
        ":getCurrency, " +
        ":getCustomer, " +
        ":getEmail, " +
        ":getItemsJSON::json, " +
        ":getMetadataJSON::json, " +
        ":getStatus, " +
        ":getStatusTransitionsJSON::json, " +
        "to_timestamp(:getUpdated))")
    void insert(@BindMethods Order order);

    /**
     * Update an order
     * @param order the order to update
     */
    @SqlUpdate("UPDATE orders SET " +
        "object = :getObject, " +
        "amount = :getAmount, " +
        "amountReturned = :getAmountReturned, " +
        "charge = :getChargeJSON::json, " +
        "created = to_timestamp(:getCreated), " +
        "currency = :getCurrency, " +
        "customer = :getCustomer, " +
        "email = :getEmail, " +
        "items = :getItemsJSON::json, " +
        "metadata = :getMetadataJSON::json, " +
        "status = :getStatus, " +
        "statusTransitions = :getStatusTransitionsJSON::json, " +
        "updated = to_timestamp(:getUpdated) " +
        "WHERE id = :id")
    void update(@BindMethods Order order);

    /**
     * Delete an order
     * @param id the order id to delete
     */
    @SqlUpdate("DELETE FROM orders WHERE id = :id")
    void delete(@Bind("id") Integer id);
}
