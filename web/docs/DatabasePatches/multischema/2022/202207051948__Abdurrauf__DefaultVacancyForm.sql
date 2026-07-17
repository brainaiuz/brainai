delete from "anv".customformsection where form_id = 'VACANCY_FORM' and section = 'VACANCY_BASIC_INFORMATION';
delete from "anv".customformsection where form_id = 'VACANCY_FORM' and section = 'DETAILED_INFORMATION';
delete from "anv".customformsection where form_id = 'VACANCY_FORM' and section = 'VACANCY_ATTACHMENTS';

insert into "anv".customformsection (active, custom, expanded, form_id, section, sorder)
values (true, false, true, 'VACANCY_FORM', 'VACANCY_BASIC_INFORMATION', 0),
       (true, false, true, 'VACANCY_FORM', 'DETAILED_INFORMATION', 1),
       (true, false, true, 'VACANCY_FORM', 'VACANCY_ATTACHMENTS', 2);

delete from "anv".modelfield where form_id = 'VACANCY_FORM' and (field_id = 'COUNTRYEMBASSY' or field_id = 'COUNTRY' or field_id = 'religion');

update "anv".modelfield set hide = true where form_id = 'VACANCY_FORM';

update "anv".modelfield set columntype = 'COL_1', hide = false, forder = 0, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'vacancyNumberID';
update "anv".modelfield set columntype = 'COL_2', hide = false, forder = 0, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'vacancyStatus';
update "anv".modelfield set columntype = 'COL_3', hide = false, forder = 0, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'vacancyManager';

update "anv".modelfield set columntype = 'COL_1', hide = false, forder = 1, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'vacancyJobTitle';
update "anv".modelfield set columntype = 'COL_2', hide = false, forder = 1, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'proposedSalary';
update "anv".modelfield set columntype = 'COL_3', hide = false, forder = 1, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'department';

update "anv".modelfield set columntype = 'COL_1', hide = false, forder = 2, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'vacancyPosition';
update "anv".modelfield set columntype = 'COL_2', hide = false, forder = 2, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'vacancyStartDate';
update "anv".modelfield set columntype = 'COL_3', hide = false, forder = 2, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'vacancyPlaceCount';

update "anv".modelfield set columntype = 'COL_1', hide = false, forder = 3, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'vacancyJobType';
update "anv".modelfield set columntype = 'COL_2', hide = false, forder = 3, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'vacancyRequiredDegree';
update "anv".modelfield set columntype = 'COL_3', hide = false, forder = 3, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'APPROVER';

update "anv".modelfield set columntype = 'COL_1', hide = false, forder = 4, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'vacancyLocation';
update "anv".modelfield set columntype = 'COL_2', hide = false, forder = 4, fsection = 'VACANCY_BASIC_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'gender';

update "anv".modelfield set columntype = 'COL_1', hide = false, forder = 0, fsection = 'DETAILED_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'vacancyDescription';
update "anv".modelfield set columntype = 'COL_2', hide = false, forder = 0, fsection = 'DETAILED_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'jobRequirement';
update "anv".modelfield set columntype = 'COL_3', hide = false, forder = 0, fsection = 'DETAILED_INFORMATION' where form_id = 'VACANCY_FORM' and field_id = 'vacancyResponsibilities';

update "anv".modelfield set columntype = 'COL_1', hide = false, forder = 0, fsection = 'VACANCY_ATTACHMENTS' where form_id = 'VACANCY_FORM' and field_id = 'VACANCY_NOTES';
update "anv".modelfield set columntype = 'COL_2', hide = false, forder = 0, fsection = 'VACANCY_ATTACHMENTS' where form_id = 'VACANCY_FORM' and field_id = 'VACANCY_ATTACHMENTS';
