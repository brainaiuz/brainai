

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('assemblyItems', 'Assembly Item', 'Assembly Item', 'Assembly Items', 'AI', 'accounting', true) on conflict do nothing;

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values (
        (select id from "anv".mymodule where code='PRODUCTION' limit 1),
        (select id from "anv".property where objectName='assemblyItems' limit 1),
        (select id from "anv".container where code='production' limit 1), 0, 'production');

