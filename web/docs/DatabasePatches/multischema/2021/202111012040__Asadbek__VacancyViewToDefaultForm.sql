
delete from "anv".customformsection where form_id='VACANCY_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('VACANCY_FORM', 'VACANCY_BASIC_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('VACANCY_FORM', 'VACANCY_ATTACHMENTS', 1, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('VACANCY_FORM', 'VACANCY_INTERNAL_DETAILS', 2, false);


delete from "anv".modelfield where form_id='VACANCY_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyNumberID', true, false, 'COL_1', 'VACANCY_BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyJobTitle', true, false, 'COL_1', 'VACANCY_BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyPosition', true, false, 'COL_1', 'VACANCY_BASIC_INFORMATION', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyResponsibilities', false, false, 'COL_1', 'VACANCY_BASIC_INFORMATION', 3);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyDescription', false, true, 'COL_1', 'VACANCY_BASIC_INFORMATION', 4);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyJobFamily', false, true, 'COL_1', 'VACANCY_BASIC_INFORMATION', 5);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyType', false, true, 'COL_1', 'VACANCY_BASIC_INFORMATION', 6);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'contractPeriod', false, true, 'COL_1', 'VACANCY_BASIC_INFORMATION', 7);



insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyStatus', true, false, 'COL_2', 'VACANCY_BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'proposedSalary', false, false, 'COL_2', 'VACANCY_BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyStartDate', true, false, 'COL_2', 'VACANCY_BASIC_INFORMATION', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'jobRequirement', false, false, 'COL_2', 'VACANCY_BASIC_INFORMATION', 3);


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyManager', false, false, 'COL_3', 'VACANCY_BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'department', true, false, 'COL_3', 'VACANCY_BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyPlaceCount', false, false, 'COL_3', 'VACANCY_BASIC_INFORMATION', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'gender', false, false, 'COL_3', 'VACANCY_BASIC_INFORMATION', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyRequiredDegree', false, false, 'COL_3', 'VACANCY_BASIC_INFORMATION', 4);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyJobType', false, false, 'COL_3', 'VACANCY_BASIC_INFORMATION', 5);



insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'VACANCY_NOTES', false, false, 'COL_1', 'VACANCY_ATTACHMENTS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'VACANCY_ATTACHMENTS', false, false, 'COL_2', 'VACANCY_ATTACHMENTS', 0);


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'COUNTRY', false, true, 'COL_1', 'VACANCY_INTERNAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'COUNTRYEMBASSY', false, true, 'COL_1', 'VACANCY_INTERNAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'vacancyLocation', false, true, 'COL_1', 'VACANCY_INTERNAL_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'religion', false, true, 'COL_1', 'VACANCY_INTERNAL_DETAILS', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('VACANCY_FORM', 'PROJECT', false, true, 'COL_1', 'VACANCY_INTERNAL_DETAILS', 4);