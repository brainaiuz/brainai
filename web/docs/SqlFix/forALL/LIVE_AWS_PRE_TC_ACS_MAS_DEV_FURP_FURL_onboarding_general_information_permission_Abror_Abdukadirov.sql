insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode)
values('HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION', 'HRMS', 'Show Employee Step General Information', 3, false, (select id from permission where code='HRMS_ONBOARDING_MANAGEMENT'), false,  'ONBOARDING');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION', 'ACCOUNTANT', 'ALLOW');