

delete from "anv".model where formid = 'PREPAYMENT_FORM';
insert into "anv".model (active, formid, title, viewname)  values
(true,  'PREPAYMENT_FORM',  'Prepayment View', 'PrepaymentView');


delete from "anv".customformsection where form_id = 'PREPAYMENT_FORM';
insert into "anv".customformsection
(form_id,            section,     sorder) values
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',   1);


delete from "anv".modelfield where form_id = 'PREPAYMENT_FORM' ;
insert into "anv".modelfield
(form_id,             fsection,                     section,                    fieldstyle,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id) values
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_1',	         true,         '',             'LOOKUP',       1,	       'CRM_ACCOUNT_LOOKUP'),
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_1',	         true,         '',             'LOOKUP',       2,   	   'PAYMENT_ACCOUNT_LOOKUP'),
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_1',	         false,        '',             'TextBox',      3,	       'REFERENCE'),
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_2',	         true,         '',             'DatePicker',   4,	       'DATE_FIELD'),
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_2',	         true,         '',             'InputGroup',   5,	       'PREPAYMENT_AMOUNT'),
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      true,    'COL_2',	         false,        '',             'LOOKUP',       6,	       'DEPARTMENT'),
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_3',	         false,        '',             'TextBox',      7, 	     'PREPAYMENT_NUMBER'),
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_2',	         false,        '',             'LOOKUP',       8,	       'ACCOUNTS_RECEIVABLE_PAYABLE'),
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_3',	         false,        '',             'LOOKUP',       9,	       'SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP'),
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      false,   'COL_3',	         false,        '',             'LOOKUP',       10,	     'PROJECT'),
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      true,    'COL_1',	         false,        '',             'KpiSwitcher',  11,	     'POST_DATE');
