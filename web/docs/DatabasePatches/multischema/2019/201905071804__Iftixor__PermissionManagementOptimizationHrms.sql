update permission set  sorder=1, parent=(select id from permission where code='HRMS_MAIN_MENU'), name='Humans' where code='HRMS_SECTION_TAB';
update permission set  sorder=1, parent=(select id from permission where code='HRMS_SECTION_TAB'), name='Manager Self Service' where code='HRMS_MANAGER_SELF_SERVICE_VIEW';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_SECTION_TAB'), name='Employee Self Service' where code='HRMS_EMPLOYEE_SELF_SERVICE_VIEW';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_SECTION_TAB'), name='Employees List' where code='HRMS_EMPLOYEES';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_SECTION_TAB'), name='Employee Documents List' where code='EMPLOYEE_DOCUMENTS_LIST';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_SECTION_TAB'), name='Company Documents List' where code='COMPANY_DOCUMENTS_LIST';


update permission set  sorder=1, parent=(select id from permission where code='COMPANY_DOCUMENTS_LIST'), name='Upload' where code='UPLOAD_COMPANY_DOCUMENTS';
update permission set  sorder=2, parent=(select id from permission where code='COMPANY_DOCUMENTS_LIST'), name='Edit' where code='EDIT_COMPANY_DOCUMENTS';
update permission set  sorder=3, parent=(select id from permission where code='COMPANY_DOCUMENTS_LIST'), name='Delete' where code='REMOVE_COMPANY_DOCUMENTS';


update permission set  sorder=1, parent=(select id from permission where code='EMPLOYEE_DOCUMENTS_LIST'), name='Show All' where code='VIEW_ALL_EMPLOYEE_DOCUMENTS';
update permission set  sorder=2, parent=(select id from permission where code='EMPLOYEE_DOCUMENTS_LIST'), name='Upload' where code='UPLOAD_EMPLOYEE_DOCUMENTS';
update permission set  sorder=3, parent=(select id from permission where code='EMPLOYEE_DOCUMENTS_LIST'), name='Edit' where code='EDIT_EMPLOYEE_DOCUMENTS';
update permission set  sorder=4, parent=(select id from permission where code='EMPLOYEE_DOCUMENTS_LIST'), name='Delete' where code='REMOVE_EMPLOYEE_DOCUMENTS';


update permission set  sorder=1, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Show All' where code='SHOW_ALL_EMPLOYEE_LIST';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Show Department Employees' where code='SHOW_DEPARTMENT_EMPLOYEE_LIST';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Show Project Employees' where code='SHOW_PROJECT_EMPLOYEE_LIST';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Show Location Employees' where code='SHOW_LOCATION_EMPLOYEE_LIST';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Show Supervised Employees' where code='SHOW_SUPERVISED_EMPLOYEE_LIST';
update permission set  sorder=6, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Activate/deactivate' where code='HRMS_ACTIVATE_DEACTIVATE';
update permission set  sorder=7, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Terminate' where code='HRMS_TERMINATE_EMPLOYMENT';
update permission set  sorder=8, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Delete' where code='HRMS_EMPLOYEE_REMOVE';
update permission set  sorder=9, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Import From CSV' where code='SHOW_IMPORT_EMPLOYEE_HRMS';
update permission set  sorder=10, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Export' where code='HRMS_EMPLOYEES_EXPORT_TO_PDF';
update permission set  sorder=11, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Add' where code='HRMS_ADD_NEW_EMPLOYEE';
update permission set  sorder=12, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Edit Own' where code='HRMS_EDIT_OWN_PROFILE';
update permission set  sorder=13, parent=(select id from permission where code='HRMS_EMPLOYEES'), name='Summary' where code='HRMS_EMPLOYEE_PROFILE';


update permission set  sorder=1, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE'), name='Basic Salary Field' where code='EMP_PROFILE_BASIC_SALARY';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE'), name='Bank Account Details' where code='SHOW_EMPLOYEE_BANK_DETAILS';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE') where code='SHOW_EMPLOYEE_PERSONAL_INFORMATION';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE'), name='Personal Identity Information' where code='SHOW_EMPLOYEE_PERSONAL_INFORMATION';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE'), name='Wage/charge Rates' where code='HRMS_EMPLOYEE_WAGE_RATE';
update permission set  sorder=6, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE'), name='Show Employment Information' where code='HRMS_SHOW_EMPLOYMENT_INFORMATION';
update permission set  sorder=7, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE'), name='Show Additional Information' where code='HRMS_SHOW_ADDITIONAL_INFORMATION';
update permission set  sorder=8, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE'), name='Birth Day' where code='HRMS_SHOW_EMPLOYEE_BIRTH_DAY';
update permission set  sorder=9, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE'), name='Address' where code='HRMS_SHOW_EMPLOYEE_ADDRESS';
update permission set  sorder=10, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE'), name='Attachments' where code='HRMS_SHOW_EMPLOYEE_ATTACHMENT';
update permission set  sorder=11, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE'), name='Payments/Deductions Table' where code='HRMS_PAYROLL_DEDUCTION_CATEGORIES';



