
delete from "anv".modelfield where form_id='SUPPLIER_FORM' and field_id='SUPPLIER_VAT';
delete from "anv".modelfield where form_id='SUPPLIER_FORM' and field_id='CLIENT_VAT';

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values('SUPPLIER_FORM', 'CLIENT_VAT', false, 'COL_1', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 11);