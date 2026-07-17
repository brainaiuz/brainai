

delete from permission where code='HRMS_MANAGER_SELF_SERVICE_VIEW';
delete from permission where code='HRMS_EMPLOYEE_SELF_SERVICE_VIEW';
delete from permission where code='HRMS_SALARY_GRADE';
delete from permission where code='HRMS_ADD_NEW_GRADE';
delete from permission where code='HRMS_EDIT_GRADE';
delete from permission where code='HRMS_GRADE_REMOVE';
delete from permission where code='HRMS_GRADE_SUMMARY';


delete from "0".rolepermission where permissioncode='CRM_VIEW_LEAD_CHANGE_LOG';
delete from "0".rolepermission where permissioncode='CRM_WELCOME_PAGE';

delete from "anv".rolepermission where permissioncode='CRM_VIEW_LEAD_CHANGE_LOG';
delete from "anv".rolepermission where permissioncode='CRM_WELCOME_PAGE';