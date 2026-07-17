
delete from permission WHERE code in('ACCOUNTING_RENTAL_LIST','ACCOUNTING_RENTAL_ADD','ACCOUNTING_RENTAL_EDIT','ACCOUNTING_RENTAL_DELETE','ACCOUNTING_RENTAL_SUMMARY');
delete from "anv".permission_context where permissioncode in('ACCOUNTING_RENTAL_LIST','ACCOUNTING_RENTAL_ADD','ACCOUNTING_RENTAL_EDIT','ACCOUNTING_RENTAL_DELETE','ACCOUNTING_RENTAL_SUMMARY');
delete from "anv".rolepermission where permissioncode in('ACCOUNTING_RENTAL_LIST','ACCOUNTING_RENTAL_ADD','ACCOUNTING_RENTAL_EDIT','ACCOUNTING_RENTAL_DELETE','ACCOUNTING_RENTAL_SUMMARY');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_RENTAL_LIST', 'ACCOUNTING', 'Rental Product List', 15, (select id from permission WHERE code='ACCOUNTING_PRODUCT_LIST'), 'PRODUCT_RENTAL_ITEMS');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_RENTAL_ADD', 'ACCOUNTING', 'Add', 2, (select id from permission WHERE code='ACCOUNTING_RENTAL_LIST'), 'PRODUCT_RENTAL_ITEMS');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_RENTAL_EDIT', 'ACCOUNTING', 'Edit', 3, (select id from permission WHERE code='ACCOUNTING_RENTAL_LIST'), 'PRODUCT_RENTAL_ITEMS');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_RENTAL_DELETE', 'ACCOUNTING', 'Delete', 4, (select id from permission WHERE code='ACCOUNTING_RENTAL_LIST'), 'PRODUCT_RENTAL_ITEMS');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_RENTAL_SUMMARY', 'ACCOUNTING', 'Summary', 5, (select id from permission WHERE code='ACCOUNTING_RENTAL_LIST'), 'PRODUCT_RENTAL_ITEMS');


insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_RENTAL_LIST', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_RENTAL_ADD', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_RENTAL_EDIT', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_RENTAL_DELETE', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_RENTAL_SUMMARY', 'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_LIST', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_LIST', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_LIST', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_DELETE', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_SUMMARY', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_SUMMARY', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_RENTAL_SUMMARY', 'ACCOUNTANT','ALLOW');
