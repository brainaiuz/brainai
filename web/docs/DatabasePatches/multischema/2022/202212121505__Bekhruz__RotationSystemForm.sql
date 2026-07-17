insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('rotation', 'rotation', 'rotation', 'rotations', 'Rn', 'hrms', true);

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code = 'HRMS_MODULE' limit 1),
       (select id from "anv".property where objectName='rotation' limit 1),
       (select id from "anv".container where code='recruitment' limit 1), 18, 'hrms');

insert into "anv".model (formid, title, viewname, active)
values ('ROTATION_FORM', 'rotation', 'rotation', true);

insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('ROTATION_FORM', 'BASIC_INFORMATION', 0, true);

insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('ROTATION_FORM', 'ROTATION_DEPARTMENT', 1, true);


insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('ROTATION_FORM', 'ROTATION_POSITION', 2,true);


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ROTATION_FORM', 'EMPLOYEES', true, false, 'COL_1', 'BASIC_INFORMATION', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ROTATION_FORM', 'NUMBER', true, false, 'COL_2', 'BASIC_INFORMATION', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ROTATION_FORM', 'DATE', true, false, 'COL_3', 'BASIC_INFORMATION', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ROTATION_FORM', 'CURRENT_DEPARTMENT', true, false, 'COL_1', 'ROTATION_DEPARTMENT', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ROTATION_FORM', 'NEW_DEPARTMENT', true, false, 'COL_2', 'ROTATION_DEPARTMENT', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ROTATION_FORM', 'CURRENT_POSITION', true, false, 'COL_1', 'ROTATION_POSITION', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('ROTATION_FORM', 'NEW_POSITION', true, false, 'COL_2', 'ROTATION_POSITION', 0);

insert into "anv".genericsettings (key, value)
values ('ENABLE_ROTATION', 'NO');






