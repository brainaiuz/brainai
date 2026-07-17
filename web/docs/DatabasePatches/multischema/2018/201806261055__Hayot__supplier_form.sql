delete from "0".modelfield where field_id='REVERSE_CHARGE_APPLICABLE';
insert into "0".modelfield
(form_id,           fsection,                             columntype, mandatory,          forder,       field_id) values
('SUPPLIER_FORM',	  'CRM_ACCOUNT_FINANCIAL_INFORMATION',  'COL_1',	  false, 	            27,	          'REVERSE_CHARGE_APPLICABLE');

delete from "anv".modelfield where field_id='REVERSE_CHARGE_APPLICABLE';
insert into "anv".modelfield
(form_id,           fsection,                             columntype, mandatory,          forder,       field_id) values
('SUPPLIER_FORM',	  'CRM_ACCOUNT_FINANCIAL_INFORMATION',  'COL_1',	  false, 	            27,	          'REVERSE_CHARGE_APPLICABLE');
