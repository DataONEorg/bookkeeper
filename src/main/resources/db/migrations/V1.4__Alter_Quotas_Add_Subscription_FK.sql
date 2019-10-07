--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Add the subscription id foreign key constraint
ALTER TABLE quotas
    ADD CONSTRAINT quotas_subscription_id_fk
    FOREIGN KEY (subscriptionId) REFERENCES subscriptions (id) ON DELETE CASCADE;
