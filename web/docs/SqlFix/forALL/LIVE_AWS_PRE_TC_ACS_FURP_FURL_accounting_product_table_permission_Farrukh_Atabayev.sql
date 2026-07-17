insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values('ACCOUNTING_PRODUCT_TABLE_SETTINGS', 'SETTINGS', false,'Product Table Settings', 18, (select id from permission where code='SETTINGS_ACCOUNTING_SETTINGS'), false, 'ACCOUNTING_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_PRODUCT_TABLE_SETTINGS', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_PRODUCT_TABLE_SETTINGS', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_PRODUCT_TABLE_SETTINGS', 'ACCOUNTANT','ALLOW');

insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_PRODUCT_TABLE_SETTINGS','SETTINGS');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_PRODUCT_TABLE_SETTINGS', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_PRODUCT_TABLE_SETTINGS', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_PRODUCT_TABLE_SETTINGS', 'ACCOUNTANT','ALLOW');

insert into "0".permission_context(permissioncode, contextcode) values('ACCOUNTING_PRODUCT_TABLE_SETTINGS','SETTINGS');