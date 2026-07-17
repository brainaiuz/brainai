delete from "0".user_dashboard_settings;
delete from "0".dashboard_accesses;
delete from "0".dashboard_components;
delete from "0".module_dashboards;
delete from "0".default_components;

alter sequence "0".user_dashboard_settings_id_seq restart with 1;
alter sequence "0".dashboard_components_id_seq restart with 1;
alter sequence "0".module_dashboards_id_seq restart with 1;
alter sequence "0".default_components_id_seq restart with 1;

insert into "0".module_dashboards(name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted)
values ('Dashboard', 'ACCOUNTING', true, true, true, CURRENT_DATE, null, false);

insert into "0".module_dashboards(name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted)
values ('HR Dashboard', 'HRMS', true, true, true, CURRENT_DATE, null, false);

insert into "0".module_dashboards(name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted)
values ('Employee Portal', 'HRMS', true, false, true, CURRENT_DATE, null, false);

insert into "0".module_dashboards(name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted)
values ('Dashboard', 'MYWORKSPACE', true, true, true, CURRENT_DATE, null, false);

insert into "0".module_dashboards(name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted)
values ('Dashboard', 'PM', true, true, true, CURRENT_DATE, null, false);

insert into "0".module_dashboards(name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted)
values ('Dashboard', 'CRM', true, true, true, CURRENT_DATE, null, false);

insert into "0".default_components(width, height, minHeight, minWidth, componentName, componentCode, modules, report_code) values
 (6, 4, 3, 2, 'My Updates', 'MY_UPDATES', '["ACCOUNTING","HRMS","MYWORKSPACE","PM","CRM"]', null),
 (6, 4, 3, 2, 'ToDo List', 'TODO_LIST', '["ACCOUNTING","HRMS","MYWORKSPACE","PM","CRM"]', null),
 (6, 4, 3, 2, 'My Contacts', 'MY_CONTACTS', '["ACCOUNTING","HRMS","MYWORKSPACE","PM","CRM"]', null),
 (6, 4, 3, 2, 'My Agenda', 'MY_CALENDAR', '["ACCOUNTING","HRMS","MYWORKSPACE","PM","CRM"]', null),
 (12, 3, 12, 2, 'Combo', 'COMBO', '["ACCOUNTING","HRMS","MYWORKSPACE","PM","CRM"]', null),
 (4, 4, 2, 2, 'Unavailable Employees', 'UNAVAILABLE_EMPLOYEES_SUPERVISION', '["HRMS"]', null),
 (4, 4, 2, 2, 'Gender Ratio', 'GENDER_RATIO', '["HRMS"]', null),
 (4, 4, 2, 2, 'Public Holidays', 'HOLIDAY', '["HRMS"]', null),
 (4, 4, 2, 2, 'Aged Reports', 'AGED_REPORTS', '["ACCOUNTING"]', null),
 (4, 4, 2, 2, 'Incomes Vs Expenses', 'INCOME_VS_EXPENSE', '["ACCOUNTING"]', null),
 (4, 4, 2, 2, 'Sale & Purchase Transactions', 'SALE_PURCHASE_TRANSACTIONS', '["ACCOUNTING"]', null),
 (4, 4, 2, 2, 'Top Expenses(YTD)', 'TOP_EXPENSES', '["ACCOUNTING","HRMS"]', null),
 (4, 4, 2, 2, 'Project Overview', 'PROJET_OVERVIEW', '["PM"]', null),
 (4, 4, 2, 2, 'Projects Due This Month', 'PROJECT_DUE_THIS_MONTH', '["PM"]', null),
 (4, 4, 2, 2, 'Tasks Due Today', 'TASKS_DUE_TODAY', '["PM"]', null),
 (4, 4, 2, 2, 'Expired Documents', 'EXPIRED_DOCUMENTS', '["HRMS"]', null),
 (4, 4, 2, 2, 'Expiry Documents', 'EXPIRY_DOCUMENTS', '["HRMS"]', null),
 (4, 4, 2, 2, 'Monthly Payroll YTD', 'PAYROLL_YTD', '["HRMS"]', null),
 (4, 4, 2, 2, 'Payslip By Month YTD', 'PAYROLL_EMPLOYEE_YTD', '["HRMS"]', null),
 (4, 4, 2, 2, 'Leave Reason Status', 'LEAVE_REASON_STATUS', '["HRMS"]', null),
 (4, 4, 2, 2, 'My Files', 'HRMS_MY_FILES', '["HRMS"]', null),
 (4, 4, 2, 2, 'My expenses', 'EMPLOYEE_TOP_EXPENSES', '["HRMS","MYWORKSPACE"]', null),
 (4, 4, 2, 2, 'Timeslot', 'TIMESLOT', '["HRMS"]', null),
 (4, 4, 2, 2, 'Estimated vs Actual Time', 'ESTIMATEDVSACTUALTIME', '["PM"]', 'ESTIMATEDVSACTUALTIME'),
 (4, 4, 2, 2, 'Estimated vs Actual Cost', 'ESTIMATEDVSACTUALCOST', '["PM"]', 'ESTIMATEDVSACTUALCOST'),
 (4, 4, 2, 2, 'Active Tasks by Assignee', 'ACTIVETASKSBYASSIGNEE', '["PM"]', 'ACTIVETASKSBYASSIGNEE'),
 (4, 4, 2, 2, 'Sales Invoice By Status', 'SALESINVOICEBYSTATUSWIDGET', '["ACCOUNTING"]', 'SALESINVOICEBYSTATUSWIDGET');

