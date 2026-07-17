
delete from permission WHERE code in('ACCOUNTING_BUILD_ASSEMBLY_LIST','ACCOUNTING_BUILD_ASSEMBLY_ADD','ACCOUNTING_BUILD_ASSEMBLY_EDIT','ACCOUNTING_BUILD_ASSEMBLY_DELETE','ACCOUNTING_BUILD_ASSEMBLY_SUMMARY','ACCOUNTING_BUILD_ASSEMBLY_PRINT_PDF');
delete from "anv".permission_context where permissioncode in('ACCOUNTING_BUILD_ASSEMBLY_LIST','ACCOUNTING_BUILD_ASSEMBLY_ADD','ACCOUNTING_BUILD_ASSEMBLY_EDIT','ACCOUNTING_BUILD_ASSEMBLY_DELETE','ACCOUNTING_BUILD_ASSEMBLY_SUMMARY', 'ACCOUNTING_BUILD_ASSEMBLY_PRINT_PDF');
delete from "anv".rolepermission where permissioncode in('ACCOUNTING_BUILD_ASSEMBLY_LIST','ACCOUNTING_BUILD_ASSEMBLY_ADD','ACCOUNTING_BUILD_ASSEMBLY_EDIT','ACCOUNTING_BUILD_ASSEMBLY_DELETE','ACCOUNTING_BUILD_ASSEMBLY_SUMMARY', 'ACCOUNTING_BUILD_ASSEMBLY_PRINT_PDF');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_BUILD_ASSEMBLY_LIST', 'ACCOUNTING', 'Build Assembly List', 15, (select id from permission WHERE code='ACCOUNTING_PRODUCT_LIST' limit 1), 'PRODUCTION');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_BUILD_ASSEMBLY_ADD', 'ACCOUNTING', 'Add', 2, (select id from permission WHERE code='ACCOUNTING_BUILD_ASSEMBLY_LIST' limit 1), 'PRODUCTION');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_BUILD_ASSEMBLY_EDIT', 'ACCOUNTING', 'Edit', 3, (select id from permission WHERE code='ACCOUNTING_BUILD_ASSEMBLY_LIST' limit 1), 'PRODUCTION');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_BUILD_ASSEMBLY_DELETE', 'ACCOUNTING', 'Delete', 4, (select id from permission WHERE code='ACCOUNTING_BUILD_ASSEMBLY_LIST' limit 1), 'PRODUCTION');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_BUILD_ASSEMBLY_SUMMARY', 'ACCOUNTING', 'Summary', 5, (select id from permission WHERE code='ACCOUNTING_BUILD_ASSEMBLY_LIST' limit 1), 'PRODUCTION');

insert into permission (code, context, name, sorder, parent, modulecode)
values('ACCOUNTING_BUILD_ASSEMBLY_PRINT_PDF', 'ACCOUNTING', 'Print PDF', 6, (select id from permission WHERE code='ACCOUNTING_BUILD_ASSEMBLY_LIST' limit 1), 'PRODUCTION');

insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_BUILD_ASSEMBLY_LIST', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_BUILD_ASSEMBLY_ADD', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_BUILD_ASSEMBLY_EDIT', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_BUILD_ASSEMBLY_DELETE', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_BUILD_ASSEMBLY_SUMMARY', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values('ACCOUNTING_BUILD_ASSEMBLY_PRINT_PDF', 'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_LIST', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_LIST', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_LIST', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_DELETE', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_SUMMARY', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_SUMMARY', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_SUMMARY', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_PRINT_PDF', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_PRINT_PDF', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_BUILD_ASSEMBLY_PRINT_PDF', 'ACCOUNTANT','ALLOW');
