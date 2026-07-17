
update permission set  sorder=1, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='See All' where code='CRM_SEE_ALL_OPPORTUNITIES_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Add' where code='CRM_ADD_NEW_OPPORTUNITIES';
update permission set  sorder=3, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Quick Add', modulecode = 'OPPORTUNITY_TRACKING' where code='CRM_QUICK_ADD_NEW_OPPORTUNITIES';
update permission set  sorder=4, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Edit' where code='CRM_EDIT_OPPORTUNITIES';
update permission set  sorder=5, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Copy' where code='CRM_COPY_OPPORTUNITIES';
update permission set  sorder=6, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Delete' where code='CRM_REMOVE_OPPORTUNITIES';
update permission set  sorder=7, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Add Note' where code='CRM_ADD_OPPORTUNITY_NOTE';
update permission set  sorder=8, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Change Stage' where code='CRM_OPPORTUNITY_CHANGE_STAGE';
update permission set  sorder=9, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Change Campaign' where code='CHANGE_OPPORTUNITIES_CAMPAIGN';
update permission set  sorder=10, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to SQ' where code='CONVERT_OPPORTUNITY_TO_SQ';
update permission set  sorder=11, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to SO' where code='CONVERT_OPPORTUNITY_TO_SO';
update permission set  sorder=12, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to RFQ' where code='CONVERT_OPPORTUNITY_TO_RFQ';
update permission set  sorder=13, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to PO' where code='CONVERT_OPPORTUNITY_TO_PO';
update permission set  sorder=16, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Convert to Project' where code='CONVERT_OPPORTUNITY_TO_PROJECT';
update permission set  sorder=17, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Import' where code='CRM_OPPORTUNITIES_IMPORT_LIST';
update permission set  sorder=18, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Export' where code='CRM_OPPORTUNITIES_EXPORT_LIST';
update permission set  sorder=19, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='Expense Claim' where code='CRM_OPPORTUNITIES_EXPENSE_CLAIM_LIST';
update permission set  sorder=20, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='RFQ' where code='CRM_OPPORTUNITIES_RFQ_LIST';
update permission set  sorder=21, parent=(select id from permission where code='CRM_OPPORTUNITIES_LIST'),name='PDF' where code='CRM_OPPORTUNITY_PDF';

delete from permission where code='CONVERT_OPPORTUNITY_TO_SI';
delete from permission where code='CONVERT_OPPORTUNITY_TO_PI';
insert into permission (code, context, name, sorder, parent, modulecode) values
        ('CONVERT_OPPORTUNITY_TO_SI', 'CRM', 'Convert to SI', 14,(select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),'SALES_INVOICING'),
        ('CONVERT_OPPORTUNITY_TO_PI', 'CRM', 'Convert to PI', 15,(select id from permission where code = 'CRM_OPPORTUNITIES_LIST'),'PURCHASE_INVOICING');

delete from "anv".permission_context where permissioncode = 'CONVERT_OPPORTUNITY_TO_SI';
delete from "anv".permission_context where permissioncode = 'CONVERT_OPPORTUNITY_TO_PI';
delete from "anv".permission_context where permissioncode = 'CONVERT_OPPORTUNITY_TO_PO';
delete from "anv".permission_context where permissioncode = 'CRM_QUICK_ADD_NEW_OPPORTUNITIES';
insert into "anv".permission_context (permissioncode, contextcode) values ('CONVERT_OPPORTUNITY_TO_PI', 'CRM'),
                                                                         ('CONVERT_OPPORTUNITY_TO_SI', 'CRM'),
                                                                          ('CONVERT_OPPORTUNITY_TO_PO', 'CRM'),
                                                                          ('CRM_QUICK_ADD_NEW_OPPORTUNITIES', 'CRM');


delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PURCHASE_ORDER_ADD', 'ACCOUNTING'),
                                                                         ('ACCOUNTING_PURCHASE_ORDER_ADD', 'PM'),
                                                                          ('ACCOUNTING_PURCHASE_ORDER_ADD', 'HRMS'),
                                                                          ('ACCOUNTING_PURCHASE_ORDER_ADD', 'CRM');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_INVOICE_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_PURCHASE_INVOICE_ADD', 'ACCOUNTING'),
                                                                            ('ACCOUNTING_PURCHASE_INVOICE_ADD', 'PM'),
                                                                            ('ACCOUNTING_PURCHASE_INVOICE_ADD', 'HRMS'),
                                                                            ('ACCOUNTING_PURCHASE_INVOICE_ADD', 'CRM');