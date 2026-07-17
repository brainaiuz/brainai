insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('attendanceTerminalList', 'Terminal List', 'Terminal List', 'Terminal List', 'ATL', 'hrms', false)
on conflict do nothing;

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CORE' limit 1),
        (select id from "anv".property where objectName='attendanceTerminalList' limit 1),
        (select id from "anv".container where code='availability' limit 1),
        7,
        'hrms');
