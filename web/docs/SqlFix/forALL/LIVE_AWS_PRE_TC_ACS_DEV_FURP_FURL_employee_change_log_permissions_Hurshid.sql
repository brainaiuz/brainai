
insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode)
values('HRMS_VIEW_EMPLOYEE_CHANGE_LOG', 'HRMS', 'Employee Change Log', 25, false, (select id from permission where code='HRMS_SECTION_TAB'), false, 'HRMS_MODULE');

--------------- 0 schema --------------------------
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_VIEW_EMPLOYEE_CHANGE_LOG', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_VIEW_EMPLOYEE_CHANGE_LOG', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_VIEW_EMPLOYEE_CHANGE_LOG', 'HR', 'ALLOW');



-------------------- Private schema -------------------
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_VIEW_EMPLOYEE_CHANGE_LOG', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_VIEW_EMPLOYEE_CHANGE_LOG', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_VIEW_EMPLOYEE_CHANGE_LOG', 'HR', 'ALLOW');

