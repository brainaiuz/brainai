insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('HRMS_CASH_ADVANCE_LIST', 'HRMS', 'f', 'Cash Advance List', 10, (select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), 'HRMS_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_CASH_ADVANCE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_CASH_ADVANCE_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_CASH_ADVANCE_LIST', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_CASH_ADVANCE_LIST', 'ESS_USER', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_CASH_ADVANCE_LIST', 'MEM', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_CASH_ADVANCE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_CASH_ADVANCE_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_CASH_ADVANCE_LIST', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_CASH_ADVANCE_LIST', 'ESS_USER', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_CASH_ADVANCE_LIST', 'MEM', 'ALLOW');