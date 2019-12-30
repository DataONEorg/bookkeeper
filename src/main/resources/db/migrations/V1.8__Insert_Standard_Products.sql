--- Insert products

--- Individual Subscription
INSERT INTO products (object, active, amount, caption, created, currency, description,
    interval, name, statementDescriptor, type, unitLabel, url, metadata)
VALUES (
    'product',
    true,
    30000,
    'Faculty or research lab',
    now(),
    'USD',
    'Create a customized portal for your work and projects. Help others understand and access your data.',
    'year',
    'Individual',
    'DataONE Subscription Plan - Individual',
    'service',
    'subscription',
    'https://dataone.org/products-and-services/individual-subscription',
    '{"features": [{"name": "branded_portal","label": "Branded Portals","description": "Showcase your research, data, results, and usage metrics by building a custom web portal.","quota": {"object": "quota","name": "portal_count","softLimit": "1","hardLimit": "1","unit": "portal"}},{"name": "custom_search_filters","label": "Custom Search Filters","description": "Create custom search filters in your portal to allow scientists to search your holdings using filters appropriate to your field of science."},{"name": "fair_data_assessment","label": "FAIR Data Assessments","description": "Access quality metric reports using the FAIR data suite of checks."},{"name": "custom_quality_service","label": "Custom Quality Metrics","description": "Create a suite of custom quality metadata checks specific to your datasets."},{"name": "aggregated_metrics","label": "Aggregated Metrics","description": "Access and share reports on aggregated usage metrics such as dataset views, data downloads, and dataset citations."},{"name": "dataone_voting_member","label": "DataONE Voting Member","description": "Vote on the direction and priorities at DataONE Community meetings."}]}'
);


--- Small Organization Subscription
INSERT INTO products (object, active, amount, caption, created, currency, description,
    interval, name, statementDescriptor, type, unitLabel, url, metadata)
VALUES (
    'product',
    true,
    120000,
    'Small institutions or groups',
    now(),
    'USD',
    'Create multiple portals for your work and projects. Help others understand and access your data.',
    'year',
    'Small Organization',
    'DataONE Subscription Plan - Small Organization',
    'service',
    'subscription',
    'https://dataone.org/products-and-services/organization-subscription',
    '{"features": [{"name": "branded_portal","label": "Branded Portals","description": "Showcase your research, data, results, and usage metrics by building a custom web portal.","quota": {"object": "quota","name": "portal_count","softLimit": "5","hardLimit": "5","unit": "portal"}},{"name": "custom_search_filters","label": "Custom Search Filters","description": "Create custom search filters in your portal to allow scientists to search your holdings using filters appropriate to your field of science."},{"name": "fair_data_assessment","label": "FAIR Data Assessments","description": "Access quality metric reports using the FAIR data suite of checks."},{"name": "custom_quality_service","label": "Custom Quality Metrics","description": "Create a suite of custom quality metadata checks specific to your datasets."},{"name": "aggregated_metrics","label": "Aggregated Metrics","description": "Access and share reports on aggregated usage metrics such as dataset views, data downloads, and dataset citations."},{"name": "dataone_voting_member","label": "DataONE Voting Member","description": "Vote on the direction and priorities at DataONE Community meetings."}]}'
);

--- Medium Organization Subscription
INSERT INTO products (object, active, amount, caption, created, currency, description,
    interval, name, statementDescriptor, type, unitLabel, url, metadata)
VALUES (
    'product',
    true,
    600000,
    'Medium institutions or groups',
    now(),
    'USD',
    'Create multiple portals for your work and projects. Help others understand and access your data.',
    'year',
    'Organization',
    'DataONE Subscription Plan - Medium Organization',
    'service',
    'subscription',
    'https://dataone.org/products-and-services/organization-subscription',
    '{"features": [{"name": "branded_portal","label": "Branded Portals","description": "Showcase your research, data, results, and usage metrics by building a custom web portal.","quota": {"object": "quota","name": "portal_count","softLimit": "15","hardLimit": "15","unit": "portal"}},{"name": "custom_search_filters","label": "Custom Search Filters","description": "Create custom search filters in your portal to allow scientists to search your holdings using filters appropriate to your field of science."},{"name": "fair_data_assessment","label": "FAIR Data Assessments","description": "Access quality metric reports using the FAIR data suite of checks."},{"name": "custom_quality_service","label": "Custom Quality Metrics","description": "Create a suite of custom quality metadata checks specific to your datasets."},{"name": "aggregated_metrics","label": "Aggregated Metrics","description": "Access and share reports on aggregated usage metrics such as dataset views, data downloads, and dataset citations."},{"name": "dataone_voting_member","label": "DataONE Voting Member","description": "Vote on the direction and priorities at DataONE Community meetings."}]}'
);

--- Large Organization Subscription
INSERT INTO products (object, active, amount, caption, created, currency, description,
    interval, name, statementDescriptor, type, unitLabel, url, metadata)
VALUES (
    'product',
    true,
    3600000,
    'Large institutions or groups',
    now(),
    'USD',
    'Create multiple portals for your work and projects. Help others understand and access your data.',
    'year',
    'Organization',
    'DataONE Subscription Plan - Large Organization',
    'service',
    'subscription',
    'https://dataone.org/products-and-services/organization-subscription',
    '{"features": [{"name": "branded_portal","label": "Branded Portals","description": "Showcase your research, data, results, and usage metrics by building a custom web portal.","quota": {"object": "quota","name": "branded_portal_count","softLimit": "50","hardLimit": "50","unit": "portal"}},{"name": "custom_search_filters","label": "Custom Search Filters","description": "Create custom search filters in your portal to allow scientists to search your holdings using filters appropriate to your field of science."},{"name": "fair_data_assessment","label": "FAIR Data Assessments","description": "Access quality metric reports using the FAIR data suite of checks."},{"name": "custom_quality_service","label": "Custom Quality Metrics","description": "Create a suite of custom quality metadata checks specific to your datasets."},{"name": "aggregated_metrics","label": "Aggregated Metrics","description": "Access and share reports on aggregated usage metrics such as dataset views, data downloads, and dataset citations."},{"name": "dataone_voting_member","label": "DataONE Voting Member","description": "Vote on the direction and priorities at DataONE Community meetings."}]}'
);

--- TODO: Hosted Repository

--- TODO: Archival Storage

--- TODO: Consultation