insert into "0".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (4,4,0,0, 2, 2, (select id from "0".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "0".default_components where componentCode = 'AGED_REPORTS' order by id limit 1)),
  (4,4,4,0, 2, 2, (select id from "0".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "0".default_components where componentCode = 'INCOME_VS_EXPENSE' order by id limit 1)),
  (12,4,0,4, 2, 2, (select id from "0".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "0".default_components where componentCode = 'SALE_PURCHASE_TRANSACTIONS' order by id limit 1)),
  (4,4,8,0, 2, 2, (select id from "0".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "0".default_components where componentCode = 'TOP_EXPENSES' order by id limit 1));

insert into "0".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,0,0, 2, 3, (select id from "0".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "0".default_components where componentCode = 'UNAVAILABLE_EMPLOYEES_SUPERVISION' order by id limit 1)),
  (6,4,6,4, 2, 3, (select id from "0".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "0".default_components where componentCode = 'GENDER_RATIO' order by id limit 1)),
  (6,4,6,8, 2, 3, (select id from "0".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "0".default_components where componentCode = 'EXPIRY_DOCUMENTS' order by id limit 1)),
  (6,4,0,8, 2, 2, (select id from "0".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "0".default_components where componentCode = 'HOLIDAY' order by id limit 1)),
  (6,4,6,0, 3, 4, (select id from "0".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "0".default_components where componentCode = 'PAYROLL_YTD' order by id limit 1)),
  (6,4,0,4, 2, 2, (select id from "0".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "0".default_components where componentCode = 'TOP_EXPENSES' order by id limit 1));

insert into "0".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,0,0, 2, 3, (select id from "0".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "0".default_components where componentCode = 'LEAVE_REASON_STATUS' order by id limit 1)),
  (6,4,6,0, 3, 4, (select id from "0".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "0".default_components where componentCode = 'PAYROLL_EMPLOYEE_YTD' order by id limit 1)),
  (6,4,0,4, 2, 3, (select id from "0".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "0".default_components where componentCode = 'TODO_LIST' order by id limit 1)),
  (6,4,6,8, 2, 3, (select id from "0".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "0".default_components where componentCode = 'HRMS_MY_FILES' order by id limit 1)),
  (12,3,0,12, 2, 12, (select id from "0".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "0".default_components where componentCode = 'COMBO' order by id limit 1)),
  (6,4,0,8, 2, 2, (select id from "0".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "0".default_components where componentCode = 'HOLIDAY' order by id limit 1)),
  (6,4,6,4, 2, 3, (select id from "0".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "0".default_components where componentCode = 'EMPLOYEE_TOP_EXPENSES' order by id limit 1));

