delete from "0".property where objectName = 'requestforpurchase';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('requestforpurchase', 'RFP', 'RFP', 'RFP', 'RFP', 'accounting', false);

delete from "0_template".property where objectName = 'requestforquote';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('requestforpurchase', 'RFP', 'RFP', 'RFP', 'RFP', 'accounting', false);

delete from "anv".property where objectName = 'requestforquote';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('requestforpurchase', 'RFP', 'RFP', 'RFP', 'RFP', 'accounting', false);