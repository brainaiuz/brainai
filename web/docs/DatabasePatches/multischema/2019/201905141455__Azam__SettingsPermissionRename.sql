update permission set name='Add' where code='ACCOUNTING_PRICE_LEVEL_ADD' and context='SETTINGS';
update permission set name='Edit' where code='ACCOUNTING_PRICE_LEVEL_EDIT' and context='SETTINGS';
update permission set name='Delete' where code='ACCOUNTING_PRICE_LEVEL_DELETE' and context='SETTINGS';

update permission set name='Add' where code='ACCOUNTING_DISCOUNT_ADD' and context='SETTINGS';

update permission set name='Edit' where code='ACCOUNTING_CURRENCY_RATE_EDIT' and context='SETTINGS';

update permission set name='Add' where code='ACCOUNTING_ACCOUNT_ADD' and context='SETTINGS';
update permission set name='Edit' where code='ACCOUNTING_ACCOUNT_EDIT' and context='SETTINGS';
update permission set name='Delete' where code='ACCOUNTING_ACCOUNT_DELETE' and context='SETTINGS';
update permission set name='Summary' where code='ACCOUNTING_ACCOUNT_SUMMARY' and context='SETTINGS';

update permission set name='Add' where code='CRM_CONTACT_CATEGORY_ADD' and context='SETTINGS';
update permission set name='Edit' where code='CRM_CONTACT_CATEGORY_EDIT' and context='SETTINGS';
update permission set name='Delete' where code='CRM_CONTACT_CATEGORY_DELETE' and context='SETTINGS';
update permission set name='Share' where code='CRM_CONTACT_CATEGORY_SHARE' and context='SETTINGS';

update permission set name='Add' where code='SETTINGS_HRMS_SETTINGS_ADD_TIMESLOT' and context='SETTINGS';
update permission set name='Edit' where code='SETTINGS_HRMS_SETTINGS_EDIT_TIMESLOT' and context='SETTINGS';
update permission set name='Delete' where code='SETTINGS_HRMS_SETTINGS_DELETE_TIMESLOT' and context='SETTINGS';

update permission set name='Add' where code='SETTINGS_HRMS_SETTINGS_ADD_HOLIDAY' and context='SETTINGS';
update permission set name='Edit' where code='SETTINGS_HRMS_SETTINGS_EDIT_HOLIDAY' and context='SETTINGS';
update permission set name='Delete' where code='SETTINGS_HRMS_SETTINGS_DELETE_HOLIDAY' and context='SETTINGS';

update permission set name='Add/Edit' where code='HRMS_LEAVE_REQUEST_REASON_ADD_EDIT' and context='SETTINGS';

update permission set name='Add' where code='BENEFIT_TYPE_ADD' and context='SETTINGS';

update permission set name='Add' where code='HRMS_ADD_NEW_DEPARTMENT' and context='SETTINGS';
update permission set name='Edit' where code='HRMS_EDIT_DEPARTMENT' and context='SETTINGS';
update permission set name='Delete' where code='HRMS_DEPARTMENT_REMOVE' and context='SETTINGS';

update permission set sorder=1, name='Summary' where code='HRMS_POSITION_SUMMARRY' and context='SETTINGS';
update permission set sorder=2, name='Rate' where code='HRMS_POSITION_RATES' and context='SETTINGS';
update permission set sorder=3, name='Add' where code='HRMS_ADD_NEW_POSITION' and context='SETTINGS';
update permission set sorder=4, name='Edit' where code='HRMS_POSITION_EDIT' and context='SETTINGS';
update permission set sorder=5, name='Delete' where code='HRMS_POSITION_REMOVE' and context='SETTINGS';

update permission set name='Add' where code='HRMS_ADD_NEW_LOCATION' and context='SETTINGS';
update permission set name='Edit' where code='HRMS_EDIT_LOCATION' and context='SETTINGS';
update permission set name='Delete' where code='HRMS_REMOVE_LOCATION' and context='SETTINGS';

update permission set name='Add/Edit' where code='HRMS_COUNTRY_SETTINGS_ADD_EDIT' and context='SETTINGS';

update permission set name='Add' where code='CETIFICATE_TEMPLATE_ADD' and context='SETTINGS';
update permission set name='Edit' where code='CETIFICATE_TEMPLATE_EDIT' and context='SETTINGS';
update permission set name='Delete' where code='CETIFICATE_TEMPLATE_DELETE' and context='SETTINGS';

update permission set name='Add' where code='SETTINGS_DASHBOARD_ADD' and context='SETTINGS';
update permission set name='Edit' where code='SETTINGS_DASHBOARD_EDIT' and context='SETTINGS';
update permission set name='Delete' where code='SETTINGS_DASHBOARD_DELETE' and context='SETTINGS';

update permission set sorder=1, name='View' where code='SETTINGS_SUMMARY_WORKFLOW' and context='SETTINGS';
update permission set sorder=2, name='Add' where code='SETTINGS_ADD_WORKFLOW' and context='SETTINGS';
update permission set sorder=3, name='Edit' where code='SETTINGS_EDIT_WORKFLOW' and context='SETTINGS';
update permission set sorder=4, name='Delete' where code='SETTINGS_REMOVE_WORKFLOW' and context='SETTINGS';

update permission set name='Add' where code='SETTINGS_SIGNATURE_ADD' and context='SETTINGS';

update permission set name='Add' where code='REFERENCE_ADD' and context='SETTINGS';
update permission set name='Edit' where code='REFERENCE_EDIT' and context='SETTINGS';
update permission set name='Delete' where code='REFERENCE_DELETE' and context='SETTINGS';

update permission set name='Users and Privileges' where code='PERMISSION_MANAGEMENT' and context='SETTINGS';