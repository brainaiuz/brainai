

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('buildAssemblyItem', 'Build Assembly Item', 'Build Assembly Item', 'Build Assembly Items', 'BAI', 'accounting', true) on conflict do nothing;

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values (
           (select id from "anv".mymodule where code='PRODUCTION' limit 1),
           (select id from "anv".property where objectName='buildAssemblyItem' limit 1),
           (select id from "anv".container where code='production' limit 1), 1, 'production');


insert into "anv".model (formid, title, viewname, active) values('BUILD_ASSEMBLY_FORM', 'Build Assembly', 'BuildAssembly', true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BUILD_ASSEMBLY_FORM', 'BASIC_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded) values('BUILD_ASSEMBLY_FORM', 'ASSEMBLY_ITEMS', 1, true);

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUILD_ASSEMBLY_FORM', 'PRODUCT', true, 'COL_1', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUILD_ASSEMBLY_FORM', 'DATE', true, 'COL_2', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUILD_ASSEMBLY_FORM', 'NUMBER', true, 'COL_3', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUILD_ASSEMBLY_FORM', 'QUANTITY', true, 'COL_1', 'BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUILD_ASSEMBLY_FORM', 'WAREHOUSE', true, 'COL_2', 'BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder) values('BUILD_ASSEMBLY_FORM', 'ASSEMBLY_ITEMS', false, 'COL_1', 'ASSEMBLY_ITEMS', 2);

