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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jackson.Jackson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataone.bookkeeper.api.Customer;
import org.dataone.bookkeeper.api.Feature;
import org.dataone.bookkeeper.api.Order;
import org.dataone.bookkeeper.api.OrderItem;
import org.dataone.bookkeeper.api.OrderList;
import org.dataone.bookkeeper.api.Product;
import org.dataone.bookkeeper.api.Quota;
import org.dataone.bookkeeper.api.Subscription;
import org.dataone.bookkeeper.jdbi.CustomerStore;
import org.dataone.bookkeeper.jdbi.OrderStore;
import org.dataone.bookkeeper.jdbi.ProductStore;
import org.dataone.bookkeeper.jdbi.SubscriptionStore;
import org.dataone.bookkeeper.security.DataONEAuthHelper;
import org.jdbi.v3.core.Jdbi;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The entry point to the orders collection
 */
@Timed
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrdersResource extends BaseResource {

    /* The logging facility for this class */
    private Log log = LogFactory.getLog(OrdersResource.class);

    /* The order store for database calls */
    private final OrderStore orderStore;

    /* The product store for database calls */
    private final ProductStore productStore;

    /* The subscription store for database calls */
    private final SubscriptionStore subscriptionStore;

    /* The customer store for database calls */
    private final CustomerStore customerStore;

    /* A Jackson mapper for marshaling types */
    private final ObjectMapper mapper = Jackson.newObjectMapper();

    /* An instance of the DataONE authn and authz delegate */
    private final DataONEAuthHelper dataONEAuthHelper;

    /**
     * Construct an order collection
     * @param database  the jdbi database access reference
     */
    public OrdersResource(Jdbi database, DataONEAuthHelper dataONEAuthHelper) {
        this.orderStore = database.onDemand(OrderStore.class);
        this.productStore = database.onDemand(ProductStore.class);
        this.subscriptionStore = database.onDemand(SubscriptionStore.class);
        this.customerStore = database.onDemand(CustomerStore.class);
        this.dataONEAuthHelper = dataONEAuthHelper;
    }

    /**
     * List orders, optionally by subscriptionId or subject.
     * Use start and count to get paginated results
     * @param start  the paging start index
     * @param count  the paging size count
     * @param customerId  the subscriptionId
     * @param subject  the order subject
     * @return orders  the order list
     */
    @Timed
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public OrderList listOrders(
        @Context SecurityContext context,
        @QueryParam("start") @DefaultValue("0") Integer start,
        @QueryParam("count") @DefaultValue("1000") Integer count,
        @QueryParam("subject") String subject,
        @QueryParam("customerId") Integer customerId)
        throws WebApplicationException {

        Customer caller = (Customer) context.getUserPrincipal();
        boolean isAdmin = this.dataONEAuthHelper.isAdmin(caller.getSubject());

        List<Order> orders = new ArrayList<Order>();
        Customer existing;
        try {
            if (customerId != null) {
                if ( ! isAdmin ) {
                    // Customer subjects must match
                    existing = this.customerStore.getCustomer(customerId);
                    if ( ! caller.getSubject().equals(existing.getSubject()) ) {
                        throw new Exception("Customer doesn't have access to this record.");
                    }
                }
                orders = orderStore.findOrdersByCustomerId(customerId);
            } else if ( ! subject.isEmpty() ) {
                if ( ! isAdmin ) {
                    // Customer subjects must match
                    existing = this.customerStore.getCustomer(customerId);
                    if ( ! caller.getSubject().equals(existing.getSubject()) ) {
                        throw new Exception("Customer doesn't have access to this record.");
                    }
                }
                orders = orderStore.findOrdersBySubject(subject);

            } else if ( isAdmin ) {
                // Allow admins to list all orders
                orders = orderStore.listOrders();
            }
        } catch (Exception e) {
            String message = "Couldn't list orders: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.EXPECTATION_FAILED);
        }

        // TODO: Incorporate paging params - new OrderList(start, count, total, orders)
        return new OrderList(orders);
    }

    /**
     * Create the given order
     * @param order  the order to create
     * @return order  the created order
     */
    @Timed
    @POST
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    public Order create(@Context SecurityContext context,
        @NotNull @Valid Order order) throws WebApplicationException {

        Customer caller = (Customer) context.getUserPrincipal();
        boolean isAdmin = this.dataONEAuthHelper.isAdmin(caller.getSubject());

        // Insert the order after it is validated
        try {
            order.setStatus("created");
            order.setCreated(new Integer((int) Instant.now().getEpochSecond()));

            // Update order item details from the listed product
            Product product = null;
            for (OrderItem item : order.getItems() ) {
                product = productStore.getProduct(item.getParent());
                if ( product == null ) {
                    String message = "Couldn't find parent product for order item.";
                    throw new WebApplicationException(message, Response.Status.NOT_FOUND);
                }
                if ( item.getType().equals("sku") ) {
                    item.setAmount(product.getAmount());
                } // TODO: reset tax, shipping, or discount types here?
                item.setCurrency("USD");
                item.setDescription(product.getStatementDescriptor());
            }

            // Reset the total based on product amounts that were set
            order.setAmount(order.getTotalAmount());

            if ( ! isAdmin ) {
                // Ensure the correct customer id for non-admins
                order.setCustomer(caller.getId());
            }
            Integer id = orderStore.insert(order);
            order = orderStore.getOrder(id);
        } catch (Exception e) {
            String message = "Couldn't insert the order: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.EXPECTATION_FAILED);
        }
        return order;
    }

    /**
     * Get the order given an id
     * @param orderId  the order id
     * @return  the order for the id
     */
    @Timed
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{orderId}")
    public Order retrieve(@Context SecurityContext context,
        @PathParam("orderId") @NotNull Integer orderId)
        throws WebApplicationException {

        Customer caller = (Customer) context.getUserPrincipal();
        boolean isAdmin = this.dataONEAuthHelper.isAdmin(caller.getSubject());

        Order order = null;
        // Get the order from the store
        try {
            order = orderStore.getOrder(orderId);
            if ( ! isAdmin ) {
                // Allow customers to get their own order
                if ( ! order.getCustomer().equals(caller.getId()) ) {
                    throw new Exception("Customer doesn't have access to this order.");
                }
            }
        } catch (Exception e) {
            String message = "Couldn't get the order: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.NOT_FOUND);
        }
        return order;
    }

    /**
     * Update the order
     * @param order  the order to update
     * @return  the updated order
     * @throws WebApplicationException  a web app exception
     */
    @Timed
    @PUT
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{orderId}")
    public Order update(@Context SecurityContext context,
        @NotNull @Valid Order order) throws WebApplicationException {
        // Update the order after validation
        Customer caller = (Customer) context.getUserPrincipal();
        boolean isAdmin = this.dataONEAuthHelper.isAdmin(caller.getSubject());

        Order existing = orderStore.getOrder(order.getId());

        // Does the order exist?
        if ( existing == null ) {
            String message = "Couldn't find the order with id " + order.getId();
            throw new WebApplicationException(message, Response.Status.NOT_FOUND);
        }

        // Ensure we have the right customer
        if ( ! isAdmin ) {
            if ( ! existing.getCustomer().equals(caller.getId()) ) {
                throw new WebApplicationException(
                    "Customer doesn't have access to this order.", Response.Status.EXPECTATION_FAILED
                );
            }
        }
        // Update the order
        try {
            order.setCreated(existing.getCreated());
            order.setCurrency(existing.getCurrency());
            if (existing.getEmail() != null) {
                order.setEmail(existing.getEmail());
            }
            order.setCustomer(existing.getCustomer());
            if ( existing.getStatusTransitions() != null ) {
                order.setStatusTransitions(existing.getStatusTransitions());
            }
            order.setStatus("created");
            order.setUpdated(new Integer((int) Instant.now().getEpochSecond()));

            // Update order item details from the listed product
            Product product = null;
            for (OrderItem item : order.getItems() ) {
                product = productStore.getProduct(item.getParent());
                if ( product == null ) {
                    String message = "Couldn't find parent product for order item.";
                    throw new WebApplicationException(message, Response.Status.NOT_FOUND);
                }
                if ( item.getType().equals("sku") ) {
                    item.setAmount(product.getAmount());
                } // TODO: reset tax, shipping, or discount types here?
                item.setCurrency("USD");
                item.setDescription(product.getStatementDescriptor());
            }

            // Reset the total based on product amounts that were set
            order.setAmount(order.getTotalAmount());
            orderStore.update(order);
        } catch (Exception e) {
            String message = "Couldn't update the order: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.EXPECTATION_FAILED);
        }
        return order;
    }

    @Timed
    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{orderId}/pay")
    public Order pay(@Context SecurityContext context,
        @NotNull @PathParam("orderId") Integer orderId) throws WebApplicationException {
        Order order = null;
        Integer secondsSinceEpoch = new Integer((int) Instant.now().getEpochSecond());

        Customer caller = (Customer) context.getUserPrincipal();
        boolean isAdmin = this.dataONEAuthHelper.isAdmin(caller.getSubject());

        long trialDurationDays = this.dataONEAuthHelper.getConfiguration().getTrialDurationDays();
        Integer trialEndSecondsSinceEpoch =
            new Integer((int) (Instant.ofEpochSecond(
                (long) secondsSinceEpoch).plus(
                    trialDurationDays, ChronoUnit.DAYS
                )
            ).getEpochSecond()
            );

        // Confirm a trial without payment
        // TODO: Pay the order through the Aventri callback proxy
        try {
            order = orderStore.getOrder(orderId);
            // Ensure we have the right customer
            if ( ! isAdmin ) {
                if ( ! order.getCustomer().equals(caller.getId()) ) {
                    throw new WebApplicationException(
                        "Customer doesn't have access to this order.", Response.Status.EXPECTATION_FAILED
                    );
                }
            }

            if ( order != null ) {
                List<OrderItem> orderItems = order.getItems();
                Customer customer = customerStore.getCustomer(order.getCustomer());
                Integer productId = null;
                for (OrderItem item : orderItems) {

                    // For SKUs, subscribe customer to the service and set quotas
                    if ( item.getType().equals("sku") ) {
                        productId = item.getParent();
                        Product product = productStore.getProduct(productId);
                        Subscription subscription = new Subscription();
                        subscription.setObject("subscription");
                        subscription.setProduct(product);
                        subscription.setStatus("trialing");
                        subscription.setCanceledAt(null);
                        subscription.setCollectionMethod("send_invoice");
                        subscription.setCreated(secondsSinceEpoch);
                        subscription.setCustomerId(order.getCustomer());
                        subscription.setQuantity(item.getQuantity());
                        subscription.setTrialStart(secondsSinceEpoch);
                        subscription.setTrialEnd(trialEndSecondsSinceEpoch);
                        subscription.setStartDate(trialEndSecondsSinceEpoch);

                        // Translate the product's feature quota into a customer/subject quota
                        Map<String, Quota> quotas = new LinkedHashMap<String, Quota>();
                        Iterator<JsonNode> featuresIterator =
                            (product.getMetadata().get("features")).elements();
                        Quota quota = null;
                        Quota existingQuota = null;
                        Integer newSoftLimit;
                        Integer newHardLimit;
                        while ( featuresIterator.hasNext() ) {
                            // Find quotas in each feature, combining equivalent quotas
                            ObjectNode featureNode = (ObjectNode) featuresIterator.next();
                            Feature feature = mapper.readValue(featureNode.toString(), Feature.class);
                            quota = feature.getQuota();
                            if ( quota != null ) {
                                quota.setUsage(0);
                                quota.setSubject(customer.getSubject());
                                if ( ! quotas.containsKey(quota.getName()) ) {
                                    // Add new quotas
                                    quotas.put(quota.getName(), quota);
                                } else {
                                    // Combine quotas of the same name
                                    existingQuota = quotas.get(quota.getName());
                                    newSoftLimit = existingQuota.getSoftLimit() + quota.getSoftLimit();
                                    quota.setSoftLimit(newSoftLimit);
                                    newHardLimit = existingQuota.getHardLimit() + quota.getHardLimit();
                                    quota.setHardLimit(newHardLimit);
                                    quotas.put(quota.getName(), quota);
                                }
                            }
                        }
                        Integer subscriptionId =
                            subscriptionStore.insertWithQuotas(subscription, quotas.values());
                    }

                }
                order.setUpdated(new Integer((int) Instant.now().getEpochSecond()));
                order.setStatus("paid");
                // TODO: Decide if this call should be a transaction with the subscription/quota
                orderStore.update(order);
            } else {
                String message = "Couldn't find the order for order id " + orderId;
                throw new WebApplicationException(message, Response.Status.NOT_FOUND);
            }

        } catch (Exception e) {
            String message = "Couldn't pay the order: " + e.getMessage();
            throw new WebApplicationException(message, Response.Status.EXPECTATION_FAILED);
        }
        return order;
    }


    /**
     * Delete the order
     * @param orderId  the order id
     * @return  response 200 if deleted
     * @throws WebApplicationException  a web app exception
     */
    @Timed
    @DELETE
    @RolesAllowed("CN=urn:node:CN,DC=dataone,DC=org")
    @Path("{orderId}")
    public Response delete(@PathParam("orderId") @Valid Integer orderId) throws WebApplicationException {
        String message = "The orderId cannot be null.";
        if (orderId == null) {
            throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
        }
        try {
            orderStore.delete(orderId);
        } catch (Exception e) {
            message = "Deleting the order with id " + orderId + " failed: " + e.getMessage();
            log.error(message);
            e.printStackTrace();
            throw e;
        }
        return Response.ok().build();
    }
}
