insert into "0".model (formid, title, viewname, active) values('DEPARTMENT_FORM', 'Department Form', 'Department', true);

insert into "0".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_FORM', 'DEPARTMENT_DETAILS', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_FORM', 'DEPARTMENT_EMPLOYEES', 1, true);


insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_NAME', true, 'COL_1', 'DEPARTMENT_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_DESCRIPTION', false, 'COL_1', 'DEPARTMENT_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_EMAIL', false, 'COL_1', 'DEPARTMENT_DETAILS', 2);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_START_DATE', false, 'COL_2', 'DEPARTMENT_DETAILS', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_PARENT', true, 'COL_2', 'DEPARTMENT_DETAILS', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_CREATED_BY', false, 'COL_2', 'DEPARTMENT_DETAILS', 2);


insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_EMPLOYEES', false, 'COL_1', 'DEPARTMENT_EMPLOYEES', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_LEADER', true, 'COL_1', 'DEPARTMENT_EMPLOYEES', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_LEADER2', true, 'COL_1', 'DEPARTMENT_EMPLOYEES', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_LEADER3', true, 'COL_1', 'DEPARTMENT_EMPLOYEES', 3);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_LEADER4', true, 'COL_1', 'DEPARTMENT_EMPLOYEES', 4);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_LEADER5', true, 'COL_1', 'DEPARTMENT_EMPLOYEES', 5);





insert into "anv".model (formid, title, viewname, active) values('DEPARTMENT_FORM', 'Department Form', 'Department', true);

insert into "anv".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_FORM', 'DEPARTMENT_DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('DEPARTMENT_FORM', 'DEPARTMENT_EMPLOYEES', 1, true);


insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_NAME', true, 'COL_1', 'DEPARTMENT_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_DESCRIPTION', false, 'COL_1', 'DEPARTMENT_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_EMAIL', false, 'COL_1', 'DEPARTMENT_DETAILS', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_START_DATE', false, 'COL_2', 'DEPARTMENT_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_PARENT', true, 'COL_2', 'DEPARTMENT_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_CREATED_BY', false, 'COL_2', 'DEPARTMENT_DETAILS', 2);


insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_EMPLOYEES', false, 'COL_1', 'DEPARTMENT_EMPLOYEES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_LEADER', true, 'COL_1', 'DEPARTMENT_EMPLOYEES', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_LEADER2', true, 'COL_1', 'DEPARTMENT_EMPLOYEES', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_LEADER3', true, 'COL_1', 'DEPARTMENT_EMPLOYEES', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_LEADER4', true, 'COL_1', 'DEPARTMENT_EMPLOYEES', 4);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('DEPARTMENT_FORM', 'DEPARTMENT_LEADER5', true, 'COL_1', 'DEPARTMENT_EMPLOYEES', 5);


