----------------------------------- HRMS SECTION ----------------------------------------
delete from permission where code = 'HRMS_SHOW_EMPLOYEE_ATTACHMENT';
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('HRMS_SHOW_EMPLOYEE_ATTACHMENT', 'HRMS', 'Show Employee Attachments', 8, false, (select id from permission where code='HRMS_EMPLOYEE_PROFILE'), false,  'HRMS_MODULE');

--for schema 0
delete from "0".rolepermission where permissioncode = 'HRMS_SHOW_EMPLOYEE_ATTACHMENT';
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ATTACHMENT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ATTACHMENT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ATTACHMENT', 'HR', 'ALLOW');

--for All schemes

delete from "anv".rolepermission where permissioncode = 'HRMS_SHOW_EMPLOYEE_ATTACHMENT';
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ATTACHMENT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ATTACHMENT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ATTACHMENT', 'HR', 'ALLOW');


----------------------------------- PM SECTION ----------------------------------------

delete from permission where code = 'PM_SHOW_EMPLOYEE_ATTACHMENT';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('PM_SHOW_EMPLOYEE_ATTACHMENT', 'PM', 'Show Employee Attachments', 21, false, (select id from permission where code='PM_EMPLOYEE_LIST'), false,  'PM');

--for schema 0
delete from "0".rolepermission where permissioncode = 'PM_SHOW_EMPLOYEE_ATTACHMENT';
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ATTACHMENT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ATTACHMENT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ATTACHMENT', 'HR', 'ALLOW');

--for All schemes

delete from "anv".rolepermission where permissioncode = 'PM_SHOW_EMPLOYEE_ATTACHMENT';
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ATTACHMENT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ATTACHMENT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ATTACHMENT', 'HR', 'ALLOW');


