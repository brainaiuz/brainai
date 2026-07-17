-------------Insert REPMISSIONS (public schema)--------
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('HRMS_EXPENSE_REPORT_VOID', 'HRMS', 'f', 'Hrms Expense Void', '4', (select id from permission where code='HRMS_EXPENCE_REPORT'), 'EMPLOYEE_EXPENSES');

insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('HRMS_CAN_APPROVE_EXPENSE_CLAIM', 'HRMS', 'f', 'Expense Approve', '5', (select id from permission where code='HRMS_EXPENCE_REPORT'), 'EMPLOYEE_EXPENSES');


-------------------------------------------------------------------for 'anv'-----------
----------------------- HRMS_EXPENSE_REPORT_VOID ----- default -----
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_EXPENSE_REPORT_VOID', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_EXPENSE_REPORT_VOID', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_EXPENSE_REPORT_VOID', 'ACCOUNTANT', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_EXPENSE_REPORT_VOID', 'PM', 'ALLOW');

----------------------- HRMS_CAN_APPROVE_EXPENSE_CLAIM ----- default -----
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_CAN_APPROVE_EXPENSE_CLAIM', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_CAN_APPROVE_EXPENSE_CLAIM', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_CAN_APPROVE_EXPENSE_CLAIM', 'ACCOUNTANT', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_CAN_APPROVE_EXPENSE_CLAIM', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_CAN_APPROVE_EXPENSE_CLAIM', 'TL', 'ALLOW');

-------------------------------------------------------------------for '0'-----------
----------------------- HRMS_EXPENSE_REPORT_VOID ----- default -----
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_EXPENSE_REPORT_VOID', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_EXPENSE_REPORT_VOID', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_EXPENSE_REPORT_VOID', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_EXPENSE_REPORT_VOID', 'PM', 'ALLOW');

----------------------- HRMS_CAN_APPROVE_EXPENSE_CLAIM ----- default -----
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_CAN_APPROVE_EXPENSE_CLAIM', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_CAN_APPROVE_EXPENSE_CLAIM', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_CAN_APPROVE_EXPENSE_CLAIM', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_CAN_APPROVE_EXPENSE_CLAIM', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_CAN_APPROVE_EXPENSE_CLAIM', 'TL', 'ALLOW');