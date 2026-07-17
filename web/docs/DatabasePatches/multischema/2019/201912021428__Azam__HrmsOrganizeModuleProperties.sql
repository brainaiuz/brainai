delete from "0".property where objectName = 'Hrms';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('Hrms', 'Humans', 'Human', 'Humans', 'H', 'hrms', false);

delete from "0_template".property where objectName = 'Hrms';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('Hrms', 'Humans', 'Human', 'Humans', 'H', 'hrms', false);

delete from "anv".property where objectName = 'Hrms';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('Hrms', 'Humans', 'Human', 'Humans', 'H', 'hrms', false);