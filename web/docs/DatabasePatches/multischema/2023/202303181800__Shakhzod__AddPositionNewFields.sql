insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('POSITION_FORM', 'LOCATION', false, 'COL_2', 'POSITION_DETAILS', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('POSITION_FORM', 'DEPARTMENT', false, 'COL_2', 'POSITION_DETAILS', 4);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('POSITION_FORM', 'COUNT', false, 'COL_3', 'POSITION_DETAILS', 0);
update "anv".modelfield
set columntype = 'COL_3'
where form_id = 'POSITION_FORM'
  and field_id = 'DESCRIPTION';
update "anv".modelfield
set forder = 1
where form_id = 'POSITION_FORM'
  and field_id = 'DESCRIPTION';

