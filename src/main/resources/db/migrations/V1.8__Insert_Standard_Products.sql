--- Insert products

--- Individual Membership
INSERT INTO products (object, active, amount, caption, created, currency, description,
    interval, name, statementDescriptor, type, unitLabel, url, metadata)
VALUES (
    'product',
    true,
    32000,
    'Faculty or research lab',
    now(),
    'USD',
    'Create a customized portal for your work and projects. Help others understand and access your data.',
    'year',
    'Individual',
    'DataONE Membership Plan - Individual',
    'service',
    'membership',
    'https://products.dataone.org/plus',
    '{"features": [{"name": "branded_portal","label": "Branded Portals","description": "Showcase your research, data, results, and usage metrics by building a custom web portal.","quota": {"object": "quota","quotaType": "portal","softLimit": "1","hardLimit": "1","totalUsage": "0","unit": "portal"}},{"name": "custom_search_filters","label": "Custom Search Filters","description": "Create custom search filters in your portal to allow scientists to search your holdings using filters appropriate to your field of science."},{"name": "fair_data_assessment","label": "FAIR Data Assessments","description": "Access quality metric reports using the FAIR data suite of checks."},{"name": "custom_quality_service","label": "Custom Quality Metrics","description": "Create a suite of custom quality metadata checks specific to your datasets."},{"name": "aggregated_metrics","label": "Aggregated Metrics","description": "Access and share reports on aggregated usage metrics such as dataset views, data downloads, and dataset citations."},{"name": "dataone_voting_member","label": "DataONE Voting Member","description": "Vote on the direction and priorities at DataONE Community meetings."}]}'
);


--- Small Organization Membership
INSERT INTO products (object, active, amount, caption, created, currency, description,
    interval, name, statementDescriptor, type, unitLabel, url, metadata)
VALUES (
    'product',
    true,
    200000,
    'Small institutions or groups',
    now(),
    'USD',
    'Create multiple portals for your work and projects. Help others understand and access your data.',
    'year',
    'Small Organization',
    'DataONE Membership Plan - Small Organization',
    'service',
    'membership',
    'https://products.dataone.org/plus',
    '{"features": [{"name": "branded_portal","label": "Branded Portals","description": "Showcase your research, data, results, and usage metrics by building a custom web portal.","quota": {"object": "quota","quotaType": "portal","softLimit": "5","hardLimit": "5","totalUsage":"0","unit": "portal"}},{"name": "custom_search_filters","label": "Custom Search Filters","description": "Create custom search filters in your portal to allow scientists to search your holdings using filters appropriate to your field of science."},{"name": "fair_data_assessment","label": "FAIR Data Assessments","description": "Access quality metric reports using the FAIR data suite of checks."},{"name": "custom_quality_service","label": "Custom Quality Metrics","description": "Create a suite of custom quality metadata checks specific to your datasets."},{"name": "aggregated_metrics","label": "Aggregated Metrics","description": "Access and share reports on aggregated usage metrics such as dataset views, data downloads, and dataset citations."},{"name": "dataone_voting_member","label": "DataONE Voting Member","description": "Vote on the direction and priorities at DataONE Community meetings."}]}'
);

--- Medium Organization Membership
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
    'Medium Organization',
    'DataONE Membership Plan - Medium Organization',
    'service',
    'membership',
    'https://products.dataone.org/plus',
    '{"features": [{"name": "branded_portal","label": "Branded Portals","description": "Showcase your research, data, results, and usage metrics by building a custom web portal.","quota": {"object": "quota","quotaType": "portal","softLimit": "15","hardLimit": "15","totalUsage":"0","unit": "portal"}},{"name": "custom_search_filters","label": "Custom Search Filters","description": "Create custom search filters in your portal to allow scientists to search your holdings using filters appropriate to your field of science."},{"name": "fair_data_assessment","label": "FAIR Data Assessments","description": "Access quality metric reports using the FAIR data suite of checks."},{"name": "custom_quality_service","label": "Custom Quality Metrics","description": "Create a suite of custom quality metadata checks specific to your datasets."},{"name": "aggregated_metrics","label": "Aggregated Metrics","description": "Access and share reports on aggregated usage metrics such as dataset views, data downloads, and dataset citations."},{"name": "dataone_voting_member","label": "DataONE Voting Member","description": "Vote on the direction and priorities at DataONE Community meetings."}]}'
);

--- Large Organization Membership
INSERT INTO products (object, active, amount, caption, created, currency, description,
    interval, name, statementDescriptor, type, unitLabel, url, metadata)
