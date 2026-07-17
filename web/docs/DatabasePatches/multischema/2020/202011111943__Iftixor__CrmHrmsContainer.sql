
delete from "anv".container_item where propertyID is null;
delete from "anv".container_item where moduleCode in ('crm', 'hrms');
delete from "anv".container where moduleCode in ('crm', 'hrms');


insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('hrmsMain', 'company', 'hrms', 1, false, 'hrmsMainWelcome');

insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('employee', 'employee', 'hrms', 2, false, 'profile');

insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('availability', 'attendance', 'hrms', 3, false, 'availabilityHome');

insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('recruitment', 'recruitmentOnly', 'hrms', 4, false, 'recruitmentWelcome');

insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('goal', 'goalManagement', 'hrms', 5, false, 'goalwelcome');

insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('pa', 'performanceAppraisals', 'hrms', 6, false, 'pawelcomepage');


insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('crmWelcome', 'sales', 'crm', 1, false, 'leadList');

insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('accountList_2', 'cusService', 'crm', 2, false, 'caseList');

insert into "anv".container(code, defaultName, moduleCode, sorder, changed, preparedView)
values ('emailMarketing', 'marketing', 'crm', 3, false, 'campaignList');


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='LEAD_MANAGEMENT' limit 1), (select id from "anv".property where objectName='leads' limit 1), (select id from "anv".container where code='crmWelcome' limit 1), 1, 'crm');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='OPPORTUNITY_TRACKING' limit 1), (select id from "anv".property where objectName='opportunities' limit 1), (select id from "anv".container where code='crmWelcome' limit 1), 2, 'crm');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='SALES_QUOTES' limit 1), (select id from "anv".property where objectName='salequote' limit 1), (select id from "anv".container where code='crmWelcome' limit 1), 3, 'crm');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CRM_MODULE' limit 1), (select id from "anv".property where objectName='accountList' limit 1), (select id from "anv".container where code='crmWelcome' limit 1), 4, 'crm');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CONTACT_MANAGEMENT' limit 1), (select id from "anv".property where objectName='contacts' limit 1), (select id from "anv".container where code='crmWelcome' limit 1), 5, 'crm');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='REQUEST_FOR_QUOTES' limit 1), (select id from "anv".property where objectName='requestforquote' limit 1), (select id from "anv".container where code='crmWelcome' limit 1), 6, 'crm');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='ACTIVITIES' limit 1), (select id from "anv".property where objectName='eventList' limit 1), (select id from "anv".container where code='crmWelcome' limit 1), 7, 'crm');


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CASE_MANAGEMENT' limit 1), (select id from "anv".property where objectName='caseList' limit 1), (select id from "anv".container where code='accountList_2' limit 1), 1, 'crm');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='SOLUTION_MANAGEMENT' limit 1), (select id from "anv".property where objectName='solutionList' limit 1), (select id from "anv".container where code='accountList_2' limit 1), 2, 'crm');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='TASK_MANAGEMENT' limit 1), (select id from "anv".property where objectName='task' limit 1), (select id from "anv".container where code='accountList_2' limit 1), 3, 'crm');


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CRM_MODULE' limit 1), (select id from "anv".property where objectName='mailList' limit 1), (select id from "anv".container where code='emailMarketing' limit 1), 1, 'crm');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CRM_MODULE' limit 1), (select id from "anv".property where objectName='scheduled_messages' limit 1), (select id from "anv".container where code='emailMarketing' limit 1), 2, 'crm');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CRM_MODULE' limit 1), (select id from "anv".property where objectName='SentMessages' limit 1), (select id from "anv".container where code='emailMarketing' limit 1), 3, 'crm');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CRM_MODULE' limit 1), (select id from "anv".property where objectName='campaignList' limit 1), (select id from "anv".container where code='emailMarketing' limit 1), 4, 'crm');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='WEB_FORMS' limit 1), (select id from "anv".property where objectName='webFormsList' limit 1), (select id from "anv".container where code='emailMarketing' limit 1), 5, 'crm');


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='employee' limit 1), (select id from "anv".container where code='hrmsMain' limit 1), 1, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='employeeDocuemnts' limit 1), (select id from "anv".container where code='hrmsMain' limit 1), 2, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='companyDocuemnts' limit 1), (select id from "anv".container where code='hrmsMain' limit 1), 3, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='benefit_requests' limit 1), (select id from "anv".container where code='hrmsMain' limit 1), 4, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='meeting' limit 1), (select id from "anv".container where code='hrmsMain' limit 1), 5, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='certificateslist' limit 1), (select id from "anv".container where code='hrmsMain' limit 1), 6, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='EMPLOYEE_INCIDENTS' limit 1), (select id from "anv".property where objectName='incidentList' limit 1), (select id from "anv".container where code='hrmsMain' limit 1), 7, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='organizationChart' limit 1), (select id from "anv".container where code='hrmsMain' limit 1), 8, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='departmentOrgChartView' limit 1), (select id from "anv".container where code='hrmsMain' limit 1), 9, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='notifications' limit 1), (select id from "anv".container where code='hrmsMain' limit 1), 10, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='news_list' limit 1), (select id from "anv".container where code='hrmsMain' limit 1), 11, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='employeeProfileView' limit 1), (select id from "anv".container where code='employee' limit 1), 1, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='LEAVE_MANAGEMENT' limit 1), (select id from "anv".property where objectName='my_attendance' limit 1), (select id from "anv".container where code='employee' limit 1), 2, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='benefit_requests' limit 1), (select id from "anv".container where code='employee' limit 1), 3, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='EXPENSE_REPORTING' limit 1), (select id from "anv".property where objectName='EXPENSES_CLAIM' limit 1), (select id from "anv".container where code='employee' limit 1), 4, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PAYROLL' limit 1), (select id from "anv".property where objectName='cashadvanceList' limit 1), (select id from "anv".container where code='employee' limit 1), 5, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='payslipList' limit 1), (select id from "anv".container where code='employee' limit 1), 6, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='employeeDocuemnts' limit 1), (select id from "anv".container where code='employee' limit 1), 7, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='dependents' limit 1), (select id from "anv".container where code='employee' limit 1), 8, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='GOAL_MANAGEMENT' limit 1), (select id from "anv".property where objectName='personalgoal' limit 1), (select id from "anv".container where code='employee' limit 1), 9, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='EMPLOYEE_INCIDENTS' limit 1), (select id from "anv".property where objectName='incidentList' limit 1), (select id from "anv".container where code='employee' limit 1), 9, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='talentProfile' limit 1), (select id from "anv".container where code='employee' limit 1), 10, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CORE' limit 1), (select id from "anv".property where objectName='my_attendance' limit 1), (select id from "anv".container where code='availability' limit 1), 1, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CORE' limit 1), (select id from "anv".property where objectName='teamAvailabilityView' limit 1), (select id from "anv".container where code='availability' limit 1), 2, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CORE' limit 1), (select id from "anv".property where objectName='leave_request_list' limit 1), (select id from "anv".container where code='availability' limit 1), 3, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CORE' limit 1), (select id from "anv".property where objectName='attendanceReport' limit 1), (select id from "anv".container where code='availability' limit 1), 4, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='HRMS_MODULE' limit 1), (select id from "anv".property where objectName='annualLeaveBalance' limit 1), (select id from "anv".container where code='availability' limit 1), 5, 'hrms');


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='RECRUITMENT_SYSTEM' limit 1), (select id from "anv".property where objectName='vacancy' limit 1), (select id from "anv".container where code='recruitment' limit 1), 1, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='RECRUITMENT_SYSTEM' limit 1), (select id from "anv".property where objectName='candidate' limit 1), (select id from "anv".container where code='recruitment' limit 1), 2, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='RECRUITMENT_SYSTEM' limit 1), (select id from "anv".property where objectName='shortListView' limit 1), (select id from "anv".container where code='recruitment' limit 1), 3, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='RECRUITMENT_SYSTEM' limit 1), (select id from "anv".property where objectName='placement' limit 1), (select id from "anv".container where code='recruitment' limit 1), 5, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='RECRUITMENT_SYSTEM' limit 1), (select id from "anv".property where objectName='eventList' limit 1), (select id from "anv".container where code='recruitment' limit 1), 6, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='GOAL_MANAGEMENT' limit 1), (select id from "anv".property where objectName='personalgoal' limit 1), (select id from "anv".container where code='goal' limit 1), 1, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='GOAL_MANAGEMENT' limit 1), (select id from "anv".property where objectName='GROUP_GOAL' limit 1), (select id from "anv".container where code='goal' limit 1), 2, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='GOAL_MANAGEMENT' limit 1), (select id from "anv".property where objectName='departmentgoal' limit 1), (select id from "anv".container where code='goal' limit 1), 3, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='GOAL_MANAGEMENT' limit 1), (select id from "anv".property where objectName='projectgoal' limit 1), (select id from "anv".container where code='goal' limit 1), 4, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='GOAL_MANAGEMENT' limit 1), (select id from "anv".property where objectName='businessgoal' limit 1), (select id from "anv".container where code='goal' limit 1), 5, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='GOAL_MANAGEMENT' limit 1), (select id from "anv".property where objectName='companygoal' limit 1), (select id from "anv".container where code='goal' limit 1), 6, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PERFORMANCE_APPRAISAL' limit 1), (select id from "anv".property where objectName='simpleAppraisal' limit 1), (select id from "anv".container where code='pa' limit 1), 1, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PERFORMANCE_APPRAISAL' limit 1), (select id from "anv".property where objectName='appraisalsArchive' limit 1), (select id from "anv".container where code='pa' limit 1), 2, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PERFORMANCE_APPRAISAL' limit 1), (select id from "anv".property where objectName='appraisalTemplate' limit 1), (select id from "anv".container where code='pa' limit 1), 3, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PERFORMANCE_APPRAISAL' limit 1), (select id from "anv".property where objectName='competencesView' limit 1), (select id from "anv".container where code='pa' limit 1), 4, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PERFORMANCE_APPRAISAL' limit 1), (select id from "anv".property where objectName='competencesGroupView' limit 1), (select id from "anv".container where code='pa' limit 1), 5, 'hrms');

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PERFORMANCE_APPRAISAL' limit 1), (select id from "anv".property where objectName='performanceNote' limit 1), (select id from "anv".container where code='pa' limit 1), 6, 'hrms');
