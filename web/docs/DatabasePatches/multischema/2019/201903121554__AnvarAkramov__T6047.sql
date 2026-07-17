insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('HRMS_ANNUAL_LEAVE_BALANCE_REPORT', 'HRMS', 'f', 'Annual Leave Report', 10, (select id from permission where code='HRMS_SECTION_TAB'), 'HRMS_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_ANNUAL_LEAVE_BALANCE_REPORT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_ANNUAL_LEAVE_BALANCE_REPORT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_ANNUAL_LEAVE_BALANCE_REPORT', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_ANNUAL_LEAVE_BALANCE_REPORT', 'MEM', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_ANNUAL_LEAVE_BALANCE_REPORT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_ANNUAL_LEAVE_BALANCE_REPORT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_ANNUAL_LEAVE_BALANCE_REPORT', 'HR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_ANNUAL_LEAVE_BALANCE_REPORT', 'MEM', 'ALLOW');