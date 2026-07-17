delete from "anv".model where formid = 'POSITION_FORM';
insert into "anv".model (formid, title, viewname, active) values('POSITION_FORM', 'Position Form', 'Positions', true);

delete from "anv".customformsection where form_id = 'POSITION_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('POSITION_FORM', 'BASIC_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('POSITION_FORM', 'INVOLVED_EMPLOYEES', 1, true);

delete from "anv".modelfield where form_id = 'POSITION_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'POSITION_TITLE', true, 'COL_1', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'COUNT', false, 'COL_1', 'BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'ESTIBLISHED', false , 'COL_1', 'BASIC_INFORMATION', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'STATUS', false, 'COL_2', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'LOCATION', false, 'COL_2', 'BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'DEPARTMENT', true, 'COL_2', 'BASIC_INFORMATION', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'POSITION_CODE', false, 'COL_3', 'BASIC_INFORMATION', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('POSITION_FORM', 'EMPLOYEES', false, 'COL_1', 'INVOLVED_EMPLOYEES', 0);
