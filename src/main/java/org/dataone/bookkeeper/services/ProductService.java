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

package org.dataone.bookkeeper.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataone.bookkeeper.api.Product;
import org.dataone.bookkeeper.jdbi.ProductStore;
import org.jdbi.v3.sqlobject.CreateSqlObject;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Objects;

/**
 * ProductService provides an abstraction of the products store to hide store
 * implementation details.
 */
public abstract class ProductService {

    /**
     * Register a logger
     */
    public Log log = LogFactory.getLog(ProductService.class);

    /**
     * Create a ProductStore instance
     * @return productStore - the product store instance
     */
    @CreateSqlObject
    abstract ProductStore productStore();

    /**
     * List all products
     * @return products the product list
     */
    public List<Product> listProducts() {
        List<Product> products = null;
        try {
            products = productStore().listProducts();
        } catch (Exception e) {
            log.error("Product listing failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return products;
    }

    /**
     * Get the product by id
     * @param id the product id
     * @return product  the product
     */
    public Product getProduct(Integer id) {
        Product product = null;

        // Do we have a valid id?
        if (Objects.isNull(id) || id.intValue() < 0) {
            throw new WebApplicationException(
                "Please provide a valid id", Status.EXPECTATION_FAILED);
        }

        try {
            product = productStore().getProduct(id);
        } catch (Exception e) {
            log.error("Product search by id failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return product;
    }

    /**
     * List the products by name
     * @param name the product name
     * @return products  the product list
     */
    public List<Product> findProductsByName(String name) {
        List<Product> products;

        // Do we have a valid name?
        if (Objects.isNull(name) || name.equals("")) {
            throw new WebApplicationException("Please provide a valid name", Status.EXPECTATION_FAILED);
        }

        // Find them
        try {
            products = productStore().findProductsByName(name);
        } catch (Exception e) {
            log.error("Product search by name failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return products;
    }

    /**
     * List the products by active status
     * @param status the product status
     * @return products  the products list
     */
    public List<Product> findProductsByActiveStatus(boolean status) {
        List<Product> products;

        try {
            products = productStore().findProductsByActiveStatus(status);
        } catch (Exception e) {
            log.error("Product search by status failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return products;
    }

    /**
     * List the products by description
     * @param description the product description
     * @return products  the product list
     */
    public List<Product> findProductsByDescription(String description) {
        List<Product> products;

        // Do we have a valid description?
        if (Objects.isNull(description) || description.equals("")) {
            throw new WebApplicationException(
                "Please provide a valid description", Status.EXPECTATION_FAILED);
        }

        try {
            products = productStore().findProductsByDescription(description);
        } catch (Exception e) {
            log.error("Product search by description failed" + e.getMessage());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.EXPECTATION_FAILED);
        }
        return products;
    }

    /**
     * Insert the product
     * @param product the product
     * @return product  the product
     */
    public Product insert(Product product){

        // Do we have a valid product?
        if (Objects.isNull(product)) {
            log.error("The product insert failed for " + product.getId());
            throw new WebApplicationException("Please provide a valid product.", Status.NOT_MODIFIED);
        }

        // Insert it
        try {
            productStore().insert(product);
        } catch (Exception e) {
            log.error("The product insert failed for " + product.getId());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.NOT_MODIFIED);
        }
        return product;
    }

    /**
     * Update the product
     * @param product the product
     * @return product  the product
     */
    public Product update(Product product) {
        // Do we have a valid product?
        if (Objects.isNull(product)) {
            log.error("The product update failed for " + product.getId());
            throw new WebApplicationException("Please provide a valid product.", Status.NOT_MODIFIED);
        }

        // Update it
        try {
            productStore().update(product);
        } catch (Exception e) {
            log.error("The product update failed for " + product.getId());
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.NOT_MODIFIED);
        }
        return product;
    }

    /**
     * Delete the product by id
     * @param id the product id
     * @return deleted  true if the product was deleted
     */
    public Boolean delete(Integer id) {
        Boolean deleted = false;
        try {
            productStore().delete(id);
            deleted = true;
        } catch (Exception e) {
            log.error("The product delete failed for " + id);
            e.printStackTrace();
            throw new WebApplicationException(e.getMessage(), Status.NOT_MODIFIED);
        }
        return deleted;
    }

    // TODO: Add health check
}
