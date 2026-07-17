
update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),name='Add' where code='ACCOUNTING_SALES_ORDER_ADD';
update permission set  sorder=2, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),name='Edit' where code='ACCOUNTING_SALES_ORDER_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),name='Summary' where code='ACCOUNTING_SALES_ORDER_SUMMARY';
update permission set  sorder=4, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),name='Delete' where code='ACCOUNTING_SALES_ORDER_DELETE';
update permission set  sorder=5, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),name='Pick List' where code='ACCOUNTING_SALES_ORDER_PICKLIST';
update permission set  sorder=6, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),name='Copy To PO' where code='ACCOUNTING_SALES_ORDER_COPYTOPO';
update permission set  sorder=7, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),name='Copy To SQ' where code='ACCOUNTING_SALES_ORDER_COPYTOSQ';
update permission set  sorder=9, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),name='Convert To Project' where code='ACCOUNTING_CONVERT_TO_PROJECT';
update permission set  sorder=10, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),name='PDF' where code='ACCOUNTING_SALES_ORDER_PDF';
update permission set  sorder=11, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),name='GDN Delete' where code='ACCOUNTING_GDN_DELETE';
update permission set  sorder=12, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),name='Full list access' where code='ACCOUNTING_SALES_ORDER_FULL_LIST_ACCESS';
update permission set  sorder=13, parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),name='Full edit access' where code='ACCOUNTING_SALES_ORDER_FULL_EDIT_ACCESS';



