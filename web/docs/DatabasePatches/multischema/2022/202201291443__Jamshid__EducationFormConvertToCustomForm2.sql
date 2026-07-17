delete from "anv".model where formid = 'EDUCATION_FORM';
insert into "anv".model (formid, title, viewname, active) values('EDUCATION_FORM', 'Education Form', 'TalentProfileView', true);

delete from "anv".customformsection where form_id = 'EDUCATION_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('EDUCATION_FORM', 'DETAILS', 0, true);

delete from "anv".modelfield where form_id = 'EDUCATION_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('EDUCATION_FORM', 'SCHOOL_NAME', true, 'COL_1', 'DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('EDUCATION_FORM', 'DEGREE', false, 'COL_1', 'DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('EDUCATION_FORM', 'FIELD_OF_STUDY', false, 'COL_1', 'DETAILS', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('EDUCATION_FORM', 'START_DATE', false , 'COL_2', 'DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('EDUCATION_FORM', 'DUE_DATE', true, 'COL_2', 'DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('EDUCATION_FORM', 'COUNTRY', false, 'COL_2', 'DETAILS', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('EDUCATION_FORM', 'ACTIVITIES', false, 'COL_3', 'DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('EDUCATION_FORM', 'COMMENTS', false, 'COL_3', 'DETAILS', 1);