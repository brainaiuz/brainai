delete from "0".modelfield where form_id='ACTIVITY_VIEW_FORM';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_VIEW_FORM', 'SUBJECT', true, 'COL_1', 'EVENT_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_VIEW_FORM', 'WHEN', true, 'COL_1', 'EVENT_INFORMATION', 1);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_VIEW_FORM', 'DESCRIPTION', false, 'COL_2', 'EVENT_INFORMATION', 0);
insert into "0".modelfield(form_id, field_id, mandatory, columntype, fsection, forder) values ('ACTIVITY_VIEW_FORM', 'INVITATION_RESPONSE', false , 'COL_2', 'EVENT_INFORMATION', 1);

insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder, nolabelfor) values('ACTIVITY_VIEW_FORM', 'SHARED_WITH', false, 'SHARED_WITH', 0, 'viewForm');
insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder, nolabelfor) values('ACTIVITY_VIEW_FORM', 'ATTACHMENTS', false, 'ATTACHMENTS', 0, 'viewForm');

delete from "anv".modelfield where form_id='ACTIVITY_VIEW_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_VIEW_FORM', 'SUBJECT', true, 'COL_1', 'EVENT_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_VIEW_FORM', 'WHEN', true, 'COL_1', 'EVENT_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_VIEW_FORM', 'DESCRIPTION', false, 'COL_2', 'EVENT_INFORMATION', 0);
insert into "anv".modelfield(form_id, field_id, mandatory, columntype, fsection, forder) values ('ACTIVITY_VIEW_FORM', 'INVITATION_RESPONSE', false, 'COL_2', 'EVENT_INFORMATION', 1);

insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder, nolabelfor) values('ACTIVITY_VIEW_FORM', 'SHARED_WITH', false, 'SHARED_WITH', 0, 'viewForm');
insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder, nolabelfor) values('ACTIVITY_VIEW_FORM', 'ATTACHMENTS', false, 'ATTACHMENTS', 0, 'viewForm');