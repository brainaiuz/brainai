insert into permission (code, context, name, sorder, parent, modulecode) values
('ACCOUNTING_STOCK_TRANSFER_SEE_OWN', 'ACCOUNTING', 'See Own', 1, (select id from permission where code ='ACCOUNTING_STOCK_TRANSFER_LIST'), 'ACCOUNTING_MODULE');

insert into "0".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_STOCK_TRANSFER_SEE_OWN', 'ADMIN', 'ALLOW');
insert into "0".permission_context(permissioncode, contextcode) values('ACCOUNTING_STOCK_TRANSFER_SEE_OWN', 'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('ACCOUNTING_STOCK_TRANSFER_SEE_OWN', 'ADMIN', 'ALLOW');
insert into "anv".permission_context(permissioncode, contextcode) values('ACCOUNTING_STOCK_TRANSFER_SEE_OWN', 'ACCOUNTING');
