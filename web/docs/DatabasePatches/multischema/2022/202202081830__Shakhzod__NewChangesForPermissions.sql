update permission
set parent=(select id from permission where code = 'HRMS_MAIN_MENU')
where code = 'HRMS_GROUP_PERSONAL_GOALS';
update permission
set parent=(select id from permission where code = 'CRM_MAIN_MENU')
where code = 'CRM_SOLUTIONS_LIST';

update permission
set parent=(select id from permission where code = 'PM_EMPLOYEE_LIST')
where code = 'PM_SHOW_EMPLOYEE_ROLE_WIDGET';
update permission
set parent=(select id from permission where code = 'PM_EMPLOYEE_LIST')
where code = 'PM_EMPLOYEE_RATE';
update permission
set parent=(select id from permission where code = 'PM_EMPLOYEE_LIST')
where code = 'PM_SHOW_EMPLOYEE_BIRTH_DAY';
update permission
set parent=(select id from permission where code = 'PM_EMPLOYEE_LIST')
where code = 'PM_SHOW_EMPLOYEE_ADDRESS';
update permission
set parent=(select id from permission where code = 'PM_EMPLOYEE_LIST')
where code = 'PM_SHOW_EMPLOYMENT_INFORMATION';
update permission
set parent=(select id from permission where code = 'PM_EMPLOYEE_LIST')
where code = 'PM_SHOW_OWN_EMPLOYMENT_INFORMATION';
update permission
set parent=(select id from permission where code = 'PM_EMPLOYEE_LIST')
where code = 'PM_SHOW_EMPLOYEE_ATTACHMENT';
update permission
set parent=(select id from permission where code = 'PM_EMPLOYEE_LIST')
where code = 'PM_SHOW_ADDITIONAL_INFORMATION';
update permission
set parent=(select id from permission where code = 'HRMS_EMPLOYEES')
where code = 'HRMS_SHOW_EMPLOYEE_ROLE_WIDGET';

update permission
set parent = (select id from permission where code = 'HRMS_SIMPLE_APPRAISALS')
where code = 'HRMS_ADD_NEW_APPRAISALS';
update permission
set parent = (select id from permission where code = 'HRMS_SIMPLE_APPRAISALS')
where code = 'HRMS_ADD_COMPETENCY_FROM_TEMPLATE';
update permission
set parent = (select id from permission where code = 'HRMS_SIMPLE_APPRAISALS')
where code = 'HRMS_APPRAISAL_EMPLOYEE_LIST_BOX';
update permission
set parent = (select id from permission where code = 'HRMS_SIMPLE_APPRAISALS')
where code = 'HRMS_APPRAISALS_REVIEWER';
update permission
set parent = (select id from permission where code = 'HRMS_SIMPLE_APPRAISALS')
where code = 'HRMS_ADD_VALIDITY_PERIOD';

update permission
set parent=(select id from permission where code = 'PM_CUSTOMER_LIST')
where code = 'PM_CUSTOMER_ADD_CLIENT';
update permission
set parent=(select id from permission where code = 'PM_CUSTOMER_LIST')
where code = 'PM_CUSTOMER_EDIT';
update permission
set parent=(select id from permission where code = 'PM_CUSTOMER_LIST')
where code = 'PM_CUSTOMER_REMOVE_CLIENT';
update permission
set parent=(select id from permission where code = 'PM_CUSTOMER_LIST')
where code = 'PM_CUSTOMER_IMPORT';
update permission
set parent=(select id from permission where code = 'PM_CUSTOMER_LIST')
where code = 'PM_CUSTOMER_EXPORT';


update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_SEE_ALL_OPPORTUNITIES_LIST';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_ADD_NEW_OPPORTUNITIES';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_EDIT_OPPORTUNITIES';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_REMOVE_OPPORTUNITIES';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CUSTOM_FORM_2_CUSTOMIZE_FORM';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'OPPORTUNITY_SEE_OWN';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_QUICK_ADD_NEW_OPPORTUNITIES';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_COPY_OPPORTUNITIES';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CHANGE_OPPORTUNITIES_CAMPAIGN';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CONVERT_OPPORTUNITY_TO_SQ';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_ADD_OPPORTUNITY_NOTE';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_OPPORTUNITY_CHANGE_STAGE';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CONVERT_OPPORTUNITY_TO_SO';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CONVERT_OPPORTUNITY_TO_RFQ';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CONVERT_OPPORTUNITY_TO_PO';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CONVERT_OPPORTUNITY_TO_PROJECT';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_OPPORTUNITIES_IMPORT_LIST';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_OPPORTUNITIES_EXPORT_LIST';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_OPPORTUNITIES_EXPENSE_CLAIM_LIST';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_OPPORTUNITIES_RFQ_LIST';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_OPPORTUNITY_PDF';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CONVERT_OPPORTUNITY_TO_SI';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CONVERT_OPPORTUNITY_TO_PI';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_OPPORTUNITY_ASSIGNEE_LIST_VALUE';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_OPPORTUNITY_BACKUP_ASSIGNEE_LIST_VALUE';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CHANGE_OPPORTUNITY_ASSIGNEE';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_OPPORTUNITIES_LIST_CUSTOMIZE_BUTTON';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_OPPORTUNITY_LINKS';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_OPPORTUNITY_HISTORY_LIST';
update permission
set parent = (select id from permission where code = 'CRM_OPPORTUNITIES_LIST')
where code = 'CRM_SHOW_SUPERVISED_OPPORTUNITIES';



