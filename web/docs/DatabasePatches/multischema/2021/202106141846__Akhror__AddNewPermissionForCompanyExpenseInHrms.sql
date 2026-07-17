delete from permission where code = 'HRMS_COMPANY_EXPENSE_LIST';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_COMPANY_EXPENSE_LIST', 'HRMS', false, 'Company Expense List', 50,
        (select id from permission where code = 'HRMS_CANDIDATE_LIST_VIEW'), true, 'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'HRMS_COMPANY_EXPENSE_LIST';
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_COMPANY_EXPENSE_LIST', 'HRMS');

delete from permission where code = 'HRMS_ADD_NEW_COMPANY_EXPENSE_CLAIM';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_ADD_NEW_COMPANY_EXPENSE_CLAIM', 'HRMS', false, 'Add', 50,
        (select id from permission where code = 'HRMS_COMPANY_EXPENSE_LIST'), true, 'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'HRMS_ADD_NEW_COMPANY_EXPENSE_CLAIM';
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_ADD_NEW_COMPANY_EXPENSE_CLAIM', 'HRMS');



