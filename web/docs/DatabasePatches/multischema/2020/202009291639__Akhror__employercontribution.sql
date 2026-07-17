insert into "anv".modelfield(form_id, fsection, columntype, mandatory, forder, field_id)
values ('PAYROLL_STARTER_FORM', 'PAYMENT_DEDUCTION_INFORMATION', 'COL_1', false, 2, 'EMPLOYER_CONTRIBUTION_TABLE');

insert into "anv".modelfield(form_id, fsection, columntype, mandatory, forder, field_id)
values ('HRMS_EMPLOYEE_FORM', 'PAYMENT_DEDUCTION_INFORMATION', 'COL_1', false, 2, 'EMPLOYER_CONTRIBUTION_TABLE');

update "anv".modelfield set forder = 1 where field_id = 'PAYMENT_TABLE' and form_id = 'HRMS_EMPLOYEE_FORM';
update "anv".modelfield set forder = 2 where field_id = 'EMPLOYER_CONTRIBUTION_TABLE' and form_id = 'HRMS_EMPLOYEE_FORM';
update "anv".modelfield set forder = 3 where field_id = 'SALARY_AMOUNT' and form_id = 'HRMS_EMPLOYEE_FORM';
update "anv".modelfield set forder = 4 where field_id = 'SALARY_TOTAL_AMOUNT' and form_id = 'HRMS_EMPLOYEE_FORM';
update "anv".modelfield set columntype = 'COL_2' where field_id = 'SALARY_TOTAL_AMOUNT' and form_id = 'HRMS_EMPLOYEE_FORM';
