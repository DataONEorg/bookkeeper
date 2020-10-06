--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Create the orders table
CREATE SEQUENCE orders_id_seq;

CREATE TABLE IF NOT EXISTS orders (
    id integer DEFAULT nextval('orders_id_seq') PRIMARY KEY,
    object text NOT NULL,
    amount integer,
    amountReturned integer,
    charge json,
    created timestamp with time zone,
    currency text,
    customer integer,
    subject text,
    email text,
    items json,
    metadata json,
    name text,
    status text,
    statusTransitions json,
    updated timestamp with time zone,
    seriesId text,
    startDate timestamp with time zone,
    endDate timestamp with time zone
);

CREATE INDEX orders_customer_idx ON orders USING btree(customer);
CREATE INDEX orders_subject_idx ON orders USING btree(subject);
CREATE INDEX orders_email_idx ON orders USING btree(email);
CREATE INDEX orders_status_idx ON orders USING btree(status);

COMMENT ON TABLE  orders IS 'Orders of products by customers.';
COMMENT ON COLUMN orders.id IS 'The unique order identifier.';
COMMENT ON COLUMN orders.object IS 'The serialized object type, set to "order".';
COMMENT ON COLUMN orders.amount IS 'The order amount in the smallest unit of the currency.';
COMMENT ON COLUMN orders.amountReturned IS 'The order amount returned in the smallest unit of the currency.';
COMMENT ON COLUMN orders.charge IS 'The order transaction charge JSON object.';
COMMENT ON COLUMN orders.created IS 'The order creation date.';
COMMENT ON COLUMN orders.currency IS 'The order currency code.';
COMMENT ON COLUMN orders.customer IS 'The order customer identifier.';
COMMENT ON COLUMN orders.subject IS 'The order user or group subject identifier.';
COMMENT ON COLUMN orders.email IS 'The order customer email.';
COMMENT ON COLUMN orders.items IS 'The order items list JSON object.';
COMMENT ON COLUMN orders.metadata IS 'The order metadata JSON object.';
COMMENT ON COLUMN orders.name IS 'The order name set by the customer.';
COMMENT ON COLUMN orders.status IS 'The order status (created, paid, canceled, fulfilled, returned.';
COMMENT ON COLUMN orders.statusTransitions IS 'The order status/date transitions JSON object';
COMMENT ON COLUMN orders.updated IS 'The order update date.';
COMMENT ON COLUMN orders.seriesId IS 'The order series identifier to track renewals.';
COMMENT ON COLUMN orders.startDate IS 'The order services start date.';
COMMENT ON COLUMN orders.endDate IS 'The order services end date.';

