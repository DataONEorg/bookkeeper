-- Insert quotas for portal and storage quotas;

-- 1GB base storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'storage_quota', 1024, 1126, NULL, 'megabyte', NULL, NULL);

-- 1TB base hosted repository storage limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'repository_storage_quota', 1048576, 1153434, NULL, 'megabyte', NULL, NULL);

-- Single base branded portal limit
INSERT INTO quotas (object, name, softLimit, hardLimit, usage, unit, subscriptionId, subject)
    VALUES ('quota', 'portal_quota', 1, 1, NULL, 'portal_count', NULL, NULL);
