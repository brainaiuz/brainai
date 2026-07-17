insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_CANDIDATE_VACANCY_SUMMARY_VIEW', 'HRMS', 'Summary', (select count(id) from permission where parent=(select id from permission where code = 'HRMS_VACANCY_LIST_VIEW')) + 1,
        (select id from permission where code = 'HRMS_VACANCY_LIST_VIEW'),'RECRUITMENT_SYSTEM');

insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_CANDIDATE_VACANCY_SUMMARY_VIEW', 'HRMS');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_CANDIDATE_VACANCY_SUMMARY_VIEW', 'ALLOW', 'HR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('HRMS_CANDIDATE_VACANCY_SUMMARY_VIEW', 'ALLOW', 'DR');
