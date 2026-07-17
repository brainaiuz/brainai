
delete from permission where code in ('SHOW_EMPLOYEE_BANK_DETAILS', 'SHOW_EMPLOYEE_PERSONAL_INFORMATION', 'HRMS_EMPLOYEE_WAGE_RATE');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('SHOW_EMPLOYEE_BANK_DETAILS', 'HRMS', 'Bank Account Details', 1, false, (select id from permission where code='HRMS_EMPLOYEE_PROFILE'), false,  'HRMS_MODULE');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('SHOW_EMPLOYEE_PERSONAL_INFORMATION', 'HRMS', 'Personal Identity Information', 2, false, (select id from permission where code='HRMS_EMPLOYEE_PROFILE'), false,  'HRMS_MODULE');
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('HRMS_EMPLOYEE_WAGE_RATE', 'HRMS', 'Employee wage/charge rates', 3, false, (select id from permission where code='HRMS_EMPLOYEE_PROFILE'), false,  'TASK_MANAGEMENT');
update permission set code='HRMS_EMPLOYEE_PROFILE_WAGE_RATE' where code='HRMS_EMPLOYEE_RATE';
update permission set name='Hrms leave request' where name='Hrms live request';

--for schema 0
delete from "0".rolepermission
where permissioncode in ('SHOW_EMPLOYEE_BANK_DETAILS','SHOW_EMPLOYEE_PERSONAL_INFORMATION','HRMS_EMPLOYEE_WAGE_RATE');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_EMPLOYEE_BANK_DETAILS', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_EMPLOYEE_BANK_DETAILS', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_EMPLOYEE_BANK_DETAILS', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_EMPLOYEE_PERSONAL_INFORMATION', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_EMPLOYEE_PERSONAL_INFORMATION', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SHOW_EMPLOYEE_PERSONAL_INFORMATION', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_EMPLOYEE_WAGE_RATE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_EMPLOYEE_WAGE_RATE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_EMPLOYEE_WAGE_RATE', 'HR', 'ALLOW');
update "0".rolepermission set permissioncode='HRMS_EMPLOYEE_PROFILE_WAGE_RATE' where permissioncode='HRMS_EMPLOYEE_RATE';



delete from "anv".rolepermission
where permissioncode in ('SHOW_EMPLOYEE_BANK_DETAILS','SHOW_EMPLOYEE_PERSONAL_INFORMATION','HRMS_EMPLOYEE_WAGE_RATE');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_EMPLOYEE_BANK_DETAILS', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_EMPLOYEE_BANK_DETAILS', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_EMPLOYEE_BANK_DETAILS', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_EMPLOYEE_PERSONAL_INFORMATION', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_EMPLOYEE_PERSONAL_INFORMATION', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SHOW_EMPLOYEE_PERSONAL_INFORMATION', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_EMPLOYEE_WAGE_RATE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_EMPLOYEE_WAGE_RATE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_EMPLOYEE_WAGE_RATE', 'HR', 'ALLOW');
update "anv".rolepermission set permissioncode='HRMS_EMPLOYEE_PROFILE_WAGE_RATE' where permissioncode='HRMS_EMPLOYEE_RATE';