insert into modelfield(field_id, form_id, hide, iscustomfield, mandatory, section, sorder, widget, systemmandatory, isentityfield, usablebyworkflow, type, fullwidth, place, split, disableupdate, hideincustomizeform, systemdisable, isworkflowattribute)
values('MIDDLE_NAME', 'CANDIDATE_FORM', false, false, false, 'CONTACT_INFORMATION', 130, 'UNKNOWN', false, false, false, 'text', false, 0, false, false, false, false, false);
insert into "anv".modelfield(field_id, form_id, hide, iscustomfield, mandatory, section, sorder, widget, systemmandatory, isentityfield, usablebyworkflow, type, fullwidth, place, split, disableupdate, hideincustomizeform, systemdisable, isworkflowattribute)
values('MARTIAL_STATUS', 'CANDIDATE_FORM', false, false, false, 'CONTACT_INFORMATION', 135, 'DataListBox', false, false, false, 'text', false, 0, false, false, false, false, false);
insert into "anv".modelfield(field_id, form_id, hide, iscustomfield, mandatory, section, sorder, widget, systemmandatory, isentityfield, usablebyworkflow, type, fullwidth, place, split, disableupdate, hideincustomizeform, systemdisable, isworkflowattribute)
values('GENDER', 'CANDIDATE_FORM', false, false, false, 'CONTACT_INFORMATION', 125, 'UNKNOWN', false, false, false, 'text', false, 0, false, false, false, false, false);

update "anv".modelfield set sorder = 1 where field_id = 'NUMBER' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 5 where field_id = 'OWNER' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 10 where field_id = 'FIRST_NAME' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 15 where field_id = 'LAST_NAME' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 20 where field_id = 'BIRTH_DAY' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 25 where field_id = 'LANGUAGE' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 30 where field_id = 'CANDIDATE_PROJECT' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 35 where field_id = 'VACANCIES' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 40 where field_id = 'CREATED_DATE' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 45 where field_id = 'LEAD_SOURCE' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 50 where field_id = 'WORK_EXPERIENCE' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 55 where field_id = 'CURRENT_EMPLOYER' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 60 where field_id = 'EXPECTED_SALARY' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 65 where field_id = 'STATUS' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 70 where field_id = 'LOCATION' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 75 where field_id = 'SKILLS' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 80 where field_id = 'PHONE' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 85 where field_id = 'EMAIL' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 90 where field_id = 'IM_ADDRESS' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 95 where field_id = 'WEB_ADDRESS' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 100 where field_id = 'ALLOWANCES' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 105 where field_id = 'CRM_NOTE' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 110 where field_id = 'ADDRESS' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 115 where field_id = 'LINKS' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 120 where field_id = 'ATTACHMENTS' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 125 where field_id = 'GENDER' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 130 where field_id = 'MIDDLE_NAME' and form_id = 'CANDIDATE_FORM';
update "anv".modelfield set sorder = 135 where field_id = 'MARTIAL_STATUS' and form_id = 'CANDIDATE_FORM';