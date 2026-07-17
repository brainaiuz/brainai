update permission set  sorder=1, parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='SETTINGS_COMPANY_SETTINGS';
update permission set  sorder=1, parent=(select id from permission where code='SETTINGS_COMPANY_SETTINGS') where code='ACCESS_FOR_IP';
update permission set  sorder=2, parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='SETTINGS_PROFILE_SETTINGS';
update permission set  sorder=3, parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='SETTINGS_USER_CREDENTIALS';

update permission set  sorder=4, parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='SETTINGS_ACCOUNTING_SETTINGS';
update permission set  sorder=1, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_CONVERSION_BALANCE';
update permission set  sorder=2, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_INVOICE_SETTINGS';
update permission set  sorder=3, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_FINANCIAL_SETTINGS';
update permission set  sorder=4, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_ACCOUNT_NUMBERING';
update permission set  sorder=5, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_NUMBERING_SETTINGS';
update permission set  sorder=6, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_PRICE_LEVELS_LIST';
update permission set  sorder=7, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_TERMS_LIST';
update permission set  sorder=8, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_DISCOUNTS_LIST';
update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_DISCOUNTS_LIST') where code='ACCOUNTING_DISCOUNT_ADD';
update permission set  sorder=9, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_UNIT_MEASUREMENTS_LIST';
update permission set  sorder=10, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_PRODUCT_CATEGORIES_LIST';
update permission set  sorder=11, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_BRANDS_LIST';
update permission set  sorder=12, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_SHIPPING_METHODS_LIST';


update permission set  sorder=14, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_ADD_ON_SETTINGS';
update permission set  sorder=15, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_CURRENCY_RATES_LIST';
update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_CURRENCY_RATES_LIST') where code='ACCOUNTING_CURRENCY_RATE_EDIT';