update permission set  sorder=1, parent=(select id from permission where code='HRMS_ADD_NEW_EMPLOYEE'), name='Show Role Widget' where code='HRMS_SHOW_EMPLOYEE_ROLE_WIDGET';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_ADD_NEW_EMPLOYEE'), name='Edit' where code='HRMS_EDIT_PROFILE';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_ADD_NEW_EMPLOYEE'), name='Edit Goal Weights' where code='HRMS_EDIT_GOAL_WEIGHTS';

update permission set  context='SETTINGS' where code='HRMS_DEPARTMENT';
update permission set  context='SETTINGS' where code='HRMS_ADD_NEW_DEPARTMENT';
update permission set  context='SETTINGS' where code='HRMS_DEPARTMENT_NOTES';
update permission set  context='SETTINGS' where code='HRMS_SEE_ALL_DEPARTMENT_LIST';
update permission set  context='SETTINGS' where code='HRMS_DEPARTMENT_REMOVE';
update permission set  context='SETTINGS' where code='HRMS_EDIT_DEPARTMENT';
update permission set  context='SETTINGS' where code='HRMS_DEPARTMENTS_EXPORT_TO_PDF';
update permission set  context='SETTINGS' where code='HRMS_DEPARTMENT_SUMMARY';
update permission set  context='SETTINGS' where code='HRMS_POSITION';
update permission set  context='SETTINGS' where code='HRMS_POSITION_REMOVE';
update permission set  context='SETTINGS' where code='HRMS_ADD_NEW_POSITION';
update permission set  context='SETTINGS' where code='HRMS_POSITION_EDIT';
update permission set  context='SETTINGS' where code='HRMS_POSITION_SUMMARRY';
update permission set  context='SETTINGS' where code='HRMS_POSITION_RATES';
update permission set  context='SETTINGS' where code='HRMS_LOCATION';
update permission set  context='SETTINGS' where code='HRMS_REMOVE_LOCATION';
update permission set  context='SETTINGS' where code='HRMS_ADD_NEW_LOCATION';
update permission set  context='SETTINGS' where code='HRMS_LOCATION_SUMMARY';
update permission set  context='SETTINGS' where code='HRMS_EDIT_LOCATION';
update permission set  sorder=6, parent=(select id from permission where code='HRMS_SECTION_TAB'), name='Benefit Request List' where code='BENEFIT_REQUEST_LIST';
update permission set  sorder=7, parent=(select id from permission where code='HRMS_SECTION_TAB'), name='Meeting Minutes List' where code='MEETING_MINUTES_LIST';
update permission set  sorder=8, parent=(select id from permission where code='HRMS_SECTION_TAB'), name='Certificate List' where code='CETIFICATE_OF_EMPLOYMENT_LIST';
update permission set  sorder=9, parent=(select id from permission where code='HRMS_SECTION_TAB'), name='Supervisor Structure' where code='HRMS_ORGANIZATION_CHART_VIEW';
update permission set  sorder=10, parent=(select id from permission where code='HRMS_SECTION_TAB'), name='Organization Chart' where code='HRMS_TEAM_ORGANIZATION_CHART_VIEW';
update permission set  sorder=11, parent=(select id from permission where code='HRMS_SECTION_TAB'), name='Company News' where code='HRMS_COMPANY_NEWS';


update permission set  sorder=1, parent=(select id from permission where code='HRMS_COMPANY_NEWS'), name='Show All' where code='HRMS_COMPANY_NEWS_LIST';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_COMPANY_NEWS_LIST'), name='Add' where code='HRMS_COMPANY_NEWS_ADD';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_COMPANY_NEWS_LIST'), name='Edit' where code='HRMS_COMPANY_NEWS_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_COMPANY_NEWS_LIST'), name='Add Comments' where code='HRMS_COMPANY_NEWS_ADD_COMMENTS';


update permission set  sorder=1, parent=(select id from permission where code='CETIFICATE_OF_EMPLOYMENT_LIST'), name='Show All' where code='CETIFICATE_OF_EMPLOYMENT_SEE_ALL_CERTIFICATE_LIST';
update permission set  sorder=2, parent=(select id from permission where code='CETIFICATE_OF_EMPLOYMENT_LIST'), name='Add' where code='CETIFICATE_OF_EMPLOYMENT_ADD';
update permission set  sorder=3, parent=(select id from permission where code='CETIFICATE_OF_EMPLOYMENT_LIST'), name='Edit' where code='CETIFICATE_OF_EMPLOYMENT_EDIT';
update permission set  sorder=4, parent=(select id from permission where code='CETIFICATE_OF_EMPLOYMENT_LIST'), name='Customize' where code='CETIFICATE_OF_EMPLOYMENT_CUSTOMIZE';
update permission set  sorder=5, parent=(select id from permission where code='CETIFICATE_OF_EMPLOYMENT_LIST'), name='Delete' where code='CETIFICATE_OF_EMPLOYMENT_DELETE';
update permission set  sorder=6, parent=(select id from permission where code='CETIFICATE_OF_EMPLOYMENT_LIST'), name='Show Details' where code='CETIFICATE_OF_EMPLOYMENT_SHOW_DETAILS_INFORMATION';


