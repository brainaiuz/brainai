delete from "0".property where objectName = 'fixedassets';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('fixedassets', 'Fixed Asset', 'Fixed Asset', 'Fixed Assets', 'FA', 'accounting', false);

delete from "0_template".property where objectName = 'fixedassets';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('fixedassets', 'Fixed Asset', 'Fixed Asset', 'Fixed Assets', 'FA', 'accounting', false);

delete from "anv".property where objectName = 'fixedassets';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('fixedassets', 'Fixed Asset', 'Fixed Asset', 'Fixed Assets', 'FA', 'accounting', false);