
delete from permission WHERE code in('ACCOUNTING_ASSEMBLY_ITEM_LIST','ACCOUNTING_ASSEMBLY_ITEM_ADD','ACCOUNTING_ASSEMBLY_ITEM_EDIT','ACCOUNTING_ASSEMBLY_ITEM_DELETE','ACCOUNTING_ASSEMBLY_ITEM_SUMMARY');
delete from "anv".permission_context where permissioncode in('ACCOUNTING_ASSEMBLY_ITEM_LIST','ACCOUNTING_ASSEMBLY_ITEM_ADD','ACCOUNTING_ASSEMBLY_ITEM_EDIT','ACCOUNTING_ASSEMBLY_ITEM_DELETE','ACCOUNTING_ASSEMBLY_ITEM_SUMMARY');
delete from "anv".rolepermission where permissioncode in('ACCOUNTING_ASSEMBLY_ITEM_LIST','ACCOUNTING_ASSEMBLY_ITEM_ADD','ACCOUNTING_ASSEMBLY_ITEM_EDIT','ACCOUNTING_ASSEMBLY_ITEM_DELETE','ACCOUNTING_ASSEMBLY_ITEM_SUMMARY');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_ASSEMBLY_ITEM_LIST', 'ACCOUNTING', 'Assembly Item List', 15, (select id from permission WHERE code='ACCOUNTING_PRODUCT_LIST' limit 1), 'PRODUCTION');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_ASSEMBLY_ITEM_ADD', 'ACCOUNTING', 'Add', 2, (select id from permission WHERE code='ACCOUNTING_ASSEMBLY_ITEM_LIST' limit 1), 'PRODUCTION');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_ASSEMBLY_ITEM_EDIT', 'ACCOUNTING', 'Edit', 3, (select id from permission WHERE code='ACCOUNTING_ASSEMBLY_ITEM_LIST' limit 1), 'PRODUCTION');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_ASSEMBLY_ITEM_DELETE', 'ACCOUNTING', 'Delete', 4, (select id from permission WHERE code='ACCOUNTING_ASSEMBLY_ITEM_LIST' limit 1), 'PRODUCTION');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_ASSEMBLY_ITEM_SUMMARY', 'ACCOUNTING', 'Summary', 5, (select id from permission WHERE code='ACCOUNTING_ASSEMBLY_ITEM_LIST' limit 1), 'PRODUCTION');


insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_ASSEMBLY_ITEM_LIST', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_ASSEMBLY_ITEM_ADD', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_ASSEMBLY_ITEM_EDIT', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_ASSEMBLY_ITEM_DELETE', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_ASSEMBLY_ITEM_SUMMARY', 'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_LIST', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_LIST', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_LIST', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_DELETE', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_SUMMARY', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_SUMMARY', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_ASSEMBLY_ITEM_SUMMARY', 'ACCOUNTANT','ALLOW');
