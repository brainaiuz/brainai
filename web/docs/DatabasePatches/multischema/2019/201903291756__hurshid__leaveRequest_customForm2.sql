delete from "anv".model where formid='LEAVE_REQUEST_FORM';
insert into "anv".model (formid, title, viewname, active) values('LEAVE_REQUEST_FORM', 'Leave Request Form', 'LeaveRequest', true);

delete from "anv".customformsection where form_id='LEAVE_REQUEST_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LEAVE_REQUEST_FORM', 'GENERAL_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LEAVE_REQUEST_FORM', 'SEND_NOTIFICATION', 1, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LEAVE_REQUEST_FORM', 'ATTACHMENTS', 2, false);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('LEAVE_REQUEST_FORM', 'ADDITIONAL_INFORMATION', 3, false);

delete from "anv".modelfield where form_id='LEAVE_REQUEST_FORM' and field_id in ('EMPLOYEES', 'HR_MANAGER', 'ASSIGNEES', 'ATTACHMENTS');
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'EMPLOYEES', true, 'COL_1', 'GENERAL_INFORMATION', 0);
update "anv".modelfield set mandatory=true, columntype='COL_1', fsection='GENERAL_INFORMATION', forder=1 where form_id='LEAVE_REQUEST_FORM' and field_id='REASON';
update "anv".modelfield set mandatory=true, columntype='COL_1', fsection='GENERAL_INFORMATION', forder=2 where form_id='LEAVE_REQUEST_FORM' and field_id='START_DATE';
update "anv".modelfield set mandatory=true, columntype='COL_1', fsection='GENERAL_INFORMATION', forder=3 where form_id='LEAVE_REQUEST_FORM' and field_id='DUE_DATE';
update "anv".modelfield set mandatory=true, columntype='COL_1', fsection='GENERAL_INFORMATION', forder=4 where form_id='LEAVE_REQUEST_FORM' and field_id='DESCRIPTION';
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'PAID', true, 'COL_1', 'GENERAL_INFORMATION', 6);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'TAKE_LIVE_TYPE', true, 'COL_1', 'GENERAL_INFORMATION', 7);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'TAKEN_FROM_ALLOWANCE', true, 'COL_1', 'GENERAL_INFORMATION', 6);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'HR_MANAGER', true, 'COL_1', 'GENERAL_INFORMATION', 5);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'CALENDAR', false, 'COL_2', 'GENERAL_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'CHART', false, 'COL_2', 'GENERAL_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'ASSIGNEES', false, 'COL_1', 'SEND_NOTIFICATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, nolabelfor) values('LEAVE_REQUEST_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS', 0, 'addForm, editForm, viewForm');

update "anv".modelfield set hideincustomizeform=true where form_id='LEAVE_REQUEST_FORM' and
field_id in ('NEXT_APPROVER_STATUS', 'STATUS','OTHER_REASON', 'START_TIME', 'DUE_TIME','PREV_APPROVER', 'PREV_APPROVER_EMAIL','PREV_APPROVER_STATUS', 'CURRENT_APPROVER',
'CURRENT_APPROVER_EMAIL', 'CURRENT_APPROVER_STATUS', 'NEXT_APPROVER', 'NEXT_APPROVER_EMAIL', 'CREATED_DATE', 'REGISTERED_BY', 'REGISTERED_BY_EMAIL');


delete from "0".model where formid='LEAVE_REQUEST_FORM';
insert into "0".model (formid, title, viewname, active) values('LEAVE_REQUEST_FORM', 'Leave Request Form', 'LeaveRequest', true);

delete from "0".customformsection where form_id='LEAVE_REQUEST_FORM';
insert into "0".customformsection (form_id, section, sorder, expanded) values('LEAVE_REQUEST_FORM', 'GENERAL_INFORMATION', 0, true);
insert into "0".customformsection (form_id, section, sorder, expanded) values('LEAVE_REQUEST_FORM', 'SEND_NOTIFICATION', 1, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('LEAVE_REQUEST_FORM', 'ATTACHMENTS', 2, false);
insert into "0".customformsection (form_id, section, sorder, expanded) values('LEAVE_REQUEST_FORM', 'ADDITIONAL_INFORMATION', 3, false);

delete from "0".modelfield where form_id='LEAVE_REQUEST_FORM' and field_id in ('EMPLOYEES', 'HR_MANAGER', 'ASSIGNEES', 'ATTACHMENTS');
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'EMPLOYEES', true, 'COL_1', 'GENERAL_INFORMATION', 0);
update "0".modelfield set mandatory=true, columntype='COL_1', fsection='GENERAL_INFORMATION', forder=1 where form_id='LEAVE_REQUEST_FORM' and field_id='REASON';
update "0".modelfield set mandatory=true, columntype='COL_1', fsection='GENERAL_INFORMATION', forder=2 where form_id='LEAVE_REQUEST_FORM' and field_id='START_DATE';
update "0".modelfield set mandatory=true, columntype='COL_1', fsection='GENERAL_INFORMATION', forder=3 where form_id='LEAVE_REQUEST_FORM' and field_id='DUE_DATE';
update "0".modelfield set mandatory=true, columntype='COL_1', fsection='GENERAL_INFORMATION', forder=4 where form_id='LEAVE_REQUEST_FORM' and field_id='DESCRIPTION';
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'PAID', true, 'COL_1', 'GENERAL_INFORMATION', 6);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'TAKE_LIVE_TYPE', true, 'COL_1', 'GENERAL_INFORMATION', 7);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'TAKEN_FROM_ALLOWANCE', true, 'COL_1', 'GENERAL_INFORMATION', 6);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'HR_MANAGER', true, 'COL_1', 'GENERAL_INFORMATION', 5);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'CALENDAR', false, 'COL_2', 'GENERAL_INFORMATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'CHART', false, 'COL_2', 'GENERAL_INFORMATION', 1);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('LEAVE_REQUEST_FORM', 'ASSIGNEES', false, 'COL_1', 'SEND_NOTIFICATION', 0);
insert into "0".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, nolabelfor) values('LEAVE_REQUEST_FORM', 'ATTACHMENTS', false, 'COL_1', 'ATTACHMENTS', 0, 'addForm, editForm, viewForm');

update "0".modelfield set hideincustomizeform=true where form_id='LEAVE_REQUEST_FORM' and
field_id in ('NEXT_APPROVER_STATUS', 'STATUS','OTHER_REASON', 'START_TIME', 'DUE_TIME','PREV_APPROVER', 'PREV_APPROVER_EMAIL','PREV_APPROVER_STATUS', 'CURRENT_APPROVER',
'CURRENT_APPROVER_EMAIL', 'CURRENT_APPROVER_STATUS', 'NEXT_APPROVER', 'NEXT_APPROVER_EMAIL', 'CREATED_DATE', 'REGISTERED_BY', 'REGISTERED_BY_EMAIL');