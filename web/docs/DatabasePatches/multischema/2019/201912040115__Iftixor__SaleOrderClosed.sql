
DELETE FROM permission WHERE code = 'ACCOUNTING_SALES_ORDER_CLOSED';

insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('ACCOUNTING_SALES_ORDER_CLOSED', 'ACCOUNTING', 'f', 'Closed', '9',
(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'), 'SALES_ORDERS');


insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_SALES_ORDER_CLOSED', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_SALES_ORDER_CLOSED', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_SALES_ORDER_CLOSED', 'ACCOUNTANT', 'ALLOW');
insert into "0".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SALES_ORDER_CLOSED',  'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_SALES_ORDER_CLOSED', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_SALES_ORDER_CLOSED', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_SALES_ORDER_CLOSED', 'ACCOUNTANT', 'ALLOW');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SALES_ORDER_CLOSED',  'ACCOUNTING');



