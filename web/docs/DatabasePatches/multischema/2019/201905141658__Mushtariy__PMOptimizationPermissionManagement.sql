
update permission set sorder=1, parent=(select id from permission where code = 'PM_MAIN_MENU'), name= 'Task List' where code='PM_TASKS_LIST';

update permission set sorder=1, parent=(select id from permission where code = 'PM_TASKS_LIST'), name= 'Show All' where code='PM_SHOW_ALL_TASKS';
update permission set sorder=2, parent=(select id from permission where code = 'PM_TASKS_LIST'), name= 'Show Non-assigned' where code='PM_SHOW_UNASSIGNED_TASKS';
update permission set sorder=3, parent=(select id from permission where code = 'PM_TASKS_LIST'), name= 'Customize' where code='PM_TASK_LIST_CUSTOMIZE_BUTTON';
update permission set sorder=4, parent=(select id from permission where code = 'PM_TASKS_LIST'), name= 'Import' where code='PM_TASK_LIST_IMPORT_BUTTON';
update permission set sorder=5, parent=(select id from permission where code = 'PM_TASKS_LIST'), name= 'Export' where code='PM_TASKS_PDF_EXCEL_EXPORT';
update permission set sorder=6, parent=(select id from permission where code = 'PM_TASKS_LIST'), name= 'Add' where code='PM_TASKS_ADD';
update permission set sorder=8, parent=(select id from permission where code = 'PM_TASKS_LIST'), name= 'Assign to Other Project Members' where code='PM_ASSIGN_TASK_TO_MEMBER';
update permission set sorder=9, parent=(select id from permission where code = 'PM_TASKS_LIST'), name= 'Add Multiple Tasks' where code='PM_TASKS_ADD_MULTI';
update permission set sorder=10, parent=(select id from permission where code = 'PM_TASKS_LIST'), name= 'Edit' where code='PM_TASKS_EDIT';
update permission set sorder=11, parent=(select id from permission where code = 'PM_TASKS_LIST'), name= 'Delete' where code='PM_TASKS_REMOVE';
update permission set sorder=12, parent=(select id from permission where code = 'PM_TASKS_LIST'), name= 'Copy Task to New' where code='COPY_TASK';

update permission set sorder=1, parent=(select id from permission where code = 'COPY_TASK'), name= 'Timer' where code='PM_TASKS_TIMER';
update permission set sorder=2, parent=(select id from permission where code = 'COPY_TASK'), name= 'Costs' where code='PM_TASKS_VIEW_PROJECT_COST';
update permission set sorder=3, parent=(select id from permission where code = 'COPY_TASK'), name= 'Documents' where code='PM_TASKS_DOCUMENTS';
update permission set sorder=4, parent=(select id from permission where code = 'COPY_TASK'), name= 'Issues' where code='PM_TASKS_ISSUE';
update permission set sorder=5, parent=(select id from permission where code = 'COPY_TASK'), name= 'Links' where code='PM_TASK_LINKS';
update permission set sorder=6, parent=(select id from permission where code = 'COPY_TASK'), name= 'Emails' where code='PM_TASKS_EMAILS';
update permission set sorder=7, parent=(select id from permission where code = 'COPY_TASK'), name= 'More Button' where code='PM_TASK_LIST_FACET_FILTER_OVERALL_STATUS';
update permission set sorder=1, parent=(select id from permission where code = 'PM_TASKS_EMAILS'), name= 'Compose' where code='PM_TASKS_COMPOSE';

update permission set sorder=2, parent=(select id from permission where code = 'PM_MAIN_MENU'), name= 'Issues' where code='PM_ISSUE_LIST';

