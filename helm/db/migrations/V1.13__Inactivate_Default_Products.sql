--- Note that this is completed in k8s via the Dockerfile
--- or via a shell script in non-k8s development envs

--- Inactivate all of the default products cretaed by the system
--- These are not accurate and so not used in production systems
UPDATE products SET active='f' WHERE products.id <= 6;
