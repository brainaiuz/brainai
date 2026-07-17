delete from permission where code = 'HRMS_VACANCY_SEE_ALL';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_VACANCY_SEE_ALL', 'HRMS', false, 'See All', 50,
        (select id from permission where code = 'HRMS_VACANCY_LIST_VIEW'), false, 'RECRUITMENT_SYSTEM');


delete from "anv".permission_context where permissioncode = 'HRMS_VACANCY_SEE_ALL';
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_VACANCY_SEE_ALL', 'HRMS');