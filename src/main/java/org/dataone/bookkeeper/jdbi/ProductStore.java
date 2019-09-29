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
public interface ProductStore {

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
    Product getProduct(@Bind("id") Integer id);

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

    /**
     * Insert a product
     * @param product the product to insert
     */
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
        "metadata " +
        ") VALUES (" +
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

    /**
     * Update a product
     * @param product the product to update
     */
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