update permission set  sorder=16, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_TAX_RATES_LIST';
update permission set  sorder=17, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_PRODUCT_TABLE_SETTINGS';
update permission set  sorder=18, parent=(select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS') where code='ACCOUNTING_ACCOUNT_LIST';
update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_ACCOUNT_LIST') where code='ACCOUNTING_ACCOUNT_ADD';
update permission set  sorder=2, parent=(select id from permission where code='ACCOUNTING_ACCOUNT_LIST') where code='ACCOUNTING_ACCOUNT_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='ACCOUNTING_ACCOUNT_LIST') where code='ACCOUNTING_ACCOUNT_DELETE';
update permission set  sorder=4, parent=(select id from permission where code='ACCOUNTING_ACCOUNT_LIST') where code='"ACCOUNTING_ACCOUNT_SUMMARY"';


update permission set  sorder=5, parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='SETTINGS_CRM_SETTINGS';
update permission set  sorder=1, parent=(select id from permission where code='SETTINGS_CRM_SETTINGS') where code='SETTINGS_COMPANY_EMAL_SETTINGS';
update permission set  sorder=2, parent=(select id from permission where code='SETTINGS_CRM_SETTINGS') where code='CRM_CONTACT_CATEGORY_LIST';
update permission set  sorder=1, parent=(select id from permission where code='CRM_CONTACT_CATEGORY_LIST') where code='CRM_CONTACT_CATEGORY_ADD';
update permission set  sorder=2, parent=(select id from permission where code='CRM_CONTACT_CATEGORY_LIST') where code='CRM_CONTACT_CATEGORY_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='CRM_CONTACT_CATEGORY_LIST') where code='CRM_CONTACT_CATEGORY_DELETE';
update permission set  sorder=4, parent=(select id from permission where code='CRM_CONTACT_CATEGORY_LIST') where code='CRM_CONTACT_CATEGORY_SHARE';

update permission set  sorder=6, parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='SETTINGS_HRMS_SETTINGS';

insert into permission (code, context, name, sorder, parent, modulecode)
values('TIMESLOT_LIST', 'SETTINGS', 'Timeslots', 1, (select id from permission where code = 'SETTINGS_HRMS_SETTINGS'), 'ATTENDING_TRACKING');

insert into "0".permission_context (permissioncode, contextcode) values ('TIMESLOT_LIST',  'SETTINGS');

insert into "0".rolepermission (permissioncode, rolecode, access) values('TIMESLOT_LIST', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('TIMESLOT_LIST', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('TIMESLOT_LIST', 'HR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('TIMESLOT_LIST', 'ADMIN_LOCATION','ALLOW');

insert into "anv".permission_context (permissioncode, contextcode) values ('TIMESLOT_LIST',  'SETTINGS');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('TIMESLOT_LIST', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('TIMESLOT_LIST', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('TIMESLOT_LIST', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('TIMESLOT_LIST', 'ADMIN_LOCATION','ALLOW');

update permission set  sorder=1, parent=(select id from permission where code='SETTINGS_HRMS_SETTINGS') where code='TIMESLOT_LIST';
update permission set  sorder=1, parent=(select id from permission where code='TIMESLOT_LIST') where code='SETTINGS_HRMS_SETTINGS_ADD_TIMESLOT';
update permission set  sorder=2, parent=(select id from permission where code='TIMESLOT_LIST') where code='SETTINGS_HRMS_SETTINGS_EDIT_TIMESLOT';
update permission set  sorder=3, parent=(select id from permission where code='TIMESLOT_LIST') where code='SETTINGS_HRMS_SETTINGS_DELETE_TIMESLOT';


insert into permission (code, context, name, sorder, parent, modulecode)
values('HOLIDAY_LIST', 'SETTINGS', 'Holidays', 2, (select id from permission where code = 'SETTINGS_HRMS_SETTINGS'), 'ATTENDING_TRACKING');

insert into "0".permission_context (permissioncode, contextcode) values ('HOLIDAY_LIST',  'SETTINGS');

insert into "0".rolepermission (permissioncode, rolecode, access) values('HOLIDAY_LIST', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HOLIDAY_LIST', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HOLIDAY_LIST', 'HR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HOLIDAY_LIST', 'ADMIN_LOCATION','ALLOW');

insert into "anv".permission_context (permissioncode, contextcode) values ('HOLIDAY_LIST',  'SETTINGS');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('HOLIDAY_LIST', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HOLIDAY_LIST', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HOLIDAY_LIST', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HOLIDAY_LIST', 'ADMIN_LOCATION','ALLOW');

update permission set sorder=2, parent=(select id from permission where code='SETTINGS_HRMS_SETTINGS') where code='HOLIDAY_LIST';
update permission set sorder=1, parent=(select id from permission where code='HOLIDAY_LIST') where code='SETTINGS_HRMS_SETTINGS_ADD_HOLIDAY';
update permission set sorder=2, parent=(select id from permission where code='HOLIDAY_LIST') where code='SETTINGS_HRMS_SETTINGS_EDIT_HOLIDAY';
update permission set sorder=3, parent=(select id from permission where code='HOLIDAY_LIST') where code='SETTINGS_HRMS_SETTINGS_DELETE_HOLIDAY';

update permission set sorder=3, name='Leave Reasons', parent=(select id from permission where code='SETTINGS_HRMS_SETTINGS') where code='HRMS_LEAVE_REQUEST_REASON_LIST';
update permission set sorder=1, name='Leave Reasons Add/Edit', parent=(select id from permission where code='HRMS_LEAVE_REQUEST_REASON_LIST') where code='HRMS_LEAVE_REQUEST_REASON_ADD_EDIT';

update permission set sorder=4, name='Leave allowances', parent=(select id from permission where code='SETTINGS_HRMS_SETTINGS') where code='HRMS_ANNUAL_ALLOWANCE';
update permission set sorder=5, name='Benefit Types', parent=(select id from permission where code='SETTINGS_HRMS_SETTINGS') where code='BENEFIT_TYPE';
update permission set sorder=1, parent=(select id from permission where code='BENEFIT_TYPE') where code='BENEFIT_TYPE_ADD';

update permission set sorder=6, name='Department List', parent=(select id from permission where code='SETTINGS_HRMS_SETTINGS') where code='HRMS_DEPARTMENT';
update permission set sorder=1, name='See All departments', parent=(select id from permission where code='HRMS_DEPARTMENT') where code='HRMS_SEE_ALL_DEPARTMENT_LIST';
update permission set sorder=2, name='Department add', parent=(select id from permission where code='HRMS_DEPARTMENT') where code='HRMS_ADD_NEW_DEPARTMENT';
update permission set sorder=3, name='Department edit', parent=(select id from permission where code='HRMS_DEPARTMENT') where code='HRMS_EDIT_DEPARTMENT';
update permission set sorder=4, name='Department remove', parent=(select id from permission where code='HRMS_DEPARTMENT') where code='HRMS_DEPARTMENT_REMOVE';


update permission set sorder=7, name='Positions', parent=(select id from permission where code='SETTINGS_HRMS_SETTINGS') where code='HRMS_POSITION';
update permission set sorder=1, parent=(select id from permission where code='HRMS_POSITION') where code='HRMS_ADD_NEW_POSITION';
update permission set sorder=2, parent=(select id from permission where code='HRMS_POSITION') where code='HRMS_POSITION_EDIT';
update permission set sorder=3, name='Delete position', parent=(select id from permission where code='HRMS_POSITION') where code='HRMS_POSITION_REMOVE';

update permission set sorder=8, name='Location List', parent=(select id from permission where code='SETTINGS_HRMS_SETTINGS') where code='HRMS_LOCATION';
update permission set sorder=1, name='Location add', parent=(select id from permission where code='HRMS_LOCATION') where code='HRMS_ADD_NEW_LOCATION';
update permission set sorder=2, name='Location edit', parent=(select id from permission where code='HRMS_LOCATION') where code='HRMS_EDIT_LOCATION';
update permission set sorder=3, name='Location remove', parent=(select id from permission where code='HRMS_LOCATION') where code='HRMS_REMOVE_LOCATION';

update permission set sorder=9, parent=(select id from permission where code='SETTINGS_HRMS_SETTINGS') where code='HRMS_COUNTRY_SETTINGS_LIST';
update permission set sorder=1, name='Country Settings Add/Edit', parent=(select id from permission where code='HRMS_COUNTRY_SETTINGS_LIST') where code='HRMS_COUNTRY_SETTINGS_ADD_EDIT';

update permission set sorder=10, parent=(select id from permission where code='SETTINGS_HRMS_SETTINGS') where code='CETIFICATE_TEMPLATE_LIST';
update permission set sorder=1, name='Certificate Template Add', parent=(select id from permission where code='CETIFICATE_TEMPLATE_LIST') where code='CETIFICATE_TEMPLATE_ADD';
update permission set sorder=2, name='Certificate Template Edit', parent=(select id from permission where code='CETIFICATE_TEMPLATE_LIST') where code='CETIFICATE_TEMPLATE_EDIT';
update permission set sorder=3, name='Certificate Template Delete', parent=(select id from permission where code='CETIFICATE_TEMPLATE_LIST') where code='CETIFICATE_TEMPLATE_DELETE';

update permission set sorder=11, name='Company News Categories', parent=(select id from permission where code='SETTINGS_HRMS_SETTINGS') where code='HRMS_COMPANY_NEWS_CATEGORIES';

update permission set sorder=12, name='Audit Log', parent=(select id from permission where code='SETTINGS_HRMS_SETTINGS') where code='HRMS_VIEW_EMPLOYEE_CHANGE_LOG';

update permission set sorder=7, parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='SETTINGS_PROJECT_MANAGEMENT_SETTINGS';
update permission set sorder=1, parent=(select id from permission where code='SETTINGS_PROJECT_MANAGEMENT_SETTINGS') where code='PM_DEPARTMENT_LIST';
update permission set sorder=2, parent=(select id from permission where code='PM_DEPARTMENT_LIST') where code='PM_DEPARTMENT_ADD';
update permission set sorder=3, parent=(select id from permission where code='PM_DEPARTMENT_LIST') where code='PM_DEPARTMENT_EDIT';
update permission set sorder=4, parent=(select id from permission where code='PM_DEPARTMENT_LIST') where code='PM_DEPARTMENT_REMOVE';

update permission set sorder=2, parent=(select id from permission where code='SETTINGS_PROJECT_MANAGEMENT_SETTINGS') where code='PM_LOCATION_LIST';
update permission set sorder=1, parent=(select id from permission where code='PM_LOCATION_LIST') where code='PM_LOCATION_ADD';
update permission set sorder=2, parent=(select id from permission where code='PM_LOCATION_LIST') where code='PM_LOCATION_EDIT';
update permission set sorder=3, parent=(select id from permission where code='PM_LOCATION_LIST') where code='PM_LOCATION_REMOVE';

update permission set sorder=8, name='Dashboard List', parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='SETTINGS_DASHBOARD_LIST';
update permission set sorder=1, parent=(select id from permission where code='SETTINGS_DASHBOARD_LIST') where code='SETTINGS_DASHBOARD_ADD';
update permission set sorder=2, parent=(select id from permission where code='SETTINGS_DASHBOARD_LIST') where code='SETTINGS_DASHBOARD_EDIT';
update permission set sorder=3, parent=(select id from permission where code='SETTINGS_DASHBOARD_LIST') where code='SETTINGS_DASHBOARD_DELETE';

update permission set sorder=9, parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='CUSTOM_FIELD_SETTINGS';

update permission set sorder=10, parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='SETTINGS_RECURRENCE_SETTINGS';
update permission set sorder=1, parent=(select id from permission where code='SETTINGS_RECURRENCE_SETTINGS') where code='TIMESHEET_REMINDER';
update permission set sorder=1, parent=(select id from permission where code='TIMESHEET_REMINDER') where code='DEFAULT_TIMESHEET_REMINDER';
update permission set sorder=2, name='Sync with Calendar', parent=(select id from permission where code='SETTINGS_RECURRENCE_SETTINGS') where code='SYNC_WITH_GOOGLE_CALENDAR';

update permission set sorder=11, name='Automation', parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='SETTINGS_WORKFLOW';
update permission set sorder=1, parent=(select id from permission where code='SETTINGS_WORKFLOW') where code='SETTINGS_ADD_WORKFLOW';
update permission set sorder=2, parent=(select id from permission where code='SETTINGS_WORKFLOW') where code='SETTINGS_EDIT_WORKFLOW';
update permission set sorder=3, parent=(select id from permission where code='SETTINGS_WORKFLOW') where code='SETTINGS_SUMMARY_WORKFLOW';
update permission set sorder=4, name='Workflow Delete', parent=(select id from permission where code='SETTINGS_WORKFLOW') where code='SETTINGS_REMOVE_WORKFLOW';
update permission set sorder=12, parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='ADD_SYSTEM_FILTER';

update permission set sorder=13, name='Templates', parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='SETTINGS_EMAIL_SETTINGS';
update permission set sorder=1, name='Email Template List', parent=(select id from permission where code='SETTINGS_EMAIL_SETTINGS') where code='SETTINGS_EMAIL_TEMPALTE_LIST';
update permission set sorder=2, name='Signature List', parent=(select id from permission where code='SETTINGS_EMAIL_SETTINGS') where code='SETTINGS_SIGNATURE_LIST';
update permission set sorder=1, name='Signature Add', parent=(select id from permission where code='SETTINGS_SIGNATURE_LIST') where code='SETTINGS_SIGNATURE_ADD';

update permission set sorder=14, parent=(select id from permission where code='SETTINGS_MAIN_MENU') where code='REFERENCE_LIST';
update permission set sorder=1, parent=(select id from permission where code='REFERENCE_LIST') where code='REFERENCE_ADD';
update permission set sorder=2, parent=(select id from permission where code='REFERENCE_LIST') where code='REFERENCE_EDIT';
update permission set sorder=3, parent=(select id from permission where code='REFERENCE_LIST') where code='REFERENCE_DELETE';