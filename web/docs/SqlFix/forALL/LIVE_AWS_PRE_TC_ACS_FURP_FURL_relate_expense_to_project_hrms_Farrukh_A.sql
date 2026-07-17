insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values('HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS', 'HRMS', false,'Expense Add/View Full Access', 6, (select id from permission where code ='HRMS_EXPENCE_REPORT'), false, 'EMPLOYEE_EXPENSES');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS', 'ACCOUNTANT','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS', 'PM','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS', 'ACCOUNTANT','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS', 'PM','ALLOW');
