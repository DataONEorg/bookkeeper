--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Add a unique constraint to the usage table for the 'quotaid' + 'instanceid' columns
CREATE UNIQUE INDEX usages_quotaid_instanceid_idx
    ON usages USING btree(quotaid,instanceid);
