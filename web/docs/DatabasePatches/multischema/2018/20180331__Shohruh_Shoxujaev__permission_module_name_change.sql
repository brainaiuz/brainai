update permission set name = 'Accounts' where code = 'ACCOUNTING_MAIN_MENU';
update permission set name = 'Humans' where code = 'HRMS_MAIN_MENU';
update permission set name = 'Sales' where code = 'CRM_MAIN_MENU';
update permission set name = 'Projects' where code = 'PM_MAIN_MENU';
update permission set name = 'Reports' where code = 'REPORTING_MAIN_MENU';

update permission set ismainmenu = false, companyid = -1 where code = 'PM_LOCATION_LIST';
update permission set ismainmenu = false, companyid = -1 where code = 'LOGISTICS_MAIN_MENU';
update permission set ismainmenu = false, companyid = -1 where code = 'NOTES_WIDGET';
update permission set ismainmenu = false, companyid = -1 where code = 'DASHBOARD_MAIN_MENU';
update permission set ismainmenu = false, companyid = -1 where code = 'WORKSPACE_MAIN_MENU';