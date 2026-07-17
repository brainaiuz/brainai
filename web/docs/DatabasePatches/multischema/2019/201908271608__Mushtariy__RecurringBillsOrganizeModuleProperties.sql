delete from "0".property where objectName = 'recurringbill';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('recurringbill', 'Recurring Bill', 'Recurring Bill', 'Recurring Bills', 'RB', 'accounting', false);

delete from "0_template".property where objectName = 'recurringbill';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('recurringbill', 'Recurring Bill', 'Recurring Bill', 'Recurring Bills', 'RB', 'accounting', false);

delete from "anv".property where objectName = 'recurringbill';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('recurringbill', 'Recurring Bill', 'Recurring Bill', 'Recurring Bills', 'RB', 'accounting', false);