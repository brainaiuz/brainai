delete from "0".property where objectName = 'requestforquote';
insert into "0".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('requestforquote', 'RFQ', 'RFQ', 'RFQ', 'RFQ', 'accounting', false);

delete from "0_template".property where objectName = 'requestforquote';
insert into "0_template".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('requestforquote', 'RFQ', 'RFQ', 'RFQ', 'RFQ', 'accounting', false);

delete from "anv".property where objectName = 'requestforquote';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('requestforquote', 'RFQ', 'RFQ', 'RFQ', 'RFQ', 'accounting', false);