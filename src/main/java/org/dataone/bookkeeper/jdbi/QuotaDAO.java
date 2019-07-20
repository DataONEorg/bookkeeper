package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.api.Quota;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * The quota data access interfaces used to create, read, update, and delete
 * quotas from the database
 */
@RegisterBeanMapper(Quota.class)
public interface QuotaDAO {

    /**
     * List all quotas
     */
    @SqlQuery(  "SELECT " +
        "q.id, q.object, q.name, q.softLimit, q.hardLimit, q.unit, q.customerId " +
        "FROM quotas q")
    List<Quota> listQuotas();

    /**
     * Find quotas by quota identifier
     * @param id
     */
    @SqlQuery("SELECT " +
        "q.id, q.object, q.name, q.softLimit, q.hardLimit, q.unit, q.customerId " +
        "FROM quotas q " +
        "WHERE q.id = :id")
    List<Quota> findQuotasById(@Bind("id") Integer id);

    /**
     * Find quotas by customer identifier.
     *
     * Pass a null customerId to list all product-associated quotas (i.e. not bound to a customer).
     * @param customerId
     */
    @SqlQuery("SELECT " +
        "q.id, q.object, q.name, q.softLimit, q.hardLimit, q.unit, q.customerId " +
        "FROM quotas q " +
        "WHERE q.customerId = :customerId")
    List<Quota> findQuotasByCustomerId(@Bind("customerId") Integer customerId);


    /**
     * Insert a quota with a given Quota instance
     * @param quota
     */
    @SqlUpdate("INSERT INTO quotas " +
        "(object, name, softLimit, hardLimit, unit, customerId) " +
        "VALUES " +
        "(:object, :name, :softLimit, :hardLimit, :unit, :customerId)")
    void insert(@BindBean Quota quota);

    /**
     * Insert a quota with an optionally null customer ID
     * @param object
     * @param name
     * @param softLimit
     * @param hardLimit
     * @param unit
     * @param customerId
     */
    @SqlUpdate("INSERT INTO quotas " +
        "(object, name, softLimit, hardLimit, unit, customerId) " +
        "VALUES " +
        "(:object, :name, :softLimit, :hardLimit, :unit, :customerId)")
    void insert(@Bind("object") String object,
                @Bind("name") String name,
                @Bind("softLimit") Integer softLimit,
                @Bind("hardLimit") Integer hardLimit,
                @Bind("unit") String unit,
                @Bind("customerId") Integer customerId);

    /**
     * Update a quota by the quota id
     * @param id
     * @param object
     * @param name
     * @param softLimit
     * @param hardLimit
     * @param unit
     * @param customerId
     */
    @SqlUpdate("UPDATE quotas " +
        "SET object = :object, " +
        "name = :name, " +
        "softLimit = :softLimit," +
        "unit = :unit, " +
        "customerId = :customerId " +
        "WHERE id = :id")
    void update(@Bind("id") Integer id,
                @Bind("object") String object,
                @Bind("name") String name,
                @Bind("softLimit") Integer softLimit,
                @Bind("hardLimit") Integer hardLimit,
                @Bind("unit") String unit,
                @Bind("customerId") Integer customerId);

    /**
     * Delete a quota given the quota id
     * @param id
     */
    @SqlUpdate("DELETE FROM quotas WHERE id = :id")
    void delete(@Bind("id") Integer id);
}
