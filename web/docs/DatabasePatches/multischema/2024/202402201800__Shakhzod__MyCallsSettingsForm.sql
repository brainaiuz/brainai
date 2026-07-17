
insert into "0".model(active, formid, title, viewname) values(true, 'MYCALLS_SETTINGS_FORM', 'My Calls account form', 'My Calls Setting');
insert into "anv".model(active, formid, title, viewname) values(true, 'MYCALLS_SETTINGS_FORM', 'My Calls account form', 'My Calls Setting');

insert into "0".modelfield
(form_id,           fsection,                  columntype, mandatory,          forder,       field_id) values
                                                                                                           ('MYCALLS_SETTINGS_FORM',	'MYCALLS_SETTINGS_FORM',  'COL_1',	  false, 	            0,	          'USER'),
                                                                                                           ('MYCALLS_SETTINGS_FORM',	'MYCALLS_SETTINGS_FORM',  'COL_1',	  false, 	            1,	          'USER_LOGIN'),
                                                                                                           ('MYCALLS_SETTINGS_FORM',	'MYCALLS_SETTINGS_FORM',  'COL_1',	  false, 	            3,	          'SECRET_KEY');
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('MYCALLS_SETTINGS_FORM', 'BASIC_INFORMATION', true, false, 0);

insert into "anv".modelfield
(form_id,           fsection,                  columntype, mandatory,          forder,       field_id) values
                                                                                                           ('MYCALLS_SETTINGS_FORM',	'MYCALLS_SETTINGS_FORM',  'COL_1',	  false, 	            0,	          'USER'),
                                                                                                           ('MYCALLS_SETTINGS_FORM',	'MYCALLS_SETTINGS_FORM',  'COL_1',	  false, 	            1,	          'USER_LOGIN'),
                                                                                                           ('MYCALLS_SETTINGS_FORM',	'MYCALLS_SETTINGS_FORM',  'COL_1',	  false, 	            3,	          'SECRET_KEY');
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('MYCALLS_SETTINGS_FORM', 'BASIC_INFORMATION', true, false, 0);
