
delete from permission where code='ACCOUNTING_SALES_ORDER_LINE_ITEM_DELETE';
delete from permission where code='ACCOUNTING_SALES_ORDER_LINE_ITEM_ACTIONS';
insert into permission (code, context, name, parent, modulecode,sorder)
values ('ACCOUNTING_SALES_ORDER_LINE_ITEM_ACTIONS', 'ACCOUNTING', 'Line Item Actions', (select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), 'SALES_ORDERS', 32);

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_ORDER_LINE_ITEM_DELETE';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_ORDER_LINE_ITEM_ACTIONS';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SALES_ORDER_LINE_ITEM_ACTIONS', 'ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_SALES_ORDER_LINE_ITEM_DELETE';
delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_SALES_ORDER_LINE_ITEM_ACTIONS';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_SALES_ORDER_LINE_ITEM_ACTIONS', 'ALLOW', 'ADMIN');


delete from permission where code='ACCOUNTING_SALES_QUOTE_LINE_ITEM_DELETE';
delete from permission where code='ACCOUNTING_SALES_QUOTE_LINE_ITEM_ACTIONS';
insert into permission (code, context, name, parent, modulecode,sorder)
values ('ACCOUNTING_SALES_QUOTE_LINE_ITEM_ACTIONS', 'ACCOUNTING', 'Line Item Actions', (select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), 'SALES_QUOTES', 21);

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_QUOTE_LINE_ITEM_DELETE';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_QUOTE_LINE_ITEM_ACTIONS';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SALES_QUOTE_LINE_ITEM_ACTIONS', 'ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_SALES_QUOTE_LINE_ITEM_DELETE';
delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_SALES_QUOTE_LINE_ITEM_ACTIONS';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_SALES_QUOTE_LINE_ITEM_ACTIONS', 'ALLOW', 'ADMIN');