update permission set  sorder=1, parent=(select id from permission where code='MEETING_MINUTES_LIST'), name='Show All' where code='SHOW_ALL_MEETING_MINUTES';
update permission set  sorder=2, parent=(select id from permission where code='MEETING_MINUTES_LIST'), name='Add' where code='ADD_MEETING_MINUTES';
update permission set  sorder=3, parent=(select id from permission where code='MEETING_MINUTES_LIST'), name='Edit' where code='EDIT_MEETING_MINUTES';
update permission set  sorder=4, parent=(select id from permission where code='MEETING_MINUTES_LIST'), name='Delete' where code='REMOVE_MEETING_MINUTES';
update permission set  sorder=5, parent=(select id from permission where code='MEETING_MINUTES_LIST'), name='Convert' where code='CONVERT_MEETING_MINUTES';


update permission set  sorder=1, parent=(select id from permission where code='BENEFIT_REQUEST_LIST'), name='Add' where code='ADD_BENEFIT_REQUEST';
update permission set  sorder=2, parent=(select id from permission where code='BENEFIT_REQUEST_LIST'), name='Edit' where code='EDIT_BENEFIT_REQUEST';
update permission set  sorder=3, parent=(select id from permission where code='BENEFIT_REQUEST_LIST'), name='Delete' where code='REMOVE_BENEFIT_REQUEST';
update permission set  sorder=4, parent=(select id from permission where code='BENEFIT_REQUEST_LIST'), name='Approve/reject' where code='APPROVE_REJECT_ALL_BENEFIT_REQUESTS';
update permission set  sorder=5, parent=(select id from permission where code='BENEFIT_REQUEST_LIST'), name='Add to Anybody' where code='ADD_BENEFIT_REQUEST_ANYBODY';
update permission set  sorder=6, parent=(select id from permission where code='BENEFIT_REQUEST_LIST'), name='Approver' where code='BENEFIT_REQUEST_APPROVER';
update permission set  sorder=7, parent=(select id from permission where code='BENEFIT_REQUEST_LIST'), name='Change Approver' where code='CHANGE_BENEFIT_REQUEST_APPROVER';
update permission set  sorder=12, parent=(select id from permission where code='HRMS_SECTION_TAB'), name='Salary Grades List' where code='HRMS_SALARY_GRADE';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_SALARY_GRADE'), name='Add' where code='HRMS_ADD_NEW_GRADE';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_SALARY_GRADE'), name='Edit' where code='HRMS_EDIT_GRADE';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_SALARY_GRADE'), name='Delete' where code='HRMS_GRADE_REMOVE';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_SALARY_GRADE'), name='Summary' where code='HRMS_GRADE_SUMMARY';
update permission set  context='SETTINGS' where code='HRMS_COMPANY_NEWS_CATEGORIES';
update permission set  context='SETTINGS' where code='HRMS_VIEW_EMPLOYEE_CHANGE_LOG';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_MAIN_MENU'), name='Employee Profile' where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), name='Employee Profile Summary' where code='HRMS_EMPLOYEE_PROFILE_SUMMARY';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), name='Leave Requests' where code='HRMS_LIVE_REQUEST';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), name='My Benefit Request' where code='MY_BENEFIT_REQUEST_LIST';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), name='Expense Reports List' where code='HRMS_EXPENCE_REPORT';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), name='Cash Advance List' where code='HRMS_CASH_ADVANCE_LIST';
update permission set  sorder=6, parent=(select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), name='Payslips List' where code='HRMS_PAYSLIP_LIST';
update permission set  sorder=7, parent=(select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), name='Documents List' where code='HRMS_DOCUMENT';
update permission set  sorder=8, parent=(select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), name='Dependents List' where code='HRMS_DEPENDENT';
update permission set  sorder=9, parent=(select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), name='Goals List' where code='HRMS_GOALS';
update permission set  sorder=10, parent=(select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), name='Incidents List' where code='HRMS_INCIDENT_LIST';
update permission set  sorder=11, parent=(select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), name='Onboarding Checklist' where code='HRMS_ONBOARDING_CHECKLIST_VIEW';
update permission set  sorder=12, parent=(select id from permission where code='HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), name='Talent Profile List' where code='HRMS_TALENT_PROFILE_LIST';


update permission set  sorder=1, parent=(select id from permission where code='HRMS_TALENT_PROFILE_LIST'), name='Add' where code='HRMS_TALENT_PROFILE_ADD';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_TALENT_PROFILE_LIST'), name='Edit' where code='HRMS_TALENT_PROFILE_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_TALENT_PROFILE_LIST'), name='Summary' where code='HRMS_TALENT_PROFILE_VIEW';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_TALENT_PROFILE_LIST'), name='Delete' where code='HRMS_TALENT_PROFILE_DELETE';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_TALENT_PROFILE_LIST'), name='Add Education' where code='HRMS_ADD_EDUCATION';
update permission set  sorder=6, parent=(select id from permission where code='HRMS_TALENT_PROFILE_LIST'), name='Edit Education' where code='HRMS_EDIT_EDUCATION';
update permission set  sorder=7, parent=(select id from permission where code='HRMS_TALENT_PROFILE_LIST'), name='Delete Education' where code='HRMS_REMOVE_EDUCATION';
update permission set  sorder=8, parent=(select id from permission where code='HRMS_TALENT_PROFILE_LIST'), name='Add Award' where code='HRMS_ADD_AWARD';
update permission set  sorder=9, parent=(select id from permission where code='HRMS_TALENT_PROFILE_LIST'), name='Edit Award' where code='HRMS_EDIT_AWARD';
update permission set  sorder=10, parent=(select id from permission where code='HRMS_TALENT_PROFILE_LIST'), name='Delete Award' where code='HRMS_REMOVE_AWARD';


