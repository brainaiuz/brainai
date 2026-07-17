
delete from "anv".mymodule where code = 'MULTI_CASH_ADVANCE_MODULE';
insert into "anv".mymodule (code,name,section,active) values ('MULTI_CASH_ADVANCE_MODULE','Multi Cash Advance Management','payroll',true);


delete from permission WHERE code in('PAYROLL_MULTI_CASH_ADVANCE_LIST','PAYROLL_MULTI_CASH_ADVANCE_ADD','PAYROLL_MULTI_CASH_ADVANCE_EDIT','PAYROLL_MULTI_CASH_ADVANCE_DELETE','PAYROLL_MULTI_CASH_ADVANCE_VIEW','PAYROLL_MULTI_CASH_ADVANCE_FULL_ACCESS');
delete from "anv".permission_context where permissioncode in('PAYROLL_MULTI_CASH_ADVANCE_LIST','PAYROLL_MULTI_CASH_ADVANCE_ADD','PAYROLL_MULTI_CASH_ADVANCE_EDIT','PAYROLL_MULTI_CASH_ADVANCE_DELETE','PAYROLL_MULTI_CASH_ADVANCE_VIEW','PAYROLL_MULTI_CASH_ADVANCE_FULL_ACCESS');
delete from "anv".rolepermission where permissioncode in('PAYROLL_MULTI_CASH_ADVANCE_LIST','PAYROLL_MULTI_CASH_ADVANCE_ADD','PAYROLL_MULTI_CASH_ADVANCE_EDIT','PAYROLL_MULTI_CASH_ADVANCE_DELETE','PAYROLL_MULTI_CASH_ADVANCE_VIEW','PAYROLL_MULTI_CASH_ADVANCE_FULL_ACCESS');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_MULTI_CASH_ADVANCE_LIST', 'PAYROLL', 'Multi Cash Advance/Loan', 7, (select id from permission WHERE code='PAYROLL_MAIN_CONTENT'), 'MULTI_CASH_ADVANCE_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_MULTI_CASH_ADVANCE_ADD', 'PAYROLL', 'Add', 2, (select id from permission WHERE code='PAYROLL_MULTI_CASH_ADVANCE_LIST'), 'MULTI_CASH_ADVANCE_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_MULTI_CASH_ADVANCE_EDIT', 'PAYROLL', 'Edit', 3, (select id from permission WHERE code='PAYROLL_MULTI_CASH_ADVANCE_LIST'), 'MULTI_CASH_ADVANCE_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_MULTI_CASH_ADVANCE_DELETE', 'PAYROLL', 'Delete', 4, (select id from permission WHERE code='PAYROLL_MULTI_CASH_ADVANCE_LIST'), 'MULTI_CASH_ADVANCE_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_MULTI_CASH_ADVANCE_VIEW', 'PAYROLL', 'Summary', 5, (select id from permission WHERE code='PAYROLL_MULTI_CASH_ADVANCE_LIST'), 'MULTI_CASH_ADVANCE_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_MULTI_CASH_ADVANCE_FULL_ACCESS', 'PAYROLL', 'Full List Access', 6, (select id from permission WHERE code='PAYROLL_MULTI_CASH_ADVANCE_LIST'), 'MULTI_CASH_ADVANCE_MODULE');


insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_MULTI_CASH_ADVANCE_LIST', 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_MULTI_CASH_ADVANCE_ADD', 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_MULTI_CASH_ADVANCE_EDIT', 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_MULTI_CASH_ADVANCE_DELETE', 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_MULTI_CASH_ADVANCE_VIEW', 'PAYROLL');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_LIST', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_LIST', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_LIST', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_DELETE', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_VIEW', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_VIEW', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_MULTI_CASH_ADVANCE_VIEW', 'ACCOUNTANT','ALLOW');