update permission set sorder=1, parent=(select id from permission where code = 'PM_ISSUE_LIST'), name= 'List Customize' where code='PM_ISSUE_LIST_CUSTOMIZE_BUTTON';
update permission set sorder=2, parent=(select id from permission where code = 'PM_ISSUE_LIST'), name= 'Add' where code='PM_ISSUE_ADD';
update permission set sorder=1, parent=(select id from permission where code = 'PM_ISSUE_ADD'), name= 'Assign to Member' where code='PM_ASSIGN_ISSUE_TO_MEMBER';
update permission set sorder=3, parent=(select id from permission where code = 'PM_ISSUE_LIST'), name= 'Edit' where code='PM_ISSUE_EDIT';
update permission set sorder=4, parent=(select id from permission where code = 'PM_ISSUE_LIST'), name= 'Delete' where code='PM_ISSUE_REMOVE';
update permission set sorder=3, parent=(select id from permission where code = 'PM_MAIN_MENU'), name= 'Timesheet' where code='PM_TIMESHEET';
update permission set sorder=1, parent=(select id from permission where code = 'PM_TIMESHEET'), name= 'Approvers' where code='PM_TIMESHEET_APPROVERS';
update permission set sorder=4, parent=(select id from permission where code = 'PM_MAIN_MENU'), name= 'Monthly Timesheet' where code='MONTHLY_TIMESHEET';
update permission set sorder=5, parent=(select id from permission where code = 'PM_MAIN_MENU'), name= 'Timesheet approval' where code='PM_TIMESHEET_APPROVAL';
update permission set sorder=1, parent=(select id from permission where code = 'PM_TIMESHEET_APPROVAL'), name= 'Approve/Reject ' where code='PM_APPROVE_REJECT_ALL_TIMESHEETS';
update permission set sorder=2, parent=(select id from permission where code = 'PM_TIMESHEET_APPROVAL'), name= 'Review' where code='PM_APPROVE_REJECT';

update permission set sorder=6, parent=(select id from permission where code = 'PM_MAIN_MENU'), name= 'Project List' where code='PM_PROJECT_LIST';

update permission set sorder=1, parent=(select id from permission where code = 'PM_PROJECT_LIST'), name= 'See All' where code='PM_SEE_ALL_PROJECTS';
update permission set sorder=2, parent=(select id from permission where code = 'PM_PROJECT_LIST'), name= 'Customize' where code='PM_PROJECT_LIST_CUSTOMIZE_BUTTON';
update permission set sorder=3, parent=(select id from permission where code = 'PM_PROJECT_LIST'), name= 'Import' where code='PM_PROJECT_LIST_IMPORT_BUTTON';
update permission set sorder=4, parent=(select id from permission where code = 'PM_PROJECT_LIST'), name= 'Export' where code='PM_PROJECT_LIST_PDF_EXCEL_EXPORT';
update permission set sorder=5, parent=(select id from permission where code = 'PM_PROJECT_LIST'), name= 'Add' where code='PM_PROJECT_ADD';
update permission set sorder=6, parent=(select id from permission where code = 'PM_PROJECT_LIST'), name= 'Edit' where code='PM_PROJECT_EDIT';
update permission set sorder=7, parent=(select id from permission where code = 'PM_PROJECT_LIST'), name= 'Delete' where code='PM_PROJECT_REMOVE';

update permission set sorder=1, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Add Assignees' where code='PM_ADD_ASSIGNEES_TO_PROJECT';
update permission set sorder=2, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Change Status' where code='PM_PROJECT_CHANGE_STATUS';
update permission set sorder=3, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Customer Name Clickable' where code='CLIENT_NAME_CLICKABLE';
update permission set sorder=4, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Cost' where code='PM_PROJECT_COST';
update permission set sorder=6, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Actual Time Spent' where code='PM_PROJECT_ACTUAL_TIME_SPENT';
update permission set sorder=7, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Project Employees' where code='PM_PROJECT_EMPLOYEES';
update permission set sorder=8, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Work Breakdown Structure' where code='PM_PROJECT_WORK_BREAKDOWN_STRUCTURE';

update permission set sorder=1, parent=(select id from permission where code = 'PM_PROJECT_WORK_BREAKDOWN_STRUCTURE'), name= 'Add' where code='PM_PROJECT_WORKSTREAM_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'PM_PROJECT_WORK_BREAKDOWN_STRUCTURE'), name= 'Edit' where code='PM_PROJECT_WORKSTREAM_EDIT';

update permission set sorder=9, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Budget Sheet' where code='PM_PROJECT_BUDGET_SHEET';
update permission set sorder=10, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Gantt Chart' where code='PM_PROJECT_GANTT_CHART';

update permission set sorder=1, parent=(select id from permission where code = 'PM_PROJECT_GANTT_CHART'), name= 'Edit' where code='GANTTCHART_EDIT_PERMISSION';

update permission set sorder=11, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Documents' where code='PM_PROJECT_DOCUMENTS';
update permission set sorder=12, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Sales Order' where code='PM_SALES_ORDER_LIST';
update permission set sorder=13, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Sales Quotes' where code='PM_SALES_QUOTE_LIST';
update permission set sorder=14, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Sales Invoices' where code='PM_SALES_INVOICE_LIST';
update permission set sorder=15, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Purchase Orders' where code='PM_PROJECT_PURCHASE_ORDER';
update permission set sorder=16, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Purchase Invoices' where code='PM_PROJECT_PURCHASE_INVOICE';
update permission set sorder=17, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Project Invoice' where code='PM_PROJECT_INVOICE';
update permission set sorder=18, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Expense Claims' where code='PM_PROJECT_EXPENSE_CLAIMS';
update permission set sorder=19, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Resource Work Load' where code='PM_PROJECT_RESOURCE_WORK_LOAD';
update permission set sorder=20, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Bill Of Materials' where code='BILL_OF_MATERIALS';

