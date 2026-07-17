delete from permission WHERE code in('PAYROLL_SETTINGS_CATEGORIES_LIST','PAYROLL_SETTINGS_CATEGORIES_ADD','PAYROLL_SETTINGS_CATEGORIES_EDIT','PAYROLL_SETTINGS_CATEGORIES_DELETE');
delete from "anv".permission_context where permissioncode in('PAYROLL_SETTINGS_CATEGORIES_LIST','PAYROLL_SETTINGS_CATEGORIES_ADD','PAYROLL_SETTINGS_CATEGORIES_EDIT','PAYROLL_SETTINGS_CATEGORIES_DELETE');
delete from "anv".rolepermission where permissioncode in('PAYROLL_SETTINGS_CATEGORIES_LIST','PAYROLL_SETTINGS_CATEGORIES_ADD','PAYROLL_SETTINGS_CATEGORIES_EDIT','PAYROLL_SETTINGS_CATEGORIES_DELETE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_SETTINGS_CATEGORIES_LIST', 'SETTINGS', 'Payroll Categories List', (SELECT max(sorder) + 1 from permission WHERE code = 'PAYROLL_SETTINGS'), (select id from permission WHERE code='PAYROLL_SETTINGS'), 'PAYROLL');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_SETTINGS_CATEGORIES_ADD', 'SETTINGS', 'Add', 1, (select id from permission WHERE code='PAYROLL_SETTINGS_CATEGORIES_LIST'), 'PAYROLL');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_SETTINGS_CATEGORIES_EDIT', 'SETTINGS', 'Edit', 2, (select id from permission WHERE code='PAYROLL_SETTINGS_CATEGORIES_LIST'), 'PAYROLL');

insert into permission (code, context, name, sorder, parent, modulecode)
values('PAYROLL_SETTINGS_CATEGORIES_DELETE', 'SETTINGS', 'Delete', 3, (select id from permission WHERE code='PAYROLL_SETTINGS_CATEGORIES_LIST'), 'PAYROLL');


insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_SETTINGS_CATEGORIES_LIST', 'SETTINGS');
insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_SETTINGS_CATEGORIES_ADD', 'SETTINGS');
insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_SETTINGS_CATEGORIES_EDIT', 'SETTINGS');
insert into "anv".permission_context (permissioncode, contextcode) values('PAYROLL_SETTINGS_CATEGORIES_DELETE', 'SETTINGS');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SETTINGS_CATEGORIES_LIST', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SETTINGS_CATEGORIES_LIST', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SETTINGS_CATEGORIES_LIST', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SETTINGS_CATEGORIES_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SETTINGS_CATEGORIES_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SETTINGS_CATEGORIES_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SETTINGS_CATEGORIES_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SETTINGS_CATEGORIES_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SETTINGS_CATEGORIES_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SETTINGS_CATEGORIES_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SETTINGS_CATEGORIES_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SETTINGS_CATEGORIES_DELETE', 'ACCOUNTANT','ALLOW');
