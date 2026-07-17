
DELETE FROM permission WHERE code = 'ACCOUNTING_PURCHASE_ORDER_CLOSED';

insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('ACCOUNTING_PURCHASE_ORDER_CLOSED', 'ACCOUNTING', 'f', 'Closed', '10',
(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'), 'PURCHASE_ORDERS');


insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PURCHASE_ORDER_CLOSED', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PURCHASE_ORDER_CLOSED', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PURCHASE_ORDER_CLOSED', 'ACCOUNTANT', 'ALLOW');
insert into "0".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PURCHASE_ORDER_CLOSED',  'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PURCHASE_ORDER_CLOSED', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PURCHASE_ORDER_CLOSED', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_PURCHASE_ORDER_CLOSED', 'ACCOUNTANT', 'ALLOW');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PURCHASE_ORDER_CLOSED',  'ACCOUNTING');



