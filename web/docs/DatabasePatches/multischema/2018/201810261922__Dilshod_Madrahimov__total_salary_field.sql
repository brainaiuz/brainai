

DELETE from "0".modelfield where field_id='SALARY_TOTAL_AMOUNT';
DELETE from "anv".modelfield where field_id='SALARY_TOTAL_AMOUNT';


insert into "0".modelfield(form_id,           fsection,                  columntype, mandatory,          forder,       field_id) values
  ('HRMS_EMPLOYEE_FORM',	'PAYMENT_DEDUCTION_INFORMATION',  'COL_1',	  false, 	            0,	          'SALARY_TOTAL_AMOUNT');

insert into "anv".modelfield
(form_id,           fsection,                  columntype, mandatory,          forder,       field_id) values
  ('HRMS_EMPLOYEE_FORM',	'PAYMENT_DEDUCTION_INFORMATION',  'COL_1',	  false, 	            0,	          'SALARY_TOTAL_AMOUNT');



 update "0".modelfield set fieldsetstyle='slideDown-content group labelLine',fieldstyle='field',halfsetstyle='halfSet-1 left',section='Salary Details',sectionstyle='slideDown-box  group expand hideCustomField',widget='TextBox' where field_id='SALARY_TOTAL_AMOUNT' and form_id='HRMS_EMPLOYEE_FORM';
 update "anv".modelfield set fieldsetstyle='slideDown-content group labelLine',fieldstyle='field',halfsetstyle='halfSet-1 left',section='Salary Details',sectionstyle='slideDown-box  group expand hideCustomField',widget='TextBox' where field_id='SALARY_TOTAL_AMOUNT' and form_id='HRMS_EMPLOYEE_FORM';