update permission set  sorder=1, parent=(select id from permission where code='HRMS_ONBOARDING_CHECKLIST_VIEW'), name='Edit' where code='HRMS_ONBOARDING_CHECKLIST_EDIT';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_INCIDENT_LIST'), name='Add' where code='HRMS_ADD_NEW_INCIDENT';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_INCIDENT_LIST'), name='Edit' where code='HRMS_EDIT_INCIDENT';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_INCIDENT_LIST'), name='Summary' where code='HRMS_SUMMARY_INCIDENT';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_INCIDENT_LIST'), name='Delete' where code='HRMS_REMOVE_INCIDENT';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_INCIDENT_LIST'), name='Owner' where code='INCIDENT_OWNER';


update permission set  sorder=1, parent=(select id from permission where code='HRMS_GOALS'), name='Add Personal Goals' where code='HRMS_NEW_PERSONAL_GOALS';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_GOALS'), name='Add Department Goals' where code='HRMS_NEW_DEPARTMENT_GOAL';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_GOALS'), name='Add Project Goals' where code='HRMS_NEW_PROJECT_GOAL';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_GOALS'), name='Add Business Goals' where code='HRMS_NEW_BUSINESS_GOALS';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_GOALS'), name='Add Company Goals' where code='HRMS_NEW_COMPANY_GOALS';
update permission set  sorder=6, parent=(select id from permission where code='HRMS_GOALS'), name='Summary' where code='HRMS_GOAL_SUMMARY';
update permission set  sorder=7, parent=(select id from permission where code='HRMS_GOALS'), name='Edit' where code='HRMS_EDIT_GOAL';
update permission set  sorder=8, parent=(select id from permission where code='HRMS_GOALS'), name='Delete' where code='HRMS_GOAL_REMOVE';


update permission set  sorder=1, parent=(select id from permission where code='HRMS_DEPENDENT'), name='Add' where code='HRMS_ADD_NEW_DEPENDENT';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_DEPENDENT'), name='Edit' where code='HRMS_EDIT_DEPENDENT';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_DEPENDENT'), name='Summary' where code='HRMS_DEPENDENT_SUMMARY';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_DEPENDENT'), name='Delete' where code='HRMS_DEPENDENT_REMOVE';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_PAYSLIP_LIST'), name='PDF' where code='HRMS_PAYSLIP_PDF';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_EXPENCE_REPORT'), name='Add' where code='HRMS_ADD_NEW_EXPENSE_CLAIM';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_EXPENCE_REPORT'), name='Edit' where code='HRMS_EXPENSE_REPORT_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_EXPENCE_REPORT'), name='Summary' where code='HRMS_VIEW_EXPENSE_CLAIM';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_EXPENCE_REPORT'), name='Add/Summary Full Access' where code='HRMS_EXPENSE_ADD_VIEW_FULL_ACCESS';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_EXPENCE_REPORT'), name='Approve' where code='HRMS_CAN_APPROVE_EXPENSE_CLAIM';
update permission set  sorder=6, parent=(select id from permission where code='HRMS_EXPENCE_REPORT'), name='Void' where code='HRMS_EXPENSE_REPORT_VOID';
update permission set  sorder=7, parent=(select id from permission where code='HRMS_EXPENCE_REPORT'), name='Delete' where code='HRMS_EXPENCE_REPORT_REMOVE';


update permission set  sorder=1, parent=(select id from permission where code='HRMS_LIVE_REQUEST'), name='Add' where code='HRMS_ADD_REQUEST';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_LIVE_REQUEST'), name='Add For Others' where code='HRMS_LEAVE_REQUESTS_ADD_FOR_OTHERS';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_LIVE_REQUEST'), name='Edit' where code='HRMS_LEAVE_REQUESTS_EDIT_TYPE';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_LIVE_REQUEST'), name='Delete' where code='HRMS_REMOVE_REQUEST';
update permission set  sorder=6, parent=(select id from permission where code='HRMS_LIVE_REQUEST'), name='Send Notification Panel',context='HRMS'  where code='HRMS_LEAVE_REQUEST_SEND_NOTIFICATION';


