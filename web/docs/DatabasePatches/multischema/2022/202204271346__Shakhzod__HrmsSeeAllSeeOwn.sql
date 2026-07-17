insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_VACANCY_SEE_OWN', 'HRMS', 'See own', 8, (select id from permission where code = 'HRMS_VACANCY_LIST_VIEW'),
        'RECRUITMENT_SYSTEM');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_VACANCY_SEE_OWN', 'HRMS');

insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_CANDIDATE_SEE_OWN', 'HRMS', 'See own', 8,
        (select id from permission where code = 'HRMS_CANDIDATE_LIST_VIEW'), 'RECRUITMENT_SYSTEM');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_CANDIDATE_SEE_OWN', 'HRMS');


insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_PLACEMENT_SEE_All', 'HRMS', 'See All', 7,
        (select id from permission where code = 'HRMS_PLACEMENT_LIST_VIEW'), 'RECRUITMENT_SYSTEM');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_PLACEMENT_SEE_All', 'HRMS');

insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_PLACEMENT_SEE_OWN', 'HRMS', 'See Own', 8,
        (select id from permission where code = 'HRMS_PLACEMENT_LIST_VIEW'), 'RECRUITMENT_SYSTEM');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_PLACEMENT_SEE_OWN', 'HRMS');

insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_PLACEMENT_SEE_All', 'ALLOW', 'HR');




