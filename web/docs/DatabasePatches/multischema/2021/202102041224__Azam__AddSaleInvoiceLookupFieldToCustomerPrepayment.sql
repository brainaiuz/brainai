delete from "anv".modelfield where form_id = 'PREPAYMENT_FORM' and field_id='SALE_INVOICE_LOOKUP';
insert into "anv".modelfield
(form_id,             fsection,                     section,                    fieldstyle,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id) values
('PREPAYMENT_FORM',	  'PREPAYMENT_FORM_TITLE',      'PREPAYMENT_FORM_TITLE',    'field',      true,   'COL_3',	         false,        '',             'LOOKUP',       12,	       'SALE_INVOICE_LOOKUP');