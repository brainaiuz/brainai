delete from "anv".property where objectName = 'accountList';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('accountList', 'Companies', 'Company', 'Companies', 'C', 'crm', false, false);

delete from "0_template".property where objectName = 'accountList';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('accountList', 'Companies', 'Company', 'Companies', 'C', 'crm', false, false);