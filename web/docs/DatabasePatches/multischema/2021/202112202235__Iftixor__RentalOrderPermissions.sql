
delete from permission WHERE code in('ACCOUNTING_RENTAL_ORDER_LIST','ACCOUNTING_RENTAL_ORDER_ADD','ACCOUNTING_RENTAL_ORDER_EDIT','ACCOUNTING_RENTAL_ORDER_DELETE','ACCOUNTING_RENTAL_ORDER_SUMMARY');
delete from "anv".permission_context where permissioncode in('ACCOUNTING_RENTAL_ORDER_LIST','ACCOUNTING_RENTAL_ORDER_ADD','ACCOUNTING_RENTAL_ORDER_EDIT','ACCOUNTING_RENTAL_ORDER_DELETE','ACCOUNTING_RENTAL_ORDER_SUMMARY');
delete from "anv".rolepermission where permissioncode in('ACCOUNTING_RENTAL_ORDER_LIST','ACCOUNTING_RENTAL_ORDER_ADD','ACCOUNTING_RENTAL_ORDER_EDIT','ACCOUNTING_RENTAL_ORDER_DELETE','ACCOUNTING_RENTAL_ORDER_SUMMARY');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_RENTAL_ORDER_LIST', 'ACCOUNTING', 'Rental Order List', 16, (select id from permission WHERE code='ACCOUNTING_ACCOUNTING_MENU'), 'RENTAL_ORDER_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_RENTAL_ORDER_ADD', 'ACCOUNTING', 'Add', 2, (select id from permission WHERE code='ACCOUNTING_RENTAL_ORDER_LIST'), 'RENTAL_ORDER_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_RENTAL_ORDER_EDIT', 'ACCOUNTING', 'Edit', 3, (select id from permission WHERE code='ACCOUNTING_RENTAL_ORDER_LIST'), 'RENTAL_ORDER_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_RENTAL_ORDER_DELETE', 'ACCOUNTING', 'Delete', 4, (select id from permission WHERE code='ACCOUNTING_RENTAL_ORDER_LIST'), 'RENTAL_ORDER_MODULE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_RENTAL_ORDER_SUMMARY', 'ACCOUNTING', 'Summary', 5, (select id from permission WHERE code='ACCOUNTING_RENTAL_ORDER_LIST'), 'RENTAL_ORDER_MODULE');


insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_RENTAL_ORDER_LIST', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_RENTAL_ORDER_ADD', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_RENTAL_ORDER_EDIT', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_RENTAL_ORDER_DELETE', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_RENTAL_ORDER_SUMMARY', 'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_LIST', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_LIST', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_LIST', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_DELETE', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_SUMMARY', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_SUMMARY', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ORDER_SUMMARY', 'ACCOUNTANT','ALLOW');
