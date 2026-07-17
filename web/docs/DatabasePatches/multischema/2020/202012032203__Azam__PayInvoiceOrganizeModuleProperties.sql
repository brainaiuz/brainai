delete from "anv".property where objectName = 'payBillsList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('payBillsList', 'Pay Invoices', 'Pay Invoice', 'Pay Invoices', 'PI', 'accounting', false, false);