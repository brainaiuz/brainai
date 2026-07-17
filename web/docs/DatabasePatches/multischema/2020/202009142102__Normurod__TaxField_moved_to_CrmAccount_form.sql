update modelfield set forder = 3 where field_id = 'CLIENT_VAT' and field_id = 'SUPPLIER_VAT';
update "anv".modelfield set forder = 3 where field_id = 'CLIENT_VAT' and field_id = 'SUPPLIER_VAT';

delete from modelfield where form_id = 'ACCOUNT_FORM' and field_id = 'CLIENT_VAT';
insert into modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory) values
('ACCOUNT_FORM', 'CLIENT_VAT', 'COL_1', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 3, false);

delete from "anv".modelfield where form_id = 'ACCOUNT_FORM' and field_id = 'CLIENT_VAT';
insert into "anv".modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory) values
('ACCOUNT_FORM', 'CLIENT_VAT', 'COL_1', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 3, false);