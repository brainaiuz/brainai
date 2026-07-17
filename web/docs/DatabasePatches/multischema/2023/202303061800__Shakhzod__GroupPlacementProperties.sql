insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('groupPlacement', 'Group Placement', 'Group Placement', 'Group Placement', 'Gp', 'hrms', true);

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code = 'HRMS_MODULE' limit 1),
       (select id from "anv".property where objectName='groupPlacement' limit 1),
       (select id from "anv".container where code='recruitment' limit 1), 17, 'hrms');

insert into "anv".model (formid, title, viewname, active)
values ('GROUP_PLACEMENT_FORM', 'Group Placement List View', 'GroupPlacementList', true);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('GROUP_PLACEMENT_FORM', 'DATE', true, false, 'COL_2', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('GROUP_PLACEMENT_FORM', 'INVOLVED_EMPLOYEES', true, false, 'COL_1', 'INVOLVED_EMPLOYEES', 1);

insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('GROUP_PLACEMENT_FORM', 'BASIC_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('GROUP_PLACEMENT_FORM', 'INVOLVED_EMPLOYEES', 0, true);

insert into "anv".genericsettings (key, value)
values ('GROUP_PLACEMENT_ENABLE', 'NO');