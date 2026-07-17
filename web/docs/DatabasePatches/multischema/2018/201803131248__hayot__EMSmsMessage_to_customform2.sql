delete from "0".model where formid='SMS_MESSAGE_FORM';
delete from "anv".model where formid='SMS_MESSAGE_FORM';
insert into "0".model(active, formid, title, viewname) values(true, 'SMS_MESSAGE_FORM', 'Mail list form', 'Mail');
insert into "anv".model(active, formid, title, viewname) values(true, 'SMS_MESSAGE_FORM', 'Mail list form', 'Mail');

delete from "0".modelfield where form_id='SMS_MESSAGE_FORM';
delete from "0".customformsection where form_id='SMS_MESSAGE_FORM';
insert into "0".modelfield
(form_id,           section,                  columntype, mandatory,          forder,       field_id) values
('SMS_MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            0,	          'SENDER'),
('SMS_MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            1,	          'CRM_MESSAGE_DATETABLE'),
('SMS_MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            2,	          'CRM_MESSAGE_TIMETABLE'),
('SMS_MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            3,	          'DESCRIPTION'),
('SMS_MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_2',	  true, 	            0,	          'CRM_MESSAGE_SUBSCRIPTION_LISTS'),
('SMS_MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_2',	  false, 	            1,	          'CRM_MESSAGE_ANTI_SPAN');
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('SMS_MESSAGE_FORM', 'CRM_MESSAGE_DETAILS', true, false, 0);

delete from "anv".modelfield where form_id='SMS_MESSAGE_FORM';
delete from "anv".customformsection where form_id='SMS_MESSAGE_FORM';
insert into "anv".modelfield
(form_id,           section,                  columntype, mandatory,          forder,       field_id) values
('SMS_MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            0,	          'SENDER'),
('SMS_MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            1,	          'CRM_MESSAGE_DATETABLE'),
('SMS_MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            2,	          'CRM_MESSAGE_TIMETABLE'),
('SMS_MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            3,	          'DESCRIPTION'),
('SMS_MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_2',	  true, 	            0,	          'CRM_MESSAGE_SUBSCRIPTION_LISTS'),
('SMS_MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_2',	  false, 	            1,	          'CRM_MESSAGE_ANTI_SPAN');
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('SMS_MESSAGE_FORM', 'CRM_MESSAGE_DETAILS', true, false, 0);
