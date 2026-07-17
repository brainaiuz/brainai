
ALTER TABLE "anv".custom_form DROP CONSTRAINT IF EXISTS "fk_1blrsiix3yp22vdg7992mtlyx"  ;
ALTER TABLE "anv".property DROP CONSTRAINT IF EXISTS "fk_3qc7evdeg1q12ss0q147ibptw"  ;
ALTER TABLE "anv".container_item DROP CONSTRAINT IF EXISTS "fk_oaf1c6wxb1n90d0mpby9chuyf";

delete from "anv".property where objectname in (select objectname from "anv".property group by objectname having count(id)>1)
and id not in (select max(id) from "anv".property group by objectname having count(id)>1);

ALTER TABLE "anv".property DROP CONSTRAINT IF EXISTS property_objectname_unique;
ALTER TABLE "anv".property ADD CONSTRAINT property_objectname_unique UNIQUE (objectname);

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('requestforquote', 'RFQ', 'RFQ', 'RFQ', 'RFQ', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('requestforpurchase', 'RFP', 'RFP', 'RFP', 'RFP', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('issue', 'Issue', 'Issue', 'Issues', 'I', 'pm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('recurringinvoice', 'Recurring Invoice', 'Recurring Invoice', 'Recurring Invoices', 'RI', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('productsOrServices', 'Product/Service', 'Product/Service', 'Products/Services', 'P/S', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('timesheet', 'Timesheet', 'Timesheet', 'Timesheet', 'Timesheet', 'pm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('contacts', 'Contacts', 'Contact', 'Contacts', 'C', 'crm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('fixedassets', 'Fixed Asset', 'Fixed Asset', 'Fixed Assets', 'FA', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('leads', 'Leads', 'Lead', 'Leads', 'L', 'crm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('saleorder', 'Sales Order', 'Sales Order', 'Sales Orders', 'SO', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('supplierList', 'Supplier Center', 'Supplier', 'Supplier Center', 'SC', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('goodsreceivednotes', 'Goods Received Notes', 'Goods Received Note', 'Goods Received Notes', 'GRN', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('goodsdeliverednotes', 'Goods Delivered Notes', 'Goods Delivered Note', 'Goods Delivered Notes', 'GDN', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('subProjectList', 'Sub Projects', 'Sub Project', 'Sub Projects', 'SP', 'pm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('departmentList', 'Departments', 'Department', 'Departments', 'D', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('solutionList', 'Solutions', 'Solution', 'Solutions', 'SOL', 'crm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('mailList', 'Mailing Lists', 'Mailing List', 'Mailing Lists', 'ML', 'crm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('scheduled_messages', 'Scheduled Messages', 'Scheduled Message', 'Scheduled Messages', 'SCHM', 'crm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('SentMessages', 'Sent Messages', 'Sent Message', 'Sent Messages', 'SM', 'crm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('campaignList', 'Campaigns', 'Campaign', 'Campaigns', 'CA', 'crm', false ) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('webFormsList', 'Web Forms', 'Web Form', 'Web Forms', 'WF', 'crm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('employee', 'Employees List', 'Employee List', 'Employees List', 'EMP', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('employeeDocuemnts', 'Employee Docs', 'Employee Doc', 'Employee Docs', 'EmpDoc', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('companyDocuemnts', 'Company Docs', 'Company Doc', 'Company Docs', 'CompDoc', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('benefit_requests', 'Benefit Requests', 'Benefit Request', 'Benefit Requests', 'BR', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('meeting', 'Meeting Minutes', 'Meeting Minute', 'Meeting Minutes', 'MM', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('certificateslist', 'Certificates', 'Certificate', 'Certificates', 'Cer', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('incidentList', 'Incidents', 'Incident', 'Incidents', 'Inc', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('organizationChart', 'Supervisor Structure', 'Supervisor Structure', 'Supervisor Structure', 'SS', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('departmentOrgChartView', 'Organization Chart', 'Organization Chart', 'Organization Chart', 'OrgChart', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('notifications', 'Notifications', 'Notification', 'Notifications', 'NTF', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('news_list', 'Company News', 'Company New', 'Company News', 'CompNew', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('employeeProfileView', 'Employee Profile', 'Employee Profile', 'Employee Profile', 'EmpProf', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('my_attendance', 'My Attendance', 'My Attendance', 'My Attendance', 'MyAT', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('cashadvanceList', 'Cash Advances', 'Cash Advance', 'Cash Advances', 'CA', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('payslipList', 'Payslips', 'Payslip', 'Payslips', 'PAY', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('dependents', 'Dependents', 'Dependent', 'Dependents', 'Dep', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('personalgoal', 'Personal Goals', 'Personal Goal', 'Personal Goals', 'PerGoal', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('talentProfile', 'Talent Profile', 'Talent Profile', 'Talent Profile', 'TalProf', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('teamAvailabilityView', 'Attendance Tracking', 'Attendance Tracking', 'Attendance Tracking', 'AT', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('leave_request_list', 'Leave Requests', 'Leave Request', 'Leave Requests', 'LR', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('attendanceReport', 'Attendance Report', 'Attendance Report', 'Attendance Report', 'AR', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('annualLeaveBalance', 'Annual Leave Report', 'Annual Leave Report', 'Annual Leave Report', 'ALR', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('vacancy', 'Vacancies', 'Vacancy', 'Vacancies', 'Vac', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('candidate', 'Candidates', 'Candidate', 'Candidates', 'Can', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('shortListView', 'Shortlists', 'Shortlist', 'Shortlists', 'Short', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('placement', 'Placements', 'Placement', 'Placements', 'PL', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('GROUP_GOAL', 'Group Goals', 'Group Goal', 'Group Goals', 'GrGoal', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('departmentgoal', 'Department Goals', 'Department Goal', 'Department Goals', 'DPGoal', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('projectgoal', 'Project Goals', 'Project Goal', 'Project Goals', 'PRGoal', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('businessgoal', 'Business Goals', 'Business Goal', 'Business Goals', 'BSSGoal', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('companygoal', 'Company Goals', 'Company Goal', 'Company Goals', 'CompGoal', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('companygoal', 'Company Goals', 'Company Goal', 'Company Goals', 'CompGoal', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('simpleAppraisal', 'Simple Appraisals', 'Simple Appraisal', 'Simple Appraisals', 'SA', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('appraisalsArchive', 'Appraisals Archive', 'Appraisal Archive', 'Appraisals Archive', 'AA', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('appraisalTemplate', 'Templates', 'Template', 'Templates', 'Temp', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('competencesView', 'Competencies', 'Competency', 'Competencies', 'CO', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('competencesGroupView', 'Competency Group', 'Competency Group', 'Competency Group', 'COG', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('performanceNote', 'Performance Notes', 'Performance Note', 'Performance Notes', 'PN', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('bankaccount', 'Bank Accounts', 'Bank Account', 'Bank Accounts', 'BA', 'accounting', false, false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('logCall', 'Log a Call', 'Log a Call', 'Log a Calls', 'LC', 'crm', false, false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('caseList', 'Cases', 'Case', 'Cases', 'CS', 'crm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('eventList', 'Activity', 'Activity', 'Activities', 'E', 'crm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('accountList', 'Companies', 'Company', 'Companies', 'C', 'crm', false, false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('opportunities', 'Opportunities', 'Opportunity', 'Opportunities', 'O', 'crm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('clientList', 'Customer Center', 'Customer Center', 'Customer Center', 'CC', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('purchaseorder', 'Purchase Order', 'Purchase Order', 'Purchase Orders', 'PO', 'accounting,logistics,crm,profile,pm,settings', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('Hrms', 'Humans', 'Human', 'Humans', 'H', 'hrms', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('EXPENSES_CLAIM', 'Expense Claim', 'Expense Claim', 'Expense Claims', 'EC', 'accounting, pm, hrms, payroll, crm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('inventoryitems', 'Inventory Item', 'Inventory Item', 'Inventory Items', 'II', 'accounting', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('projectList', 'Projects', 'Project', 'Projects', 'P', 'pm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('saleinvoice', 'Sales Invoice', 'Sales Invoice', 'Sales Invoices', 'SI', 'accounting,crm,pm,profile,settings', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('salequote', 'Sales Quote', 'Sales Quote', 'Sales Quotes', 'SQ', 'accounting,crm,pm,profile,settings', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('saleorder', 'Sales Order', 'Sales Order', 'Sales Orders', 'SO', 'accounting,crm,pm,profile,settings', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('task', 'Tasks', 'Task', 'Tasks', 'T', 'pm', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('purchaseinvoice', 'Purchase Invoice', 'Purchase Invoice', 'Purchase Invoices', 'PI', 'accounting,logistics,crm,settings', false) on conflict do nothing;

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('recurringbill', 'Recurring Bill', 'Recurring Bill', 'Recurring Bills', 'RB', 'accounting', false) on conflict do nothing;

update "anv".property set defaultname='Activity',plural='Activities',singular='Activity' where objectName='eventList';
update "anv".property set convertitems ='[{"code":"TASK","name":"Task"},{"code":"callLog","name":"Log a Call"},{"code":"event","name":"Event"},{"code":"SMS","name":"SMS"}]' where objectname='logCall';
