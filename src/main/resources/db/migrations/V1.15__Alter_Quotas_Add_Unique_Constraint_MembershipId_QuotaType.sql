--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Add a unique constraint to the quotas table for the 'membershipId' + 'quotaType' columns
CREATE UNIQUE INDEX quotas_membership_id_quota_type_idx
    ON quotas USING btree(membershipId, quotaType);
