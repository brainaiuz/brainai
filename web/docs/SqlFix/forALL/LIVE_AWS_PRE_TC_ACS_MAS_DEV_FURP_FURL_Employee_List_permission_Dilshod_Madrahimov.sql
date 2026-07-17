
delete from permission where code in ('SHOW_ALL_EMPLOYEE_LIST', 'SHOW_DEPARTMENT_EMPLOYEE_LIST', 'SHOW_PROJECT_EMPLOYEE_LIST', 'SHOW_LOCATION_EMPLOYEE_LIST', 'SHOW_SUPERVISED_EMPLOYEE_LIST', 'HRMS_SEE_ALL_EMPLOYEES');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('SHOW_ALL_EMPLOYEE_LIST', 'HRMS', 'Show all employees', 1, false, (select id from permission where code='HRMS_EMPLOYEES'), false,  'HRMS_MODULE');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('SHOW_DEPARTMENT_EMPLOYEE_LIST', 'HRMS', 'Show department employees', 2, false, (select id from permission where code='HRMS_EMPLOYEES'), false,  'HRMS_MODULE');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('SHOW_PROJECT_EMPLOYEE_LIST', 'HRMS', 'Show project employees', 3, false, (select id from permission where code='HRMS_EMPLOYEES'), false,  'HRMS_MODULE');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('SHOW_LOCATION_EMPLOYEE_LIST', 'HRMS', 'Show location employees', 4, false, (select id from permission where code='HRMS_EMPLOYEES'), false,  'HRMS_MODULE');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('SHOW_SUPERVISED_EMPLOYEE_LIST', 'HRMS', 'Show supervised employees', 5, false, (select id from permission where code='HRMS_EMPLOYEES'), false,  'HRMS_MODULE');
update permission set sorder=6 where code='HRMS_EMPLOYEE_PROFILE' and modulecode='HRMS_MODULE';
update permission set sorder=7 where code='HRMS_ADD_NEW_EMPLOYEE' and modulecode='HRMS_MODULE';


delete from permission where code in ('PM_SHOW_ALL_EMPLOYEE_LIST', 'PM_SHOW_DEPARTMENT_EMPLOYEE_LIST', 'PM_SHOW_PROJECT_EMPLOYEE_LIST', 'PM_SHOW_LOCATION_EMPLOYEE_LIST', 'PM_SHOW_SUPERVISED_EMPLOYEE_LIST');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('PM_SHOW_ALL_EMPLOYEE_LIST', 'PM', 'Show all employees', 1, false, (select id from permission where code='PM_EMPLOYEE_LIST'), false,  'PM');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('PM_SHOW_DEPARTMENT_EMPLOYEE_LIST', 'PM', 'Show department employees', 2, false, (select id from permission where code='PM_EMPLOYEE_LIST'), false,  'PM');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('PM_SHOW_PROJECT_EMPLOYEE_LIST', 'PM', 'Show project employees', 3, false, (select id from permission where code='PM_EMPLOYEE_LIST'), false,  'PM');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('PM_SHOW_LOCATION_EMPLOYEE_LIST', 'PM', 'Show location employees', 4, false, (select id from permission where code='PM_EMPLOYEE_LIST'), false,  'PM');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('PM_SHOW_SUPERVISED_EMPLOYEE_LIST', 'PM', 'Show supervised employees', 5, false, (select id from permission where code='PM_EMPLOYEE_LIST'), false,  'PM');
update permission set sorder=6 where code='PM_EMPLOYEE_SUMMARY';
update permission set sorder=7 where code='PM_EMPLOYEE_NOTE';
update permission set sorder=8 where code='PM_EMPLOYEE_EDIT';
update permission set sorder=9 where code='PM_EMPLOYEE_REMOVE';
update permission set sorder=10 where code='PM_EMPLOYEE_ACTIVATE_DEACTIVATE';



