delete from permission where code = 'HRMS_QUICK_ADD_VACANCY';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_QUICK_ADD_VACANCY', 'HRMS', false, 'Vacancy quick add', 50,
        (select id from permission where code = 'HRMS_VACANCY_LIST_VIEW'), true, 'RECRUITMENT_SYSTEM');


delete from "anv".permission_context where permissioncode = 'HRMS_QUICK_ADD_VACANCY';
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_QUICK_ADD_VACANCY', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_QUICK_ADD_VACANCY';
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('HRMS_QUICK_ADD_VACANCY', 'ALLOW', 'ADMIN');



delete from permission where code = 'HRMS_QUICK_ADD_CANDIDATE';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_QUICK_ADD_CANDIDATE', 'HRMS', false, 'Candidate quick add', 50,
        (select id from permission where code = 'HRMS_CANDIDATE_LIST_VIEW'), true, 'RECRUITMENT_SYSTEM');


delete from "anv".permission_context where permissioncode = 'HRMS_QUICK_ADD_CANDIDATE';
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_QUICK_ADD_CANDIDATE', 'HRMS');

delete from "anv".rolepermission where permissioncode = 'HRMS_QUICK_ADD_CANDIDATE';
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('HRMS_QUICK_ADD_CANDIDATE', 'ALLOW', 'ADMIN');



