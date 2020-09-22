--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Create the quotas table
CREATE SEQUENCE IF NOT EXISTS quotas_id_seq;

CREATE TABLE IF NOT EXISTS quotas (
    id integer DEFAULT nextval('quotas_id_seq') PRIMARY KEY,
    object text NOT NULL,
    quotaType text NOT NULL,
    softLimit double precision NOT NULL,
    hardLimit double precision NOT NULL,
    totalUsage double precision,
    unit text NOT NULL,
    membershipId integer,
    owner text
);
ALTER SEQUENCE quotas_id_seq OWNED BY quotas.id;

CREATE INDEX quotas_quotaType_idx ON quotas USING btree(quotaType);
CREATE INDEX quotas_membershipId_idx ON quotas USING btree(membershipId);
CREATE INDEX quotas_owner_idx ON quotas USING btree(owner);

COMMENT ON TABLE quotas IS 'Quotas limiting resources for products per customer. Quotas may be associated with products or customers by their ids.';
COMMENT ON COLUMN quotas.id IS 'The unique quota identifier';
COMMENT ON COLUMN quotas.object IS 'The serialized object type, set to "quota"';
COMMENT ON COLUMN quotas.quotaType IS 'The quota type';
COMMENT ON COLUMN quotas.softLimit IS 'The soft limit of the resource.';
COMMENT ON COLUMN quotas.hardLimit IS 'The hard limit of the resource.';
COMMENT ON COLUMN quotas.totalUsage IS 'The current observed total usage of the quota.';
COMMENT ON COLUMN quotas.unit IS 'The named unit of the quota.';
COMMENT ON COLUMN quotas.membershipId IS 'The membership id to which the quota is applied.';
COMMENT ON COLUMN quotas.owner IS 'The owner identifier to which the quota is applied.';