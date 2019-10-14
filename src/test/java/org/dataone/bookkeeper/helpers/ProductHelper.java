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

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Feature;
import org.dataone.bookkeeper.api.Product;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.jdbi.mappers.ProductMapper;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * A delegate class with helper methods for manipulating the products table during testing
 */
public class ProductHelper {

    /**
     * Remove a test product given its id
     * @param productId
     */
    public static void removeTestProduct(Integer productId) throws SQLException {

        BaseTestCase.dbi.useHandle(handle ->
            handle.execute("DELETE FROM products WHERE id = ?", productId)
        );
    }

    /**
     * Create a test product
     * @param productId
     * @return
     */
    public static Product createTestProduct(Integer productId) {

        Product product = new Product();
        product.setId(productId);
        product.setObject("product");
        product.setActive(true);
        product.setAmount(180000);
        product.setCaption("Small institutions or groups");
        product.setCreated(1559768309);
        product.setCurrency("USD");
        product.setDescription("Create multiple portals for your work and projects. " +
            "Help others understand and access your data.");
        product.setInterval("year");
        // product.setCreated(new Integer((int) Instant.now().getEpochSecond()));
        product.setName("Organization");
        product.setStatementDescriptor("DataONE Membership Plan - Organization");
        product.setType("service");
        product.setUnitLabel("membership");
        product.setUrl("https://dataone.org/memberships/organization");
        product.setMetadata(ProductHelper.createTestMetadata());

        return product;
    }

    /**
     * Create a test metadata instance as a Jackson ObjectNode
     * @return
     */
    public static ObjectNode createTestMetadata() {
        ObjectMapper mapper = Jackson.newObjectMapper();
        mapper.setSerializationInclusion(Include.NON_EMPTY);
        mapper.setSerializationInclusion(Include.NON_NULL);
        ObjectNode metadata = mapper.createObjectNode();

        // Build up a feature list
        Feature feature1 = new Feature();
        Feature feature2 = new Feature();
        Feature feature3 = new Feature();
        Feature feature4 = new Feature();
        Feature feature5 = new Feature();
        Feature feature6 = new Feature();

        // Build a quota for feature 1
        Integer quota_id = 1;
        String quota_type = "quota";
        String quota_name = "custom_portal_count";
        Integer quota_soft_limit = 3;
        Integer quota_hard_limit = 3;
        Integer quota_usage = null;
        String quota_unit = "portal";
        Integer quota_customer_id = null;
        String quota_subject = null;

        Quota quota = new Quota(quota_id, quota_type, quota_name,
            quota_soft_limit, quota_hard_limit, quota_usage, quota_unit, quota_customer_id, quota_subject);

        feature1.setName("custom_portal");
        feature1.setLabel("Branded Portals");
        feature1.setDescription("Showcase your research, data, results, and usage metrics by building a custom web portal.");
        feature1.setQuota(quota);

        feature2.setName("custom_search_filters");
        feature2.setLabel("Custom Search Filters");
        feature2.setDescription("Create custom search filters in your portal to allow scientists to search your holdings using filters appropriate to your field of science.");

        feature3.setName("fair_data_assessment");
        feature3.setLabel("FAIR Data Assessments");
        feature3.setDescription("Access quality metric reports using the FAIR data suite of checks.");

        feature4.setName("custom_quality_metrics");
        feature4.setLabel("Custom Quality Metrics");
        feature4.setDescription("Create a suite of custom quality metadata checks specific to your datasets.");

        feature5.setName("aggregated_metrics");
        feature5.setLabel("Aggregated Metrics");
        feature5.setDescription("Access and share reports on aggregated usage metrics such as dataset views, data downloads, and dataset citations.");

        feature6.setName("dataone_voting_member");
        feature6.setLabel("DataONE Voting Member");
        feature6.setDescription("Vote on the direction and priorities at DataONE Community meetings.");

        // Add the feature list to the metadata object
        metadata.putArray("features")
            .add(mapper.convertValue(feature1, ObjectNode.class))
            .add(mapper.convertValue(feature2, ObjectNode.class))
            .add(mapper.convertValue(feature3, ObjectNode.class))
            .add(mapper.convertValue(feature4, ObjectNode.class))
            .add(mapper.convertValue(feature5, ObjectNode.class))
            .add(mapper.convertValue(feature6, ObjectNode.class));

        return metadata;
    }
    /**
     * Return the number of products for the given product id
     * @param productId
     * @return
     */
    public static Integer getProductCountById(Integer productId) {
        Integer count = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT count(*) FROM products WHERE id = :id")
                .bind("id", productId)
                .mapTo(Integer.class)
                .one()
        );
        return count;

    }

    /**
     * Insert a test product into the database
     * @param productId
     * @return
     */
    public static Integer insertTestProduct(Integer productId) {

        try {
            Product product = ProductHelper.createTestProduct(productId);

            BaseTestCase.dbi.useHandle(handle ->
                handle.execute("INSERT INTO products " +
                    "(id, " +
                    "object, " +
                    "active, " +
                    "amount, " +
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
                    "metadata) VALUES " +
                    "(?, ?, ?, ?, ?, to_timestamp(?), ?, ?, ?, ?, ?, ?, ?, ?, ?::json)",
                    product.getId(),
                    product.getObject(),
                    product.isActive(),
                    product.getAmount(),
                    product.getCaption(),
                    product.getCreated(),
                    product.getCurrency(),
                    product.getDescription(),
                    product.getInterval(),
                    product.getName(),
                    product.getStatementDescriptor(),
                    product.getType(),
                    product.getUnitLabel(),
                    product.getUrl(),
                    product.getMetadata().toString()
                )
            );
        } catch (Exception e) {
            fail(e);
        }
        return productId;
    }

    public static Product insertTestProduct(Product product) {

        try {
            BaseTestCase.dbi.useHandle(handle ->
                handle.execute("INSERT INTO products " +
                        "(id, " +
                        "object, " +
                        "active, " +
                        "amount, " +
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
                        "metadata) VALUES " +
                        "(?, ?, ?, ?, ?, to_timestamp(?), ?, ?, ?, ?, ?, ?, ?, ?, ?::json)",
                    product.getId(),
                    product.getObject(),
                    product.isActive(),
                    product.getAmount(),
                    product.getCaption(),
                    product.getCreated(),
                    product.getCurrency(),
                    product.getDescription(),
                    product.getInterval(),
                    product.getName(),
                    product.getStatementDescriptor(),
                    product.getType(),
                    product.getUnitLabel(),
                    product.getUrl(),
                    product.getMetadata().toString()
                )
            );

        } catch (Exception e) {
            fail(e);
        }
        return product;
    }
    /**
     * Return a product given its id
     * @param productId
     * @return
     */
    public static Product getProductById(Integer productId) {
        Product product = BaseTestCase.dbi.withHandle(handle ->
            handle.createQuery("SELECT " +
                "id, " +
                "object, " +
                "active, " +
                "amount, " +
                "caption, " +
                "date_part('epoch', created)::int as created, " +
                "currency, " +
                "description, " +
                "interval, " +
                "name, " +
                "statementDescriptor, " +
                "type, " +
                "unitLabel, " +
                "url, " +
                "metadata " +
                "FROM products WHERE id = :id")
                .bind("id", productId)
                .map(new ProductMapper())
                .one()
        );
        return product;
    }
}
