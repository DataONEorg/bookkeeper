-- Insert quotas for portal and storage quotas;

-- 1GB base storage limit
INSERT INTO quotas (object, quotaType, softLimit, hardLimit, totalUsage, unit, orderId, subject)
    VALUES ('quota', 'storage', 1073741824.0, 1181116006.4, NULL, 'byte', NULL, NULL);

-- 1TB base hosted repository storage limit
INSERT INTO quotas (object, quotaType, softLimit, hardLimit, totalUsage, unit, orderId, subject)
    VALUES ('quota', 'repository_storage', 1099511627776.0, 1209462790553.6, NULL, 'byte', NULL,
    NULL);

-- Single base branded portal limit
INSERT INTO quotas (object, quotaType, softLimit, hardLimit, totalUsage, unit, orderId, subject)
    VALUES ('quota', 'portal', 1.0, 1.0, NULL, 'portal', NULL, NULL);
