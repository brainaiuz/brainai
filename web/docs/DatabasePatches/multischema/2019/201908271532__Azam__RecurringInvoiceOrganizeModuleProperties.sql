delete from "0".property where objectName = 'recurringinvoice';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('recurringinvoice', 'Recurring Invoice', 'Recurring Invoice', 'Recurring Invoices', 'RI', 'accounting', false);

delete from "0_template".property where objectName = 'recurringinvoice';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('recurringinvoice', 'Recurring Invoice', 'Recurring Invoice', 'Recurring Invoices', 'RI', 'accounting', false);

delete from "anv".property where objectName = 'recurringinvoice';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('recurringinvoice', 'Recurring Invoice', 'Recurring Invoice', 'Recurring Invoices', 'RI', 'accounting', false);