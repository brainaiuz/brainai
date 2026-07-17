DELETE FROM "anv".container_item
WHERE propertyID = (
    SELECT id
    FROM "anv".property
    WHERE objectName = 'newFlameOrgChart'
    LIMIT 1
)
  AND moduleID = (
    SELECT id
    FROM "anv".mymodule
    WHERE code = 'HRMS_MODULE'
    LIMIT 1
)
  AND containerId = (
    SELECT id
    FROM "anv".container
    WHERE code = 'hrmsMain'
    LIMIT 1
);

DELETE FROM "anv".property
WHERE objectName = 'newFlameOrgChart';

insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('newFlameOrgChart', 'New Organization Chart', 'New Organization Chart', 'New Organization Chart', 'OC', 'hrms',
        false)
on conflict do nothing;


insert into "anv".container_item(moduleID, propertyID, containerId, moduleCode, sorder)
values ((select id from "anv".mymodule where code = 'HRMS_MODULE' limit 1),
        (select id from "anv".property where objectName = 'newFlameOrgChart' limit 1),
        (select id from "anv".container where code = 'hrmsMain' limit 1), 'hrms', 15);