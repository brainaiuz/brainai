delete from permission where code='HRMS_ADD_VACANCY_FOR_CURRENT_POSITION';
insert into permission (code, context, name, parent, modulecode)
values ('HRMS_ADD_VACANCY_FOR_CURRENT_POSITION', 'HRMS', 'Add Vacancy for Current Position',(select id from permission where code = 'HRMS_ADD_VACANCY'),
        'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'HRMS_ADD_VACANCY_FOR_CURRENT_POSITION';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_ADD_VACANCY_FOR_CURRENT_POSITION', 'HRMS');