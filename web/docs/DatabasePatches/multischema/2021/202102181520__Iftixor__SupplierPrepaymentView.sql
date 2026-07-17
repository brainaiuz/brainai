

delete from "anv".modelfield where form_id='SUPPLIER_CREDIT_FORM' and field_id='PAYMENT_TABLE';
insert into "anv".modelfield
(form_id,             fsection,                     section,                    fieldstyle,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id) values
('SUPPLIER_CREDIT_FORM',	  'INVOICE_DETAILS',      'INVOICE_DETAILS',    'field',      false,   'COL_1',	         true,         '',             'DynamicTable',       12,	       'PAYMENT_TABLE');

