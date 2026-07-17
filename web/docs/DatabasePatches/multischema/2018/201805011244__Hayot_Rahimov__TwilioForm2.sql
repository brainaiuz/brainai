delete from "0".model where formid='TWILIO_SETTINGS_FORM';
delete from "anv".model where formid='TWILIO_SETTINGS_FORM';
insert into "0".model(active, formid, title, viewname) values(true, 'TWILIO_SETTINGS_FORM', 'Twilio account form', 'TwilioSetting');
insert into "anv".model(active, formid, title, viewname) values(true, 'TWILIO_SETTINGS_FORM', 'Twilio account form', 'TwilioSetting');
delete from "0".customformsection where form_id='TWILIO_SETTINGS_FORM';
delete from "anv".customformsection where form_id='TWILIO_SETTINGS_FORM';

delete from "0".modelfield where form_id='TWILIO_SETTINGS_FORM';
insert into "0".modelfield
(form_id,           fsection,                  columntype, mandatory,          forder,       field_id) values
('TWILIO_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            0,	          'NUMBER'),
('TWILIO_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            1,	          'ACCOUNT_SID'),
('TWILIO_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            2,	          'AUTH_TOKEN'),
('TWILIO_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            3,	          'APPLICATION_SID'),
('TWILIO_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            4,	          'LINK');
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('TWILIO_SETTINGS_FORM', 'PROVIDER_INFORMATION', true, false, 0);

delete from "anv".modelfield where form_id='TWILIO_SETTINGS_FORM';
insert into "anv".modelfield
(form_id,           fsection,                  columntype, mandatory,          forder,       field_id) values
('TWILIO_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            0,	          'NUMBER'),
('TWILIO_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            1,	          'ACCOUNT_SID'),
('TWILIO_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            2,	          'AUTH_TOKEN'),
('TWILIO_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            3,	          'APPLICATION_SID'),
('TWILIO_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            4,	          'LINK');
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('TWILIO_SETTINGS_FORM', 'PROVIDER_INFORMATION', true, false, 0);
