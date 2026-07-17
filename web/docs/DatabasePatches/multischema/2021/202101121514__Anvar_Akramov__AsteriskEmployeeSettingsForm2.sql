delete from "0".model where formid='ASTERISK_EMPLOYEE_FORM';
delete from "anv".model where formid='ASTERISK_EMPLOYEE_FORM';
insert into "0".model(active, formid, title, viewname) values(true, 'ASTERISK_EMPLOYEE_FORM', 'Asterisk employee form', 'AsteriskEmployeeSetting');
insert into "anv".model(active, formid, title, viewname) values(true, 'ASTERISK_EMPLOYEE_FORM', 'Asterisk employee form', 'AsteriskEmployeeSetting');
delete from "0".customformsection where form_id='ASTERISK_EMPLOYEE_FORM';
delete from "anv".customformsection where form_id='ASTERISK_EMPLOYEE_FORM';

delete from "0".modelfield where form_id='ASTERISK_EMPLOYEE_FORM';
insert into "0".modelfield
(form_id,           fsection,                  columntype, mandatory,          forder,       field_id) values
('ASTERISK_EMPLOYEE_FORM',	'EMPLOYEE_INFORMATION',  'COL_1',	  false, 	            1,	          'EMPLOYEE_CODE'),
('ASTERISK_EMPLOYEE_FORM',	'EMPLOYEE_INFORMATION',  'COL_1',	  false, 	            2,	          'FIRST_NAME'),
('ASTERISK_EMPLOYEE_FORM',	'EMPLOYEE_INFORMATION',  'COL_1',	  false, 	            3,	          'LAST_NAME'),
('ASTERISK_EMPLOYEE_FORM',	'EMPLOYEE_INFORMATION',  'COL_1',	  false, 	            4,	          'EMAIL'),
('ASTERISK_EMPLOYEE_FORM',	'EMPLOYEE_INFORMATION',  'COL_1',	  false, 	            5,	          'PHONE'),
('ASTERISK_EMPLOYEE_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            6,	          'ASTERISK_USERNAME'),
('ASTERISK_EMPLOYEE_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            7,	          'ASTERISK_PASSWORD');

INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('ASTERISK_EMPLOYEE_FORM', 'EMPLOYEE_INFORMATION', true, false, 0);
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('ASTERISK_EMPLOYEE_FORM', 'PROVIDER_INFORMATION', true, false, 0);

delete from "anv".modelfield where form_id='ASTERISK_EMPLOYEE_FORM';
insert into "anv".modelfield
(form_id,           fsection,                  columntype, mandatory,          forder,       field_id) values
('ASTERISK_EMPLOYEE_FORM',	'EMPLOYEE_INFORMATION',  'COL_1',	  false, 	            1,	          'EMPLOYEE_CODE'),
('ASTERISK_EMPLOYEE_FORM',	'EMPLOYEE_INFORMATION',  'COL_1',	  false, 	            2,	          'FIRST_NAME'),
('ASTERISK_EMPLOYEE_FORM',	'EMPLOYEE_INFORMATION',  'COL_1',	  false, 	            3,	          'LAST_NAME'),
('ASTERISK_EMPLOYEE_FORM',	'EMPLOYEE_INFORMATION',  'COL_1',	  false, 	            4,	          'EMAIL'),
('ASTERISK_EMPLOYEE_FORM',	'EMPLOYEE_INFORMATION',  'COL_1',	  false, 	            5,	          'PHONE'),
('ASTERISK_EMPLOYEE_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            6,	          'ASTERISK_USERNAME'),
('ASTERISK_EMPLOYEE_FORM',	'PROVIDER_INFORMATION',  'COL_1',	  false, 	            7,	          'ASTERISK_PASSWORD'),
('ASTERISK_EMPLOYEE_FORM',	'EMPLOYEE_INFORMATION',  'COL_1',	  false, 	            8,	          'DEPARTMENT'),
('ASTERISK_EMPLOYEE_FORM',	'EMPLOYEE_INFORMATION',  'COL_1',	  false, 	            9,	          'POSITION');
delete  from "anv".customformsection where form_id = 'ASTERISK_EMPLOYEE_FORM';
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('ASTERISK_EMPLOYEE_FORM', 'EMPLOYEE_INFORMATION', true, false, 0);
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('ASTERISK_EMPLOYEE_FORM', 'PROVIDER_INFORMATION', true, false, 0);