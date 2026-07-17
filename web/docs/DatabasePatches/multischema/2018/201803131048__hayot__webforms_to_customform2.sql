delete from "0".model where formid='MAIL_LIST_FORM';
delete from "anv".model where formid='MAIL_LIST_FORM';
insert into "0".model(active, formid, title, viewname) values(true, 'MAIL_LIST_FORM', 'Mail list form', 'Mail');
insert into "anv".model(active, formid, title, viewname) values(true, 'MAIL_LIST_FORM', 'Mail list form', 'Mail');

delete from "0".modelfield where form_id='MAIL_LIST_FORM';
delete from "0".customformsection where form_id='MAIL_LIST_FORM';
insert into "0".modelfield
(form_id,           section,                  columntype, mandatory,          forder,       field_id) values
('MAIL_LIST_FORM',	'MAIL_LIST_INFORMATION',  'COL_1',	  false, 	            0,	          'NAME'),
('MAIL_LIST_FORM',	'MAIL_LIST_INFORMATION',  'COL_2',	  false, 	            0,	          'DESCRIPTION'),
('MAIL_LIST_FORM',	'MAIL_LIST_INFORMATION',  'COL_3',	  false, 	            0,	          'STATUS');
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('MAIL_LIST_FORM', 'MAIL_LIST_INFORMATION', true, false, 0);

delete from "anv".modelfield where form_id='MAIL_LIST_FORM';
delete from "anv".customformsection where form_id='MAIL_LIST_FORM';
insert into "anv".modelfield
(form_id,       section,                columntype, mandatory,         forder,       field_id) values
('MAIL_LIST_FORM',	'MAIL_LIST_INFORMATION',  'COL_1',	  false, 	            0,	          'NAME'),
('MAIL_LIST_FORM',	'MAIL_LIST_INFORMATION',  'COL_2',	  false, 	            0,	          'DESCRIPTION'),
('MAIL_LIST_FORM',	'MAIL_LIST_INFORMATION',  'COL_3',	  false, 	            0,	          'STATUS');
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('MAIL_LIST_FORM', 'MAIL_LIST_INFORMATION', true, false, 0);
