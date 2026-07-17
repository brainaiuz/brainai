

delete from "anv".model where formid = 'CUSTOMER_REFUND';
insert into "anv".model (active, formid, title, viewname)  values
(true,  'CUSTOMER_REFUND',  'Customer Prepayment Refund', null);


delete from "anv".customformsection where form_id = 'CUSTOMER_REFUND';
insert into "anv".customformsection
(form_id,section,sorder,expanded) values
('CUSTOMER_REFUND',	  'PREPAYMENT_FORM_TITLE',   0, true ),
('CUSTOMER_REFUND',	  'ITEMS_TABLE',   1, true );


delete from "anv".modelfield where form_id = 'CUSTOMER_REFUND' ;
insert into "anv".modelfield
(form_id,             fsection,                     section,                    fieldstyle, fullwidth,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id) values
('CUSTOMER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_1',	         true,         '',             'LOOKUP',       1,	       'CRM_ACCOUNT_LOOKUP'),
('CUSTOMER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_1',	         true,        '',             'LOOKUP',       2,   	     'BANK_ACCOUNT'),
('CUSTOMER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_1',	         false,        '',             'TextBox',       3,   	    'REFERENCE'),
('CUSTOMER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_2',	         true,         '',             'DatePicker',   1,	       'DATE_FIELD'),
('CUSTOMER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_2',	         true,         '',             'UNKNOWN',      2,	       'CURRENCY'),
('CUSTOMER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_3',	         true,        '',             'TextBox',      1, 	       'NUMBER'),
('CUSTOMER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_3',	         false,         '',             'UNKNOWN',      2,	       'TYPE'),
('CUSTOMER_REFUND',	  'ITEMS_TABLE',      'ITEMS_TABLE',    'field',    true ,      false,   'COL_1',	        false,          '',             'UNKNOWN',        4,	       'ITEMS_TABLE');



delete from "anv".model where formid = 'SUPPLIER_REFUND';
insert into "anv".model (active, formid, title, viewname)  values
(true,  'SUPPLIER_REFUND',  'Customer Prepayment Refund', null);


delete from "anv".customformsection where form_id = 'SUPPLIER_REFUND';
insert into "anv".customformsection
(form_id,section,sorder,expanded) values
('SUPPLIER_REFUND',	  'PREPAYMENT_FORM_TITLE',   0, true ),
('SUPPLIER_REFUND',	  'ITEMS_TABLE',   1, true );


delete from "anv".modelfield where form_id = 'SUPPLIER_REFUND' ;
insert into "anv".modelfield
(form_id,             fsection,                     section,                    fieldstyle, fullwidth,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id) values
('SUPPLIER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_1',	         true,         '',             'LOOKUP',       1,	       'CRM_ACCOUNT_LOOKUP'),
('SUPPLIER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_1',	         true,        '',             'LOOKUP',       2,   	     'BANK_ACCOUNT'),
('SUPPLIER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_1',	         false,        '',             'TextBox',       3,   	   'REFERENCE'),
('SUPPLIER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_2',	         true,         '',             'DatePicker',   1,	       'DATE_FIELD'),
('SUPPLIER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_2',	         true,         '',             'UNKNOWN',      2,	       'CURRENCY'),
('SUPPLIER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_3',	         true,        '',             'TextBox',      1, 	       'NUMBER'),
('SUPPLIER_REFUND',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',    false,      false,   'COL_3',	         false,         '',             'UNKNOWN',      2,	       'TYPE'),
('SUPPLIER_REFUND',	  'ITEMS_TABLE',      'ITEMS_TABLE',    'field',    true ,      false,   'COL_1',	        false,          '',             'UNKNOWN',        4,	       'ITEMS_TABLE');