insert into "0".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,0,7, 2, 3, (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1), (select id from "0".default_components where componentCode = 'TODO_LIST' order by id limit 1)),
  (6,4,6,7, 2, 3, (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1), (select id from "0".default_components where componentCode = 'MY_UPDATES' order by id limit 1)),
  (5,4,7,3, 2, 3, (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1), (select id from "0".default_components where componentCode = 'MY_CONTACTS' order by id limit 1)),
  (7,4,0,3, 2, 3, (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1), (select id from "0".default_components where componentCode = 'MY_CALENDAR' order by id limit 1)),
  (12,3,0,0, 2, 12, (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1), (select id from "0".default_components where componentCode = 'COMBO' order by id limit 1));

insert into "0".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (8,4,0,0, 2, 2, (select id from "0".module_dashboards where module = 'PM' order by id limit 1), (select id from "0".default_components where componentCode = 'PROJECT_DUE_THIS_MONTH' order by id limit 1)),
  (4,4,8,0, 2, 2, (select id from "0".module_dashboards where module = 'PM' order by id limit 1), (select id from "0".default_components where componentCode = 'PROJET_OVERVIEW' order by id limit 1)),
  (6,4,6,4, 2, 2, (select id from "0".module_dashboards where module = 'PM' order by id limit 1), (select id from "0".default_components where componentCode = 'TASKS_DUE_TODAY' order by id limit 1)),
  (6,4,0,4, 2, 3, (select id from "0".module_dashboards where module = 'PM' order by id limit 1), (select id from "0".default_components where componentCode = 'MY_UPDATES' order by id limit 1)),
  (4,4,0,8, 2, 2, (select id from "0".module_dashboards where module = 'PM' order by id limit 1), (select id from "0".default_components where report_code = 'ESTIMATEDVSACTUALTIME' order by id limit 1)),
  (4,4,4,8, 2, 2, (select id from "0".module_dashboards where module = 'PM' order by id limit 1), (select id from "0".default_components where report_code = 'ACTIVETASKSBYASSIGNEE' order by id limit 1)),
  (4,4,8,8, 2, 2, (select id from "0".module_dashboards where module = 'PM' order by id limit 1), (select id from "0".default_components where report_code = 'ESTIMATEDVSACTUALCOST' order by id limit 1));

insert into "0".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,0,0, 2, 3, (select id from "0".module_dashboards where module = 'CRM' order by id limit 1), (select id from "0".default_components where componentCode = 'TODO_LIST' order by id limit 1)),
  (6,4,6,4, 2, 3, (select id from "0".module_dashboards where module = 'CRM' order by id limit 1), (select id from "0".default_components where componentCode = 'MY_UPDATES' order by id limit 1)),
  (6,4,0,4, 2, 3, (select id from "0".module_dashboards where module = 'CRM' order by id limit 1), (select id from "0".default_components where componentCode = 'MY_CONTACTS' order by id limit 1)),
  (6,4,6,0, 2, 3, (select id from "0".module_dashboards where module = 'CRM' order by id limit 1), (select id from "0".default_components where componentCode = 'MY_CALENDAR' order by id limit 1));

delete from "anv".user_dashboard_settings;
delete from "anv".dashboard_accesses;
delete from "anv".dashboard_components;
delete from "anv".module_dashboards;
delete from "anv".default_components;

alter sequence "anv".user_dashboard_settings_id_seq restart with 1;
alter sequence "anv".dashboard_components_id_seq restart with 1;
alter sequence "anv".module_dashboards_id_seq restart with 1;
alter sequence "anv".default_components_id_seq restart with 1;

