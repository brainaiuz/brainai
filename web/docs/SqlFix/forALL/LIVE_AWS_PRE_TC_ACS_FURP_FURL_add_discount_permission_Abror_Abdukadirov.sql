insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_DISCOUNT_ADD', 'SETTINGS', 'Add Discount', 2, (select id from permission where code='ACCOUNTING_DISCOUNTS_LIST'), 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_DISCOUNT_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_DISCOUNT_ADD', 'PM', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_DISCOUNT_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_DISCOUNT_ADD', 'ACCOUNTANT', 'ALLOW');

insert into "0".permission_context(permissioncode, contextcode) values('ACCOUNTING_DISCOUNT_ADD','SETTINGS');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_DISCOUNT_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_DISCOUNT_ADD', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_DISCOUNT_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_DISCOUNT_ADD', 'ACCOUNTANT', 'ALLOW');

insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_DISCOUNT_ADD','SETTINGS');


