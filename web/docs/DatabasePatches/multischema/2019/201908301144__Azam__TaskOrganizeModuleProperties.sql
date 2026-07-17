delete from "0".property where objectName = 'taskList';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('taskList', 'Tasks', 'Task', 'Tasks', 'T', 'pm', false);

delete from "0_template".property where objectName = 'taskList';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('taskList', 'Tasks', 'Task', 'Tasks', 'T', 'pm', false);

delete from "anv".property where objectName = 'taskList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('taskList', 'Tasks', 'Task', 'Tasks', 'T', 'pm', false);