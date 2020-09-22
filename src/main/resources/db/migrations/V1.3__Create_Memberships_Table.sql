--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Create the memberships table
CREATE SEQUENCE memberships_id_seq;

CREATE TABLE IF NOT EXISTS memberships (
    id integer DEFAULT nextval('memberships_id_seq') PRIMARY KEY,
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
ALTER SEQUENCE memberships_id_seq OWNED BY memberships.id;
CREATE INDEX memberships_customer_id_idx ON memberships USING btree(customerId);
CREATE INDEX memberships_status_idx ON memberships USING btree(status);

COMMENT ON TABLE  memberships IS 'Memberships with products and their quotas by customers.';
COMMENT ON COLUMN memberships.id IS 'The unique membership identifier.';
COMMENT ON COLUMN memberships.object IS 'The serialized object type, set to "membership".';
--- COMMENT ON COLUMN memberships.billingCycleAnchor IS 'The timestamp used to calculate membership billing cycles.';
COMMENT ON COLUMN memberships.canceledAt IS 'The timestamp when the membership was canceled.';
COMMENT ON COLUMN memberships.collectionMethod IS 'The payment collection (charge_automatically, or send_invoice).';
COMMENT ON COLUMN memberships.created IS 'The timestamp when the membership was created.';
--- COMMENT ON COLUMN memberships.currentPeriodEnd IS 'The timestamp when the current membership period ends.';
--- COMMENT ON COLUMN memberships.currentPeriodStart IS 'The timestamp when the current membership period ends.';
COMMENT ON COLUMN memberships.customerId IS 'The customer identifier.';
--- COMMENT ON COLUMN memberships.daysUntilDue IS 'The number of days until the membership payment is due.';
--- COMMENT ON COLUMN memberships.discount IS 'The discount JSON object applied to the membership.';
--- COMMENT ON COLUMN memberships.endedAt IS 'The timestamp when the membership ended.';
--- COMMENT ON COLUMN memberships.latestInvoice IS 'The latest invoice JSON object for the membership.';
COMMENT ON COLUMN memberships.metadata IS 'The membership metadata, stored as a JSON object (undefined as of yet)';
COMMENT ON COLUMN memberships.productId IS 'The purchased product identifier.';
COMMENT ON COLUMN memberships.quantity IS 'The quantity of the purchased product.';
COMMENT ON COLUMN memberships.startDate IS 'The timestamp when the membership starts.';
COMMENT ON COLUMN memberships.status  IS 'The membership status';
COMMENT ON COLUMN memberships.trialEnd  IS 'The timestamp when the membership trial starts.';
COMMENT ON COLUMN memberships.trialStart  IS 'The timestamp when the membership trial ends.';