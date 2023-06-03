/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2023. All rights reserved.
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

import java.util.List;
import org.dataone.bookkeeper.api.Payment;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterBeanMapper(Payment.class)
public interface PaymentStore {

    /** The query used to find all usages */
    String SELECT_CLAUSE =
                "SELECT " +
                        "transactionId, " +
                        "orderId, " +
                        "accountId, " +
                        "timestamp, " +
                        "count, " +
                        "hash, " +
                        "authorizationCode, " +
                        "authorizationMessage, " +
                        "requestAmount, " +
                        "transactionAmount, " +
                        "products, " +
                        "transactionApproved, " +
                        "transactionTimestamp, " +
                        "updated " +
                "FROM payments ";

    /** The full query */
    String SELECT_ALL = SELECT_CLAUSE;

    /** Select by transaction identifier */
    String SELECT_BY_TX_ID = SELECT_CLAUSE + "WHERE transactionId = :transactionId";

    /**
     * List all payments
     * @return payments the list of payments
     */
    @SqlQuery(SELECT_ALL)
    List<Payment> listPayments();

    /**
     * Find payments by transaction identifier
     * @param transactionId the transaction id
     * @return payments the payments for the transactionId
     */
    @SqlQuery(SELECT_BY_TX_ID)
    List <Payment> findUsagesByTransactionId(@Bind("transactionId") String transactionId);

    /**
     * Insert a payment with a given Payment instance
     * @param payment the payment to insert
     */
    @SqlUpdate("INSERT INTO payments " +
            "(transactionId, " +
            "orderId, " +
            "accountId, " +
            "timestamp, " +
            "count, " +
            "hash, " +
            "authorizationCode, " +
            "authorizationMessage, " +
            "requestAmount, " +
            "transactionAmount, " +
            "products, " +
            "transactionApproved, " +
            "transactionTimestamp) " +
            "VALUES " +
            "(:transactionId, " +
            ":orderId, " +
            ":accountId, " +
            ":timestamp, " +
            ":count, " +
            ":hash, " +
            ":authorizationCode, " +
            ":authorizationMessage, " +
            ":requestAmount, " +
            ":transactionAmount, " +
            ":products, " +
            ":transactionApproved, " +
            ":transactionTimestamp) " +
            "RETURNING transactionId")
    @GetGeneratedKeys
    Integer insert(@BindBean Payment payment);

    /**
     * Update a payment for a given id
     * @param payment the payment to update
     */
    @SqlUpdate("UPDATE payments SET " +
            "orderId = :orderId,  " +
            "accountId = :accountId, " +
            "timestamp = :timestamp, " +
            "count = :count, " +
            "hash = :hash, " +
            "authorizationCode = :authorizationCode, " +
            "authorizationMessage = :authorizationMessage, " +
            "requestAmount = :requestAmount, " +
            "transactionAmount = :transactionAmount, " +
            "products = :products, " +
            "transactionApproved = :transactionApproved, " +
            "transactionTimestamp = :transactionTimestamp) " +
            "WHERE transactionId = :transactionId")
    @GetGeneratedKeys
    Payment update(@BindBean Payment payment);

    /**
     * Delete a payment
     * @param transactionId the payment id to delete
     */
    @SqlUpdate("DELETE FROM payments WHERE transactionId = :transactionId")
    void delete(@Bind("transactionId") Integer transactionId);

}
