
DELETE FROM "anv".permission_context WHERE permissioncode in ('SETTINGS_COMPANY_SETTINGS',
'SETTINGS_PROFILE_SETTINGS',
'SETTINGS_USER_CREDENTIALS',
'SETTINGS_ACCOUNTING_SETTINGS',
'SETTINGS_CRM_SETTINGS',
'SETTINGS_HRMS_SETTINGS',
'SETTINGS_PROJECT_MANAGEMENT_SETTINGS',
'PAYROLL_SETTINGS',
'CUSTOM_FIELD_SETTINGS',
'SETTINGS_RECURRENCE_SETTINGS',
'SETTINGS_WORKFLOW',
'SETTINGS_EMAIL_SETTINGS');

INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'SETTINGS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'ACCOUNTING');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'PM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'HRMS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'CRM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'PAYROLL');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'WORKSPACE');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'DOCUMENTS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'DASHBOARD');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'REPORTING');


INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'SETTINGS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'ACCOUNTING');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'PM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'HRMS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'CRM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'PAYROLL');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'WORKSPACE');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'DOCUMENTS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'DASHBOARD');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'REPORTING');

INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'SETTINGS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'ACCOUNTING');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'PM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'HRMS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'CRM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'PAYROLL');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'WORKSPACE');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'DOCUMENTS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'DASHBOARD');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'REPORTING');

INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'SETTINGS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'ACCOUNTING');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'PM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'HRMS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'CRM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'PAYROLL');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'WORKSPACE');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'DOCUMENTS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'DASHBOARD');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'REPORTING');

INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'SETTINGS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'ACCOUNTING');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'PM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'HRMS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'CRM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'PAYROLL');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'WORKSPACE');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'DOCUMENTS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'DASHBOARD');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'REPORTING');

INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'SETTINGS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'ACCOUNTING');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'PM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'HRMS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'CRM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'PAYROLL');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'WORKSPACE');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'DOCUMENTS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'DASHBOARD');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'REPORTING');

INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'SETTINGS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'ACCOUNTING');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'PM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'HRMS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'CRM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'PAYROLL');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'WORKSPACE');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'DOCUMENTS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'DASHBOARD');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'REPORTING');

INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'SETTINGS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'ACCOUNTING');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'PM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'HRMS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'CRM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'PAYROLL');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'WORKSPACE');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'DOCUMENTS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'DASHBOARD');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'REPORTING');

INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'SETTINGS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'ACCOUNTING');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'PM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'HRMS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'CRM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'PAYROLL');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'WORKSPACE');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'DOCUMENTS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'DASHBOARD');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'REPORTING');

INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'SETTINGS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'ACCOUNTING');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'PM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'HRMS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'CRM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'PAYROLL');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'WORKSPACE');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'DOCUMENTS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'DASHBOARD');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'REPORTING');

INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'SETTINGS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'ACCOUNTING');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'PM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'HRMS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'CRM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'PAYROLL');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'WORKSPACE');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'DOCUMENTS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'DASHBOARD');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'REPORTING');

INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'SETTINGS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'ACCOUNTING');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'PM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'HRMS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'CRM');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'PAYROLL');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'WORKSPACE');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'DOCUMENTS');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'DASHBOARD');
INSERT INTO "anv".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'REPORTING');






DELETE FROM "0".permission_context WHERE permissioncode in ('SETTINGS_COMPANY_SETTINGS',
'SETTINGS_PROFILE_SETTINGS',
'SETTINGS_USER_CREDENTIALS',
'SETTINGS_ACCOUNTING_SETTINGS',
'SETTINGS_CRM_SETTINGS',
'SETTINGS_HRMS_SETTINGS',
'SETTINGS_PROJECT_MANAGEMENT_SETTINGS',
'PAYROLL_SETTINGS',
'CUSTOM_FIELD_SETTINGS',
'SETTINGS_RECURRENCE_SETTINGS',
'SETTINGS_WORKFLOW',
'SETTINGS_EMAIL_SETTINGS');

INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'SETTINGS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'ACCOUNTING');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'PM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'HRMS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'CRM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'PAYROLL');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'WORKSPACE');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'DOCUMENTS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'DASHBOARD');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_COMPANY_SETTINGS', 'REPORTING');


INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'SETTINGS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'ACCOUNTING');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'PM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'HRMS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'CRM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'PAYROLL');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'WORKSPACE');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'DOCUMENTS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'DASHBOARD');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROFILE_SETTINGS', 'REPORTING');

INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'SETTINGS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'ACCOUNTING');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'PM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'HRMS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'CRM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'PAYROLL');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'WORKSPACE');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'DOCUMENTS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'DASHBOARD');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_USER_CREDENTIALS', 'REPORTING');

INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'SETTINGS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'ACCOUNTING');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'PM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'HRMS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'CRM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'PAYROLL');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'WORKSPACE');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'DOCUMENTS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'DASHBOARD');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_ACCOUNTING_SETTINGS', 'REPORTING');

INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'SETTINGS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'ACCOUNTING');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'PM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'HRMS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'CRM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'PAYROLL');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'WORKSPACE');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'DOCUMENTS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'DASHBOARD');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_CRM_SETTINGS', 'REPORTING');

INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'SETTINGS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'ACCOUNTING');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'PM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'HRMS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'CRM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'PAYROLL');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'WORKSPACE');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'DOCUMENTS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'DASHBOARD');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_HRMS_SETTINGS', 'REPORTING');

INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'SETTINGS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'ACCOUNTING');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'PM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'HRMS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'CRM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'PAYROLL');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'WORKSPACE');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'DOCUMENTS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'DASHBOARD');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_PROJECT_MANAGEMENT_SETTINGS', 'REPORTING');

INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'SETTINGS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'ACCOUNTING');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'PM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'HRMS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'CRM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'PAYROLL');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'WORKSPACE');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'DOCUMENTS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'DASHBOARD');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('PAYROLL_SETTINGS', 'REPORTING');

INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'SETTINGS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'ACCOUNTING');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'PM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'HRMS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'CRM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'PAYROLL');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'WORKSPACE');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'DOCUMENTS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'DASHBOARD');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('CUSTOM_FIELD_SETTINGS', 'REPORTING');

INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'SETTINGS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'ACCOUNTING');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'PM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'HRMS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'CRM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'PAYROLL');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'WORKSPACE');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'DOCUMENTS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'DASHBOARD');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_RECURRENCE_SETTINGS', 'REPORTING');

INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'SETTINGS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'ACCOUNTING');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'PM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'HRMS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'CRM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'PAYROLL');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'WORKSPACE');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'DOCUMENTS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'DASHBOARD');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_WORKFLOW', 'REPORTING');

INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'SETTINGS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'ACCOUNTING');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'PM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'HRMS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'CRM');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'PAYROLL');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'WORKSPACE');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'DOCUMENTS');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'DASHBOARD');
INSERT INTO "0".permission_context (permissioncode, contextcode) VALUES ('SETTINGS_EMAIL_SETTINGS', 'REPORTING');






