--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Create the usages table
CREATE SEQUENCE IF NOT EXISTS usages_id_seq;

CREATE TABLE IF NOT EXISTS usages (
    id integer DEFAULT nextval('usages_id_seq') PRIMARY KEY,
    object text NOT NULL,
    quotaId integer NOT NULL,
    instanceId text NOT NULL,
    quantity double precision NOT NULL,
    status text,
    nodeId text NOT NULL
);
ALTER SEQUENCE usages_id_seq OWNED BY usages.id;

CREATE INDEX usages_quotaId_idx ON usages USING btree(quotaId);
CREATE INDEX usages_instanceId_idx ON usages USING btree(instanceId);

ALTER TABLE usages
    ADD CONSTRAINT usages_quotas_id_fk
    FOREIGN KEY (quotaId) REFERENCES quotas (id) ON DELETE CASCADE;

COMMENT ON TABLE  usages IS 'Usages records each instance a portion of a quota is used.';
COMMENT ON COLUMN usages.id IS 'The unique usage identifier.';
COMMENT ON COLUMN usages.object IS 'The serialized object type, set to "usage".';
COMMENT ON COLUMN usages.quotaId IS 'The quota identifier.';
COMMENT ON COLUMN usages.instanceId IS 'The instance identifier using a portion of the quota.';
COMMENT ON COLUMN usages.quantity IS 'The quantity used by the instance, in the quota units.';
COMMENT ON COLUMN usages.status IS 'The status of the usage, active or inactive.';
COMMENT ON COLUMN usages.nodeId IS 'The quota node identifier".';
