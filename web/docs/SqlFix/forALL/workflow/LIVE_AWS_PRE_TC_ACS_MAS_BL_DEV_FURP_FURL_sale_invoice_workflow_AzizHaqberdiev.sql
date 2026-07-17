delete from "model"  where formid = 'SALE_INVOICE_FORM';
delete from "modelfield"  where form_id = 'SALE_INVOICE_FORM';
delete from "0".reference  where code = '_WORKFLOW_MODULE_SALE_INVOICE';
insert into "0".reference (code, name, sorder, parentid)
values ('_WORKFLOW_MODULE_SALE_INVOICE', 'Sales Invoice', (select max(sorder) from "0".reference where parentid = (select id from "0".reference where code = '_WORKFLOW_MODULE')),
(select id from "0".reference where code = '_WORKFLOW_MODULE'));

insert into model(active,formID, title,viewname) values(true,'SALE_INVOICE_FORM', 'Sales Invoice Form', 'SaleInvoice');
insert into modelfield(       form_ID,                             field_ID,  sorder,  isCustomField,        widget,                              source,        type,   usableByWorkflow, disableupdate) values
                      ('SALE_INVOICE_FORM',                      'CUSTOMER',       1,          false,      'LOOKUP',    'ACCOUNTING@SALE_QUOTE_CUSTOMER',      'text',               true,          true),
                      ('SALE_INVOICE_FORM',                       'PROJECT',       2,          false,      'LOOKUP',     'ACCOUNTING@SALE_QUOTE_PROJECT',      'text',               true,          true),
                      ('SALE_INVOICE_FORM',                        'STATUS',       3,          false,      'LOOKUP',    'ACCOUNTING@SALE_INVOICE_STATUS',      'text',               true,          true),
                      ('SALE_INVOICE_FORM',                    'START_DATE',       4,          false,  'DatePicker',                                null,      'Date',               true,          true),
                      ('SALE_INVOICE_FORM',                      'DUE_DATE',       5,          false,  'DatePicker',                                null,      'Date',               true,          true),
                      ('SALE_INVOICE_FORM',                  'INVOICE_TYPE',       6,          false,    'DropDown',      'ACCOUNTING@SALE_INVOICE_TYPE',      'text',               true,          true),
                      ('SALE_INVOICE_FORM',                        'NUMBER',       7,          false,     'TextBox',                                null,      'Text',               true,          true),
                      ('SALE_INVOICE_FORM',                     'REFERENCE',       8,          false,     'TextBox',                                null,      'Text',               true,          true),
                      ('SALE_INVOICE_FORM',                        'AMOUNT',       9,          false,    'DropDown',    'ACCOUNTING@SALE_INVOICE_AMOUNT',      'text',               true,          true),
                      ('SALE_INVOICE_FORM',                     'SQ_NUMBER',      10,          false,     'TextBox',                                null,      'Text',               true,          true),
                      ('SALE_INVOICE_FORM',                     'PO_NUMBER',      11,          false,     'TextBox',                                null,      'Text',               true,          true),
                      ('SALE_INVOICE_FORM',                      'SHIP_VIA',      12,          false,      'LOOKUP',    'ACCOUNTING@SALE_QUOTE_SHIP_VIA',      'text',               true,          true),
                      ('SALE_INVOICE_FORM',                  'BANK_ACCOUNT',      13,          false,    'DropDown',           'ACCOUNTING@BANK_ACCOUNT',      'text',               true,          true),
                      ('SALE_INVOICE_FORM',                  'INSTRUCTIONS',      14,          false,    'TextArea',                                null,      'Text',               true,          true),
                      ('SALE_INVOICE_FORM',                  'TOTAL_AMOUNT',      15,          false,     'TextBox',                                null,    'Number',               true,          true),
                      ('SALE_INVOICE_FORM',        'TOTAL_INVOICE_CURRENCY',      16,          false,     'TextBox',                                null,    'Number',               true,          true),
                      ('SALE_INVOICE_FORM',                   'PAID_AMOUNT',      17,          false,     'TextBox',                                null,    'Number',               true,          true),
                      ('SALE_INVOICE_FORM',     'PAID_AMOUNT_BASE_CURRENCY',      18,          false,     'TextBox',                                null,    'Number',               true,          true),
                      ('SALE_INVOICE_FORM',      'DUE_AMOUNT_BASE_CURRENCY',      19,          false,     'TextBox',                                null,    'Number',               true,          true),
                      ('SALE_INVOICE_FORM',   'DUE_AMOUNT_INVOICE_CURRENCY',      20,          false,     'TextBox',                                null,    'Number',               true,          true),
                      ('SALE_INVOICE_FORM',                'TOTAL_DISCOUNT',      21,          false,     'TextBox',                                null,    'Number',               true,          true),
                      ('SALE_INVOICE_FORM',                      'CURRENCY',      22,          false,    'DropDown',               'ACCOUNTING@CURRENCY',      'text',               true,          true);


delete from "anv"."model"  where formid = 'SALE_INVOICE_FORM';
delete from "anv"."modelfield"  where form_id = 'SALE_INVOICE_FORM';

delete from "anv".reference  where code = '_WORKFLOW_MODULE_SALE_INVOICE';
insert into "anv".reference (code, name, sorder, parentid)
values ('_WORKFLOW_MODULE_SALE_INVOICE', 'Sales Invoice', (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = '_WORKFLOW_MODULE')),
(select id from "anv".reference where code = '_WORKFLOW_MODULE'));

UPDATE company SET isdeleted = FALSE WHERE isdeleted IS NULL AND (SELECT "anv".convertCustomFieldsToModelFields('SALE_INVOICE_FORM', 'SaleInvoice')) IS NOT NULL;
