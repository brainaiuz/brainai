delete from "anv".model where formid='MEETING_MINUTES';
insert into "anv".model (formid, title, viewname, active) values('MEETING_MINUTES', 'Meeting Minutes', 'MeetingMInutesView', true);


delete from "anv".customformsection where form_id='MEETING_MINUTES';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('MEETING_MINUTES', 'BASIC_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('MEETING_MINUTES', 'AGENDA', 1, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('MEETING_MINUTES', 'NOTES_AND_ATTACHMENTS', 2, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('MEETING_MINUTES', 'NOTES', 3, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('MEETING_MINUTES', 'ADDITIONAL_INFORMATION', 4, false);


delete from "anv".modelfield where form_id='MEETING_MINUTES';
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_CALLED_BY', true, false, 'COL_1', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_PREPARED_BY', false, false, 'COL_1', 'BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_TITLE', true, false, 'COL_1', 'BASIC_INFORMATION', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_NUMBER', false, true, 'COL_1', 'BASIC_INFORMATION', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_LOCATION', false, true, 'COL_1', 'BASIC_INFORMATION', 4);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'project', false, true, 'COL_1', 'BASIC_INFORMATION', 5);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_TYPE', false, true, 'COL_1', 'BASIC_INFORMATION', 6);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_ABSENT', false, true, 'COL_1', 'BASIC_INFORMATION', 7);


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_MEETING_PERIOD', true, false, 'COL_2', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_NEXT_DATE', false, false, 'COL_2', 'BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_PURPOSE', false, false, 'COL_2', 'BASIC_INFORMATION', 2);


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_ATTENDEES', false, false, 'COL_3', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_EMAIL_TEMPLATES', false, false, 'COL_3', 'BASIC_INFORMATION', 1);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_AGENDA_PANEL', false, false, 'COL_1', 'AGENDA', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_ATTACHMENTS', false, false, 'COL_2', 'NOTES_AND_ATTACHMENTS', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'MEETING_M_NOTES', false, false, 'COL_1', 'NOTES_AND_ATTACHMENTS', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('MEETING_MINUTES', 'ADDITIONAL_INFORMATION', false, false, 'COL_1', 'ADDITIONAL_INFORMATION', 0);