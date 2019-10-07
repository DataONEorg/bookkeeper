-- Insert quotas for portal and storage quotas;

-- 5GB standard account limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'custom_portal_quota', 5120, 5632, NULL, 'megabyte', NULL, NULL);

-- 1 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'custom_portal_quota', 1048576, 1153433, NULL, 'megabyte', NULL, NULL);

-- 2 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'custom_portal_quota', 2097152, 2202009, NULL, 'megabyte', NULL, NULL);

-- 3 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'custom_portal_quota', 3145728, 3250585, NULL, 'megabyte', NULL, NULL);

-- 4 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'custom_portal_quota', 4194304, 4299161, NULL, 'megabyte', NULL, NULL);

-- 5 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'custom_portal_quota', 5242880, 5347737, NULL, 'megabyte', NULL, NULL);

-- 6 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'custom_portal_quota', 6291456, 6396313, NULL, 'megabyte', NULL, NULL);

-- 7 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'custom_portal_quota', 7340032, 7444889, NULL, 'megabyte', NULL, NULL);

-- 8 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'custom_portal_quota', 8388608, 8493465, NULL, 'megabyte', NULL, NULL);

-- 9 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'custom_portal_quota', 9437184, 9542041, NULL, 'megabyte', NULL, NULL);

-- 10 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'custom_portal_quota', 10485760, 10590617, NULL, 'megabyte', NULL, NULL);
