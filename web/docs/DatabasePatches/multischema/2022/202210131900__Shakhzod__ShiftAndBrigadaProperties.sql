insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('shift', 'shift', 'shift', 'shifts', 'Sh', 'hrms', true);



insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code = 'HRMS_MODULE' limit 1),
       (select id from "anv".property where objectName='shift' limit 1),
       (select id from "anv".container where code='availability' limit 1), 17, 'hrms');

insert into "anv".model (formid, title, viewname, active)
values ('SHIFT_FORM', 'shift', 'shiftList', true);


insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('brigada', 'brigada', 'brigada', 'brigadas', 'B', 'hrms', true);


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code = 'HRMS_MODULE' limit 1),
       (select id from "anv".property where objectName='brigada' limit 1),
       (select id from "anv".container where code='availability' limit 1), 17, 'hrms');



insert into "anv".model (formid, title, viewname, active)
values ('BRIGADA_FORM', 'brigada', 'brigadaList', true);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('SHIFT_FORM', 'monthPicker', true, false, 'COL_3', 'BASIC_INFORMATION', 0);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('SHIFT_FORM', 'shiftContainer', true, false, 'COL_3', 'BASIC_INFORMATION', 2);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('SHIFT_FORM', 'shift', true, false, 'COL_3', 'BASIC_INFORMATION', 1);

insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('SHIFT_FORM', 'BASIC_INFORMATION', 0, true);

insert into "anv".genericsettings (key, value)
values ('SHIFT_ENABLE', 'NO');
