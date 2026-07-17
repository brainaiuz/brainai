
update "anv".modelfield set hideincustomizeform=true where form_id='COMPANY_SETTINGS_FORM' and field_id in ('CRM_ACCOUNT_BILLING_ADDRESS', 'HIDDEN_FIELD', 'CRM_ACCOUNT_SHIPPING_ADDRESS', 'CS_SYTEM_THEME_COLOR', 'CS_KPI_MODE', 'COMPANY_LOGO_LABEL', 'COMPANY_LOGO', 'PDF_LOGO_LABEL');
update "anv".modelfield set hideincustomizeform=true where form_id='OPPORTUNITY_FORM' and field_id in ('LATEST_ESTIMATE', 'CRM_ESTIMETOR');
update "anv".modelfield set hideincustomizeform=true where form_id='HRMS_EMPLOYEE_FORM' and field_id in ('COMPETENCIES', 'SALARY_GRADE');
update "anv".modelfield set hideincustomizeform=true where form_id='PAYROLL_CASH_ADVANCE_FORM' and field_id='DRIVER_NUMBER';

delete from "anv".customformsection where form_id='HRMS_EMPLOYEE_FORM' and section='YEAR_LEAVE_DURATION';
delete from "anv".customformsection where form_id='HRMS_EMPLOYEE_FORM' and section='YEAR_LEAVE_HOUR_DURATION';
delete from "anv".customformsection where form_id='HRMS_EMPLOYEE_FORM' and section='PAST_EMPLOYMENT';
delete from "anv".customformsection where form_id='HRMS_EMPLOYEE_FORM' and section='INTERNAL_EMPLOYMENT';
delete from "anv".customformsection where form_id='HRMS_EMPLOYEE_FORM' and section='PUNISHMENT_PROMOTION';
delete from "anv".customformsection where form_id='PROJECT_SUMMARY_FORM' and section='PROJECT_INVOICES_DETAILS';
delete from "anv".customformsection where form_id='PROJECT_SUMMARY_FORM' and section='LOGGED_TIME_DETAILS';



update "0".modelfield set hideincustomizeform=true where form_id='COMPANY_SETTINGS_FORM' and field_id in ('CRM_ACCOUNT_BILLING_ADDRESS', 'HIDDEN_FIELD', 'CRM_ACCOUNT_SHIPPING_ADDRESS', 'CS_SYTEM_THEME_COLOR', 'CS_KPI_MODE', 'COMPANY_LOGO_LABEL', 'COMPANY_LOGO', 'PDF_LOGO_LABEL');
update "0".modelfield set hideincustomizeform=true where form_id='OPPORTUNITY_FORM' and field_id in ('LATEST_ESTIMATE', 'CRM_ESTIMETOR');
update "0".modelfield set hideincustomizeform=true where form_id='HRMS_EMPLOYEE_FORM' and field_id in ('COMPETENCIES', 'SALARY_GRADE');
update "0".modelfield set hideincustomizeform=true where form_id='PAYROLL_CASH_ADVANCE_FORM' and field_id='DRIVER_NUMBER';

delete from "0".customformsection where form_id='HRMS_EMPLOYEE_FORM' and section='YEAR_LEAVE_DURATION';
delete from "0".customformsection where form_id='HRMS_EMPLOYEE_FORM' and section='YEAR_LEAVE_HOUR_DURATION';
delete from "0".customformsection where form_id='HRMS_EMPLOYEE_FORM' and section='PAST_EMPLOYMENT';
delete from "0".customformsection where form_id='HRMS_EMPLOYEE_FORM' and section='INTERNAL_EMPLOYMENT';
delete from "0".customformsection where form_id='HRMS_EMPLOYEE_FORM' and section='PUNISHMENT_PROMOTION';
delete from "0".customformsection where form_id='PROJECT_SUMMARY_FORM' and section='PROJECT_INVOICES_DETAILS';
delete from "0".customformsection where form_id='PROJECT_SUMMARY_FORM' and section='LOGGED_TIME_DETAILS';