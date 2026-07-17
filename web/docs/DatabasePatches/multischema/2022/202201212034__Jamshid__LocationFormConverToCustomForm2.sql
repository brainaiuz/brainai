delete from "anv".model where formid = 'LOCATION_FORM';
insert into "anv".model (formid, title, viewname, active) values('LOCATION_FORM', 'Location Form', 'Location', true);

delete from "anv".customformsection where form_id = 'LOCATION_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LOCATION_FORM', 'GENERAL_DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LOCATION_FORM', 'EMPLOYEE_LOCATION', 1, true);

delete from "anv".modelfield where form_id = 'LOCATION_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOCATION_FORM', 'NAME', false, 'COL_1', 'GENERAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOCATION_FORM', 'COUNTRY', true, 'COL_1', 'GENERAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOCATION_FORM', 'STATE', false, 'COL_1', 'GENERAL_DETAILS', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOCATION_FORM', 'CITY', false, 'COL_2', 'GENERAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOCATION_FORM', 'EMAIL', false , 'COL_2', 'GENERAL_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOCATION_FORM', 'PHONE', true, 'COL_2', 'GENERAL_DETAILS', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOCATION_FORM', 'FAX', false, 'COL_3', 'GENERAL_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOCATION_FORM', 'ZIP_CODE', false, 'COL_3', 'GENERAL_DETAILS', 1);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOCATION_FORM', 'EMPLOYEES', false, 'COL_1', 'EMPLOYEE_LOCATION', 0);