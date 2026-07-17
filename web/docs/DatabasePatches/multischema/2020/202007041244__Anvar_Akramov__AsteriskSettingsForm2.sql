delete from "0".model where formid='ASTERISK_SETTINGS_FORM';
delete from "anv".model where formid='ASTERISK_SETTINGS_FORM';
insert into "0".model(active, formid, title, viewname) values(true, 'ASTERISK_SETTINGS_FORM', 'Asterisk account form', 'AsteriskSetting');
insert into "anv".model(active, formid, title, viewname) values(true, 'ASTERISK_SETTINGS_FORM', 'Asterisk account form', 'AsteriskSetting');
delete from "0".customformsection where form_id='ASTERISK_SETTINGS_FORM';
delete from "anv".customformsection where form_id='ASTERISK_SETTINGS_FORM';

delete from "0".modelfield where form_id='ASTERISK_SETTINGS_FORM';
insert into "0".modelfield
(form_id,           fsection,                  columntype, mandatory,          forder,       field_id) values
('ASTERISK_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            0,	          'NUMBER'),
('ASTERISK_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            1,	          'ASTERISK_HOST'),
('ASTERISK_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            2,	          'ASTERISK_PORT'));
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('ASTERISK_SETTINGS_FORM', 'PROVIDER_INFORMATION', true, false, 0);

delete from "anv".modelfield where form_id='ASTERISK_SETTINGS_FORM';
insert into "anv".modelfield
(form_id,           fsection,                  columntype, mandatory,          forder,       field_id) values
('ASTERISK_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            0,	          'NUMBER'),
('ASTERISK_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            1,	          'ASTERISK_HOST'),
('ASTERISK_SETTINGS_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            2,	          'ASTERISK_PORT');
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('ASTERISK_SETTINGS_FORM', 'PROVIDER_INFORMATION', true, false, 0);
