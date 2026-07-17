
delete from "0".model where formid='LOGACALL_FORM';
insert into "0".model (formid, title, viewname, active) values('LOGACALL_FORM', 'Log A Call Form', 'LogACall', true);

delete from "0".customformsection where form_id='LOGACALL_FORM';
insert into "0".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM', 'CALL_INFORMATION', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM', 'ATTACHMENTS', 1, true );
insert into "0".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM', 'ADVANCED', 2, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM', 'ADDITIONAL_INFORMATION', 3, false);

delete from "0".modelfield where form_id='LOGACALL_FORM';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'SUBJECT', true, 'COL_1', 'CALL_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'SHARED_WITH', true, 'COL_1', 'CALL_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'GUESTS', false, 'COL_1', 'CALL_INFORMATION', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'ENABLE_REMINDER', false, 'COL_1', 'CALL_INFORMATION', 3);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'WHEN', true, 'COL_2', 'CALL_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'DESCRIPTION', false, 'COL_2', 'CALL_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'CALL_TYPE', false, 'COL_2', 'CALL_INFORMATION', 2);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'CALL_DETAILS', false, 'COL_2', 'CALL_INFORMATION', 3);

insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder) values('LOGACALL_FORM', 'ATTACHMENTS', false, 'ATTACHMENTS', 0);
insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder) values('LOGACALL_FORM', 'RECURRING_WIDGET', false, 'ADVANCED', 0);



delete from "anv".model where formid='LOGACALL_FORM';
insert into "anv".model (formid, title, viewname, active) values('LOGACALL_FORM', 'Log A Call Form', 'LogACall', true);

delete from "anv".customformsection where form_id='LOGACALL_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM', 'CALL_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM', 'ATTACHMENTS', 1, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM', 'ADVANCED', 2, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LOGACALL_FORM', 'ADDITIONAL_INFORMATION', 3, false);

delete from "anv".modelfield where form_id='LOGACALL_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'SUBJECT', true, 'COL_1', 'CALL_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'SHARED_WITH', true, 'COL_1', 'CALL_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'GUESTS', false, 'COL_1', 'CALL_INFORMATION', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'ENABLE_REMINDER', false, 'COL_1', 'CALL_INFORMATION', 3);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'WHEN', true, 'COL_2', 'CALL_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'DESCRIPTION', false, 'COL_2', 'CALL_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'CALL_TYPE', false, 'COL_2', 'CALL_INFORMATION', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LOGACALL_FORM', 'CALL_DETAILS', false, 'COL_2', 'CALL_INFORMATION', 3);

insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder) values('LOGACALL_FORM', 'ATTACHMENTS', false, 'ATTACHMENTS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder) values('LOGACALL_FORM', 'RECURRING_WIDGET', false, 'ADVANCED', 0);











delete from "0".model where formid='ACTIVITY_FORM';
insert into "0".model (formid, title, viewname, active) values('ACTIVITY_FORM', 'Activity Form', 'Activity', true);

delete from "0".customformsection where form_id='ACTIVITY_FORM';
insert into "0".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_FORM', 'EVENT_INFORMATION', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_FORM', 'ATTACHMENTS', 1, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_FORM', 'ADVANCED', 2, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_FORM', 'ADDITIONAL_INFORMATION', 3, false);

delete from "0".modelfield where form_id='ACTIVITY_FORM';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_FORM', 'SUBJECT', true, 'COL_1', 'EVENT_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_FORM', 'SHARED_WITH', true, 'COL_1', 'EVENT_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_FORM', 'GUESTS', false, 'COL_1', 'EVENT_INFORMATION', 2);

insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_FORM', 'WHEN', true, 'COL_2', 'EVENT_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_FORM', 'DESCRIPTION', false, 'COL_2', 'EVENT_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_FORM', 'ENABLE_REMINDER', false, 'COL_2', 'EVENT_INFORMATION', 2);


insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder, nolabelfor) values('ACTIVITY_FORM', 'ATTACHMENTS', false, 'ATTACHMENTS', 0, 'editForm');
insert into "0".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ACTIVITY_FORM', 'RECURRING_WIDGET', false, 'ADVANCED', 0);


delete from "anv".model where formid='ACTIVITY_FORM';
insert into "anv".model (formid, title, viewname, active) values('ACTIVITY_FORM', 'Activity Form', 'Activity', true);

delete from "anv".customformsection where form_id='ACTIVITY_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_FORM', 'EVENT_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_FORM', 'ATTACHMENTS', 1, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_FORM', 'ADVANCED', 2, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('ACTIVITY_FORM', 'ADDITIONAL_INFORMATION', 3, false);

delete from "anv".modelfield where form_id='ACTIVITY_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_FORM', 'SUBJECT', true, 'COL_1', 'EVENT_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_FORM', 'SHARED_WITH', true, 'COL_1', 'EVENT_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_FORM', 'GUESTS', false, 'COL_1', 'EVENT_INFORMATION', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_FORM', 'WHEN', true, 'COL_2', 'EVENT_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_FORM', 'DESCRIPTION', false, 'COL_2', 'EVENT_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('ACTIVITY_FORM', 'ENABLE_REMINDER', false, 'COL_2', 'EVENT_INFORMATION', 2);

insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder, nolabelfor) values('ACTIVITY_FORM', 'ATTACHMENTS', false, 'ATTACHMENTS', 0, 'editForm');
insert into "anv".modelfield (form_id, field_id, mandatory,  fsection, forder) values('ACTIVITY_FORM', 'RECURRING_WIDGET', false, 'ADVANCED', 0);


update "0".modelfield set nolabelfor ='viewForm,addForm,editForm' where field_id = 'TASK_NOTES' and form_id = 'TASK_MAX_FORM';
update "0".modelfield set nolabelfor ='viewForm,addForm,editForm' where field_id = 'LINKS' and form_id = 'TASK_MAX_FORM';

update "anv".modelfield set nolabelfor ='viewForm,addForm,editForm' where field_id = 'TASK_NOTES' and form_id = 'TASK_MAX_FORM';
update "anv".modelfield set nolabelfor ='viewForm,addForm,editForm' where field_id = 'LINKS' and form_id = 'TASK_MAX_FORM';


UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('LOGACALL_FORM', 'LogACall')) WHERE  id=(SELECT id FROM company LIMIT 1);
UPDATE company SET selectFunctioncolumn =(SELECT "anv".convertCustomFieldsToModelFields('ACTIVITY_FORM', 'Activity')) WHERE  id=(SELECT id FROM company LIMIT 1);


