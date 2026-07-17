delete from "anv".property where objectName = 'eventList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('eventList', 'Event', 'Event', 'Events', 'E', 'crm', false);

delete from "0_template".property where objectName = 'eventList';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('eventList', 'Event', 'Event', 'Events', 'E', 'crm', false);