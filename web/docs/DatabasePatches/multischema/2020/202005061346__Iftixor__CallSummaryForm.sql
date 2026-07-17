delete from "0".model where formid='LOGACALL_FORM_VIEW';
insert into "0".model (formid, title, viewname, active) values('LOGACALL_FORM_VIEW', 'Log A Call Form View', 'LogACall', true);

delete from "0".customformsection where form_id='LOGACALL_FORM_VIEW';
insert into "0".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM_VIEW', 'CALL_INFORMATION', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM_VIEW', 'ATTACHMENTS', 1, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM_VIEW', 'SHARED_WITH', 2, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM_VIEW', 'ADDITIONAL_INFORMATION', 3, false);

delete from "0".modelfield where form_id='LOGACALL_FORM_VIEW';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM_VIEW', 'SUBJECT', true, 'COL_1', 'CALL_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM_VIEW', 'WHEN', true, 'COL_1', 'CALL_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM_VIEW', 'CALL_DURATION', false, 'COL_1', 'CALL_INFORMATION', 2);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM_VIEW', 'DESCRIPTION', false, 'COL_2', 'CALL_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM_VIEW', 'CALL_TYPE', false, 'COL_2', 'CALL_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM_VIEW', 'CALL_DETAILS', false, 'COL_2', 'CALL_INFORMATION', 2);

insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder) values('LOGACALL_FORM_VIEW', 'SHARED_WITH', false, 'SHARED_WITH', 0);
insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder) values('LOGACALL_FORM_VIEW', 'ATTACHMENTS', false, 'ATTACHMENTS', 0);



delete from "anv".model where formid='LOGACALL_FORM_VIEW';
insert into "anv".model (formid, title, viewname, active) values('LOGACALL_FORM_VIEW', 'Log A Call Form View', 'LogACall', true);

delete from "anv".customformsection where form_id='LOGACALL_FORM_VIEW';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM_VIEW', 'CALL_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM_VIEW', 'ATTACHMENTS', 1, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM_VIEW', 'SHARED_WITH', 2, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM_VIEW', 'ADDITIONAL_INFORMATION', 3, false);

delete from "anv".modelfield where form_id='LOGACALL_FORM_VIEW';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM_VIEW', 'SUBJECT', true, 'COL_1', 'CALL_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM_VIEW', 'WHEN', true, 'COL_1', 'CALL_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM_VIEW', 'CALL_DURATION', false, 'COL_1', 'CALL_INFORMATION', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM_VIEW', 'DESCRIPTION', false, 'COL_2', 'CALL_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM_VIEW', 'CALL_TYPE', false, 'COL_2', 'CALL_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM_VIEW', 'CALL_DETAILS', false, 'COL_2', 'CALL_INFORMATION', 2);

insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder) values('LOGACALL_FORM_VIEW', 'SHARED_WITH', false, 'SHARED_WITH', 0);
insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder) values('LOGACALL_FORM_VIEW', 'ATTACHMENTS', false, 'ATTACHMENTS', 0);








delete from "0".model where formid='ACTIVITY_VIEW_FORM';
insert into "0".model (formid, title, viewname, active) values('ACTIVITY_VIEW_FORM', 'Activity Form View', 'Activity', true);

delete from "0".customformsection where form_id='ACTIVITY_VIEW_FORM';
insert into "0".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_VIEW_FORM', 'EVENT_INFORMATION', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_VIEW_FORM', 'ATTACHMENTS', 1, true );
insert into "0".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_VIEW_FORM', 'SHARED_WITH', 2, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_VIEW_FORM', 'ADDITIONAL_INFORMATION', 3, false);

delete from "0".modelfield where form_id='ACTIVITY_VIEW_FORM';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_VIEW_FORM', 'SUBJECT', true, 'COL_1', 'EVENT_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_VIEW_FORM', 'WHEN', true, 'COL_1', 'EVENT_INFORMATION', 1);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_VIEW_FORM', 'DESCRIPTION', false, 'COL_2', 'EVENT_INFORMATION', 0);

insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder, nolabelfor) values('ACTIVITY_VIEW_FORM', 'SHARED_WITH', false, 'SHARED_WITH', 0, 'viewForm');
insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder, nolabelfor) values('ACTIVITY_VIEW_FORM', 'ATTACHMENTS', false, 'ATTACHMENTS', 0, 'viewForm');



delete from "anv".model where formid='ACTIVITY_VIEW_FORM';
insert into "anv".model (formid, title, viewname, active) values('ACTIVITY_VIEW_FORM', 'Activity Form View', 'Activity', true);

delete from "anv".customformsection where form_id='ACTIVITY_VIEW_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_VIEW_FORM', 'EVENT_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_VIEW_FORM', 'ATTACHMENTS', 1, true );
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_VIEW_FORM', 'SHARED_WITH', 2, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_VIEW_FORM', 'ADDITIONAL_INFORMATION', 3, false);

delete from "anv".modelfield where form_id='ACTIVITY_VIEW_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_VIEW_FORM', 'SUBJECT', true, 'COL_1', 'EVENT_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_VIEW_FORM', 'WHEN', true, 'COL_1', 'EVENT_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_VIEW_FORM', 'DESCRIPTION', false, 'COL_2', 'EVENT_INFORMATION', 0);

insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder, nolabelfor) values('ACTIVITY_VIEW_FORM', 'SHARED_WITH', false, 'SHARED_WITH', 0, 'viewForm');
insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder, nolabelfor) values('ACTIVITY_VIEW_FORM', 'ATTACHMENTS', false, 'ATTACHMENTS', 0, 'viewForm');


UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('LOGACALL_FORM_VIEW', 'LogACall')) WHERE  id=(SELECT id FROM company LIMIT 1);
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('ACTIVITY_VIEW_FORM', 'Activity')) WHERE  id=(SELECT id FROM company LIMIT 1);
