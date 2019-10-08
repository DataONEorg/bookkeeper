--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Create the subscriptions table
CREATE SEQUENCE subscriptions_id_seq;

CREATE TABLE IF NOT EXISTS subscriptions (
    id integer DEFAULT nextval('subscriptions_id_seq') PRIMARY KEY,
    object text NOT NULL,
    --- billingCycleAnchor timestamp with time zone,
    canceledAt timestamp with time zone,
    collectionMethod text NOT NULL,
    created timestamp with time zone,
    --- currentPeriodEnd timestamp with time zone,
    --- currentPeriodStart timestamp with time zone,
    customerId integer NOT NULL,
    --- daysUntilDue integer,
    discount json,
    --- endedAt timestamp with time zone,
    --- latestInvoice json,
    metadata json,
    productId integer NOT NULL,
    quantity integer,
    startDate timestamp with time zone,
    status text NOT NULL,
    trialEnd timestamp with time zone,
    trialStart timestamp with time zone
);
ALTER SEQUENCE subscriptions_id_seq OWNED BY subscriptions.id;
CREATE INDEX subscriptions_customer_id_idx ON subscriptions USING btree(customerId);
CREATE INDEX subscriptions_status_idx ON subscriptions USING btree(status);

COMMENT ON TABLE  subscriptions IS 'Subscriptions of products and their quotas by customers.';
COMMENT ON COLUMN subscriptions.id IS 'The unique subscription identifier.';
COMMENT ON COLUMN subscriptions.object IS 'The serialized object type, set to "subscription".';
--- COMMENT ON COLUMN subscriptions.billingCycleAnchor IS 'The timestamp used to calculate subscription billing cycles.';
COMMENT ON COLUMN subscriptions.canceledAt IS 'The timestamp when the subscription was canceled.';
COMMENT ON COLUMN subscriptions.collectionMethod IS 'The payment collection (charge_automatically, or send_invoice).';
COMMENT ON COLUMN subscriptions.created IS 'The timestamp when the subscription was created.';
--- COMMENT ON COLUMN subscriptions.currentPeriodEnd IS 'The timestamp when the current subscription period ends.';
--- COMMENT ON COLUMN subscriptions.currentPeriodStart IS 'The timestamp when the current subscription period ends.';
COMMENT ON COLUMN subscriptions.customerId IS 'The customer identifier.';
--- COMMENT ON COLUMN subscriptions.daysUntilDue IS 'The number of days until the subscription payment is due.';
--- COMMENT ON COLUMN subscriptions.discount IS 'The discount JSON object applied to the subscription.';
--- COMMENT ON COLUMN subscriptions.endedAt IS 'The timestamp when the subscription ended.';
--- COMMENT ON COLUMN subscriptions.latestInvoice IS 'The latest invoice JSON object for the subscription.';
COMMENT ON COLUMN subscriptions.metadata IS 'The subscription metadata, stored as a JSON object (undefined as of yet)';
COMMENT ON COLUMN subscriptions.productId IS 'The subscribed product identifier.';
COMMENT ON COLUMN subscriptions.quantity IS 'The quantity of the subscribed product.';
COMMENT ON COLUMN subscriptions.startDate IS 'The timestamp when the subscription starts.';
COMMENT ON COLUMN subscriptions.status  IS 'The subscription status';
COMMENT ON COLUMN subscriptions.trialEnd  IS 'The timestamp when the subscription trial starts.';
COMMENT ON COLUMN subscriptions.trialStart  IS 'The timestamp when the subscription trial ends.';