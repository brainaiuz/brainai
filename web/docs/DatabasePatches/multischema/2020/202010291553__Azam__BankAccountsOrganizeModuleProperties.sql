delete from "anv".property where objectName = 'bankaccount';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('bankaccount', 'Bank Accounts', 'Bank Account', 'Bank Accounts', 'BA', 'accounting', false, false);

delete from "0_template".property where objectName = 'bankaccount';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('bankaccount', 'Bank Accounts', 'Bank Account', 'Bank Accounts', 'BA', 'accounting', false, false);