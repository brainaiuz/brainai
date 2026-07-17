---SALES_QUOTE_SEE_OWN
delete from permission where code='SALES_QUOTE_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('SALES_QUOTE_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST')),
                                                                             (select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'),
                                                                             'SALES_QUOTES'
                                                                            );

delete from "anv".rolepermission where permissioncode='SALES_QUOTE_SEE_OWN';
delete from "anv".permission_context where permissioncode='SALES_QUOTE_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('SALES_QUOTE_SEE_OWN','ACCOUNTING');
insert into "anv".permission_context (permissioncode,contextcode) values ('SALES_QUOTE_SEE_OWN','CRM');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('SALES_QUOTE_SEE_OWN','ALLOW','ADMIN');


---SALES_ORDER_SEE_OWN
delete from permission where code='SALES_ORDER_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('SALES_ORDER_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='ACCOUNTING_SALES_ORDER_LIST')),
                                                                             (select id from permission where code='ACCOUNTING_SALES_ORDER_LIST'),
                                                                             'SALES_ORDERS'
                                                                            );

delete from "anv".rolepermission where permissioncode='SALES_ORDER_SEE_OWN';
delete from "anv".permission_context where permissioncode='SALES_ORDER_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('SALES_ORDER_SEE_OWN','ACCOUNTING');
insert into "anv".permission_context (permissioncode,contextcode) values ('SALES_ORDER_SEE_OWN','CRM');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('SALES_ORDER_SEE_OWN','ALLOW','ADMIN');



---SALES_INVOICE_SEE_OWN
delete from permission where code='SALES_INVOICE_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('SALES_INVOICE_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST')),
                                                                             (select id from permission where code='ACCOUNTING_SALES_INVOICE_LIST'),
                                                                             'SALES_INVOICING'
                                                                            );

delete from "anv".rolepermission where permissioncode = 'SALES_INVOICE_SEE_OWN';
delete from "anv".permission_context where permissioncode='SALES_INVOICE_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('SALES_INVOICE_SEE_OWN','ACCOUNTING');
insert into "anv".permission_context (permissioncode,contextcode) values ('SALES_INVOICE_SEE_OWN','CRM');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('SALES_INVOICE_SEE_OWN','ALLOW','ADMIN');


---RFQ_SEE_OWN
delete from permission where code='RFQ_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('RFQ_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='ACCOUNTING_REQUEST_FOR_QUOTE_LIST')),
                                                                             (select id from permission where code='ACCOUNTING_REQUEST_FOR_QUOTE_LIST'),
                                                                             'REQUEST_FOR_QUOTES'
                                                                            );

delete from "anv".rolepermission where permissioncode = 'RFQ_SEE_OWN';
delete from "anv".permission_context where permissioncode='RFQ_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('RFQ_SEE_OWN','ACCOUNTING');
insert into "anv".permission_context (permissioncode,contextcode) values ('RFQ_SEE_OWN','CRM');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('RFQ_SEE_OWN','ALLOW','ADMIN');


---RFP_SEE_OWN
delete from permission where code='RFP_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('RFP_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='ACCOUNTING_REQUEST_FOR_PURCHASE_LIST')),
                                                                             (select id from permission where code='ACCOUNTING_REQUEST_FOR_PURCHASE_LIST'),
                                                                             'REQUEST_FOR_PURCHASES'
                                                                            );

delete from "anv".rolepermission where permissioncode = 'RFP_SEE_OWN';
delete from "anv".permission_context where permissioncode='RFP_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('RFP_SEE_OWN','ACCOUNTING');
insert into "anv".permission_context (permissioncode,contextcode) values ('RFP_SEE_OWN','CRM');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('RFP_SEE_OWN','ALLOW','ADMIN');


---PURCHASE_ORDER_SEE_OWN
delete from permission where code='PURCHASE_ORDER_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('PURCHASE_ORDER_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST')),
                                                                             (select id from permission where code='ACCOUNTING_PURCHASE_ORDER_LIST'),
                                                                             'PURCHASE_ORDERS'
                                                                            );

delete from "anv".rolepermission where permissioncode = 'PURCHASE_ORDER_SEE_OWN';
delete from "anv".permission_context where permissioncode='PURCHASE_ORDER_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('PURCHASE_ORDER_SEE_OWN','ACCOUNTING');
insert into "anv".permission_context (permissioncode,contextcode) values ('PURCHASE_ORDER_SEE_OWN','CRM');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('PURCHASE_ORDER_SEE_OWN','ALLOW','ADMIN');



---PURCHASE_INVOICE_SEE_OWN
delete from permission where code='PURCHASE_INVOICE_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('PURCHASE_INVOICE_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='ACCOUNTING_PURCHASE_INVOICE_LIST')),
                                                                             (select id from permission where code='ACCOUNTING_PURCHASE_INVOICE_LIST'),
                                                                             'PURCHASE_INVOICING'
                                                                            );

delete from "anv".rolepermission where permissioncode = 'PURCHASE_INVOICE_SEE_OWN';
delete from "anv".permission_context where permissioncode='PURCHASE_INVOICE_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('PURCHASE_INVOICE_SEE_OWN','ACCOUNTING');
insert into "anv".permission_context (permissioncode,contextcode) values ('PURCHASE_INVOICE_SEE_OWN','CRM');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('PURCHASE_INVOICE_SEE_OWN','ALLOW','ADMIN');



---EXPENSE_SEE_OWN
delete from permission where code='EXPENSE_SEE_OWN';
insert into permission (code,context,name,sorder,parent,modulecode) values ('EXPENSE_SEE_OWN',
                                                                            'ACCOUNTING',
                                                                            'See Own',
                                                                            (select max(sorder)+1 from permission where parent=(select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST')),
                                                                             (select id from permission where code='ACCOUNTING_EXPENSE_REPORT_LIST'),
                                                                             'EXPENSE_REPORTING'
                                                                            );

delete from "anv".rolepermission where permissioncode = 'EXPENSE_SEE_OWN';
delete from "anv".permission_context where permissioncode='EXPENSE_SEE_OWN';

insert into "anv".permission_context (permissioncode,contextcode) values ('EXPENSE_SEE_OWN','ACCOUNTING');
insert into "anv".permission_context (permissioncode,contextcode) values ('EXPENSE_SEE_OWN','CRM');

insert into "anv".rolepermission (permissioncode,access,rolecode) values ('EXPENSE_SEE_OWN','ALLOW','ADMIN');
