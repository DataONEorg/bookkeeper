package org.dataone.bookkeeper.jdbi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.dataone.bookkeeper.BaseTestCase;
import org.dataone.bookkeeper.api.Product;
import org.dataone.bookkeeper.helpers.DAOHelper;
import org.dataone.bookkeeper.helpers.ProductHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.dataone.bookkeeper.helpers.DAOHelper.getRandomId;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test the Product data access object
 */
public class ProductDAOTest extends BaseTestCase {

    // The ProductDAO to test
    private ProductDAO productDAO;

    // A list of product ids used in testing
    private List<Integer> productIds = new ArrayList<Integer>();

    /**
     * Set up the DAO for testing
     */
    @BeforeEach
    public void init() {
        productDAO = dbi.onDemand(ProductDAO.class);
    }

    /**
     * Tear down resources
     */
    @AfterEach
    public void tearDown() {
        // Remove test product entries
        for (Integer productId : this.productIds) {
            try {
                ProductHelper.removeTestProduct(productId);
            } catch (SQLException e) {
                fail(e);
            }
        }
    }

    /**
     * Test getting the full Product list
     */
    @Test
    @DisplayName("Test listing the products")
    public void testListProducts() {
        // Insert a new product
        Integer product1Id = ProductHelper.insertTestProduct(DAOHelper.getRandomId());
        this.productIds.add(product1Id);
        Integer product2Id = ProductHelper.insertTestProduct(DAOHelper.getRandomId());
        this.productIds.add(product2Id);
        assertTrue(productDAO.listProducts().size() >= 2);
    }

    /**
     * Test getting a single product by ID
     */
    @Test
    @DisplayName("Test getting a product")
    public void testGetProduct() {
        // Insert a new product
        Integer productId = ProductHelper.insertTestProduct(DAOHelper.getRandomId());
        this.productIds.add(productId);

        Product returnedProduct = productDAO.getProduct(productId);
        assertTrue(returnedProduct.getId().equals(productId));
    }

    /**
     * Test getting a single product by name
     */
    @Test
    @DisplayName("Test gettting a product by name")
    public void testFindProductsByName() {
        // Insert a new product
        Integer productId = DAOHelper.getRandomId();
        Product product = ProductHelper.createTestProduct(productId);
        this.productIds.add(productId);
        productDAO.insert(product);

        assertTrue(productDAO.findProductsByName(
            product.getName()).get(0).getName().equals(product.getName()));
    }

    /**
     * Test getting a single product by active status
     */
    @Test
    @DisplayName("Test gettting a product by active status")
    public void testFindProductsByActiveStatus() {
        // Insert a new product
        Integer productId = DAOHelper.getRandomId();
        Product product = ProductHelper.createTestProduct(productId);
        this.productIds.add(productId);
        productDAO.insert(product);

        assertTrue(productDAO.findProductsByActiveStatus(
            product.isActive()).get(0).isActive() == product.isActive());
    }

    /**
     * Test getting a single product by description
     */
    @Test
    @DisplayName("Test getting a product by description")
    public void testFindProductsByDescription() {
        // Insert a new product
        Integer productId = DAOHelper.getRandomId();
        Product product = ProductHelper.createTestProduct(productId);
        this.productIds.add(productId);
        productDAO.insert(product);

        assertTrue(productDAO.findProductsByDescription(
            product.getDescription()).get(0).getDescription().equals(product.getDescription()));
    }

    /**
     * Test inserting a Product instance
     */
    @Test
    @DisplayName("Test inserting a Product instance")
    public void testInsert() {
        try {
            Integer productId = DAOHelper.getRandomId();
            Product product = ProductHelper.createTestProduct(productId);
            this.productIds.add(productId);
            productDAO.insert(product);
            assertThat( ProductHelper.getProductCountById(productId) == 1);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test updating a product
     */
    @Test
    @DisplayName("Test updating a product")
    public void testUpdate() {
        try {
            // Insert a new product
            Integer productId = ProductHelper.insertTestProduct(DAOHelper.getRandomId());

            // Set some product fields to be changed
            this.productIds.add(productId); // Clean up
            String objectString = "product";
            String productName = "test_product_name_" + DAOHelper.getRandomId();
            String productCaption = "My updated product caption";
            String productDescription = "My updated product description";
            String productStatementDescriptor = "My updated statement descriptor";
            String productType = "service";
            String productUnitLabel = "membership";
            String productURL = "https://dataone.org/products/membership/organization";

            // Build the new product
            Product expectedProduct = new Product();
            expectedProduct.setId(productId);
            expectedProduct.setObject(objectString);
            expectedProduct.setActive(true);
            expectedProduct.setName(productName);
            expectedProduct.setCaption(productCaption);
            expectedProduct.setDescription(productDescription);
            expectedProduct.setCreated(new Integer((int) Instant.now().getEpochSecond()));
            expectedProduct.setStatementDescriptor(productStatementDescriptor);
            expectedProduct.setType(productType);
            expectedProduct.setUnitLabel(productUnitLabel);
            expectedProduct.setUrl(productURL);
            ObjectNode metadata = ProductHelper.createTestMetadata();
            expectedProduct.setMetadata(metadata);

            // Update the existing product in the database
            productDAO.update(expectedProduct);

            // Get the updated product
            Product updatedProduct = ProductHelper.getProductById(productId);

            // Compare fields
            assertThat(updatedProduct.isActive() == true);
            assertThat(updatedProduct.getObject() == objectString);
            assertThat(updatedProduct.getName() == productName);
            assertThat(updatedProduct.getCaption() == productCaption);
            assertThat(updatedProduct.getDescription() == productDescription);
            assertThat(updatedProduct.getStatementDescriptor() == productStatementDescriptor);
            assertThat(updatedProduct.getType() == productType);
            assertThat(updatedProduct.getUnitLabel() == productUnitLabel);
            assertThat(updatedProduct.getUrl() == productURL);
            assertThat(updatedProduct.getMetadata().equals(metadata));
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Test deleting a product
     */
    @Test
    @DisplayName("Test deleting a product")
    public void testDelete() {
        Integer productId = null;
        try {
            productId = ProductHelper.insertTestProduct(DAOHelper.getRandomId());
            productDAO.delete(productId);
            assertThat(ProductHelper.getProductCountById(productId) == 0);
        } catch (Exception e) {
            this.productIds.add(productId); // Clean up on fail
            fail();
        }

    }
}
