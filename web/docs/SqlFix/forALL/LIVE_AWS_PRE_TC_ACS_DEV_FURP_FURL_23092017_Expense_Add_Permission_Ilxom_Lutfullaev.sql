DELETE from permission WHERE code='ACCOUNTING_COMPANY_EXPENSE_ADD';
DELETE from "0".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_ADD';
DELETE from "anv".rolepermission WHERE permissioncode='ACCOUNTING_COMPANY_EXPENSE_ADD';

insert into permission (code, context, name, sorder, ismainmenu, parent, iscore, modulecode) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'ACCOUNTING', 'Company Expense Add',(select max(sorder) from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST')+1 , 'false', (select id from permission where code='ACCOUNTING_COMPANY_EXPENSE_LIST'), 'false', 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'ACCOUNTANT', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_COMPANY_EXPENSE_ADD', 'ACCOUNTANT', 'ALLOW');
