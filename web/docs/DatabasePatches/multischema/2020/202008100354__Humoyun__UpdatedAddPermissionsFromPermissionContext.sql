
delete from "anv".permission_context where permissioncode = 'CRM_ACCOUNT_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values
('CRM_ACCOUNT_ADD', 'CRM'),
('CRM_ACCOUNT_ADD', 'ACCOUNTING'),
('CRM_ACCOUNT_ADD', 'PM'),
('CRM_ACCOUNT_ADD', 'HRMS');

delete from "anv".permission_context where permissioncode = 'CRM_ADD_NEW_CAMPAIGN';
insert into "anv".permission_context (permissioncode, contextcode) values
('CRM_ADD_NEW_CAMPAIGN', 'CRM'),
('CRM_ADD_NEW_CAMPAIGN', 'ACCOUNTING'),
('CRM_ADD_NEW_CAMPAIGN', 'PM'),
('CRM_ADD_NEW_CAMPAIGN', 'HRMS');

delete from "anv".permission_context where permissioncode = 'ADD_NEW_CASE';
insert into "anv".permission_context (permissioncode, contextcode) values
('ADD_NEW_CASE', 'CRM'),
('ADD_NEW_CASE', 'ACCOUNTING'),
('ADD_NEW_CASE', 'PM'),
('ADD_NEW_CASE', 'HRMS');

delete from "anv".permission_context where permissioncode = 'CRM_ADD_NEW_CONTACT';
insert into "anv".permission_context (permissioncode, contextcode) values
('CRM_ADD_NEW_CONTACT', 'CRM'),
('CRM_ADD_NEW_CONTACT', 'ACCOUNTING'),
('CRM_ADD_NEW_CONTACT', 'PM'),
('CRM_ADD_NEW_CONTACT', 'HRMS');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_CUSTOMER_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values
('ACCOUNTING_CUSTOMER_ADD', 'CRM'),
('ACCOUNTING_CUSTOMER_ADD', 'ACCOUNTING'),
('ACCOUNTING_CUSTOMER_ADD', 'PM'),
('ACCOUNTING_CUSTOMER_ADD', 'HRMS');

delete from "anv".permission_context where permissioncode = 'HRMS_ADD_NEW_DEPARTMENT';
insert into "anv".permission_context (permissioncode, contextcode) values
('HRMS_ADD_NEW_DEPARTMENT', 'CRM'),
('HRMS_ADD_NEW_DEPARTMENT', 'ACCOUNTING'),
('HRMS_ADD_NEW_DEPARTMENT', 'PM'),
('HRMS_ADD_NEW_DEPARTMENT', 'HRMS');

delete from "anv".permission_context where permissioncode = 'HRMS_ADD_NEW_EMPLOYEE';
insert into "anv".permission_context (permissioncode, contextcode) values
('HRMS_ADD_NEW_EMPLOYEE', 'CRM'),
('HRMS_ADD_NEW_EMPLOYEE', 'ACCOUNTING'),
('HRMS_ADD_NEW_EMPLOYEE', 'PM'),
('HRMS_ADD_NEW_EMPLOYEE', 'HRMS');

delete from "anv".permission_context where permissioncode = 'CRM_ADD_NEW_ACTIVITY_EVENT';
insert into "anv".permission_context (permissioncode, contextcode) values
('CRM_ADD_NEW_ACTIVITY_EVENT', 'CRM'),
('CRM_ADD_NEW_ACTIVITY_EVENT', 'ACCOUNTING'),
('CRM_ADD_NEW_ACTIVITY_EVENT', 'PM'),
('CRM_ADD_NEW_ACTIVITY_EVENT', 'HRMS');

delete from "anv".permission_context where permissioncode = 'PM_ISSUE_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values
('PM_ISSUE_ADD', 'CRM'),
('PM_ISSUE_ADD', 'ACCOUNTING'),
('PM_ISSUE_ADD', 'PM'),
('PM_ISSUE_ADD', 'HRMS');

delete from "anv".permission_context where permissioncode = 'ADD_NEW_LEAD';
insert into "anv".permission_context (permissioncode, contextcode) values
('ADD_NEW_LEAD', 'CRM'),
('ADD_NEW_LEAD', 'ACCOUNTING'),
('ADD_NEW_LEAD', 'PM'),
('ADD_NEW_LEAD', 'HRMS');

delete from "anv".permission_context where permissioncode = 'CRM_ADD_NEW_OPPORTUNITIES';
insert into "anv".permission_context (permissioncode, contextcode) values
('CRM_ADD_NEW_OPPORTUNITIES', 'CRM'),
('CRM_ADD_NEW_OPPORTUNITIES', 'ACCOUNTING'),
('CRM_ADD_NEW_OPPORTUNITIES', 'PM'),
('CRM_ADD_NEW_OPPORTUNITIES', 'HRMS');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PRODUCT_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values
('ACCOUNTING_PRODUCT_ADD', 'CRM'),
('ACCOUNTING_PRODUCT_ADD', 'ACCOUNTING'),
('ACCOUNTING_PRODUCT_ADD', 'PM'),
('ACCOUNTING_PRODUCT_ADD', 'HRMS');

delete from "anv".permission_context where permissioncode = 'PM_PROJECT_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values
('PM_PROJECT_ADD', 'CRM'),
('PM_PROJECT_ADD', 'ACCOUNTING'),
('PM_PROJECT_ADD', 'PM'),
('PM_PROJECT_ADD', 'HRMS');

delete from "anv".permission_context where permissioncode = 'ACCOUNTING_PURCHASE_ORDER_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values
('ACCOUNTING_PURCHASE_ORDER_ADD', 'ACCOUNTING'),
('ACCOUNTING_PURCHASE_ORDER_ADD', 'PM'),
('ACCOUNTING_PURCHASE_ORDER_ADD', 'HRMS');


delete from "anv".permission_context where permissioncode = 'ACCOUNTING_REQUEST_FOR_QUOTE_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values
('ACCOUNTING_REQUEST_FOR_QUOTE_ADD', 'CRM'),
('ACCOUNTING_REQUEST_FOR_QUOTE_ADD', 'ACCOUNTING'),
('ACCOUNTING_REQUEST_FOR_QUOTE_ADD', 'PM'),
('ACCOUNTING_REQUEST_FOR_QUOTE_ADD', 'HRMS');


delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_ORDER_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values
('ACCOUNTING_SALES_ORDER_ADD', 'ACCOUNTING'),
('ACCOUNTING_SALES_ORDER_ADD', 'PM'),
('ACCOUNTING_SALES_ORDER_ADD', 'HRMS');


delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_QUOTE_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values
('ACCOUNTING_SALES_QUOTE_ADD', 'ACCOUNTING'),
('ACCOUNTING_SALES_QUOTE_ADD', 'PM'),
('ACCOUNTING_SALES_QUOTE_ADD', 'HRMS');


delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SUPPLIER_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values
('ACCOUNTING_SUPPLIER_ADD', 'CRM'),
('ACCOUNTING_SUPPLIER_ADD', 'ACCOUNTING'),
('ACCOUNTING_SUPPLIER_ADD', 'PM'),
('ACCOUNTING_SUPPLIER_ADD', 'HRMS');

delete from "anv".permission_context where permissioncode = 'PM_TASKS_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values
('PM_TASKS_ADD', 'ACCOUNTING'),
('PM_TASKS_ADD', 'PM'),
('PM_TASKS_ADD', 'HRMS');