insert into "anv".module_dashboards(name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted)
values ('Dashboard', 'ACCOUNTING', true, true, true, CURRENT_DATE, null, false);

insert into "anv".module_dashboards(name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted)
values ('HR Dashboard', 'HRMS', true, true, true, CURRENT_DATE, null, false);

insert into "anv".module_dashboards(name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted)
values ('Employee Portal', 'HRMS', true, false, true, CURRENT_DATE, null, false);

insert into "anv".module_dashboards(name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted)
values ('Dashboard', 'MYWORKSPACE', true, true, true, CURRENT_DATE, null, false);

insert into "anv".module_dashboards(name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted)
values ('Dashboard', 'PM', true, true, true, CURRENT_DATE, null, false);

insert into "anv".module_dashboards(name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted)
values ('Dashboard', 'CRM', true, true, true, CURRENT_DATE, null, false);

insert into "anv".default_components(width, height, minHeight, minWidth, componentName, componentCode, modules, report_code) values
 (6, 4, 3, 2, 'My Updates', 'MY_UPDATES', '["ACCOUNTING","HRMS","MYWORKSPACE","PM","CRM"]', null),
 (6, 4, 3, 2, 'ToDo List', 'TODO_LIST', '["ACCOUNTING","HRMS","MYWORKSPACE","PM","CRM"]', null),
 (6, 4, 3, 2, 'My Contacts', 'MY_CONTACTS', '["ACCOUNTING","HRMS","MYWORKSPACE","PM","CRM"]', null),
 (6, 4, 3, 2, 'My Agenda', 'MY_CALENDAR', '["ACCOUNTING","HRMS","MYWORKSPACE","PM","CRM"]', null),
 (12, 3, 12, 2, 'Combo', 'COMBO', '["ACCOUNTING","HRMS","MYWORKSPACE","PM","CRM"]', null),
 (4, 4, 2, 2, 'Unavailable Employees', 'UNAVAILABLE_EMPLOYEES_SUPERVISION', '["HRMS"]', null),
 (4, 4, 2, 2, 'Gender Ratio', 'GENDER_RATIO', '["HRMS"]', null),
 (4, 4, 2, 2, 'Public Holidays', 'HOLIDAY', '["HRMS"]', null),
 (4, 4, 2, 2, 'Aged Reports', 'AGED_REPORTS', '["ACCOUNTING"]', null),
 (4, 4, 2, 2, 'Incomes Vs Expenses', 'INCOME_VS_EXPENSE', '["ACCOUNTING"]', null),
 (4, 4, 2, 2, 'Sale & Purchase Transactions', 'SALE_PURCHASE_TRANSACTIONS', '["ACCOUNTING"]', null),
 (4, 4, 2, 2, 'Top Expenses(YTD)', 'TOP_EXPENSES', '["ACCOUNTING","HRMS"]', null),
 (4, 4, 2, 2, 'Project Overview', 'PROJET_OVERVIEW', '["PM"]', null),
 (4, 4, 2, 2, 'Projects Due This Month', 'PROJECT_DUE_THIS_MONTH', '["PM"]', null),
 (4, 4, 2, 2, 'Tasks Due Today', 'TASKS_DUE_TODAY', '["PM"]', null),
 (4, 4, 2, 2, 'Expired Documents', 'EXPIRED_DOCUMENTS', '["HRMS"]', null),
 (4, 4, 2, 2, 'Expiry Documents', 'EXPIRY_DOCUMENTS', '["HRMS"]', null),
 (4, 4, 2, 2, 'Monthly Payroll YTD', 'PAYROLL_YTD', '["HRMS"]', null),
 (4, 4, 2, 2, 'Payslip By Month YTD', 'PAYROLL_EMPLOYEE_YTD', '["HRMS"]', null),
 (4, 4, 2, 2, 'Leave Reason Status', 'LEAVE_REASON_STATUS', '["HRMS"]', null),
 (4, 4, 2, 2, 'Timeslot', 'TIMESLOT', '["HRMS"]', null),
 (4, 4, 2, 2, 'My Files', 'HRMS_MY_FILES', '["HRMS"]', null),
 (4, 4, 2, 2, 'My expenses', 'EMPLOYEE_TOP_EXPENSES', '["HRMS","MYWORKSPACE"]', null),
 (4, 4, 2, 2, 'Estimated vs Actual Time', 'ESTIMATEDVSACTUALTIME', '["PM"]', 'ESTIMATEDVSACTUALTIME'),
 (4, 4, 2, 2, 'Estimated vs Actual Cost', 'ESTIMATEDVSACTUALCOST', '["PM"]', 'ESTIMATEDVSACTUALCOST'),
 (4, 4, 2, 2, 'Active Tasks by Assignee', 'ACTIVETASKSBYASSIGNEE', '["PM"]', 'ACTIVETASKSBYASSIGNEE'),
 (4, 4, 2, 2, 'Sales Invoice By Status', 'SALESINVOICEBYSTATUSWIDGET', '["ACCOUNTING"]', 'SALESINVOICEBYSTATUSWIDGET');

