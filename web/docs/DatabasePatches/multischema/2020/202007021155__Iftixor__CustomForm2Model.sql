

delete from "anv".model where formid='CASE_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'CASE_FORM',false,'Case Form','CrmCase',false);

delete from "anv".model where formid='CLIENT_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'CLIENT_FORM',false,'Client Form','CrmAccount',false);

delete from "anv".model where formid='CONTACT_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'CONTACT_FORM',false,'Contact Form','Contact',false);

delete from "anv".model where formid='ACCOUNT_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'ACCOUNT_FORM',false,'Account Form','CrmAccount',false);

delete from "anv".model where formid='LEAD_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'LEAD_FORM',false,'Lead Form','Lead',false);

delete from "anv".model where formid='OPPORTUNITY_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'OPPORTUNITY_FORM',false,'Opportunity Form','Opportunity',false);


delete from "anv".model where formid='LOGACALL_FORM';
insert into "anv".model (formid, title, viewname, active) values('LOGACALL_FORM', 'Log A Call Form', 'LogACall', true);

delete from "anv".model where formid='ACTIVITY_FORM';
insert into "anv".model (formid, title, viewname, active) values('ACTIVITY_FORM', 'Activity Form', 'Activity', true);

delete from "anv".model where formid='LOGACALL_FORM_VIEW';
insert into "anv".model (formid, title, viewname, active) values('LOGACALL_FORM_VIEW', 'Log A Call Form View', 'LogACall', true);

delete from "anv".model where formid='ACTIVITY_VIEW_FORM';
insert into "anv".model (formid, title, viewname, active) values('ACTIVITY_VIEW_FORM', 'Activity Form View', 'Activity', true);

delete from "anv".model where formid='PROJECT_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'PROJECT_FORM',false,'Add Edit Project','Project',false);

delete from "anv".model where formid='TASK_MAX_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'TASK_MAX_FORM',false,'Add Edit Task','Task',false);

delete from "anv".model where formid='ISSUE_FORM';
insert into "anv".model (formid, title, viewname, active)
values('ISSUE_FORM', 'Issue Form', 'Issues', true);

delete from "anv".model where formid='CONTRACT_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'CONTRACT_FORM',false,'Contract Form','Contract',false);

delete from "anv".model where formid='HRMS_EMPLOYEE_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'HRMS_EMPLOYEE_FORM',false,'HRMS Employee Form','Employee',false);

delete from "anv".model where formid='PAYROLL_EMPLOYEE_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'PAYROLL_EMPLOYEE_FORM',false,'Payroll Employee Form','Employee',false);

delete from "anv".model where formid='PM_EMPLOYEE_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'PM_EMPLOYEE_FORM',false,'PM Employee Form','Employee',false);

delete from "anv".model where formid='SETTINGS_EMPLOYEE_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'SETTINGS_EMPLOYEE_FORM',false,'Setting Employee Form','Employee',false);

delete from "anv".model where formid='PERSONAL_GOAL_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'PERSONAL_GOAL_FORM',false,'Personal Goal Form','PersonalGoal',false);

delete from "anv".model where formid='DEPARTMENT_GOAL_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'DEPARTMENT_GOAL_FORM',false,'Department Goal Form','DepartmentGoal',false);

delete from "anv".model where formid='PROJECT_GOAL_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'PROJECT_GOAL_FORM',false,'Project Goal Form','ProjectGoal',false);

delete from "anv".model where formid='BUSINESS_GOAL_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'BUSINESS_GOAL_FORM',false,'Businees Goal Form','BusinessGoal',false);

delete from "anv".model where formid='COMPANY_GOAL_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'COMPANY_GOAL_FORM',false,'Company Goal Form','CompanyGoal',false);

delete from "anv".model where formid='ONBOARDING_STEP_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'ONBOARDING_STEP_FORM',false,'Onboarding Step Form','OnboardingStep',false);

delete from "anv".model where formid='MEETING_MINUTES';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'MEETING_MINUTES',false,'Meeting Minutes Form','MeetingMInutesView',false);

delete from "anv".model where formid='LEAVE_REQUEST_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'LEAVE_REQUEST_FORM',false,'Leave Request Form','LeaveRequest',false);

delete from "anv".model where formid='CANDIDATE_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'CANDIDATE_FORM',false,'Candidate Form','Candidate',false);

delete from "anv".model where formid='VACANCY_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'VACANCY_FORM',false,'Vacancy Form','Vacancy',false);

delete from "anv".model where formid='PLACEMENT_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'PLACEMENT_FORM',false,'Placement Form','Placement',false);

delete from "anv".model where formid='COMPANY_SETTINGS_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'COMPANY_SETTINGS_FORM',false,'Company Settings Form','CompanySettings',false);

delete from "anv".model where formid='SUPPLIER_FORM';
insert into "anv".model(active,certificateform,formid,stepform,title,viewname,customform)
values(true,false,'SUPPLIER_FORM',false,'Supplier Form','CrmAccount',false);