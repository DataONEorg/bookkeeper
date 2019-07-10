-- Insert quotas for portal and storage quotas;

-- 5GB standard account limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 5368709120, 5905580032, 'byte', NULL);

-- 1 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 1099511627776, 1209462790554, 'byte', NULL);

-- 2 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 2199023255552, 2308974418330, 'byte', NULL);

-- 3 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 3298534883328, 3408486046106, 'byte', NULL);

-- 4 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 4398046511104, 4507997673882, 'byte', NULL);

-- 5 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 5497558138880, 5607509301658, 'byte', NULL);

-- 6 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 6597069766656, 6707020929434, 'byte', NULL);

-- 7 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 7696581394432, 7806532557210, 'byte', NULL);

-- 8 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 8796093022208, 8906044184986, 'byte', NULL);

-- 9 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 9895604649984, 10005555812761, 'byte', NULL);

-- 10 TB hosted repository or archival storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, unit, customerId)
    VALUES ('quota', 'custom_portal_quota', 10995116277760, 11105067440538, 'byte', NULL);
