--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Add the order id foreign key constraint
ALTER TABLE quotas
    ADD CONSTRAINT quotas_order_id_fk
    FOREIGN KEY (orderId) REFERENCES orders (id) ON DELETE CASCADE;
