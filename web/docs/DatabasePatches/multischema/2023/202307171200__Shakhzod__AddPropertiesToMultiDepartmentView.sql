insert into "anv".model (formid, title, viewname, active)
values ('MULTI_DEPARTMENT_FORM', 'Multi Department Form', 'Multi Department', true);
insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('MULTI_DEPARTMENT_FORM', 'DEPARTMENT_DETAILS', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('MULTI_DEPARTMENT_FORM', 'DEPARTMENT_LOCATIONS', 1, true);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('MULTI_DEPARTMENT_FORM', 'DEPARTMENT_NAME', false, 'COL_1', 'DEPARTMENT_DETAILS', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('MULTI_DEPARTMENT_FORM', 'DEPARTMENT_PARENT', false, 'COL_1', 'DEPARTMENT_DETAILS', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('MULTI_DEPARTMENT_FORM', 'DEPARTMENT_LEADER', false, 'COL_2', 'DEPARTMENT_DETAILS', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('MULTI_DEPARTMENT_FORM', 'DEPARTMENT_LOCATION', false, 'COL_2', 'DEPARTMENT_LOCATIONS', 3);

