delete from "anv".property where objectName = 'customerPrepayment';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('customerPrepayment', 'Customer Prepayment', 'Customer Prepayment', 'Customer Prepayments', 'CP', 'accounting', false, false);