update permission set  sorder=1, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE_SUMMARY'), name='Wage/charge Rates' where code='HRMS_EMPLOYEE_PROFILE_WAGE_RATE';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE_SUMMARY'), name='Visa Expiration Date' where code='HRMS_VISA_EXPIRATION_DATE';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE_SUMMARY'), name='Set Expiration Reminder' where code='HRMS_VISA_EXPIRATION_DATE_REMINDER';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_EMPLOYEE_PROFILE_SUMMARY'), name='Show Own Information' where code='HRMS_SHOW_OWN_EMPLOYMENT_INFORMATION';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_MAIN_MENU'), name='Goal Management', ismainmenu=false  where code='HRMS_GOAL_MANAGEMENT_TAB';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_GOAL_MANAGEMENT_TAB'), name='Personal Goals List' where code='HRMS_PERSONAL_GOALS';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_GOAL_MANAGEMENT_TAB'), name='Department Goals List' where code='HRMS_DEPARTMENT_GOALS';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_GOAL_MANAGEMENT_TAB'), name='Project Goals List' where code='HRMS_PROJECT_GOALS';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_GOAL_MANAGEMENT_TAB'), name='Business Goals List' where code='HRMS_BUSINESS_GOALS';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_GOAL_MANAGEMENT_TAB'), name='Company Goals List' where code='HRMS_COMPANY_GOALS';
update permission set  sorder=6, parent=(select id from permission where code='HRMS_GOAL_MANAGEMENT_TAB'), name='Goals Links' where code='HRMS_GOAL_LINKS';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_PERSONAL_GOALS'), name='Add' where code='HRMS_ADD_NEW_PERSONAL_GOALS';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_PERSONAL_GOALS'), name='Edit' where code='HRMS_EDIT_PERSONAL_GOAL';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_PERSONAL_GOALS'), name='Summary' where code='HRMS_PERSONAL_GOAL_SUMMARY';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_PERSONAL_GOALS'), name='Notes' where code='HRMS_NOTES_PERSONAL_GOAL';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_PERSONAL_GOALS'), name='Delete' where code='HRMS_PERSONAL_GOAL_REMOVE';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_DEPARTMENT_GOALS'), name='Add' where code='HRMS_ADD_NEW_DEPARTMENT_GOALS';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_DEPARTMENT_GOALS'), name='Edit' where code='HRMS_EDIT_DEPARTMENT_GOAL';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_DEPARTMENT_GOALS'), name='Summary' where code='HRMS_DEPARTMENT_GOAL_SUMMARY';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_DEPARTMENT_GOALS'), name='Notes' where code='HRMS_NOTES_DEPARTMENT_GOAL';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_DEPARTMENT_GOALS'), name='Delete' where code='HRMS_DEPARTMENT_GOAL_REMOVE';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_PROJECT_GOALS'), name='Add' where code='HRMS_ADD_NEW_PROJECT_GOALS';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_PROJECT_GOALS'), name='Edit' where code='HRMS_EDIT_PROJECT_GOAL';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_PROJECT_GOALS'), name='Summary' where code='HRMS_PROJECT_GOAL_SUMMARY';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_PROJECT_GOALS'), name='Notes' where code='HRMS_NOTES_PROJECT_GOAL';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_PROJECT_GOALS'), name='Delete' where code='HRMS_PROJECT_GOAL_REMOVE';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_BUSINESS_GOALS'), name='Add' where code='HRMS_ADD_NEW_BUSINESS_GOALS';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_BUSINESS_GOALS'), name='Edit' where code='HRMS_EDIT_BUSINESS_GOAL';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_BUSINESS_GOALS'), name='Summary' where code='HRMS_BUSINESS_GOAL_SUMMARY';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_BUSINESS_GOALS'), name='Notes' where code='HRMS_NOTES_BUSINESS_GOAL';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_BUSINESS_GOALS'), name='Delete' where code='HRMS_BUSINESS_GOAL_REMOVE';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_COMPANY_GOALS'), name='Add' where code='HRMS_ADD_NEW_COMPANY_GOALS';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_COMPANY_GOALS'), name='Edit' where code='HRMS_EDIT_COMPANY_GOAL';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_COMPANY_GOALS'), name='Summary' where code='HRMS_COMPANY_GOAL_SUMMARY';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_COMPANY_GOALS'), name='Notes' where code='HRMS_NOTES_COMPANY_GOAL';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_COMPANY_GOALS'), name='Delete' where code='HRMS_COMPANY_GOAL_REMOVE';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_MAIN_MENU'), name='Attendance Tracking' where code='HRMS_ATTENDANCE_TRACKING_TAB';

