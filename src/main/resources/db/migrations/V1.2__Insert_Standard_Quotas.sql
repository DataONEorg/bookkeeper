-- Insert quotas for portal and storage quotas;

-- 5GB standard account limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 5120, 5632, 'megabyte', NULL);

-- 1 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 1048576, 1153433, 'megabyte', NULL);

-- 2 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 2097152, 2202009, 'megabyte', NULL);

-- 3 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 3145728, 3250585, 'megabyte', NULL);

-- 4 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 4194304, 4299161, 'megabyte', NULL);

-- 5 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 5242880, 5347737, 'megabyte', NULL);

-- 6 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 6291456, 6396313, 'megabyte', NULL);

-- 7 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 7340032, 7444889, 'megabyte', NULL);

-- 8 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 8388608, 8493465, 'megabyte', NULL);

-- 9 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 9437184, 9542041, 'megabyte', NULL);

-- 10 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 10485760, 10590617, 'megabyte', NULL);
