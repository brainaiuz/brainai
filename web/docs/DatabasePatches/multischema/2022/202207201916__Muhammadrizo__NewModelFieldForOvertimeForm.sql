delete from "anv".modelfield where form_id = 'OVERTIME_FORM' and field_id = 'DEFAULT_HOUR';
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('OVERTIME_FORM', 'DEFAULT_HOUR', false, false, 'COL_3', 'BASIC_INFORMATION', 1);


update "anv".pmnumberingsettings set overtimenumberingformat = 'prefixP:OVR/numbersP:0001/suffixP:false/',delimetrovertimenumbering = '' where id = 1;

delete from "anv".modelfield where form_id = 'OVERTIME_FORM' and field_id = 'OVERTIME_NUMBER';
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder, label)
values ('OVERTIME_FORM', 'OVERTIME_NUMBER', false, false, 'COL_3', 'BASIC_INFORMATION', 0, 'Overtime Code');
