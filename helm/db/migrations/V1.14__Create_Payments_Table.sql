--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Create the sequence and table for storing payments
CREATE SEQUENCE payments_id_seq;

CREATE TABLE IF NOT EXISTS payments (
    id integer DEFAULT nextval('payments_id_seq') PRIMARY KEY,
    transactionId text UNIQUE NOT NULL,
    orderId integer NOT NULL,
    accountId text NOT NULL,
    timestamp text,
    count integer,
    hash text NOT NULL,
    authorizationCode text,
    authorizationMessage text,
    requestAmount float,
    transactionAmount float,
    products text,
    transactionApproved integer,
    transactionTimestamp text,
    updated timestamp with time zone,
    FOREIGN KEY (orderId) REFERENCES orders (id) ON DELETE CASCADE
);

CREATE INDEX payments_order_idx ON payments USING btree(orderId);
CREATE INDEX payments_transaction_idx ON payments USING btree(transactionId);

COMMENT ON TABLE  payments IS 'Payment transactions for Orders.';
COMMENT ON COLUMN payments.id IS 'The unique local payment identifier.';
COMMENT ON COLUMN payments.transactionId IS 'The unique identifier of the transaction from the payment processor.';
COMMENT ON COLUMN payments.orderId IS 'The identifier of the order to which this payment applies.';
COMMENT ON COLUMN payments.accountId IS 'The account identifier for this order.';
COMMENT ON COLUMN payments.timestamp IS 'The string timestamp when the transaction was verified, used in the hash.';
COMMENT ON COLUMN payments.count IS 'The count of transactions in a request, should be 1.';
COMMENT ON COLUMN payments.hash IS 'The verification hash from the payment processor for the transaction message.';
COMMENT ON COLUMN payments.authorizationCode IS 'The authorization code for this payment.';
COMMENT ON COLUMN payments.authorizationMessage IS 'The authorization message corresponding to the auth code.';
COMMENT ON COLUMN payments.requestAmount IS 'The requested payment amount.';
COMMENT ON COLUMN payments.transactionAmount IS 'The actual transaction amount.';
COMMENT ON COLUMN payments.products IS 'The text summary of the products in the order.';
COMMENT ON COLUMN payments.transactionApproved IS 'The flag with value 1 indicating if the transaction was approved.';
COMMENT ON COLUMN payments.transactionTimestamp IS 'The string representation of the time the transaction was requested.';
COMMENT ON COLUMN payments.updated IS 'The datetime on which this record was updated in the payments table.';

