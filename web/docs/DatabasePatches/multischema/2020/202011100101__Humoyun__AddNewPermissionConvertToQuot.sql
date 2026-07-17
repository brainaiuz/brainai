
update permission set  sorder=7, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST') where code='ACCOUNTING_SALES_ORDER_PDF';
update permission set  sorder=8, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST') where code='ACCOUNTING_CONVERT_TO_PROJECT';
update permission set  sorder=9, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST') where code='ACCOUNTING_GDN_DELETE';
update permission set  sorder=10, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST') where code='ACCOUNTING_SALES_ORDER_FULL_LIST_ACCESS';
update permission set  sorder=11, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST') where code='ACCOUNTING_SALES_ORDER_FULL_EDIT_ACCESS';

delete from permission where code='ACCOUNTING_SALES_ORDER_COPYTOSQ';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('ACCOUNTING_SALES_ORDER_COPYTOSQ', 'ACCOUNTING', 'Copy To SQ', 6,
        (select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),'SALES_ORDERS');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_ORDER_COPYTOSQ';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SALES_ORDER_COPYTOSQ', 'ACCOUNTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SALES_ORDER_COPYTOSQ', 'CRM');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_SALES_ORDER_COPYTOSQ';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_SALES_ORDER_COPYTOSQ', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_SALES_ORDER_COPYTOSQ', 'ALLOW', 'SALESMAN');

