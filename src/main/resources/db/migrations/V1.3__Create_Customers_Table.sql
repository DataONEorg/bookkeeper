--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Create the customers table
CREATE SEQUENCE customers_id_seq;

CREATE TABLE IF NOT EXISTS customers (
    id integer DEFAULT nextval('customers_id_seq') PRIMARY KEY,
    object text NOT NULL,
    orcid text NOT NULL,
    balance integer,
    address json,
    created timestamp without time zone,
    currency text,
    delinquent boolean,
    description text,
    discount json,
    email text,
    invoicePrefix text,
    invoiceSettings json,
    metadata json,
    givenName text,
    surName text,
    phone text
);
ALTER SEQUENCE customers_id_seq OWNED BY customers.id;
CREATE INDEX customers_orcid_idx ON customers USING btree(orcid);
CREATE INDEX customers_email_idx ON customers USING btree(email);

COMMENT ON TABLE customers IS 'Customers that have ordered products and have quotas.';
COMMENT ON COLUMN customers.id IS 'The unique customer identifier.';
COMMENT ON COLUMN customers.object IS 'The serialized object type, set to "customer".';
COMMENT ON COLUMN customers.orcid IS 'The unique customer ORCID identifier.';
COMMENT ON COLUMN customers.balance IS 'The customer balance, in pence of the currency (i.e. USD cents).';
COMMENT ON COLUMN customers.address IS 'The customer address, stored as a JSON address object.';
COMMENT ON COLUMN customers.created IS 'The customer creation date.';
COMMENT ON COLUMN customers.currency IS 'The customer currency, defaults to "USD".';
COMMENT ON COLUMN customers.delinquent IS 'The customer delinquency status, true or false';
COMMENT ON COLUMN customers.description IS 'The customer description';
COMMENT ON COLUMN customers.discount IS 'The customer discount, stored as a JSON Discount object.';
COMMENT ON COLUMN customers.email IS 'The customer email address.';
COMMENT ON COLUMN customers.invoicePrefix IS 'The customer invoice prefix';
COMMENT ON COLUMN customers.invoiceSettings IS 'The customer settings, stored as a JSON InvoiceSettings object';
COMMENT ON COLUMN customers.metadata IS 'The customer metadata, stored as a JSON object (undefined as of yet)';
COMMENT ON COLUMN customers.givenName IS 'The customer given name';
COMMENT ON COLUMN customers.surName IS 'The customer sur name';
COMMENT ON COLUMN customers.phone IS 'The customer phone';