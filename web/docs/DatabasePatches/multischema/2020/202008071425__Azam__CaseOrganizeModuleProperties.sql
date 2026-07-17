delete from "anv".property where objectName = 'caseList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('caseList', 'Cases', 'Case', 'Cases', 'CS', 'crm', false);

delete from "0_template".property where objectName = 'caseList';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('caseList', 'Cases', 'Case', 'Cases', 'CS', 'crm', false);
