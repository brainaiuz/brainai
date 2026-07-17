delete from "0".model where formid = 'INCIDENT_FORM';
insert into "0".model (formid, title, viewname, active) values('INCIDENT_FORM', 'Add Incident Form', 'PerformanceNote', true);

delete from "0".customformsection where form_id = 'INCIDENT_FORM';

insert into "0".customformsection (form_id, section, sorder, expanded) values('INCIDENT_FORM', 'BASIC_INFORMATION', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('INCIDENT_FORM', 'ATTACHMENTS', 1, true);

delete from "0".modelfield where form_id = 'INCIDENT_FORM';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'NAME', true, 'COL_1', 'BASIC_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'DESCRIPTION', false, 'COL_1', 'BASIC_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'RELATED_EMPLOYEES', true, 'COL_1', 'BASIC_INFORMATION', 2);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'PERIOD', true, 'COL_2', 'BASIC_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'STATUS', true, 'COL_2', 'BASIC_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'PRIORITY', false, 'COL_2', 'BASIC_INFORMATION', 2);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'REPORTED_BY', false, 'COL_3', 'BASIC_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'RESOLVER', false, 'COL_3', 'BASIC_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'VISIBILITY', true, 'COL_3', 'BASIC_INFORMATION', 2);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS', 0);



delete from "anv".model where formid = 'INCIDENT_FORM';
insert into "anv".model (formid, title, viewname, active) values('INCIDENT_FORM', 'Add Incident Form', 'PerformanceNote', true);

delete from "anv".customformsection where form_id = 'INCIDENT_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('INCIDENT_FORM', 'BASIC_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('INCIDENT_FORM', 'ATTACHMENTS', 1, true);

delete from "anv".modelfield where form_id = 'INCIDENT_FORM';

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'NAME', true, 'COL_1', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'DESCRIPTION', false, 'COL_1', 'BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'RELATED_EMPLOYEES', true, 'COL_1', 'BASIC_INFORMATION', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'PERIOD', true, 'COL_2', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'STATUS', true, 'COL_2', 'BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'PRIORITY', false, 'COL_2', 'BASIC_INFORMATION', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'REPORTED_BY', false, 'COL_3', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'RESOLVER', false, 'COL_3', 'BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'VISIBILITY', true, 'COL_3', 'BASIC_INFORMATION', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('INCIDENT_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS', 0);