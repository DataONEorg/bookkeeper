--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Add the product id foreign key constraint
ALTER TABLE subscriptions
    ADD CONSTRAINT subscriptions_products_id_fk
    FOREIGN KEY (productId) REFERENCES products (id) ON DELETE CASCADE;
