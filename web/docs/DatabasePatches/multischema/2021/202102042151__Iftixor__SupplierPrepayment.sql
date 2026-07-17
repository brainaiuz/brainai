

update "anv".model set viewname = 'Prepayment' where formid = 'PREPAYMENT_FORM' ;

delete from "anv".model where formid = 'SUPPLIER_CREDIT_FORM';
insert into "anv".model (active, formid, title, viewname)  values
(true,  'SUPPLIER_CREDIT_FORM',  'Prepayment View', 'Supplier');


delete from "anv".customformsection where form_id = 'SUPPLIER_CREDIT_FORM';
insert into "anv".customformsection
(form_id,            section,     sorder, expanded) values
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',   1, true ),
('SUPPLIER_CREDIT_FORM',	  'INVOICE_DETAILS',   2,true ),
('SUPPLIER_CREDIT_FORM',	  'REFUND_DETAILS', 3, true );

delete from "anv".customformsection where form_id = 'PREPAYMENT_FORM';
insert into "anv".customformsection
(form_id,            section,     sorder, expanded) values
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',   1, true ),
('PREPAYMENT_FORM',	  'INVOICE_DETAILS',   2, true),
('PREPAYMENT_FORM',	  'REFUND_DETAILS', 3, true );


delete from "anv".modelfield where form_id = 'SUPPLIER_CREDIT_FORM' ;

insert into "anv".modelfield
(form_id,             fsection,                     section,                    fieldstyle,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id) values
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_1',	         true,         '',             'LOOKUP',       1,	       'CRM_ACCOUNT_LOOKUP'),
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_1',	         true,         '',             'LOOKUP',       2,   	   'PAYMENT_ACCOUNT_LOOKUP'),
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_1',	         false,        '',             'TextBox',      3,	       'REFERENCE'),
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_2',	         true,         '',             'DatePicker',   4,	       'DATE_FIELD'),
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_2',	         true,         '',             'InputGroup',   5,	       'PREPAYMENT_AMOUNT'),
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      true,    'COL_2',	         false,        '',             'LOOKUP',       6,	       'DEPARTMENT'),
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_3',	         false,        '',             'TextBox',      7, 	     'PREPAYMENT_NUMBER'),
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_2',	         false,        '',             'LOOKUP',       8,	       'ACCOUNTS_RECEIVABLE_PAYABLE'),
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_3',	         false,        '',             'LOOKUP',       9,	       'SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP'),
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_3',	         false,        '',             'LOOKUP',       10,	     'PROJECT'),
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      true,    'COL_1',	         false,        '',             'KpiSwitcher',  11,	     'POST_DATE'),
('SUPPLIER_CREDIT_FORM',	  'PREPAYMENT_FORM_TITLE',  'PREPAYMENT_FORM_TITLE',    'field',      false,    'COL_1',	         false,        '',      'DynamicTable', 12,	     'PAYMENT_TABLE');


delete from "anv".modelfield where form_id='PREPAYMENT_FORM' and field_id='PAYMENT_TABLE';
insert into "anv".modelfield
(form_id,             fsection,                     section,                    fieldstyle,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id) values
('PREPAYMENT_FORM',	  'INVOICE_DETAILS',      'INVOICE_DETAILS',    'field',      false,   'COL_1',	         true,         '',             'DynamicTable',       12,	       'PAYMENT_TABLE');


delete from "anv".modelfield where form_id='SUPPLIER_CREDIT_FORM' and field_id='REFUND_TABLE';
insert into "anv".modelfield (form_id,             fsection,                     section,                    fieldstyle,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id)
values ('SUPPLIER_CREDIT_FORM',	  'REFUND_DETAILS',      'REFUND_DETAILS',    'field',      false,   'COL_1',	         true,         '',             'DynamicTable',       12,	       'REFUND_TABLE');

delete from "anv".modelfield where form_id='PREPAYMENT_FORM' and field_id='REFUND_TABLE';
insert into "anv".modelfield
(form_id,             fsection,                     section,                    fieldstyle,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id) values
('PREPAYMENT_FORM',	  'REFUND_DETAILS',      'REFUND_DETAILS',    'field',      false,   'COL_1',	         true,         '',             'DynamicTable',       12,	       'REFUND_TABLE');