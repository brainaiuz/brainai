delete from "anv".property where objectName = 'logCall';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('logCall', 'Log a Call', 'Log a Call', 'Log a Calls', 'LC', 'crm', false, false);

delete from "0_template".property where objectName = 'logCall';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive, iscustom)
values ('logCall', 'Log a Call', 'Log a Call', 'Log a Calls', 'LC', 'crm', false, false);