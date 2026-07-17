
delete from permission where code in ('HRMS_SHOW_EMPLOYEE_ADDRESS', 'PM_SHOW_EMPLOYEE_ADDRESS');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode)
values('HRMS_SHOW_EMPLOYEE_ADDRESS', 'HRMS', 'Show Employee Address', 7, false, (select id from permission where code='HRMS_EMPLOYEE_PROFILE'), false,  'HRMS_MODULE');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode)
values('PM_SHOW_EMPLOYEE_ADDRESS', 'PM', 'Show Employee Address', 20, false, (select id from permission where code='PM_EMPLOYEE_LIST'), false,  'PM');

delete from "0".rolepermission where permissioncode in ('HRMS_SHOW_EMPLOYEE_ADDRESS', 'PM_SHOW_EMPLOYEE_ADDRESS');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ADDRESS', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ADDRESS', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ADDRESS', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ADDRESS', 'MEM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ADDRESS', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ADDRESS', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ADDRESS', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ADDRESS', 'MEM', 'ALLOW');


delete from "anv".rolepermission where permissioncode in ('HRMS_SHOW_EMPLOYEE_ADDRESS', 'PM_SHOW_EMPLOYEE_ADDRESS');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ADDRESS', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ADDRESS', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ADDRESS', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_SHOW_EMPLOYEE_ADDRESS', 'MEM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ADDRESS', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ADDRESS', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ADDRESS', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PM_SHOW_EMPLOYEE_ADDRESS', 'MEM', 'ALLOW');

