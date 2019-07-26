package org.dataone.bookkeeper.jdbi;

import org.dataone.bookkeeper.api.Product;
import org.dataone.bookkeeper.jdbi.mappers.ProductMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

/**
 * The product data access interfaces used to create, read, update, and delete
 * products from the database
 */
public interface ProductDAO {

    /**
     * List all products
     */
    @SqlQuery("SELECT id, object, active, name, caption, description, " +
        "date_part('epoch', created)::int as created, statementDescriptor, type, " +
        "unitLabel, url, metadata::json as metadata FROM products ")
    @RegisterRowMapper(ProductMapper.class)
    @UseRowMapper(ProductMapper.class)
    List<Product> listProducts();

    /**
     * Find products by identifier
     * @param id
     */
    @SqlQuery("SELECT id, object, active, name, caption, description, " +
        "date_part('epoch', created)::int as created, statementDescriptor, type, " +
        "unitLabel, url, metadata::json as metadata FROM products WHERE id = :id")
    @RegisterRowMapper(ProductMapper.class)
    @UseRowMapper(ProductMapper.class)
    List<Product> findProductsById(@Bind("id") Integer id);

    /**
     * Find products by name
     * @param name
     */
    @SqlQuery("SELECT id, object, active, name, caption, description, " +
        "date_part('epoch', created)::int as created, statementDescriptor, type, " +
        "unitLabel, url, metadata::json as metadata FROM products WHERE name = :name")
    @RegisterRowMapper(ProductMapper.class)
    @UseRowMapper(ProductMapper.class)
    List<Product> findProductsByName(@Bind("name") String name);

    /**
     * Find products by active status
     * @param active
     */
    @SqlQuery("SELECT id, object, active, name, caption, description, " +
        "date_part('epoch', created)::int as created, statementDescriptor, type, " +
        "unitLabel, url, metadata::json as metadata FROM products WHERE active = :active")
    @RegisterRowMapper(ProductMapper.class)
    @UseRowMapper(ProductMapper.class)
    List<Product> findProductsByActiveStatus(@Bind("active") boolean active);

    /**
     * Find products by description
     * @param description
     */
    @SqlQuery("SELECT id, object, active, name, caption, description, " +
        "date_part('epoch', created)::int as created, statementDescriptor, type, " +
        "unitLabel, url, metadata::json as metadata FROM products " +
        "WHERE description LIKE :description")
    @RegisterRowMapper(ProductMapper.class)
    @UseRowMapper(ProductMapper.class)
    List<Product> findProductsByDescription(@Bind("description") String description);

    @SqlUpdate("INSERT INTO products (" +
        "id, " +
        "object , " +
        "active, " +
        "name, " +
        "caption, " +
        "description, " +
        "created, " +
        "statementDescriptor, " +
        "type, " +
        "unitLabel, " +
        "url, " +
        "metadata) " +
        "VALUES (" +
        ":getId, " +
        ":getObject , " +
        ":isActive, " +
        ":getName, " +
        ":getCaption, " +
        ":getDescription, " +
        "to_timestamp(:getCreated), " +
        ":getStatementDescriptor, " +
        ":getType, " +
        ":getUnitLabel, " +
        ":getUrl, " +
        ":getMetadataJSON::json)")
    void insert(@BindMethods Product product);

    @SqlUpdate("UPDATE products SET " +
        "object = :getObject, " +
        "active = :isActive, " +
        "name = :getName, " +
        "caption = :getCaption, " +
        "description = :getDescription, " +
        "created = to_timestamp(:getCreated), " +
        "statementDescriptor = :getStatementDescriptor, " +
        "type = :getType, " +
        "unitLabel = :getUnitLabel, " +
        "url = :getUrl, " +
        "metadata = :getMetadataJSON::json " +
        "WHERE id = :getId")
    void update(@BindMethods Product product);

    /**
     * Delete a product given the id
     * @param id
     */
    @SqlUpdate("DELETE FROM products WHERE id = :id")
    void delete(@Bind("id") Integer id);
}
