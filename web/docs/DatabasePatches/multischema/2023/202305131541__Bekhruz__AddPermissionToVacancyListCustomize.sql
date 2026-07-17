
delete from permission where code='VACANCY_CUSTOMIZE_LIST';
insert into permission (code, context, name, parent, modulecode)
values ('VACANCY_CUSTOMIZE_LIST', 'HRMS', 'Customize Vacancy List',(select id from permission where code = 'HRMS_VACANCY_LIST_VIEW'),
        'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'VACANCY_CUSTOMIZE_LIST';
insert into "anv".permission_context (permissioncode, contextcode)
values ('VACANCY_CUSTOMIZE_LIST', 'HRMS');


delete from permission where code='VACANCY_LIST_FILTER';
insert into permission (code, context, name, parent, modulecode)
values ('VACANCY_LIST_FILTER', 'HRMS', 'Show Filter',(select id from permission where code = 'HRMS_VACANCY_LIST_VIEW'),
			'RECRUITMENT_SYSTEM');

delete from "anv".permission_context where permissioncode = 'VACANCY_LIST_FILTER';
insert into "anv".permission_context (permissioncode, contextcode)
values ('VACANCY_LIST_FILTER', 'HRMS');

