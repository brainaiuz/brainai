insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode)
values('HRMS_SEE_ALL_EMPLOYEE_STEPS_LIST', 'HRMS', 'See All Employee Steps List', 4, false, (select id from permission where code='HRMS_ONBOARDING_MANAGEMENT'), false,  'ONBOARDING');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_SEE_ALL_EMPLOYEE_STEPS_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_SEE_ALL_EMPLOYEE_STEPS_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_SEE_ALL_EMPLOYEE_STEPS_LIST', 'HR', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_SEE_ALL_EMPLOYEE_STEPS_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_SEE_ALL_EMPLOYEE_STEPS_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_SEE_ALL_EMPLOYEE_STEPS_LIST', 'HR', 'ALLOW');
