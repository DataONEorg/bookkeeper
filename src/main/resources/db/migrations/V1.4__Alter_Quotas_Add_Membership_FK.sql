--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Add the membership id foreign key constraint
ALTER TABLE quotas
    ADD CONSTRAINT quotas_membership_id_fk
    FOREIGN KEY (membershipId) REFERENCES memberships (id) ON DELETE CASCADE;
