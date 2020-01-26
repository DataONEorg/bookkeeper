--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Create the quotas table
CREATE SEQUENCE IF NOT EXISTS quotas_id_seq;

CREATE TABLE IF NOT EXISTS quotas (
    id integer DEFAULT nextval('quotas_id_seq') PRIMARY KEY,
    object text NOT NULL,
    name text NOT NULL,
    softLimit real NOT NULL,
    hardLimit real NOT NULL,
    usage real,
    unit text NOT NULL,
    subscriptionId integer,
    subject text
);
ALTER SEQUENCE quotas_id_seq OWNED BY quotas.id;

CREATE INDEX quotas_name_idx ON quotas USING btree(name);
CREATE INDEX quotas_subscriptionId_idx ON quotas USING btree(subscriptionId);
CREATE INDEX quotas_subject_idx ON quotas USING btree(subject);

COMMENT ON TABLE quotas IS 'Quotas limiting resources for products per customer. Quotas may be associated with products or customers by their ids.';
COMMENT ON COLUMN quotas.id IS 'The unique quota identifier';
COMMENT ON COLUMN quotas.object IS 'The serialized object type, set to "quota"';
COMMENT ON COLUMN quotas.name IS 'The quota name';
COMMENT ON COLUMN quotas.softLimit IS 'The soft limit of the resource.';
COMMENT ON COLUMN quotas.hardLimit IS 'The hard limit of the resource.';
COMMENT ON COLUMN quotas.usage IS 'The current observed usage of the quota.';
COMMENT ON COLUMN quotas.unit IS 'The named unit of the quota.';
COMMENT ON COLUMN quotas.subscriptionId IS 'The subscription id to which the quota is applied.';
COMMENT ON COLUMN quotas.subject IS 'The subject id to which the quota is applied.';