update permission set  sorder=5, parent=(select id from permission where code='HRMS_LIVE_REQUEST'), name='Approve'  where code='HRMS_APPROVE_LIVE_STATUS';
update permission set  sorder=1, parent=(select id from permission where code='HRMS_ATTENDANCE_TRACKING_TAB'), name='My Attendance'  where code='HRMS_MY_ATTENDANCE';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_ATTENDANCE_TRACKING_TAB'), name='Attendance Tracking List'  where code='HRMS_ATTENDANCE_TRACKING';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_ATTENDANCE_TRACKING_TAB'), name='Attendance Report'  where code='HRMS_ATTENDANCE_REPORT';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_ATTENDANCE_TRACKING_TAB'), name='Employee Leave Status List'  where code='HRMS_EMPLOYEE_LIVE_STATUS';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_ATTENDANCE_TRACKING_TAB'), name='Annual Leave Report'  where code='HRMS_ANNUAL_LEAVE_BALANCE_REPORT';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_ATTENDANCE_TRACKING_TAB'), name='Import Data'  where code='HRMS_IMPORT_ATTENDANCE_DATA';

update permission set  context='SETTINGS'  where code='HRMS_ANNUAL_ALLOWANCE';
update permission set  context='SETTINGS'  where code='HRMS_ADD_TIMESLOT';
update permission set  context='SETTINGS'  where code='HRMS_ADD_HOLIDAY';
update permission set  context='SETTINGS'  where code='HRMS_LEAVE_REQUEST_SEND_NOTIFICATION';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_MAIN_MENU'), name='Recruitment' where code='HRMS_RECRUITMENT';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_RECRUITMENT'), name='Vacancies List' where code='HRMS_VACANCY_LIST_VIEW';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_RECRUITMENT'), name='Candidates List' where code='HRMS_CANDIDATE_LIST_VIEW';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_RECRUITMENT'), name='Shortlists' where code='HRMS_SHORT_LIST_VIEW';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_RECRUITMENT'), name='Activities List' where code='HRMS_ACTIVITIES_VIEW';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_RECRUITMENT'), name='Placements List' where code='HRMS_PLACEMENT_LIST_VIEW';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_PLACEMENT_LIST_VIEW'), name='Add' where code='HRMS_ADD_PLACEMENT';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_PLACEMENT_LIST_VIEW'), name='Hire Candidate' where code='HRMS_HIRE_PLACEMENT';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_ACTIVITIES_VIEW'), name='Add Interview' where code='HRMS_ADD_NEW_ACTIVITY_EVENT';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_ACTIVITIES_VIEW'), name='Add Call Log' where code='HRMS_ADD_NEW_ACTIVITY_LOG_A_CALL';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_ACTIVITIES_VIEW'), name='Summary' where code='HRMS_SUMMARY_ACTIVITY';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_ACTIVITIES_VIEW'), name='Edit' where code='HRMS_EDIT_ACTIVITY';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_ACTIVITIES_VIEW'), name='Delete' where code='HRMS_REMOVE_ACTIVITY';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Add' where code='HRMS_ADD_CANDIDATE';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Edit' where code='HRMS_EDIT_CANDIDATE';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Change Status' where code='HRMS_CHANGE_STATUS_CANDIDATE';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Delete' where code='HRMS_DELETE_CANDIDATE';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Candidate Owner' where code='HRMS_SHOW_IN_CANDIDATE_OWNER';
update permission set  sorder=6, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Interview' where code='HRMS_INTERVIEW_CANDIDATE';
update permission set  sorder=7, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Select' where code='HRMS_SELECT_CANDIDATE';
update permission set  sorder=8, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Log a Call' where code='HRMS_CALL_CANDIDATE';
update permission set  sorder=9, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Make Placement' where code='HRMS_MAKE_PLACEMENT';
update permission set  sorder=10, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Add to Short List' where code='HRMS_ADD_TO_SHORT_LIST';
update permission set  sorder=11, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Match to Vacancies' where code='HRMS_CANDIDATE_MATCH_TO_VACANCY';
update permission set  sorder=12, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Show All' where code='HRMS_SHOW_ALL_CANDIDATES';
update permission set  sorder=13, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Show Owned' where code='HRMS_SHOW_OWNED_CANDIDATES';
update permission set  sorder=14, parent=(select id from permission where code='HRMS_CANDIDATE_LIST_VIEW'), name='Show Related' where code='HRMS_SHOW_RELATED_CANDIDATES';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_VACANCY_LIST_VIEW'), name='Add' where code='HRMS_ADD_VACANCY';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_VACANCY_LIST_VIEW'), name='Edit' where code='HRMS_EDIT_VACANCY';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_VACANCY_LIST_VIEW'), name='Change Status' where code='HRMS_CHANGE_STATUS_VACANCY';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_VACANCY_LIST_VIEW'), name='Delete' where code='HRMS_DELETE_VACANCY';
update permission set  sorder=6, parent=(select id from permission where code='HRMS_MAIN_MENU'), name='Documents' where code='HRMS_DOCUMENTS_MANAGEMENT';
update permission set  sorder=7, parent=(select id from permission where code='HRMS_MAIN_MENU'), name='Performance Appraisals' where code='HRMS_PERFORMANCE_APPRAISALS_TAB';

