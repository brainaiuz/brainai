delete from permission WHERE code in('ACCOUNTING_INVENTORY_LIST','ACCOUNTING_INVENTORY_ADD','ACCOUNTING_INVENTORY_EDIT','ACCOUNTING_INVENTORY_DELETE','ACCOUNTING_INVENTORY_SUMMARY');

delete from "0".permission_context where permissioncode in('ACCOUNTING_INVENTORY_LIST','ACCOUNTING_INVENTORY_ADD','ACCOUNTING_INVENTORY_EDIT','ACCOUNTING_INVENTORY_DELETE','ACCOUNTING_INVENTORY_SUMMARY');
delete from "anv".permission_context where permissioncode in('ACCOUNTING_INVENTORY_LIST','ACCOUNTING_INVENTORY_ADD','ACCOUNTING_INVENTORY_EDIT','ACCOUNTING_INVENTORY_DELETE','ACCOUNTING_INVENTORY_SUMMARY');

delete from "0".rolepermission where permissioncode in('ACCOUNTING_INVENTORY_LIST','ACCOUNTING_INVENTORY_ADD','ACCOUNTING_INVENTORY_EDIT','ACCOUNTING_INVENTORY_DELETE','ACCOUNTING_INVENTORY_SUMMARY');
delete from "anv".rolepermission where permissioncode in('ACCOUNTING_INVENTORY_LIST','ACCOUNTING_INVENTORY_ADD','ACCOUNTING_INVENTORY_EDIT','ACCOUNTING_INVENTORY_DELETE','ACCOUNTING_INVENTORY_SUMMARY');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_INVENTORY_LIST', 'ACCOUNTING', 'Inventory List', 1, 0, 'PRODUCT_INVENTORY_ITEMS');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_INVENTORY_ADD', 'ACCOUNTING', 'Add', 2, (select id from permission WHERE code='ACCOUNTING_INVENTORY_LIST'), 'PRODUCT_INVENTORY_ITEMS');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_INVENTORY_EDIT', 'ACCOUNTING', 'Edit', 3, (select id from permission WHERE code='ACCOUNTING_INVENTORY_LIST'), 'PRODUCT_INVENTORY_ITEMS');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_INVENTORY_DELETE', 'ACCOUNTING', 'Delete', 4, (select id from permission WHERE code='ACCOUNTING_INVENTORY_LIST'), 'PRODUCT_INVENTORY_ITEMS');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_INVENTORY_SUMMARY', 'ACCOUNTING', 'Summary', 5, (select id from permission WHERE code='ACCOUNTING_INVENTORY_LIST'), 'PRODUCT_INVENTORY_ITEMS');


insert into "0".permission_context (permissioncode, contextcode) values('ACCOUNTING_INVENTORY_LIST', 'ACCOUNTING');
insert into "0".permission_context (permissioncode, contextcode) values('ACCOUNTING_INVENTORY_ADD', 'ACCOUNTING');
insert into "0".permission_context (permissioncode, contextcode) values('ACCOUNTING_INVENTORY_EDIT', 'ACCOUNTING');
insert into "0".permission_context (permissioncode, contextcode) values('ACCOUNTING_INVENTORY_DELETE', 'ACCOUNTING');
insert into "0".permission_context (permissioncode, contextcode) values('ACCOUNTING_INVENTORY_SUMMARY', 'ACCOUNTING');

insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_INVENTORY_LIST', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_INVENTORY_ADD', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_INVENTORY_EDIT', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_INVENTORY_DELETE', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_INVENTORY_SUMMARY', 'ACCOUNTING');


insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_LIST', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_LIST', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_LIST', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_ADD', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_ADD', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_ADD', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_EDIT', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_EDIT', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_EDIT', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_DELETE', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_DELETE', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_DELETE', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_SUMMARY', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_SUMMARY', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_SUMMARY', 'ACCOUNTANT','ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_LIST', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_LIST', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_LIST', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_DELETE', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_SUMMARY', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_SUMMARY', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_INVENTORY_SUMMARY', 'ACCOUNTANT','ALLOW');