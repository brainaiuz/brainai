delete from "anv".modelfield where form_id = 'SHIFT_FORM';
delete from "anv".customformsection where form_id = 'SHIFT_FORM';

insert into "anv".customformsection (form_id, section, sorder, expanded) values ('SHIFT_FORM', 'BASIC_INFORMATION', 0, true);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values ('SHIFT_FORM', 'monthPicker', true, false, 'COL_1', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values ('SHIFT_FORM', 'APPROVERS', true, false, 'COL_2', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('SHIFT_FORM', 'MANAGER', false, 'COL_3', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('SHIFT_FORM', 'BACKUP_MANAGER', false, 'COL_3', 'BASIC_INFORMATION', 1);

insert into "anv".customformsection (form_id, section, sorder, expanded) values('SHIFT_FORM', 'INVOLVED_EMPLOYEES', 1, true);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values ('SHIFT_FORM', 'shift', true, false, 'COL_1', 'INVOLVED_EMPLOYEES', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values ('SHIFT_FORM', 'shiftContainer', true, false, 'COL_1', 'INVOLVED_EMPLOYEES', 1);

