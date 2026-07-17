delete from "0".property where objectName = 'opportunities';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('opportunities', 'Opportunities', 'Opportunity', 'Opportunities', 'O', 'crm', false);


delete from "0_template".property where objectName = 'opportunities';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('opportunities', 'Opportunities', 'Opportunity', 'Opportunities', 'O', 'crm', false);


delete from "anv".property where objectName = 'opportunities';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('opportunities', 'Opportunities', 'Opportunity', 'Opportunities', 'O', 'crm', false);
