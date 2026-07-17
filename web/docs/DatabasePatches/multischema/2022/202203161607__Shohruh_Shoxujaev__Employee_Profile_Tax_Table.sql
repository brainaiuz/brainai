delete from "0".modelfield where field_id = 'TAX_TABLE' and form_id = 'HRMS_EMPLOYEE_FORM';
insert into "0".modelfield (form_id, fsection, hide, nolabelfor, fieldstyle, columntype, fieldsetstyle, rowstyle, mandatory, sectionstyle, widget, forder, field_id)
values ('HRMS_EMPLOYEE_FORM', 'PAYMENT_DEDUCTION_INFORMATION', false, '', 'field', 'COL_1', '', '', false, '', 'MULTITABLE', 3, 'TAX_TABLE');

delete from "anv".modelfield where field_id = 'TAX_TABLE' and form_id = 'HRMS_EMPLOYEE_FORM';
insert into "anv".modelfield (form_id, fsection, hide, nolabelfor, fieldstyle, columntype, fieldsetstyle, rowstyle, mandatory, sectionstyle, widget, forder, field_id)
values ('HRMS_EMPLOYEE_FORM', 'PAYMENT_DEDUCTION_INFORMATION', false, '', 'field', 'COL_1', '', '', false, '', 'MULTITABLE', 3, 'TAX_TABLE');
