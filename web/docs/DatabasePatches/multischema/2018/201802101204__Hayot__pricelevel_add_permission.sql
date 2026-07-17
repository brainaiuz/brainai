-------------Insert REPMISSIONS (public schema)--------
--
-- delete from permission where code like 'ACCOUNTING_PRICE_LEVEL%';
-- delete from "23039".rolepermission where permissioncode like 'ACCOUNTING_PRICE_LEVEL%';
-- delete from "23039".permission_context where permissioncode like 'ACCOUNTING_PRICE_LEVEL%';
-- delete from "0".rolepermission where permissioncode like 'ACCOUNTING_PRICE_LEVEL%';
-- delete from "0".permission_context where permissioncode like 'ACCOUNTING_PRICE_LEVEL%';
--
-- insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
-- values ('ACCOUNTING_PRICE_LEVELS_LIST', 'SETTINGS', 'f', 'Price levels list', '6', (select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS'), 'ACCOUNTING_MODULE');
-- insert into "23039".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVELS_LIST', 'PM', 'ALLOW');
-- insert into "23039".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRICE_LEVELS_LIST',  'SETTINGS');
--
-- insert into "23039".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRICE_LEVELS_LIST',  'ACCOUNTING');
-- insert into "0".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRICE_LEVELS_LIST',  'ACCOUNTING');
--

insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('ACCOUNTING_PRICE_LEVEL_ADD', 'SETTINGS', 'f', 'Add Price level', '1', (select id from permission where code='ACCOUNTING_PRICE_LEVELS_LIST'), 'ACCOUNTING_MODULE'),
('ACCOUNTING_PRICE_LEVEL_EDIT', 'SETTINGS', 'f', 'Edit Price level', '2', (select id from permission where code='ACCOUNTING_PRICE_LEVELS_LIST'), 'ACCOUNTING_MODULE'),
('ACCOUNTING_PRICE_LEVEL_DELETE', 'SETTINGS', 'f', 'Delete Price level', '3', (select id from permission where code='ACCOUNTING_PRICE_LEVELS_LIST'), 'ACCOUNTING_MODULE');


-------------------------------------------------------------------for 'anv'-----------
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_ADD', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_ADD', 'ACCOUNTANT', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_EDIT', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_EDIT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_EDIT', 'ACCOUNTANT', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRICE_LEVEL_ADD',  'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRICE_LEVEL_EDIT',  'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRICE_LEVEL_DELETE',  'ACCOUNTING');

-------------------------------------------------------------------for '0'-----------
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_ADD', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_ADD', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_EDIT', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_EDIT', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PRICE_LEVEL_DELETE', 'ADMIN', 'ALLOW');
insert into "0".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRICE_LEVEL_ADD',  'ACCOUNTING');
insert into "0".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRICE_LEVEL_EDIT',  'ACCOUNTING');
insert into "0".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PRICE_LEVEL_DELETE',  'ACCOUNTING');


