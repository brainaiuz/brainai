delete from permission WHERE code = 'PAYROLL_RECURRING_PD_LIST';
delete from permission WHERE code = 'PAYROLL_RECURRING_PD_ADD';
delete from permission WHERE code = 'PAYROLL_RECURRING_PD_VIEW';
delete from permission WHERE code = 'PAYROLL_RECURRING_PD_EDIT';
delete from permission WHERE code = 'PAYROLL_RECURRING_PD_DELETE';

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_RECURRING_PD_LIST', 'PAYROLL', 'Recurring Payment/Deduction', 7, (select id from permission WHERE code='PAYROLL_MAIN_CONTENT'), 'RECURRING_PAYMENT_DEDUCTION_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_RECURRING_PD_ADD', 'PAYROLL', 'Add', 2, (select id from permission WHERE code='PAYROLL_RECURRING_PD_LIST'), 'RECURRING_PAYMENT_DEDUCTION_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_RECURRING_PD_EDIT', 'PAYROLL', 'Edit', 3, (select id from permission WHERE code='PAYROLL_RECURRING_PD_LIST'), 'RECURRING_PAYMENT_DEDUCTION_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_RECURRING_PD_DELETE', 'PAYROLL', 'Delete', 4, (select id from permission WHERE code='PAYROLL_RECURRING_PD_LIST'), 'RECURRING_PAYMENT_DEDUCTION_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_RECURRING_PD_VIEW', 'PAYROLL', 'Summary', 5, (select id from permission WHERE code='PAYROLL_RECURRING_PD_LIST'), 'RECURRING_PAYMENT_DEDUCTION_MODULE');

delete from "anv".permission_context where permissioncode = 'PAYROLL_RECURRING_PD_LIST';
delete from "anv".permission_context where permissioncode = 'PAYROLL_RECURRING_PD_ADD';
delete from "anv".permission_context where permissioncode = 'PAYROLL_RECURRING_PD_VIEW';
delete from "anv".permission_context where permissioncode = 'PAYROLL_RECURRING_PD_EDIT';
delete from "anv".permission_context where permissioncode = 'PAYROLL_RECURRING_PD_DELETE';
delete from "anv".rolepermission where permissioncode = 'PAYROLL_RECURRING_PD_LIST';
delete from "anv".rolepermission where permissioncode = 'PAYROLL_RECURRING_PD_ADD';
delete from "anv".rolepermission where permissioncode = 'PAYROLL_RECURRING_PD_VIEW';
delete from "anv".rolepermission where permissioncode = 'PAYROLL_RECURRING_PD_EDIT';
delete from "anv".rolepermission where permissioncode = 'PAYROLL_RECURRING_PD_DELETE';

insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_RECURRING_PD_LIST', 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_RECURRING_PD_ADD', 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_RECURRING_PD_EDIT', 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_RECURRING_PD_DELETE', 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_RECURRING_PD_VIEW', 'PAYROLL');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_LIST', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_LIST', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_LIST', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_DELETE', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_VIEW', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_VIEW', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_VIEW', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_VIEW', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_VIEW', 'ACCOUNTANT','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_RECURRING_PD_VIEW', 'ACCOUNTANT','ALLOW');
