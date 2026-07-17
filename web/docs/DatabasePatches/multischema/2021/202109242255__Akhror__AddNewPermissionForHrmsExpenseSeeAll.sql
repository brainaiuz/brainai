delete from permission where code = 'HRMS_EXPENSES_SEE_ALL' ;
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_EXPENSES_SEE_ALL',  'HRMS', false, 'See All', 15, (select id from permission where code = 'HRMS_COMPANY_EXPENSE_LIST' limit 1), false, 'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'HRMS_EXPENSES_SEE_ALL';
insert into "anv".permission_context(permissioncode,contextcode) values ('HRMS_EXPENSES_SEE_ALL','HRMS');