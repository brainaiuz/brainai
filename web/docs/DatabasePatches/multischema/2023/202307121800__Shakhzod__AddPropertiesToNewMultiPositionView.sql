insert into "anv".model (formid, title, viewname, active)
values ('MULTI_POSITION_FORM', 'Multi Position Form', 'Multi Position', true);
insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('MULTI_POSITION_FORM', 'POSITION_DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('MULTI_POSITION_FORM', 'POSITION_DEPARTMENTS', 1, true);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('MULTI_POSITION_FORM', 'POSITION_TITLE', false, 'COL_1', 'POSITION_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('MULTI_POSITION_FORM', 'STATUS', false, 'COL_1', 'POSITION_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('MULTI_POSITION_FORM', 'TYPE', false, 'COL_2', 'POSITION_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('MULTI_POSITION_FORM', 'COUNT', false, 'COL_2', 'POSITION_DETAILS', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('MULTI_POSITION_FORM', 'DEPARTMENTS', false, 'COL_1', 'POSITION_DEPARTMENTS', 0);
