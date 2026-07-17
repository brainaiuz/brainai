
delete from "anv".property where objectName = 'birgadaList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('birgadaList', 'Teams', 'Team', 'Teams', 'T', 'hrms', false);