--for anv schema
delete from "anv".rolepermission
where permissioncode in ('SHOW_ALL_EMPLOYEE_LIST','SHOW_DEPARTMENT_EMPLOYEE_LIST', 'SHOW_PROJECT_EMPLOYEE_LIST', 'SHOW_LOCATION_EMPLOYEE_LIST', 'SHOW_SUPERVISED_EMPLOYEE_LIST', 'HRMS_SEE_ALL_EMPLOYEES');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_ALL_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_ALL_EMPLOYEE_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_ALL_EMPLOYEE_LIST', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_DEPARTMENT_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_DEPARTMENT_EMPLOYEE_LIST', 'TL', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_PROJECT_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_PROJECT_EMPLOYEE_LIST', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_PROJECT_EMPLOYEE_LIST', 'PMOFPR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_PROJECT_EMPLOYEE_LIST', 'DLOFPR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_PROJECT_EMPLOYEE_LIST', 'BMOFPR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_LOCATION_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_LOCATION_EMPLOYEE_LIST', 'ADMIN_LOCATION', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_SUPERVISED_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_SUPERVISED_EMPLOYEE_LIST', 'SUPERVISOR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_EMPLOYEES', 'ADMIN_LOCATION', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_EMPLOYEES', 'TL', 'ALLOW');

delete from "anv".rolepermission
where permissioncode in ('PM_SHOW_ALL_EMPLOYEE_LIST','PM_SHOW_DEPARTMENT_EMPLOYEE_LIST', 'PM_SHOW_PROJECT_EMPLOYEE_LIST', 'PM_SHOW_LOCATION_EMPLOYEE_LIST', 'PM_SHOW_SUPERVISED_EMPLOYEE_LIST');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ALL_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ALL_EMPLOYEE_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ALL_EMPLOYEE_LIST', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_DEPARTMENT_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_DEPARTMENT_EMPLOYEE_LIST', 'TL', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_PROJECT_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_PROJECT_EMPLOYEE_LIST', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_PROJECT_EMPLOYEE_LIST', 'PMOFPR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_PROJECT_EMPLOYEE_LIST', 'DLOFPR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_PROJECT_EMPLOYEE_LIST', 'BMOFPR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_LOCATION_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_LOCATION_EMPLOYEE_LIST', 'ADMIN_LOCATION', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_SUPERVISED_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_SUPERVISED_EMPLOYEE_LIST', 'SUPERVISOR', 'ALLOW');



--for schema 0
delete from "0".rolepermission
where permissioncode in ('SHOW_ALL_EMPLOYEE_LIST','SHOW_DEPARTMENT_EMPLOYEE_LIST', 'SHOW_PROJECT_EMPLOYEE_LIST', 'SHOW_LOCATION_EMPLOYEE_LIST', 'SHOW_SUPERVISED_EMPLOYEE_LIST', 'HRMS_SEE_ALL_EMPLOYEES');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_ALL_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_ALL_EMPLOYEE_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_ALL_EMPLOYEE_LIST', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_DEPARTMENT_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_DEPARTMENT_EMPLOYEE_LIST', 'TL', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_PROJECT_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_PROJECT_EMPLOYEE_LIST', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_PROJECT_EMPLOYEE_LIST', 'PMOFPR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_PROJECT_EMPLOYEE_LIST', 'DLOFPR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_PROJECT_EMPLOYEE_LIST', 'BMOFPR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_LOCATION_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_LOCATION_EMPLOYEE_LIST', 'ADMIN_LOCATION', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_SUPERVISED_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_SUPERVISED_EMPLOYEE_LIST', 'SUPERVISOR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_EMPLOYEES', 'ADMIN_LOCATION', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_EMPLOYEES', 'TL', 'ALLOW');

delete from "0".rolepermission
where permissioncode in ('PM_SHOW_ALL_EMPLOYEE_LIST','PM_SHOW_DEPARTMENT_EMPLOYEE_LIST', 'PM_SHOW_PROJECT_EMPLOYEE_LIST', 'PM_SHOW_LOCATION_EMPLOYEE_LIST', 'PM_SHOW_SUPERVISED_EMPLOYEE_LIST');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ALL_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ALL_EMPLOYEE_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_ALL_EMPLOYEE_LIST', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_DEPARTMENT_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_DEPARTMENT_EMPLOYEE_LIST', 'TL', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_PROJECT_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_PROJECT_EMPLOYEE_LIST', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_PROJECT_EMPLOYEE_LIST', 'PMOFPR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_PROJECT_EMPLOYEE_LIST', 'DLOFPR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_PROJECT_EMPLOYEE_LIST', 'BMOFPR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_LOCATION_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_LOCATION_EMPLOYEE_LIST', 'ADMIN_LOCATION', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_SUPERVISED_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_SUPERVISED_EMPLOYEE_LIST', 'SUPERVISOR', 'ALLOW');