insert into "anv".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (4,4,0,0, 2, 2, (select id from "anv".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "anv".default_components where componentCode = 'AGED_REPORTS' order by id limit 1)),
  (4,4,4,0, 2, 2, (select id from "anv".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "anv".default_components where componentCode = 'INCOME_VS_EXPENSE' order by id limit 1)),
  (12,4,0,4, 2, 2, (select id from "anv".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "anv".default_components where componentCode = 'SALE_PURCHASE_TRANSACTIONS' order by id limit 1)),
  (4,4,8,0, 2, 2, (select id from "anv".module_dashboards where module = 'ACCOUNTING' order by id limit 1), (select id from "anv".default_components where componentCode = 'TOP_EXPENSES' order by id limit 1));

insert into "anv".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,0,0, 2, 3, (select id from "anv".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "anv".default_components where componentCode = 'UNAVAILABLE_EMPLOYEES_SUPERVISION' order by id limit 1)),
  (6,4,6,4, 2, 3, (select id from "anv".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "anv".default_components where componentCode = 'GENDER_RATIO' order by id limit 1)),
  (6,4,6,8, 2, 3, (select id from "anv".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "anv".default_components where componentCode = 'EXPIRY_DOCUMENTS' order by id limit 1)),
  (6,4,0,8, 2, 2, (select id from "anv".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "anv".default_components where componentCode = 'HOLIDAY' order by id limit 1)),
  (6,4,6,0, 3, 4, (select id from "anv".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "anv".default_components where componentCode = 'PAYROLL_YTD' order by id limit 1)),
  (6,4,0,4, 2, 2, (select id from "anv".module_dashboards where module = 'HRMS' order by id limit 1), (select id from "anv".default_components where componentCode = 'TOP_EXPENSES' order by id limit 1));

insert into "anv".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,0,0, 2, 3, (select id from "anv".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "anv".default_components where componentCode = 'LEAVE_REASON_STATUS' order by id limit 1)),
  (6,4,6,0, 3, 4, (select id from "anv".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "anv".default_components where componentCode = 'PAYROLL_EMPLOYEE_YTD' order by id limit 1)),
  (6,4,0,4, 2, 3, (select id from "anv".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "anv".default_components where componentCode = 'TODO_LIST' order by id limit 1)),
  (6,4,6,8, 2, 3, (select id from "anv".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "anv".default_components where componentCode = 'HRMS_MY_FILES' order by id limit 1)),
  (12,3,0,12, 2, 12, (select id from "anv".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "anv".default_components where componentCode = 'COMBO' order by id limit 1)),
  (6,4,0,8, 2, 2, (select id from "anv".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "anv".default_components where componentCode = 'HOLIDAY' order by id limit 1)),
  (6,4,6,4, 2, 3, (select id from "anv".module_dashboards where module = 'HRMS' and name = 'Employee Portal' order by id limit 1), (select id from "anv".default_components where componentCode = 'EMPLOYEE_TOP_EXPENSES' order by id limit 1));