update permission set  sorder=2, parent=(select id from permission where code='HRMS_PERFORMANCE_APPRAISALS_TAB'), name='Simple Appraisals' where code='HRMS_SIMPLE_APPRAISALS';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_PERFORMANCE_APPRAISALS_TAB'), name='Appraisals Archive' where code='HRMS_APPRAISALS_ARCHIVE';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_PERFORMANCE_APPRAISALS_TAB'), name='Templates' where code='HRMS_TEMPLATES';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_PERFORMANCE_APPRAISALS_TAB'), name='Competencies List' where code='HRMS_COMPETENCES';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_PERFORMANCE_APPRAISALS_TAB'), name='Performance Notes List' where code='HRMS_PERFORMANCE_NOTE';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_PERFORMANCE_NOTE'), name='Add' where code='HRMS_ADD_NEW_PERFORMANCE_NOTE';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_PERFORMANCE_NOTE'), name='Summary' where code='HRMS_PERFORMANCE_NOTE_SUMMERY';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_PERFORMANCE_NOTE'), name='Edit' where code='HRMS_EDIT_PERFORMANCE_NOTE';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_PERFORMANCE_NOTE'), name='Delete' where code='HRMS_REMOVE_PERFORMANCE_NOTE';

update permission
set sorder=1,
    parent=(select id from permission where code = 'HRMS_COMPETENCES'),
    name='Add'
where code = 'HRMS_ADD_COMPETENCES';
update permission
set sorder=2,
    parent=(select id from permission where code = 'HRMS_COMPETENCES'),
    name='Delete'
where code = 'HRMS_REMOVE_COMPETENCES';
update permission
set sorder=3,
    parent=(select id from permission where code = 'HRMS_COMPETENCES'),
    name='Export'
where code = 'COMPETENCES_LIST_PDF_EXCEL_EXPORT';
update permission
set sorder=1,
    parent=(select id from permission where code = 'HRMS_TEMPLATES'),
    name='Add'
where code = 'HRMS_NEW_APPRAISALS_TEMPLATES';
update permission
set sorder=2,
    parent=(select id from permission where code = 'HRMS_TEMPLATES'),
    name='Edit'
where code = 'HRMS_EDIT_TEMPLATED';
update permission
set sorder=3,
    parent=(select id from permission where code = 'HRMS_TEMPLATES'),
    name='Copy'
where code = 'HRMS_COPY_TEMPLATED';
update permission
set sorder=4,
    parent=(select id from permission where code = 'HRMS_TEMPLATES'),
    name='Delete'
where code = 'HRMS_REMOVE_TEMPLATED';
update permission
set sorder=1,
    parent=(select id from permission where code = 'HRMS_SIMPLE_APPRAISALS'),
    name='Add'
where code = 'HRMS_ADD_NEW_APPRAISALS';
update permission
set sorder=2,
    parent=(select id from permission where code = 'HRMS_SIMPLE_APPRAISALS'),
    name='Add Competency From Template'
where code = 'HRMS_ADD_COMPETENCY_FROM_TEMPLATE';
update permission
set sorder=3,
    parent=(select id from permission where code = 'HRMS_SIMPLE_APPRAISALS'),
    name='Add Validity Period'
where code = 'HRMS_ADD_VALIDITY_PERIOD';
update permission
set sorder=4,
    parent=(select id from permission where code = 'HRMS_SIMPLE_APPRAISALS'),
    name=' Appraisals Reviewer(s)'
where code = 'HRMS_APPRAISALS_REVIEWER';
update permission
set sorder=1,
    parent=(select id from permission where code = 'HRMS_DOCUMENTS_MANAGEMENT'),
    name='Insurance Documents List'
where code = 'EMPLOYEE_INSURANCE_DOCUMENTS_LIST';
update permission
set sorder=8,
    parent=(select id from permission where code = 'HRMS_MAIN_MENU'),
    name='Onboarding'
where code = 'HRMS_ONBOARDING_MANAGEMENT';

update permission
set sorder=1,
    parent=(select id from permission where code = 'HRMS_ONBOARDING_MANAGEMENT'),
    name='Onboarding Step List'
where code = 'HRMS_ONBOARDING_STEP_LIST';
update permission
set sorder=2,
    parent=(select id from permission where code = 'HRMS_ONBOARDING_MANAGEMENT'),
    name='Onboarding Period List'
where code = 'HRMS_ONBOARDING_LIST';
update permission
set sorder=3,
    parent=(select id from permission where code = 'HRMS_ONBOARDING_MANAGEMENT'),
    name='Humans Employee Steps'
where code = 'HRMS_EMPLOYEE_STEPS';

update permission
set sorder=1,
    parent=(select id from permission where code = 'HRMS_ONBOARDING_LIST'),
    name='Add'
where code = 'HRMS_ONBOARDING_ADD';
update permission
set sorder=2,
    parent=(select id from permission where code = 'HRMS_ONBOARDING_LIST'),
    name='Edit'
where code = 'HRMS_ONBOARDING_EDIT';
update permission
set sorder=3,
    parent=(select id from permission where code = 'HRMS_ONBOARDING_LIST'),
    name='Delete'
where code = 'HRMS_ONBOARDING_DELETE';

