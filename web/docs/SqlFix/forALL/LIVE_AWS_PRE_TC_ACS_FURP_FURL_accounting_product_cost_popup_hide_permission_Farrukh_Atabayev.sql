insert into permission (code, context, name, sorder, parent, modulecode)
values ('HIDE_PRODUCT_PRICE', 'ACCOUNTING', 'Hide Product Price', '20', (select id from permission where code='ACCOUNTING_ACCOUNTING_MENU'), 'ACCOUNTING_MODULE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HIDE_PRODUCT_PRICE', 'MEM', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HIDE_PRODUCT_PRICE', 'MEM', 'ALLOW');
