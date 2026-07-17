
insert into "0".model(active, formid, title, viewname) values(true, 'SIPUNI_SETTINGS_FORM', 'Sipuni account form', 'Sipuni Setting');
insert into "anv".model(active, formid, title, viewname) values(true, 'SIPUNI_SETTINGS_FORM', 'Sipuni account form', 'Sipuni Setting');

insert into "0".modelfield
(form_id,           fsection,                  columntype, mandatory,          forder,       field_id) values
                                                                                                           ('SIPUNI_SETTINGS_FORM',	'USER',  'COL_1',	  false, 	            0,	          'USER'),
                                                                                                           ('SIPUNI_SETTINGS_FORM',	'SIP_NUMBER',  'COL_1',	  false, 	            1,	          'SIP_NUMBER'),
                                                                                                           ('SIPUNI_SETTINGS_FORM',	'OPERATOR',  'COL_1',	  false, 	            2,	          'OPERATOR'),
                                                                                                           ('SIPUNI_SETTINGS_FORM',	'SECRET_KEY',  'COL_1',	  false, 	            3,	          'SECRET_KEY');
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('SIPUNI_SETTINGS_FORM', 'BASIC_INFORMATION', true, false, 0);

insert into "anv".modelfield
(form_id,           fsection,                  columntype, mandatory,          forder,       field_id) values
                                                                                                           ('SIPUNI_SETTINGS_FORM',	'USER',  'COL_1',	  false, 	            0,	          'USER'),
                                                                                                           ('SIPUNI_SETTINGS_FORM',	'SIP_NUMBER',  'COL_1',	  false, 	            1,	          'SIP_NUMBER'),
                                                                                                           ('SIPUNI_SETTINGS_FORM',	'OPERATOR',  'COL_1',	  false, 	            2,	          'OPERATOR'),
                                                                                                           ('SIPUNI_SETTINGS_FORM',	'SECRET_KEY',  'COL_1',	  false, 	            3,	          'SECRET_KEY');
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('SIPUNI_SETTINGS_FORM', 'BASIC_INFORMATION', true, false, 0);