update permission set sorder=1, parent=(select id from permission where code = 'BILL_OF_MATERIALS'), name= 'Create Project Plan' where code='BILL_OF_MATERIAL_CREATE_PROJECT_PLAN';
update permission set sorder=2, parent=(select id from permission where code = 'BILL_OF_MATERIALS'), name= 'Submit to Manager' where code='BILL_OF_MATERIAL_SUBMIT_TO_MANAGER';
update permission set sorder=3, parent=(select id from permission where code = 'BILL_OF_MATERIALS'), name= 'Approve/Reject' where code='BILL_OF_MATERIAL_APPROVE_REJECT';
update permission set sorder=4, parent=(select id from permission where code = 'BILL_OF_MATERIALS'), name= 'Request materials' where code='BILL_OF_MATERIAL_REQUEST_MATERIALS';
update permission set sorder=5, parent=(select id from permission where code = 'BILL_OF_MATERIALS'), name= 'Edit' where code='BILL_OF_MATERIAL_EDIT';

update permission set sorder=21, parent=(select id from permission where code = 'PM_PROJECT_REMOVE'), name= 'Rate History' where code='PM_EMPLOYEE_RATE_HISTORY';

update permission set sorder=8, parent=(select id from permission where code = 'PM_PROJECT_LIST'), name= 'Issues' where code='PM_PROJECT_ISSUE';
update permission set sorder=9, parent=(select id from permission where code = 'PM_PROJECT_LIST'), name= 'Emails' where code='PM_PROJECT_EMAIL';

update permission set sorder=1, parent=(select id from permission where code = 'PM_PROJECT_EMAIL'), name= 'Compose' where code='PM_PROJECT_EMAIL_COMPOSE';



update permission set sorder=7, parent=(select id from permission where code = 'PM_MAIN_MENU'), name= 'Customer Center' where code='PM_CUSTOMER_LIST';
update permission set sorder=1, parent=(select id from permission where code = 'PM_CUSTOMER_LIST'), name= 'Add' where code='PM_CUSTOMER_ADD_CLIENT';
update permission set sorder=2, parent=(select id from permission where code = 'PM_CUSTOMER_LIST'), name= 'Edit' where code='PM_CUSTOMER_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'PM_CUSTOMER_LIST'), name= 'Delete' where code='PM_CUSTOMER_REMOVE_CLIENT';
update permission set sorder=4, parent=(select id from permission where code = 'PM_CUSTOMER_LIST'), name= 'Import' where code='PM_CUSTOMER_IMPORT';
update permission set sorder=5, parent=(select id from permission where code = 'PM_CUSTOMER_LIST'), name= 'Export' where code='PM_CUSTOMER_EXPORT';

update permission set sorder=8, parent=(select id from permission where code = 'PM_MAIN_MENU'), name= 'Employees' where code='PM_EMPLOYEE_LIST';
update permission set sorder=1, parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'), name= 'Show All' where code='PM_SHOW_ALL_EMPLOYEE_LIST';
update permission set sorder=2, parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'), name= 'Show Department Employees' where code='PM_SHOW_DEPARTMENT_EMPLOYEE_LIST';
update permission set sorder=3, parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'), name= 'Show Project Employees' where code='PM_SHOW_PROJECT_EMPLOYEE_LIST';
update permission set sorder=4, parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'), name= 'Show Location Employees' where code='PM_SHOW_LOCATION_EMPLOYEE_LIST';
update permission set sorder=5, parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'), name= 'Show Supervised Employees' where code='PM_SHOW_SUPERVISED_EMPLOYEE_LIST';
update permission set sorder=6, parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'), name= 'Add' where code='PM_EMPLOYEE_ADD';
update permission set sorder=7, parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'), name= 'Edit' where code='PM_EMPLOYEE_EDIT';
update permission set sorder=8, parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'), name= 'Edit Own Profile' where code='PM_EMPLOYEE_EDIT_OWN_PROFILE';
update permission set sorder=9, parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'), name= 'Activate/Deactivate' where code='PM_EMPLOYEE_ACTIVATE_DEACTIVATE';
update permission set sorder=10, parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'), name= 'Delete' where code='PM_EMPLOYEE_REMOVE';
update permission set sorder=11, parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'), name= 'Import from CSV' where code='SHOW_IMPORT_EMPLOYEE';
update permission set sorder=13, parent=(select id from permission where code = 'PM_EMPLOYEE_LIST'), name= 'Summary' where code='PM_EMPLOYEE_SUMMARY';

