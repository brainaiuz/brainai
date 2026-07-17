
delete from "anv".model where formid = 'INVOICE_PAYMENT_VIEW';
insert into "anv".model (active, formid, title, viewname)  values
(true,  'INVOICE_PAYMENT_VIEW',  'Prepayment Summary View', 'PrepaymentSummaryView');


delete from "anv".customformsection where form_id = 'INVOICE_PAYMENT_VIEW';
insert into "anv".customformsection
(form_id,            section,     sorder) values
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',   1),
('INVOICE_PAYMENT_VIEW',	  'INVOICE_DETAILS',   2);


delete from "anv".modelfield where form_id = 'INVOICE_PAYMENT_VIEW' ;
insert into "anv".modelfield
(form_id,                   fsection,               section,          fieldstyle,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id) values
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',      'INFORMATION',        'field',      false,    'COL_1',	         true,         '',     'HTML',         1,	     'CUSTOMER_SUPPLIER'),
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',      'INFORMATION',        'field',      false,    'COL_1',	         true,         '',     'HTML',         2,	     'ACCOUNT_NAME'),
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',      'INFORMATION',        'field',      false,    'COL_1',	         false,        '',     'HTML',         3,	     'REFERENCE'),
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',      'INFORMATION',        'field',      false,    'COL_2',	         true,         '',     'HTML',         4,   	   'DATE_FIELD'),
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',      'INFORMATION',        'field',      false,    'COL_2',	         true,         '',     'HTML',         5,	     'AMOUNT'),
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',      'INFORMATION',        'field',      false,    'COL_2',	         false,        '',     'HTML',         6,	     'ACCOUNTS_RECEIVABLE_PAYABLE'),
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',      'INFORMATION',        'field',      false,    'COL_3',	         false,        '',     'HTML',         7,	     'PREPAYMENT_NUMBER'),
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',      'INFORMATION',        'field',      false,    'COL_3',	         false,        '',     'HTML',         8,	     'SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP'),
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',      'INFORMATION',        'field',      false,    'COL_3',	         false,        '',     'HTML',         9, 	     'PROJECT'),
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',      'INFORMATION',        'field',      true,     'COL_1',	         false,        '',     'HTML',         10,	     'DEPARTMENT'),
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',      'INFORMATION',        'field',      true,     'COL_2',	         false,        '',     'HTML',         11,	     'NOTES'),
('INVOICE_PAYMENT_VIEW',	  'INFORMATION',      'INFORMATION',        'field',      true,     'COL_3',	         false,        '',     'DataListBox',  12,	     'CUSTOM_HTML_TEMPLATE'),
('INVOICE_PAYMENT_VIEW',	  'INVOICE_DETAILS',  'INVOICE_DETAILS',    'field',      false,    'COL_1',	         false,        '',      'DynamicTable', 13,	     'PAYMENT_TABLE');
