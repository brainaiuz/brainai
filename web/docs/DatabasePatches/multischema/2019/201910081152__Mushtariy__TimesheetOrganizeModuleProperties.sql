delete from "0".property where objectName = 'timesheet';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('timesheet', 'Timesheet', 'Timesheet', 'Timesheet', 'Timesheet', 'accounting', false);

delete from "0_template".property where objectName = 'timesheet';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('timesheet', 'Timesheet', 'Timesheet', 'Timesheet', 'Timesheet', 'accounting', false);

delete from "anv".property where objectName = 'timesheet';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('timesheet', 'Timesheet', 'Timesheet', 'Timesheet', 'Timesheet', 'accounting', false);