VALUES (
    'product',
    true,
    2800000,
    'Large institutions or groups',
    now(),
    'USD',
    'Create multiple portals for your work and projects. Help others understand and access your data.',
    'year',
    'Large Organization',
    'DataONE Membership Plan - Large Organization',
    'service',
    'membership',
    'https://products.dataone.org/plus',
    '{"features": [{"name": "branded_portal","label": "Branded Portals","description": "Showcase your research, data, results, and usage metrics by building a custom web portal.","quota": {"object": "quota","quotaType": "branded_portal","softLimit": "50","hardLimit": "50","totalUsage":"0","unit": "portal"}},{"name": "custom_search_filters","label": "Custom Search Filters","description": "Create custom search filters in your portal to allow scientists to search your holdings using filters appropriate to your field of science."},{"name": "fair_data_assessment","label": "FAIR Data Assessments","description": "Access quality metric reports using the FAIR data suite of checks."},{"name": "custom_quality_service","label": "Custom Quality Metrics","description": "Create a suite of custom quality metadata checks specific to your datasets."},{"name": "aggregated_metrics","label": "Aggregated Metrics","description": "Access and share reports on aggregated usage metrics such as dataset views, data downloads, and dataset citations."},{"name": "dataone_voting_member","label": "DataONE Voting Member","description": "Vote on the direction and priorities at DataONE Community meetings."}]}'
);

-- Hosted Repository
INSERT INTO products (object, active, amount, caption, created, currency, description,
    interval, name, statementDescriptor, type, unitLabel, url, metadata)
VALUES (
    'product',
    'true',
    1200000,
    'A dedicated repository solution for your group or institution’s data, managed by DataONE',
    now(),
    'USD',
    'Bring the products of your research lab, field station, or library together in a centralized location for efficient sharing, access, and reuse. Accelerate research activity, foster new collaborations, and build community with a repository that meets your needs and reflects your brand.',
    'year',
    'DataONE Hosted Repository',
    'DataONE Hosted Repository',
    'service',
    'repository',
    'https://products.dataone.org/hostedrepo',
    '{"features": [{"name": "trusted_data_repository","label": "A Trusted Data Repository","description": "Preserve and share your data, software, and derived products in a dedicated repository system. Built on our robust and expertly managed repository software and hardware, your research products are safely inactive and easily accessible."},{"name": "individual_fair_Assessments","label": "Individual FAIR Assessments","description": "Evaluate your metadata with community established FAIR principles. Scores are refreshed with updates to your metadata, helping make your data even more Findable, Accessible, Interoperable, and Reusable."},{"name": "powerful_online_submission","label": "Powerful Online Submission","description": "The user friendly data submission tool helps your researchers efficiently upload and describe their data. Users can easily create detailed metadata to enhance interoperability, reusability, and value of data."},{"name": "comprehensive_search","label": "Comprehensive Search","description": "Quickly find data with detailed search filters, or by navigating the interactive map."},{"name": "usage_metrics","label": "Usage Metrics","description": "Understand how your data are being used over time with view, download, and citation metrics."},{"name": "usage_metrics","label": "Usage Metrics","description": "Understand how your data are being used over time with view, download, and citation metrics."},{"name": "expandable_storage","label": "Expandable Storage","description": "Grow your repository capacity based on your storage needs in 1 TB increments.","quota": {"object": "quota","quotaType": "repository_storage","softLimit": 1048576,"hardLimit": 1153434,"totalUsage": 0,"unit": "portal"}},{"name": "geographic_replicas","label": "Geographic Replicas","description": "Your data are replicated to distinct geographic regions for high availability and preservation."},{"name": "api_access","label": "API Access","description": "Programmatically work with your repository through the DataONE tools in R, Python, Matlab, and Java."},{"name": "link_data_and_software","label": "Link data and Software","description": "Easily show how your files relate to each other by providing well-described provenance workflows."},{"name": "private_and_public_access","label": "Private and Public Access","description": "Control access to your datasets\nprior to publication with private groups or just yourself."},{"name": "private_and_public_access","label": "Private and Public Access","description": "Control access to your datasets\nprior to publication with private groups or just yourself."},{"name": "any_file_format","label": "Any File Format","description": "Use the scientific file formats for your community: image, tabular, text, audio, video, and others."},{"name": "share_when_ready","label": "Share When Ready","description": "Keep your dataset private while you document it thoroughly, and then make it public when you are ready."}]}'
);

-- Archival Storage
INSERT INTO products (object, active, amount, caption, created, currency, description,
    interval, name, statementDescriptor, type, unitLabel, url, metadata)
VALUES (
    'product',
    'true',
    126000,
    'Additional High Availability Data Storage',
    now(),
    'USD',
    'Increase the available storage in your dedicated hosted repository in 1 TB increments.',
    'year',
    'Additional Data Storage',
    'Additional Data Storage',
    'service',
    'storage',
    'https://products.dataone.org/hostedrepo',
    '{}'
);