update permission set  sorder=1, parent=(select id from permission where code='HRMS_ONBOARDING_STEP_LIST'), name='Add' where code='HRMS_ONBOARDING_STEP_ADD';
update permission set  sorder=2, parent=(select id from permission where code='HRMS_ONBOARDING_STEP_LIST'), name='Edit' where code='HRMS_ONBOARDING_STEP_EDIT';
update permission set  sorder=3, parent=(select id from permission where code='HRMS_ONBOARDING_STEP_LIST'), name='Delete' where code='HRMS_ONBOARDING_STEP_DELETE';
update permission set  sorder=4, parent=(select id from permission where code='HRMS_ONBOARDING_MANAGEMENT'), name='Show Employee Step General Information' where code='HRMS_SHOW_EMPLOYEE_STEP_GENERAL_INFORMATION';
update permission set  sorder=5, parent=(select id from permission where code='HRMS_ONBOARDING_MANAGEMENT'), name='See All Employee Steps List' where code='HRMS_SEE_ALL_EMPLOYEE_STEPS_LIST';

delete from permission where code='HRMS_ADD_NEW_EMPLOYEE_PUNISHMENTS_PROMOTIONS';
delete from permission where code='HRMS_EMPLOYEE_PUNISHMENTS_PROMOTIONS_SUMMARY';
delete from permission where code='HRMS_EMPLOYEE_PUNISHMENTS_PROMOTIONS_REMOVE';
delete from permission where code='HRMS_PER_EMPLOYEE_PUNISHMENTS_PROMOTIONS';
delete from permission where code='HRMS_PER_EMPLOYEE_PUNISHMENTS_PROMOTIONS_SUMMARY';
delete from permission where code='HRMS_PER_EMPLOYEE_PUNISHMENTS_PROMOTIONS_REMOVE';
delete from permission where code='HRMS_ADD_NEW_PER_EMPLOYEE_PUNISHMENTS_PROMOTIONS';
delete from permission where code='HRMS_EMPLOYEE_PUNISHMENTS_PROMOTIONS';
delete from permission where code='HRMS_ADD_NEW_ALL_EMPLOYEE_BONUS_RECOMMENDATIONS';
delete from permission where code='HRMS_ATTENDANCE_WELCOME';
delete from permission where code='HRMS_REMOVE_PAST_EMPLOYMENT';
delete from permission where code='HRMS_EDIT_PAST_EMPLOYMENT';
delete from permission where code='HRMS_ADD_PAST_EMPLOYMENT';
delete from permission where code='HRMS_REMOVE_COMPETENCY';
delete from permission where code='HRMS_RECRUITMENT_HOME';
delete from permission where code='HRMS_WELCOME_PAGE';
delete from permission where code='HRMS_ATTENDANCE_HOME';
delete from permission where code='SHOW_ALL_WAITING_FOR_APPROVAL';
delete from permission where code='HRMS_LEAVE_REQUEST_APPROVERS_2';
delete from permission where code='HRMS_LEAVE_REQUEST_APPROVERS';
delete from permission where code='HRMS_PAST_EMPLOYMENT';
delete from permission where code='HRMS_PAST_EMPLOYMENT_SUMMARY';
delete from permission where code='HRMS_TALENT_PROFILE';
delete from permission where code='HRMS_PROFILE_NOTE';
delete from permission where code='HRMS_EMPLOYEE_BONUS_RECOMMENDATIONS';
delete from permission where code='HRMS_ADD_NEW_EMPLOYEE_BONUS_RECOMMENDATIONS';
delete from permission where code='HRMS_EMPLOYEE_BONUS_RECOMMENDATIONS_SUMMARY';
delete from permission where code='HRMS_EMPLOYEE_BONUS_RECOMMENDATIONS_STATUS_APPROVE';
delete from permission where code='HRMS_EMPLOYEE_BONUS_RECOMMENDATIONS_STATUS_REJECT';
delete from permission where code='HRMS_EMPLOYEE_BONUS_RECOMMENDATIONS_REMOVE';
delete from permission where code='HRMS_ALL_EMPLOYEE_BONUS_RECOMMENDATIONS';
delete from permission where code='HRMS_ALL_EMPLOYEE_BONUS_RECOMMENDATIONS_STATUS_APPROVE';
delete from permission where code='HRMS_ALL_EMPLOYEE_BONUS_RECOMMENDATIONS_STATUS_REJECT';
delete from permission where code='HRMS_ALL_EMPLOYEE_BONUS_RECOMMENDATIONS_REMOVE';
delete from permission where code='HRMS_ALL_EMPLOYEE_BONUS_RECOMMENDATIONS_SUMMARY';
delete from permission where code='HRMS_MAP_LOCATION';
delete from permission where code='HRMS_ADD_NEW_BONUS_DISTRIBUTION';
delete from permission where code='HRMS_EMPLOYEE_BONUS_LIST';
delete from permission where code='HRMS_TELEGRAM_CHAT_LIST';
delete from permission where code='HRMS_MAIN_SECTION';
delete from permission where code='HRMS_NEW_EMPLOYEE_360_APPRAISALS';
delete from permission where code='HRMS_360_REVIEW';