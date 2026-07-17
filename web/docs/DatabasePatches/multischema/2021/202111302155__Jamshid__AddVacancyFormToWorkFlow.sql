update "anv".modelfield set usableByWorkflow = true where form_id = 'VACANCY_FORM' and field_id != 'VACANCY_NOTES';
insert into "anv".reference(code, name, sorder, parentid) values ('_WORKFLOW_MODULE_VACANCY', 'Vacancy', 20, (select id from "anv".reference where code = '_WORKFLOW_MODULE' limit 1));

update "anv".modelfield set source = 'REFERENCE@VACANCY_STATUSES', widget = 'LOOKUP' where form_id = 'VACANCY_FORM' and field_id = 'vacancyStatus';
update "anv".modelfield set source = 'COUNTRY', widget = 'LOOKUP' where form_id = 'VACANCY_FORM' and field_id = 'COUNTRY';
update "anv".modelfield set source = 'HRMS@EMBASSY', widget = 'LOOKUP' where form_id = 'VACANCY_FORM' and field_id = 'COUNTRYEMBASSY';
update "anv".modelfield set source = 'Male;Female', widget = 'DropDown' where form_id = 'VACANCY_FORM' and field_id = 'gender';
update "anv".modelfield set source = 'HRMS@JOB_FAMILY', widget = 'LOOKUP' where form_id = 'VACANCY_FORM' and field_id = 'vacancyJobFamily';
update "anv".modelfield set source = 'REFERENCE@TIME_TYPES', widget = 'LOOKUP' where form_id = 'VACANCY_FORM' and field_id = 'vacancyJobType';
update "anv".modelfield set source = 'HRMS@LOCATION', widget = 'LOOKUP' where form_id = 'VACANCY_FORM' and field_id = 'vacancyLocation';
update "anv".modelfield set source = 'HRMS@POSITION', widget = 'LOOKUP' where form_id = 'VACANCY_FORM' and field_id = 'vacancyPosition';
update "anv".modelfield set source = 'REFERENCE@_VACANCY_RELIGION', widget = 'DropDown' where form_id = 'VACANCY_FORM' and field_id = 'religion';
update "anv".modelfield set source = 'HRMS@PROJECT', widget = 'LOOKUP' where form_id = 'VACANCY_FORM' and field_id = 'PROJECT';
update "anv".modelfield set source = 'HRMS@MANAGER', widget = 'DropDown' where form_id = 'VACANCY_FORM' and field_id = 'vacancyManager';
update "anv".modelfield set source = 'REFERENCE@VACANCY_DEGREES', widget = 'DropDown' where form_id = 'VACANCY_FORM' and field_id = 'vacancyRequiredDegree';