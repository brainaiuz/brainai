delete from "anv".modelfield where form_id = 'LEAVE_REQUEST_FORM' and field_id = 'LEAVE_REQUEST_NUMBER';

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values('LEAVE_REQUEST_FORM', 'LEAVE_REQUEST_NUMBER', false, 'COL_1', 'GENERAL_INFORMATION', -1);