update permission set sorder=1, parent=(select id from permission where code = 'PM_EMPLOYEE_SUMMARY'), name= 'Show Role Widget' where code='PM_SHOW_EMPLOYEE_ROLE_WIDGET';
update permission set sorder=2, parent=(select id from permission where code = 'PM_EMPLOYEE_SUMMARY'), name= 'Rate' where code='PM_EMPLOYEE_RATE';
update permission set sorder=3, parent=(select id from permission where code = 'PM_EMPLOYEE_SUMMARY'), name= 'Birth Day' where code='PM_SHOW_EMPLOYEE_BIRTH_DAY';
update permission set sorder=4, parent=(select id from permission where code = 'PM_EMPLOYEE_SUMMARY'), name= 'Address' where code='PM_SHOW_EMPLOYEE_ADDRESS';
update permission set sorder=5, parent=(select id from permission where code = 'PM_EMPLOYEE_SUMMARY'), name= 'Show Information' where code='PM_SHOW_EMPLOYMENT_INFORMATION';
update permission set sorder=6, parent=(select id from permission where code = 'PM_EMPLOYEE_SUMMARY'), name= 'Own Employment Information' where code='PM_SHOW_OWN_EMPLOYMENT_INFORMATION';
update permission set sorder=7, parent=(select id from permission where code = 'PM_EMPLOYEE_SUMMARY'), name= 'Show Attachments' where code='PM_SHOW_EMPLOYEE_ATTACHMENT';
update permission set sorder=8, parent=(select id from permission where code = 'PM_EMPLOYEE_SUMMARY'), name= 'Show Additional Information' where code='PM_SHOW_ADDITIONAL_INFORMATION';
update permission set sorder=9, parent=(select id from permission where code = 'PM_MAIN_MENU'), name= 'Booking Items' where code='PM_BOOKING_ITEMS';

update permission set sorder=1, parent=(select id from permission where code = 'PM_BOOKING_ITEMS'), name= 'Add' where code='PM_BOOKING_ITEMS_ADD';
update permission set sorder=2, parent=(select id from permission where code = 'PM_BOOKING_ITEMS'), name= 'Edit' where code='PM_BOOKING_EDIT';
update permission set sorder=3, parent=(select id from permission where code = 'PM_BOOKING_ITEMS'), name= 'Add Reservation' where code='PM_ADD_RESERVATION';

update permission set sorder=10, parent=(select id from permission where code = 'PM_MAIN_MENU'), name= 'Resource Utilization' where code='PM_RESOURCE_UTILIZATION_LIST';

update permission set sorder=1, parent=(select id from permission where code = 'PM_RESOURCE_UTILIZATION_LIST'), name= 'Editable' where code='PM_RESOURCE_UTILIZATION_EDITABLE';
update permission set sorder=2, parent=(select id from permission where code = 'PM_RESOURCE_UTILIZATION_LIST'), name= 'Show All' where code='PM_SHOW_ALL_EMPLOYEES';
update permission set sorder=3, parent=(select id from permission where code = 'PM_RESOURCE_UTILIZATION_LIST'), name= 'Show Department Employees' where code='PM_SHOW_DEPARTMENT_EMPLOYEES';
update permission set sorder=4, parent=(select id from permission where code = 'PM_RESOURCE_UTILIZATION_LIST'), name= 'Show Project Employees' where code='PM_SHOW_PROJECT_EMPLOYEES';
update permission set sorder=5, parent=(select id from permission where code = 'PM_RESOURCE_UTILIZATION_LIST'), name= 'Show Supervised Employees' where code='PM_SHOW_SUPERVISED_EMPLOYEES';

update permission set sorder=11, parent=(select id from permission where code = 'PM_MAIN_MENU'), name= 'Contracts' where code='PM_CONTRACT_LIST';

