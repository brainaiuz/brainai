insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('terminalAttendance', 'Terminal Attendance', 'Terminal Attendance', 'Terminal Attendance', 'TA', 'hrms', false)
on conflict do nothing;



insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CORE' limit 1),
        (select id from "anv".property where objectName='terminalAttendance' limit 1),
        (select id from "anv".container where code='availability' limit 1), 7, 'hrms');