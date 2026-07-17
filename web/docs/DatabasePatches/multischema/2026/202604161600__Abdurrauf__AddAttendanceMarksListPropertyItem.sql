insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('attendanceMarksList', 'Attendance Marks', 'Attendance Marks', 'Attendance Marks', 'AML', 'hrms', false)
on conflict do nothing;

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='CORE' limit 1),
        (select id from "anv".property where objectName='attendanceMarksList' limit 1),
        (select id from "anv".container where code='availability' limit 1),
        (select sorder from "anv".container_item where propertyID = (select id from "anv".property where objectName='terminalAttendance' limit 1) limit 1),
        'hrms');

update "anv".container_item
set sorder = sorder + 1
where propertyID = (select id from "anv".property where objectName='terminalAttendance' limit 1);