insert into "anv".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,0,7, 2, 3, (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1), (select id from "anv".default_components where componentCode = 'TODO_LIST' order by id limit 1)),
  (6,4,6,7, 2, 3, (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1), (select id from "anv".default_components where componentCode = 'MY_UPDATES' order by id limit 1)),
  (5,4,7,3, 2, 3, (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1), (select id from "anv".default_components where componentCode = 'MY_CONTACTS' order by id limit 1)),
  (7,4,0,3, 2, 3, (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1), (select id from "anv".default_components where componentCode = 'MY_CALENDAR' order by id limit 1)),
  (12,3,0,0, 2, 12, (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1), (select id from "anv".default_components where componentCode = 'COMBO' order by id limit 1));

insert into "anv".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (8,4,0,0, 2, 2, (select id from "anv".module_dashboards where module = 'PM' order by id limit 1), (select id from "anv".default_components where componentCode = 'PROJECT_DUE_THIS_MONTH' order by id limit 1)),
  (4,4,8,0, 2, 2, (select id from "anv".module_dashboards where module = 'PM' order by id limit 1), (select id from "anv".default_components where componentCode = 'PROJET_OVERVIEW' order by id limit 1)),
  (6,4,6,4, 2, 2, (select id from "anv".module_dashboards where module = 'PM' order by id limit 1), (select id from "anv".default_components where componentCode = 'TASKS_DUE_TODAY' order by id limit 1)),
  (6,4,0,4, 2, 3, (select id from "anv".module_dashboards where module = 'PM' order by id limit 1), (select id from "anv".default_components where componentCode = 'MY_UPDATES' order by id limit 1)),
  (4,4,0,8, 2, 2, (select id from "anv".module_dashboards where module = 'PM' order by id limit 1), (select id from "anv".default_components where report_code = 'ESTIMATEDVSACTUALTIME' order by id limit 1)),
  (4,4,4,8, 2, 2, (select id from "anv".module_dashboards where module = 'PM' order by id limit 1), (select id from "anv".default_components where report_code = 'ACTIVETASKSBYASSIGNEE' order by id limit 1)),
  (4,4,8,8, 2, 2, (select id from "anv".module_dashboards where module = 'PM' order by id limit 1), (select id from "anv".default_components where report_code = 'ESTIMATEDVSACTUALCOST' order by id limit 1));

insert into "anv".dashboard_components(width, height, x, y, minHeight, minWidth, dashboard_id, component_id) values
  (6,4,0,0, 2, 3, (select id from "anv".module_dashboards where module = 'CRM' order by id limit 1), (select id from "anv".default_components where componentCode = 'TODO_LIST' order by id limit 1)),
  (6,4,6,4, 2, 3, (select id from "anv".module_dashboards where module = 'CRM' order by id limit 1), (select id from "anv".default_components where componentCode = 'MY_UPDATES' order by id limit 1)),
  (6,4,0,4, 2, 3, (select id from "anv".module_dashboards where module = 'CRM' order by id limit 1), (select id from "anv".default_components where componentCode = 'MY_CONTACTS' order by id limit 1)),
  (6,4,6,0, 2, 3, (select id from "anv".module_dashboards where module = 'CRM' order by id limit 1), (select id from "anv".default_components where componentCode = 'MY_CALENDAR' order by id limit 1));


--AFTER UPDATE MODULE DASHBOARD SETTINGS
update "0".dashboard_setup_configuration set dashboard_id = (select id from "0".module_dashboards where module = 'MYWORKSPACE' order by id limit 1);
update "anv".dashboard_setup_configuration set dashboard_id = (select id from "anv".module_dashboards where module = 'MYWORKSPACE' order by id limit 1);