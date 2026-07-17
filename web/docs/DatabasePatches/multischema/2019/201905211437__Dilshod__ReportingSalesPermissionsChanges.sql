update "0".reportingpermission set name = REPLACE(name, 'Sales','') where parent = (select id from "0".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_CRM');
update "0".reportingpermission set name = trim(name) where parent = (select id from "0".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_CRM');

update "anv".reportingpermission set name = REPLACE(name, 'Sales','') where parent = (select id from "anv".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_CRM');
update "anv".reportingpermission set name = trim(name) where parent = (select id from "anv".reportingpermission where code='REPORTING_TEMPLATE_CATEGORY_CRM');

update "anv".reportingpermission set name='Opportunities by Lead Source' where name='Sales by Lead Source';