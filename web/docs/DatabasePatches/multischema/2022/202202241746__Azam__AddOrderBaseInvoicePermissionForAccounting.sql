
update permission set name='Prepayment Add' where code='ACCOUNTING_PREPAYMENT_ADD';

--accounting base invoice
delete from permission where code='ACCOUNTING_BASE_INVOICE_ADD';
insert into permission (code, context, name, sorder, parent, modulecode) values
('ACCOUNTING_BASE_INVOICE_ADD', 'ACCOUNTING', 'Base Invoice Add', 2,
 (select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), 'SALES_ORDERS');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_BASE_INVOICE_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_BASE_INVOICE_ADD', 'ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_BASE_INVOICE_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_ITEM_LIST', 'ALLOW', 'ACCOUNTANT');

--children sorder update
update permission set sorder=3 where code='ACCOUNTING_SALES_ORDER_EDIT';
update permission set sorder=4 where code='ACCOUNTING_SALES_ORDER_DELETE';
update permission set sorder=5 where code='ACCOUNTING_SALES_ORDER_SUMMARY';
update permission set sorder=6 where code='ACCOUNTING_SALES_ORDER_PICKLIST';
update permission set sorder=7 where code='ACCOUNTING_SALES_ORDER_COPYTOPO';
update permission set sorder=8 where code='ACCOUNTING_SALES_ORDER_COPYTOSQ';
update permission set sorder=9 where code='ACCOUNTING_SALES_ORDER_COPYTOSO';
update permission set sorder=10 where code='ACCOUNTING_CONVERT_TO_PROJECT';
update permission set sorder=11 where code='CONVERT_SALE_ORDER_TO_SALE_INVOICE';
update permission set sorder=12 where code='ACCOUNTING_SALES_ORDER_CLOSED';
update permission set sorder=13 where code='ACCOUNTING_SALES_ORDER_PDF';
update permission set sorder=14 where code='ACCOUNTING_GDN_DELETE';
update permission set sorder=15 where code='ACCOUNTING_SALES_ORDER_FULL_LIST_ACCESS';
update permission set sorder=16 where code='ACCOUNTING_CAN_APPROVE_SALES_ORDER';
update permission set sorder=17 where code='ACCOUNTING_SALES_ORDER_FULL_EDIT_ACCESS';
update permission set sorder=18 where code='ACCOUNTING_SET_READY_QTY_TO_SHIP';
update permission set sorder=19 where code='ACCOUNTING_ENABLE_SHIPPING_BUTTON';
update permission set sorder=20 where code='ACCOUNTING_GDN_CONVERT_TO_INVOICE';
update permission set sorder=21 where code='SAVE_FILTER';
update permission set sorder=22 where code='RESET_FILTER';
update permission set sorder=23 where code='SALES_ORDER_SEE_OWN';
update permission set sorder=24 where code='ACCOUNTING_SALES_ORDER_LIST_CUSTOMIZE';
update permission set sorder=25 where code='SALES_ORDER_APPROVE_EMAIL_SEND';
update permission set sorder=26 where code='ACCOUNTING_SALES_ORDER_LINKS';
update permission set sorder=27 where code='ACCOUNTING_SALES_ORDER_LIST_FILTER';
update permission set sorder=28 where code='ACCOUNTING_SALES_ORDER_HISTORY_NOTES';
update permission set sorder=29 where code='ACCOUNTING_PACKING_LIST';
update permission set sorder=30 where code='SALES_ORDER_SUBMIT_AND_EMAIL_SEND';
update permission set sorder=31 where code='ACCOUNTING_SALES_ORDER_UPLOAD_FILES';