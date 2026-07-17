delete from "0".property where objectName = 'clientList';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('clientList', 'Customer Center', 'Customer Center', 'Customer Center', 'CC', 'accounting', false);

delete from "0_template".property where objectName = 'clientList';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('clientList', 'Customer Center', 'Customer Center', 'Customer Center', 'CC', 'accounting', false);

delete from "anv".property where objectName = 'clientList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('clientList', 'Customer Center', 'Customer Center', 'Customer Center', 'CC', 'accounting', false);