update permission set sorder=1, parent=(select id from permission where code = 'PM_CONTRACT_LIST'), name= 'Add/Edit' where code='PM_CONTRACT_ADD_EDIT';
update permission set sorder=2, parent=(select id from permission where code = 'PM_CONTRACT_LIST'), name= 'Delete' where code='PM_CONTRACT_DELETE';
update permission set sorder=3, parent=(select id from permission where code = 'PM_CONTRACT_LIST'), name= 'Convert To Project' where code='PM_CONTRACT_CONVERT_TO_PROJECT';
update permission set sorder=4, parent=(select id from permission where code = 'PM_CONTRACT_LIST'), name= 'Reminder Receivers' where code='PM_CONTRACT_REMINDER';



delete from permission where code='PM_EVENT_ADD';
delete from permission where code='PM_EVENT_EDIT';
delete from permission where code='PM_EVENT_DELETE';
delete from permission where code='PM_EVENT_SUMMARY';
delete from permission where code='PM_SALES_INVOICE_EDIT';
delete from permission where code='PM_WELLCOME_VIEW';
delete from permission where code='PM_TASK_EMAIL_TRASH';
delete from permission where code='PM_TASK_LIST_MORE_BUTTON';
delete from permission where code='PM_TASK_PLANNED_VS_ACTUAL';
delete from permission where code='PM_TASKS_BUDGET';
delete from permission where code='PM_TASKS_NOTES';
delete from permission where code='PM_TASKS_COMMENTS';
delete from permission where code='PM_TASK_COMMENTS_SHOW_CLIENT_CONTACTS';
delete from permission where code='PM_TASKS_CASE';
delete from permission where code='PM_EMPLOYEE_NOTE';
delete from permission where code='PM_CUSTOMER_CLIENT_NOTES';
delete from permission where code='PM_PROJECT_EMAIL_TRASH';
delete from permission where code='TIMER_MAIN_MENU';
delete from permission where code='GETTING_STARTED';
delete from permission where code='PM_OPPORTUNITY_LIST';
delete from permission where code='PM_OPPORTUNITY_ADD';
delete from permission where code='PM_OPPORTUNITY_EDIT';
delete from permission where code='PM_OPPORTUNITY_DELETE';
delete from permission where code='PM_OPPORTUNITY_SUMMARY';
delete from permission where code='PM_SALES_QUOTE_EDIT';
delete from permission where code='PM_CONTACT_ADD';
delete from permission where code='PM_CONTACT_EDIT';
delete from permission where code='PM_CONTACT_DELETE';
delete from permission where code='PM_CONTACT_SUMMARY';
delete from permission where code='PM_CASE_ADD';
delete from permission where code='PM_CASE_EDIT';
delete from permission where code='PM_CASE_DELETE';
delete from permission where code='PM_CASE_SUMMARY';
delete from permission where code='PM_SALES_ORDER_ADD';
delete from permission where code='PM_SALES_ORDER_EDIT';
delete from permission where code='PM_SALES_ORDER_DELETE';
delete from permission where code='PM_SALES_ORDER_SUMMARY';
delete from permission where code='TIMESHEET_PLUGIN';
delete from permission where code='PM_TASK_NOTES_SHOW_CLIENT_CONTACTS';
delete from permission where code='PM_CASE_ADD_NEW';
delete from permission where code='PM_SALES_QUOTE_ADD';
delete from permission where code='PM_SALES_QUOTE_PDF';
delete from permission where code='PM_SALES_QUOTE_DELETE';
delete from permission where code='PM_SALES_QUOTE_SUMMARY';
delete from permission where code='PM_SALES_ORDER_PDF';
delete from permission where code='PM_SALES_INVOICE_PDF';
delete from permission where code='PM_SALES_INVOICE_DELETE';
delete from permission where code='PM_SALES_INVOICE_ADD';
delete from permission where code='PM_SALES_INVOICE_SUMMARY';
delete from permission where code='PM_PROJECT_EXPENSE_REPORT_ADD_TO_STAFF';
delete from permission where code='PM_PROJECT_EXPENSE_REPORT_EDIT';
delete from permission where code='PM_PROJECT_EXPENSE_REPORT_VOID';
delete from permission where code='PM_PROJECT_EXPENSE_REPORT_DELETE';
delete from permission where code='PM_LINKS';
delete from permission where code='PM_PROJECT_NOTES';
delete from permission where code='PM_CASE_LIST';
delete from permission where code='PM_CONTACT_LIST';
delete from permission where code='PM_EVENT_LIST';
delete from permission where code='PM_PROJECT_PLANNED_VS_ACTUAL';
delete from permission where code='PM_PROJECT_NOTES_SHOW_CLIENT_CONTACTS';
delete from permission where code='PM_PROJECT_CASE';
delete from permission where code='PM_PROJECT_SALES_QUOTE';
