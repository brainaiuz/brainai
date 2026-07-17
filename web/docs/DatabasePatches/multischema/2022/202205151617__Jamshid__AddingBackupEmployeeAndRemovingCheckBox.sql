delete from "anv".modelfield where field_id = 'BACKUP_EMPLOYEE_CHECK_BOX' and form_id = 'LEAVE_REQUEST_FORM';
delete from "anv".modelfield where field_id = 'BACKUP_EMPLOYEE' and form_id = 'LEAVE_REQUEST_FORM';
insert into "anv".modelfield(field_id, form_id, columntype, fsection, forder) values('BACKUP_EMPLOYEE', 'LEAVE_REQUEST_FORM', 'COL_1', 'GENERAL_INFORMATION', 20);

delete from "0".modelfield where field_id = 'BACKUP_EMPLOYEE_CHECK_BOX' and form_id = 'LEAVE_REQUEST_FORM';
delete from "0".modelfield where field_id = 'BACKUP_EMPLOYEE' and form_id = 'LEAVE_REQUEST_FORM';
insert into "0".modelfield(field_id, form_id, columntype, fsection, forder) values('BACKUP_EMPLOYEE', 'LEAVE_REQUEST_FORM', 'COL_1', 'GENERAL_INFORMATION', 20);