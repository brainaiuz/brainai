delete from "0".modelfield where field_id = 'TAX_TABLE' and form_id = 'PAYROLL_STARTER_FORM';
insert into "0".modelfield (form_id, fsection, hide, columntype, mandatory, widget, forder, field_id)
values ('PAYROLL_STARTER_FORM', 'PAYMENT_DEDUCTION_INFORMATION', false, 'COL_2', false, 'MULTITABLE', 3, 'TAX_TABLE');

delete from "anv".modelfield where field_id = 'TAX_TABLE' and form_id = 'PAYROLL_STARTER_FORM';
insert into "anv".modelfield (form_id, fsection, hide, columntype, mandatory, widget, forder, field_id)
values ('PAYROLL_STARTER_FORM', 'PAYMENT_DEDUCTION_INFORMATION', false, 'COL_2', false, 'MULTITABLE', 3, 'TAX_TABLE');