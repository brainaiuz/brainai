delete from "0".property where objectName = 'purchaseinvoice';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('purchaseinvoice', 'Purchase Invoice', 'Purchase Invoice', 'Purchase Invoices', 'PI', 'accounting', false);

delete from "0_template".property where objectName = 'purchaseinvoice';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('purchaseinvoice', 'Purchase Invoice', 'Purchase Invoice', 'Purchase Invoices', 'PI', 'accounting', false);

delete from "anv".property where objectName = 'purchaseinvoice';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('purchaseinvoice', 'Purchase Invoice', 'Purchase Invoice', 'Purchase Invoices', 'PI', 'accounting', false);