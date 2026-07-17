delete from "0".property where objectName = 'projectList';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('projectList', 'Projects', 'Project', 'Projects', 'P', 'pm', false);

delete from "0_template".property where objectName = 'projectList';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('projectList', 'Projects', 'Project', 'Projects', 'P', 'pm', false);

delete from "anv".property where objectName = 'projectList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('projectList', 'Projects', 'Project', 'Projects', 'P', 'pm', false);