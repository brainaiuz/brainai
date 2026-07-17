delete from "0".property where objectName = 'EXPENSES_CLAIM';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('EXPENSES_CLAIM', 'Expense Claim', 'Expense Claim', 'Expense Claims', 'EC', 'accounting', false);

delete from "0_template".property where objectName = 'EXPENSES_CLAIM';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('EXPENSES_CLAIM', 'Expense Claim', 'Expense Claim', 'Expense Claims', 'EC', 'accounting', false);

delete from "anv".property where objectName = 'EXPENSES_CLAIM';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('EXPENSES_CLAIM', 'Expense Claim', 'Expense Claim', 'Expense Claims', 'EC', 'accounting', false);