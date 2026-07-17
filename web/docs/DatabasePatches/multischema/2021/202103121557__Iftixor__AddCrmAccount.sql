
delete from "anv".modelfield where form_id = 'ACCOUNT_FORM' and field_ID = 'CRM_ACCOUNT_TAX_TREATMENT';
insert into "anv".modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory) values
('ACCOUNT_FORM', 'CRM_ACCOUNT_TAX_TREATMENT', 'COL_2', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 5, true);