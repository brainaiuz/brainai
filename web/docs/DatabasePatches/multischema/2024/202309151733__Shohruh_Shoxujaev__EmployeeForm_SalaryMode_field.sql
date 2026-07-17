delete from "anv".modelfield where field_id = 'SALARY_MODE' and form_id = 'HRMS_EMPLOYEE_FORM';
insert into "anv".modelfield (form_id, fsection, hide, nolabelfor, fieldstyle, columntype, fieldsetstyle, rowstyle, mandatory, sectionstyle, widget, forder, field_id)
values ('HRMS_EMPLOYEE_FORM', 'EMPLOYMENT_INFORMATION', false, '', 'field', 'COL_3', '', '', false, '', 'DropDown', 4, 'SALARY_MODE');

delete from "anv".modelfield where field_id = 'SALARY_MODE' and form_id = 'PAYROLL_STARTER_FORM';
insert into "anv".modelfield (form_id, fsection, hide, nolabelfor, fieldstyle, columntype, fieldsetstyle, rowstyle, mandatory, sectionstyle, widget, forder, field_id)
values ('PAYROLL_STARTER_FORM', 'PAYMENT_SETTINGS', false, '', 'field', 'COL_1', '', '', false, '', 'DropDown', 2, 'SALARY_MODE');
