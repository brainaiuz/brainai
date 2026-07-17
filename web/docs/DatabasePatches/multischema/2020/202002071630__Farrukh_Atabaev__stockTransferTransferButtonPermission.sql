insert into permission (code, context, name, sorder, parent, modulecode) values
('ACCOUNTING_STOCK_TRANSFER_BUTTON', 'ACCOUNTING', 'Transfer Button', 1, (select id from permission where code ='ACCOUNTING_STOCK_TRANSFER_LIST'), 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_STOCK_TRANSFER_BUTTON', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_STOCK_TRANSFER_BUTTON', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_STOCK_TRANSFER_BUTTON', 'ACCOUNTANT', 'ALLOW');
insert into "0".permission_context(permissioncode, contextcode) values('ACCOUNTING_STOCK_TRANSFER_BUTTON', 'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_STOCK_TRANSFER_BUTTON', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_STOCK_TRANSFER_BUTTON', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_STOCK_TRANSFER_BUTTON', 'ACCOUNTANT', 'ALLOW');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_STOCK_TRANSFER_BUTTON', 'ACCOUNTING');


insert into permission (code, context, name, sorder, parent, modulecode) values
('LOGISTICS_STOCK_TRANSFER_BUTTON', '"LOGISTICS"', 'Transfer Button', 1, (select id from permission where code ='LOGISTICS_STOCK_TRANSFER_LIST'), 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('LOGISTICS_STOCK_TRANSFER_BUTTON', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('LOGISTICS_STOCK_TRANSFER_BUTTON', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('LOGISTICS_STOCK_TRANSFER_BUTTON', 'ACCOUNTANT', 'ALLOW');
insert into "0".permission_context(permissioncode, contextcode) values('LOGISTICS_STOCK_TRANSFER_BUTTON', '"LOGISTICS"');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('LOGISTICS_STOCK_TRANSFER_BUTTON', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('LOGISTICS_STOCK_TRANSFER_BUTTON', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('LOGISTICS_STOCK_TRANSFER_BUTTON', 'ACCOUNTANT', 'ALLOW');
insert into "anv".permission_context(permissioncode, contextcode) values('LOGISTICS_STOCK_TRANSFER_BUTTON', '"LOGISTICS"');
