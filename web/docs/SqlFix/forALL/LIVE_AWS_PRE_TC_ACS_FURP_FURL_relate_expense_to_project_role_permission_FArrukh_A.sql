insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode) values('EXPENSE_ADD_VIEW_FULL_ACCESS', 'ACCOUNTING', false,'Expense Add/View Full Access', 7, (select id from permission where code ='ACCOUNTING_EXPENSE_REPORT_LIST'), false, 'EXPENSE_REPORTING');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('EXPENSE_ADD_VIEW_FULL_ACCESS', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('EXPENSE_ADD_VIEW_FULL_ACCESS', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('EXPENSE_ADD_VIEW_FULL_ACCESS', 'ACCOUNTANT','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('EXPENSE_ADD_VIEW_FULL_ACCESS', 'PM','ALLOW')

insert into "0".rolepermission (permissioncode, rolecode, access) values('EXPENSE_ADD_VIEW_FULL_ACCESS', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('EXPENSE_ADD_VIEW_FULL_ACCESS', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('EXPENSE_ADD_VIEW_FULL_ACCESS', 'ACCOUNTANT','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('EXPENSE_ADD_VIEW_FULL_ACCESS', 'PM','ALLOW');