delete from "0".property where objectName = 'subProjectList';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('subProjectList', 'Sub Projects', 'Sub Project', 'Sub Projects', 'SP', 'pm', false);

delete from "0_template".property where objectName = 'subProjectList';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('subProjectList', 'Sub Projects', 'Sub Project', 'Sub Projects', 'SP', 'pm', false);

delete from "anv".property where objectName = 'subProjectList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('subProjectList', 'Sub Projects', 'Sub Project', 'Sub Projects', 'SP', 'pm', false);