
update permission set  name='Add' where parent=(select id from permission where code = 'CRM_SALES_ORDER_LIST') and code='CRM_SALES_ORDER_ADD';
update permission set  name='Edit' where parent=(select id from permission where code = 'CRM_SALES_ORDER_LIST') and code='CRM_SALES_ORDER_EDIT';
update permission set  name='Delete' where parent=(select id from permission where code = 'CRM_SALES_ORDER_LIST') and code='CRM_SALES_ORDER_DELETE';
update permission set  name='Summary' where parent=(select id from permission where code = 'CRM_SALES_ORDER_LIST') and code='CRM_SALES_ORDER_SUMMARY';
update permission set  name='PDF' where parent=(select id from permission where code = 'CRM_SALES_ORDER_LIST') and code='CRM_SALES_ORDER_PDF';


update permission set  name='Add' where parent=(select id from permission where code = 'CRM_SALES_QUOTE_LIST') and code='CRM_SALES_QUOTE_ADD';
update permission set  name='Edit' where parent=(select id from permission where code = 'CRM_SALES_QUOTE_LIST') and code='CRM_SALES_QUOTE_EDIT';
update permission set  name='Delete' where parent=(select id from permission where code = 'CRM_SALES_QUOTE_LIST') and code='CRM_SALES_QUOTE_DELETE';
update permission set  name='Summary' where parent=(select id from permission where code = 'CRM_SALES_QUOTE_LIST') and code='CRM_SALES_QUOTE_SUMMARY';
update permission set  name='PDF' where parent=(select id from permission where code = 'CRM_SALES_QUOTE_LIST') and code='CRM_SALES_QUOTE_PDF';


update permission set  name='Add' where parent=(select id from permission where code = 'CRM_SALES_INVOICE_LIST') and code='CRM_SALES_INVOICE_ADD';
update permission set  name='Edit' where parent=(select id from permission where code = 'CRM_SALES_INVOICE_LIST') and code='CRM_SALES_INVOICE_EDIT';
update permission set  name='Delete' where parent=(select id from permission where code = 'CRM_SALES_INVOICE_LIST') and code='CRM_SALES_INVOICE_DELETE';
update permission set  name='Summary' where parent=(select id from permission where code = 'CRM_SALES_INVOICE_LIST') and code='CRM_SALES_INVOICE_SUMMARY';
update permission set  name='PDF' where parent=(select id from permission where code = 'CRM_SALES_INVOICE_LIST') and code='CRM_SALES_INVOICE_PDF';


update permission set  name='Add' where parent=(select id from permission where code = 'CRM_PURCHASE_ORDER_LIST') and code='CRM_PURCHASE_ORDER_ADD';
update permission set  name='Edit' where parent=(select id from permission where code = 'CRM_PURCHASE_ORDER_LIST') and code='CRM_PURCHASE_ORDER_EDIT';
update permission set  name='Delete' where parent=(select id from permission where code = 'CRM_PURCHASE_ORDER_LIST') and code='CRM_PURCHASE_ORDER_DELETE';
update permission set  name='Summary' where parent=(select id from permission where code = 'CRM_PURCHASE_ORDER_LIST') and code='CRM_PURCHASE_ORDER_SUMMARY';
update permission set  name='PDF' where parent=(select id from permission where code = 'CRM_PURCHASE_ORDER_LIST') and code='CRM_PURCHASE_ORDER_PDF';


update permission set  name='Add' where parent=(select id from permission where code = 'CRM_PURCHASE_INVOICE_LIST') and code='CRM_PURCHASE_INVOICE_ADD';
update permission set  name='Edit' where parent=(select id from permission where code = 'CRM_PURCHASE_INVOICE_LIST') and code='CRM_PURCHASE_INVOICE_EDIT';
update permission set  name='Delete' where parent=(select id from permission where code = 'CRM_PURCHASE_INVOICE_LIST') and code='CRM_PURCHASE_INVOICE_DELETE';
update permission set  name='Summary' where parent=(select id from permission where code = 'CRM_PURCHASE_INVOICE_LIST') and code='CRM_PURCHASE_INVOICE_SUMMARY';
update permission set  name='PDF' where parent=(select id from permission where code = 'CRM_PURCHASE_INVOICE_LIST') and code='CRM_PURCHASE_INVOICE_PDF';


delete from permission where parent=(select id from permission where code = 'CRM_REQUEST_FOR_QUOTE_LIST');
delete from permission where code='CRM_REQUEST_FOR_QUOTE_LIST';

insert into permission (code, context, name, sorder, parent, modulecode)
values ('CRM_REQUEST_FOR_QUOTE_LIST', 'CRM', 'Request For Quote List', 14,
        (select id from permission where code = 'CRM_SALES_TAB'),'CRM_MODULE');

delete from "anv".permission_context where permissioncode = 'CRM_REQUEST_FOR_QUOTE_LIST';
insert into "anv".permission_context (permissioncode, contextcode) values ('CRM_REQUEST_FOR_QUOTE_LIST', 'CRM');


insert into permission (code, context, name, sorder, parent, modulecode) values
('CRM_REQUEST_FOR_QUOTE_ADD', 'CRM', 'Add', 1, (select id from permission where code = 'CRM_REQUEST_FOR_QUOTE_LIST'),'CRM_MODULE'),
('CRM_REQUEST_FOR_QUOTE_EDIT', 'CRM', 'Edit', 2, (select id from permission where code = 'CRM_REQUEST_FOR_QUOTE_LIST'),'CRM_MODULE'),
('CRM_REQUEST_FOR_QUOTE_DELETE', 'CRM', 'Delete', 3, (select id from permission where code = 'CRM_REQUEST_FOR_QUOTE_LIST'),'CRM_MODULE'),
('CRM_REQUEST_FOR_QUOTE_SUMMARY', 'CRM', 'Summary', 4, (select id from permission where code = 'CRM_REQUEST_FOR_QUOTE_LIST'),'CRM_MODULE'),
('CRM_REQUEST_FOR_QUOTE_PDF', 'CRM', 'PDF', 5, (select id from permission where code = 'CRM_REQUEST_FOR_QUOTE_LIST'),'CRM_MODULE');

insert into "anv".permission_context (permissioncode, contextcode)
values ('CRM_REQUEST_FOR_QUOTE_ADD', 'CRM'),
       ('CRM_REQUEST_FOR_QUOTE_EDIT', 'CRM'),
       ('CRM_REQUEST_FOR_QUOTE_DELETE', 'CRM'),
       ('CRM_REQUEST_FOR_QUOTE_SUMMARY', 'CRM'),
       ('CRM_REQUEST_FOR_QUOTE_PDF', 'CRM');