delete from permission where code='ACCOUNTING_SALES_ORDER_COPYTOSO';
insert into permission (code, context, name, sorder, parent, modulecode) values
        ('ACCOUNTING_SALES_ORDER_COPYTOSO', 'ACCOUNTING', 'Copy to SO', 8,(select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'),'SALES_ORDERS');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_ORDER_COPYTOSO';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SALES_ORDER_COPYTOSO', 'ACCOUNTING'),
                                                                          ('ACCOUNTING_SALES_ORDER_COPYTOSO', 'CRM');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_SALES_ORDER_COPYTOSO';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_SALES_ORDER_COPYTOSO', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_SALES_ORDER_COPYTOSO', 'ALLOW', 'ACCOUNTANT');




update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'),name='Add' where code='ACCOUNTING_SALES_QUOTE_ADD';
update permission set  sorder=2, parent=(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'),name='Edit' where code='ACCOUNTING_SALES_QUOTE_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'),name='Summary' where code='ACCOUNTING_SALES_QUOTE_SUMMARY';
update permission set  sorder=4, parent=(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'),name='Delete' where code='ACCOUNTING_SALES_QUOTE_DELETE';
update permission set  sorder=5, parent=(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'),name='PDF' where code='ACCOUNTING_SALES_QUOTE_PDF';
update permission set  sorder=6, parent=(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'),name='Approve Or Reject' where code='ACCOUNTING_CAN_APPROVE_SALES_QUOTE';
update permission set  sorder=7, parent=(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'),name='Copy To SQ' where code='ACCOUNTING_SALES_QUOTE_COPY';
update permission set  sorder=8, parent=(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'),name='Copy To SO' where code='CONVERT_SALE_QUOTE_TO_SALE_ORDER';
update permission set  sorder=9, parent=(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'),name='Copy To SI' where code='CONVERT_SALE_QUOTE_TO_SALE_INVOICE';
update permission set  sorder=11, parent=(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'),name='Full list access' where code='ACCOUNTING_SALES_QUOTE_FULL_LIST_ACCESS';
update permission set  sorder=12, parent=(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'),name='Full edit access' where code='ACCOUNTING_SALES_QUOTE_FULL_EDIT_ACCESS';



delete from permission where code='CONVERT_SALE_QUOTE_TO_PURCHASE_ORDER';
insert into permission (code, context, name, sorder, parent, modulecode) values
('CONVERT_SALE_QUOTE_TO_PURCHASE_ORDER', 'ACCOUNTING', 'Copy to PO', 10,(select id from permission where code = 'ACCOUNTING_SALES_QUOTE_LIST'),'PURCHASE_ORDERS');

delete from "anv".permission_context where permissioncode = 'CONVERT_SALE_QUOTE_TO_PURCHASE_ORDER';
insert into "anv".permission_context (permissioncode, contextcode) values ('CONVERT_SALE_QUOTE_TO_PURCHASE_ORDER', 'ACCOUNTING'),
                                                                             ('CONVERT_SALE_QUOTE_TO_PURCHASE_ORDER', 'CRM');

delete from "anv".rolepermission where permissioncode = 'CONVERT_SALE_QUOTE_TO_PURCHASE_ORDER';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CONVERT_SALE_QUOTE_TO_PURCHASE_ORDER', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('CONVERT_SALE_QUOTE_TO_PURCHASE_ORDER', 'ALLOW', 'ACCOUNTANT');





update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Add' where code='ACCOUNTING_SALES_INVOICE_ADD';
update permission set  sorder=2, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Edit' where code='ACCOUNTING_SALES_INVOICE_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Summary' where code='ACCOUNTING_SALES_INVOICE_SUMMARY';
update permission set  sorder=4, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Delete' where code='ACCOUNTING_SALES_INVOICE_DELETE';
update permission set  sorder=5, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='PDF' where code='ACCOUNTING_SALES_INVOICE_PDF';
update permission set  sorder=6, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Void' where code='ACCOUNTING_SALES_INVOICE_VOID';
update permission set  sorder=7, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Credit Note Add' where code='ACCOUNTING_SALES_CREDIT_NOTE_ADD';
update permission set  sorder=8, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Unsaved Invoice Pdf Version' where code='ACCOUNTING_UNSAVED_SALES_INVOICE_PDF';
update permission set  sorder=9, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Copy To SI' where code='ACCOUNTING_SALES_INVOICE_COPY';
update permission set  sorder=10, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Copy To PO' where code='ACCOUNTING_SALES_INVOICE_COPYTOPO';
update permission set  sorder=11, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Full list access' where code='ACCOUNTING_SALES_INVOICE_FULL_LIST_ACCESS';
update permission set  sorder=12, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Full edit access' where code='ACCOUNTING_SALES_INVOICE_FULL_EDIT_ACCESS';
update permission set  sorder=13, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Note Edit' where code='ACCOUNTING_SALES_CREDIT_NOTE_EDIT';
update permission set  sorder=14, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Credit Note Full Edit Access' where code='ACCOUNTING_SALES_CREDIT_NOTE_FULL_EDIT_ACCESS';
update permission set  sorder=15, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Draft' where code='ACCOUNTING_SI_AND_PI_DRAFT_BUTTON';
update permission set  sorder=16, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Credit/Debit Note Approve' where code='CREDIT_NOTE_APPROVE';
update permission set  sorder=17, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Save & Approve' where code='ACCOUNTING_SI_SAVE_APPROVE';
update permission set  sorder=18, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Approve & Sent Email' where code='ACCOUNTING_SI_APPROVE_SENT';
update permission set  sorder=19, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Credit/Debit Note Draft' where code='CREDIT_NOTE_DRAFT';
update permission set  sorder=120, parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),name='Client link clickable' where code='CUSTOMER_CLICKABLE';











update permission set  sorder=1, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Add' where code='ACCOUNTING_PURCHASE_ORDER_ADD';
update permission set  sorder=2, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Edit' where code='ACCOUNTING_PURCHASE_ORDER_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Summary' where code='ACCOUNTING_PURCHASE_ORDER_SUMMARY';
update permission set  sorder=4, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Delete' where code='ACCOUNTING_PURCHASE_ORDER_DELETE';
update permission set  sorder=5, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='PDF' where code='ACCOUNTING_PURCHASE_ORDER_PDF';
update permission set  sorder=6, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Copy To PO' where code='ACCOUNTING_PURCHASE_ORDER_COPY';
update permission set  sorder=9, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Approve and Send' where code='ACCOUNTING_PURCHASE_APPROVE_AND_SEND_BUTTON';
update permission set  sorder=10, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Save and Approve' where code='ACCOUNTING_PURCHASE_SAVE_AND_APPROVE_BUTTON';
update permission set  sorder=11, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Mark As Open' where code='ACCOUNTING_PURCHASE_ORDER_MARK_AS_OPEN_BUTTON';
update permission set  sorder=12, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Ignore Manager Approval' where code='ACCOUNTING_PURCHASE_ORDER_IGNORE_MANAGER_APPROVAL';
update permission set  sorder=13, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Purchase Order Receive' where code='ACCOUNTING_PURCHASE_ORDER_RECEIVE';
update permission set  sorder=14, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Can Approve Purchase Order' where code='ACCOUNTING_CAN_APPROVE_PURCHASE_ORDER';
update permission set  sorder=15, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Full list access' where code='ACCOUNTING_PURCHASE_ORDER_FULL_LIST_ACCESS';
update permission set  sorder=16, parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),name='Full edit access' where code='ACCOUNTING_PURCHASE_ORDER_FULL_EDIT_ACCESS';



delete from permission where code='ACCOUNTING_PURCHASE_ORDER_COPY_SC';
delete from permission where code='ACCOUNTING_PURCHASE_ORDER_COPY_SQ';
delete from permission where code='ACCOUNTING_PURCHASE_ORDER_COPY_PI';
insert into permission (code, context, name, sorder, parent, modulecode) values
('ACCOUNTING_PURCHASE_ORDER_COPY_SQ', 'ACCOUNTING', 'Copy to SQ', 7,(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),'PURCHASE_ORDERS'),
('ACCOUNTING_PURCHASE_ORDER_COPY_PI', 'ACCOUNTING', 'Copy to PI', 8,(select id from permission where code = 'ACCOUNTING_PURCHASE_ORDER_LIST'),'PURCHASE_ORDERS');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_COPY_SQ';
delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_COPY_PI';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PURCHASE_ORDER_COPY_PI', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_COPY_PI', 'CRM'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_COPY_SQ', 'ACCOUNTING'),
                                                                             ('ACCOUNTING_PURCHASE_ORDER_COPY_SQ', 'CRM');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_COPY_PI';
delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_COPY_SQ';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('ACCOUNTING_PURCHASE_ORDER_COPY_SQ', 'ALLOW', 'DR'),
                                                                              ('ACCOUNTING_PURCHASE_ORDER_COPY_SQ', 'ALLOW', 'ACCOUNTANT'),
                                                                              ('ACCOUNTING_PURCHASE_ORDER_COPY_PI', 'ALLOW', 'DR'),
                                                                              ('ACCOUNTING_PURCHASE_ORDER_COPY_PI', 'ALLOW', 'ACCOUNTANT');






update permission set  sorder=1, parent=(select id from permission where code='LOGISTICS_PURCHASE_ORDER_LIST'),name='Add' where code='LOGISTICS_PURCHASE_ORDER_ADD';
update permission set  sorder=2, parent=(select id from permission where code='LOGISTICS_PURCHASE_ORDER_LIST'),name='Edit' where code='LOGISTICS_PURCHASE_ORDER_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='LOGISTICS_PURCHASE_ORDER_LIST'),name='Summary' where code='LOGISTICS_PURCHASE_ORDER_SUMMARY';
update permission set  sorder=4, parent=(select id from permission where code='LOGISTICS_PURCHASE_ORDER_LIST'),name='Delete' where code='LOGISTICS_PURCHASE_ORDER_DELETE';
update permission set  sorder=6, parent=(select id from permission where code='LOGISTICS_PURCHASE_ORDER_LIST'),name='Copy To PO' where code='LOGISTICS_PURCHASE_ORDER_COPY';
update permission set  sorder=8, parent=(select id from permission where code='LOGISTICS_PURCHASE_ORDER_LIST'),name='Approve and Send' where code='LOGISTICS_PURCHASE_APPROVE_AND_SEND_BUTTON';
update permission set  sorder=9, parent=(select id from permission where code='LOGISTICS_PURCHASE_ORDER_LIST'),name='Save and Approve' where code='LOGISTICS_PURCHASE_ORDER_MARK_AS_OPEN_BUTTON';
update permission set  sorder=10, parent=(select id from permission where code='LOGISTICS_PURCHASE_ORDER_LIST'),name='Mark As Open' where code='LOGISTICS_PURCHASE_ORDER_MARK_AS_OPEN_BUTTON';
update permission set  sorder=11, parent=(select id from permission where code='LOGISTICS_PURCHASE_ORDER_LIST'),name='Purchase Order Receive' where code='LOGISTICS_PURCHASE_ORDER_RECEIVE';
update permission set  sorder=12, parent=(select id from permission where code='LOGISTICS_PURCHASE_ORDER_LIST'),name='Full list access' where code='LOGISTICS_PURCHASE_ORDER_FULL_LIST_ACCESS';
update permission set  sorder=13, parent=(select id from permission where code='LOGISTICS_PURCHASE_ORDER_LIST'),name='Full edit access' where code='LOGISTICS_PURCHASE_ORDER_FULL_EDIT_ACCESS';



delete from permission where code='LOGISTICS_PURCHASE_ORDER_COPY_SQ';
delete from permission where code='LOGISTICS_PURCHASE_ORDER_COPY_PI';
insert into permission (code, context, name, sorder, parent, modulecode) values
('LOGISTICS_PURCHASE_ORDER_COPY_SQ', 'ACCOUNTING', 'Copy to SQ', 7,(select id from permission where code = 'LOGISTICS_PURCHASE_ORDER_LIST'),'PURCHASE_ORDERS'),
('LOGISTICS_PURCHASE_ORDER_COPY_PI', 'ACCOUNTING', 'Copy to PI', 8,(select id from permission where code = 'LOGISTICS_PURCHASE_ORDER_LIST'),'PURCHASE_ORDERS');

delete from "anv".permission_context where permissioncode = 'LOGISTICS_PURCHASE_ORDER_COPY_SQ';
delete from "anv".permission_context where permissioncode = 'LOGISTICS_PURCHASE_ORDER_COPY_PI';
insert into "anv".permission_context (permissioncode, contextcode) values ('LOGISTICS_PURCHASE_ORDER_COPY_PI', 'ACCOUNTING'),
                                                                             ('LOGISTICS_PURCHASE_ORDER_COPY_PI', 'CRM'),
                                                                             ('LOGISTICS_PURCHASE_ORDER_COPY_SQ', 'ACCOUNTING'),
                                                                             ('LOGISTICS_PURCHASE_ORDER_COPY_SQ', 'CRM');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_COPY_PI';
delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_COPY_SQ';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('LOGISTICS_PURCHASE_ORDER_COPY_SQ', 'ALLOW', 'DR'),
                                                                              ('LOGISTICS_PURCHASE_ORDER_COPY_SQ', 'ALLOW', 'ACCOUNTANT'),
                                                                              ('LOGISTICS_PURCHASE_ORDER_COPY_PI', 'ALLOW', 'DR'),
                                                                              ('LOGISTICS_PURCHASE_ORDER_COPY_PI', 'ALLOW', 'ACCOUNTANT');
