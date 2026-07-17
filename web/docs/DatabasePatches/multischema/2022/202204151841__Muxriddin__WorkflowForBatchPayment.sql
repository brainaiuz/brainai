delete from "anv".model  where formid = 'BATCH_PAYMENT_FORM';
delete from "anv".modelfield  where form_id = 'BATCH_PAYMENT_FORM';
delete from "anv".model  where formid = 'RECEIVE_PAYMENT_FORM';
delete from "anv".modelfield  where form_id = 'RECEIVE_PAYMENT_FORM';
delete from "anv".model  where formid = 'PAY_INVOICE_FORM';
delete from "anv".modelfield  where form_id = 'PAY_INVOICE_FORM';
delete from "anv".reference  where code = '_WORKFLOW_MODULE_RECEIVE_PAYMENT';
delete from "anv".reference  where code = '_WORKFLOW_MODULE_PAY_INVOICE';
insert into "anv".reference (code, name, sorder, parentid)
values ('_WORKFLOW_MODULE_RECEIVE_PAYMENT', 'Receive Payment', (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = '_WORKFLOW_MODULE')),
(select id from "anv".reference where code = '_WORKFLOW_MODULE')),
('_WORKFLOW_MODULE_PAY_INVOICE', 'Pay Invoice', (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = '_WORKFLOW_MODULE')),
(select id from "anv".reference where code = '_WORKFLOW_MODULE'));

insert into "anv".model(active,formID, title,viewname) values(true,'RECEIVE_PAYMENT_FORM', 'Receive Payment Form', 'BatchInvoicePaymentView');
insert into "anv".model(active,formID, title,viewname) values(true,'PAY_INVOICE_FORM', 'Pay Invoice Form', 'BatchPayBillView');
insert into "anv".modelfield(         form_ID,                       field_ID,  sorder,  isCustomField,        widget,                                 source,        type,   usableByWorkflow, disableupdate) values
                      ('RECEIVE_PAYMENT_FORM',                     'CUSTOMER',       1,          false,      'LOOKUP',          'ACCOUNTING@PAYMENT_CUSTOMER',      'Text',               true,          true),
                      ('RECEIVE_PAYMENT_FORM',                 'PAYMENT_DATE',       2,          false,  'DatePicker',                                   null,      'Date',               true,          true),
                      ('RECEIVE_PAYMENT_FORM',                       'NUMBER',       3,          false,     'TextBox',                                   null,      'Text',               true,          true),
                      ('RECEIVE_PAYMENT_FORM',                      'ACCOUNT',       4,          false,      'LOOKUP',           'ACCOUNTING@PAYMENT_ACCOUNT',      'Text',               true,          true),
                      ('RECEIVE_PAYMENT_FORM',                       'AMOUNT',       5,          false,     'TextBox',                                   null,      'Text',               true,          true),
                      ('RECEIVE_PAYMENT_FORM',                 'PAYMENT_TYPE',       6,          false,     'UNKNOWN',                                   null,      'Text',               true,          true),
                      ('RECEIVE_PAYMENT_FORM',                     'CURRENCY',       7,          false,    'DropDown',                  'ACCOUNTING@CURRENCY',      'Text',               true,          true),
                      ('RECEIVE_PAYMENT_FORM',                    'REFERENCE',       8,          false,     'TextBox',                                   null,      'Text',               true,          true),
                      ('RECEIVE_PAYMENT_FORM',                   'DEPARTMENT',       9,          false,      'LOOKUP',        'ACCOUNTING@PAYMENT_DEPARTMENT',      'Text',               true,          true);

insert into "anv".modelfield(     form_ID,                       field_ID,  sorder,  isCustomField,        widget,                                 source,        type,   usableByWorkflow, disableupdate) values
                      ('PAY_INVOICE_FORM',                     'SUPPLIER',       1,          false,      'LOOKUP',              'ACCOUNTING@PAY_CUSTOMER',      'Text',               true,          true),
                      ('PAY_INVOICE_FORM',                 'PAYMENT_DATE',       2,          false,  'DatePicker',                                   null,      'Date',               true,          true),
                      ('PAY_INVOICE_FORM',                       'NUMBER',       3,          false,     'TextBox',                                   null,      'Text',               true,          true),
                      ('PAY_INVOICE_FORM',                      'ACCOUNT',       4,          false,      'LOOKUP',               'ACCOUNTING@PAY_ACCOUNT',      'Text',               true,          true),
                      ('PAY_INVOICE_FORM',                       'AMOUNT',       5,          false,     'TextBox',                                   null,      'Text',               true,          true),
                      ('PAY_INVOICE_FORM',                 'PAYMENT_TYPE',       6,          false,     'UNKNOWN',                                   null,      'Text',               true,          true),
                      ('PAY_INVOICE_FORM',                     'CURRENCY',       7,          false,    'DropDown',                  'ACCOUNTING@CURRENCY',      'Text',               true,          true),
                      ('PAY_INVOICE_FORM',                    'REFERENCE',       8,          false,     'TextBox',                                   null,      'Text',               true,          true),
                      ('PAY_INVOICE_FORM',                   'DEPARTMENT',       9,          false,      'LOOKUP',            'ACCOUNTING@PAY_DEPARTMENT',      'Text',               true,          true);
