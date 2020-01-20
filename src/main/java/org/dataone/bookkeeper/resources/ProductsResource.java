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

package org.dataone.bookkeeper.resources;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataone.bookkeeper.api.Product;
import org.dataone.bookkeeper.api.ProductList;
import org.dataone.bookkeeper.jdbi.ProductStore;
import org.dataone.bookkeeper.security.DataONEAuthHelper;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;

/**
 * The entry point to the products collection
 */
@Timed
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductsResource extends BaseResource {

    /* The logging facility for this class */
    private Log log = LogFactory.getLog(ProductsResource.class);

    /* The product store for database calls */
    private final ProductStore productStore;

    /* An instance of the DataONE authn and authz delegate */
    private final DataONEAuthHelper dataONEAuthHelper;

    /**
     * Construct a product collection
     * @param database  the jdbi database access reference
     */
    public ProductsResource(Jdbi database, DataONEAuthHelper dataONEAuthHelper) {
        this.productStore = database.onDemand(ProductStore.class);
        this.dataONEAuthHelper = dataONEAuthHelper;
    }

    /**
     * List products, optionally by name, description, or active status.
     * Use start and count to get paginated results
     * @param start  the paging start index
     * @param count  the paging size count
     * @param name  the product name
     * @param description  the product description
     * @param status  the product active status
     * @return products  the product list
     */
    @Timed
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ProductList listProducts(
        @QueryParam("start") @DefaultValue("0") Integer start,
        @QueryParam("count") @DefaultValue("1000") Integer count,
        @QueryParam("name") String name,
        @QueryParam("description") String description,
        @QueryParam("status") Boolean status) throws WebApplicationException {

        List<Product> products;
        if (name != null) {
            products = productStore.findProductsByName(name);
        } else if (description != null) {
            products = productStore.findProductsByDescription(description);
        } else if (status != null) {
            products = productStore.findProductsByActiveStatus(status);
        } else {
            products = productStore.listProducts();
        }

        // TODO: Incorporate paging params - new ProductList(start, count, total, products)
        return new ProductList(products);
    }

    /**
     * Create the given product
     * @param product  the product to create
     * @return product  the created product
     */
    @Timed
    @POST
    @RolesAllowed("CN=urn:node:CN,DC=dataone,DC=org")
    @Consumes(MediaType.APPLICATION_JSON)
    public Product create(@NotNull @Valid Product product) throws WebApplicationException {
        // Insert the product after it is validated
        try {
            // Set the created timestamp
            product.setCreated(new Integer((int) Instant.now().getEpochSecond()));
            Integer id = productStore.insert(product);
            product = productStore.getProduct(id);
        } catch (Exception e) {
            String message = "Couldn't insert the product: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.EXPECTATION_FAILED);
        }
        return product;
    }

    /**
     * Get the product given an id
     * @param productId  the product id
     * @return  the product for the id
     */
    @Timed
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{productId}")
    public Product retrieve(@PathParam("productId") @NotNull Integer productId)
        throws WebApplicationException {

        Product product = null;
        // Get the product from the store
        try {
            product = productStore.getProduct(productId);
        } catch (Exception e) {
            String message = "Couldn't get the product: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.NOT_FOUND);
        }
        return product;
    }

    /**
     * Update the product
     * @param product  the product to update
     * @return  the updated product
     * @throws WebApplicationException  a web app exception
     */
    @Timed
    @PUT
    @RolesAllowed("CN=urn:node:CN,DC=dataone,DC=org")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{productId}")
    public Product update(@NotNull @Valid Product product) throws WebApplicationException {
        // Update the product after validation
        try {
            // Reset the created field to keep it managed server-side
            Product existing = productStore.getProduct(product.getId());

            product.setCreated(existing.getCreated());
            productStore.update(product);
        } catch (Exception e) {
            String message = "Couldn't update the product: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.EXPECTATION_FAILED);
        }
        return product;
    }

    /**
     * Delete the product
     * @param productId  the product id
     * @return  response 200 if deleted
     * @throws WebApplicationException  a web app exception
     */
    @Timed
    @DELETE
    @RolesAllowed("CN=urn:node:CN,DC=dataone,DC=org")
    @Path("{productId}")
    public Response delete(@PathParam("productId") @Valid Integer productId) throws WebApplicationException {
        String message = "The productId cannot be null.";
        if (productId == null) {
            throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
        }
        try {
            productStore.delete(productId);
        } catch (Exception e) {
            message = "Deleting the product with id " + productId + " failed: " + e.getMessage();
            log.error(message);
            e.printStackTrace();
            throw e;
        }
        return Response.ok().build();
    }
}
