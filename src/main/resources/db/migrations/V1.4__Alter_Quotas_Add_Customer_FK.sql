--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Add the customer id foreign key constraint
ALTER TABLE quotas
    ADD CONSTRAINT quotas_customer_id_fk
    FOREIGN KEY (customerId) REFERENCES customers (id) ON DELETE CASCADE;
