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
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

/**
 * The product data access interfaces used to create, read, update, and delete
 * products from the database
 */
public interface ProductStore {

    String SELECT_CLAUSE =
        "SELECT " +
            "id, " +
            "object, " +
            "active, " +
            "amount, " +
            "caption, " +
            "date_part('epoch', created)::int AS created, " +
            "currency, " +
            "description, " +
            "interval, " +
            "name, " +
            "statementDescriptor, " +
            "type, " +
            "unitLabel, " +
            "url, " +
            "metadata::json AS metadata " +
        "FROM products ";

    String ORDER_CLAUSE = "ORDER BY name, created DESC ";

    String SELECT_ALL = SELECT_CLAUSE + ORDER_CLAUSE;

    String SELECT_BY_ID = SELECT_CLAUSE + "WHERE id = :id " + ORDER_CLAUSE;

    String SELECT_BY_NAME = SELECT_CLAUSE + "WHERE name = :name " + ORDER_CLAUSE;

    String SELECT_BY_ACTIVE = SELECT_CLAUSE + "WHERE active = :active " + ORDER_CLAUSE;

    String SELECT_BY_DESCRIPTION = SELECT_CLAUSE + "WHERE description LIKE :description " + ORDER_CLAUSE;

    /**
     * List all products
     */
    @SqlQuery(SELECT_ALL)
    @RegisterRowMapper(ProductMapper.class)
    @UseRowMapper(ProductMapper.class)
    List<Product> listProducts();

    /**
     * Find products by identifier
     * @param id
     */
    @SqlQuery(SELECT_BY_ID)
    @RegisterRowMapper(ProductMapper.class)
    @UseRowMapper(ProductMapper.class)
    Product getProduct(@Bind("id") Integer id);

    /**
     * Find products by name
     * @param name
     */
    @SqlQuery(SELECT_BY_NAME)
    @RegisterRowMapper(ProductMapper.class)
    @UseRowMapper(ProductMapper.class)
    List<Product> findProductsByName(@Bind("name") String name);

    /**
     * Find products by active status
     * @param active
     */
    @SqlQuery(SELECT_BY_ACTIVE)
    @RegisterRowMapper(ProductMapper.class)
    @UseRowMapper(ProductMapper.class)
    List<Product> findProductsByActiveStatus(@Bind("active") boolean active);

    /**
     * Find products by description
     * @param description
     */
    @SqlQuery(SELECT_BY_DESCRIPTION)
    @RegisterRowMapper(ProductMapper.class)
    @UseRowMapper(ProductMapper.class)
    List<Product> findProductsByDescription(@Bind("description") String description);

    /**
     * Insert a product
     * @param product the product to insert
     */
    @SqlUpdate("INSERT INTO products (" +
        "object , " +
        "active, " +
        "amount," +
        "caption, " +
        "created, " +
        "currency, " +
        "description, " +
        "interval, " +
        "name, " +
        "statementDescriptor, " +
        "type, " +
        "unitLabel, " +
        "url, " +
        "metadata " +
        ") VALUES (" +
        ":getObject, " +
        ":isActive, " +
        ":getAmount, " +
        ":getCaption, " +
        "to_timestamp(:getCreated), " +
        ":getCurrency, " +
        ":getDescription, " +
        ":getInterval, " +
        ":getName, " +
        ":getStatementDescriptor, " +
        ":getType, " +
        ":getUnitLabel, " +
        ":getUrl, " +
        ":getMetadataJSON::json) " +
        "RETURNING id")
    @GetGeneratedKeys
    Integer insert(@BindMethods Product product);

    /**
     * Update a product
     * @param product the product to update
     */
    @SqlUpdate("UPDATE products SET " +
        "object = :getObject, " +
        "active = :isActive, " +
        "amount = :getAmount, " +
        "caption = :getCaption, " +
        "created = to_timestamp(:getCreated), " +
        "currency = :getCurrency, " +
        "description = :getDescription, " +
        "interval = :getInterval, " +
        "name = :getName, " +
        "statementDescriptor = :getStatementDescriptor, " +
        "type = :getType, " +
        "unitLabel = :getUnitLabel, " +
        "url = :getUrl, " +
        "metadata = :getMetadataJSON::json " +
        "WHERE id = :getId " +
        "RETURNING id")
    @GetGeneratedKeys
    Integer update(@BindMethods Product product);

    /**
     * Delete a product given the id
     * @param id
     */
    @SqlUpdate("DELETE FROM products WHERE id = :id")
    void delete(@Bind("id") Integer id);
}
