delete from "0".model where formid='CRM_WEB_FORM';
delete from "anv".model where formid='CRM_WEB_FORM';
insert into "0".model(active, formid, title, viewname) values(true, 'CRM_WEB_FORM', 'Web form', 'WebForm');
insert into "anv".model(active, formid, title, viewname) values(true, 'CRM_WEB_FORM', 'Web form', 'WebForm');

delete from "0".modelfield where form_id='CRM_WEB_FORM';
delete from "0".customformsection where form_id='CRM_WEB_FORM';
insert into "0".modelfield
(form_id,       section,                columntype, mandatory,         forder,       field_id) values
('CRM_WEB_FORM',	'CUSTOM_LAYOUT'	      , 'COL_1',	false, 	0,	'CUSTOM_LAYOUT'),
('CRM_WEB_FORM',	'CUSTOM_LAYOUT'	      , 'COL_1',	false, 	1,	'CUSTOM_LAYOUT_CSS'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_1',	false, 	2,	'BUTTON_TEXT'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_1',	false, 	1,	'DESCRIPTION'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_2',	true	,   0,	'TITLE'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_2',	false, 	1,	'CONFIRMATION_MESSAGE'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_1',	false, 	3,	'LAYOUT_PICKER'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_2',	false, 	2,	'REDIRECT'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_2',	false, 	3,	'CAPTCHA'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FIELD'	, 'COL_1',	false, 	0,	'TABLE_OF_FIELDS'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_1',	false, 	0,	'FORM_TYPE');
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('CRM_WEB_FORM', 'CUSTOM_LAYOUT', true, false, 0);
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('CRM_WEB_FORM', 'CONFIGURE_WEB_FORM', true, false, 1);
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('CRM_WEB_FORM', 'CONFIGURE_WEB_FIELD', true, false, 2);

delete from "anv".modelfield where form_id='CRM_WEB_FORM';
delete from "anv".customformsection where form_id='CRM_WEB_FORM';
insert into "anv".modelfield
(form_id,       section,                columntype, mandatory,         forder,       field_id) values
('CRM_WEB_FORM',	'CUSTOM_LAYOUT'	      , 'COL_1',	false, 	0,	'CUSTOM_LAYOUT'),
('CRM_WEB_FORM',	'CUSTOM_LAYOUT'	      , 'COL_1',	false, 	1,	'CUSTOM_LAYOUT_CSS'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_1',	false, 	2,	'BUTTON_TEXT'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_1',	false, 	1,	'DESCRIPTION'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_2',	true	,   0,	'TITLE'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_2',	false, 	1,	'CONFIRMATION_MESSAGE'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_1',	false, 	3,	'LAYOUT_PICKER'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_2',	false, 	2,	'REDIRECT'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_2',	false, 	3,	'CAPTCHA'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FIELD'	, 'COL_1',	false, 	0,	'TABLE_OF_FIELDS'),
('CRM_WEB_FORM',	'CONFIGURE_WEB_FORM'	, 'COL_1',	false, 	0,	'FORM_TYPE');
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('CRM_WEB_FORM', 'CUSTOM_LAYOUT', true, false, 0);
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('CRM_WEB_FORM', 'CONFIGURE_WEB_FORM', true, false, 1);
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('CRM_WEB_FORM', 'CONFIGURE_WEB_FIELD', true, false, 2);



delete from "0".model where formid='CRM_WEB_FORM_FOR_VIEW';
delete from "anv".model where formid='CRM_WEB_FORM_FOR_VIEW';
insert into "0".model(active, formid, title, viewname) values(true, 'CRM_WEB_FORM_FOR_VIEW', 'Web form View', 'WebForm');
insert into "anv".model(active, formid, title, viewname) values(true, 'CRM_WEB_FORM_FOR_VIEW', 'Web form View', 'WebForm');
--select form_id, section, columntype, mandatory. forder,field_id from "anv".modelfield where form_id='CRM_WEB_FORM_FOR_VIEW';
delete from "0".modelfield where form_id='CRM_WEB_FORM_FOR_VIEW';
delete from "0".customformsection where form_id='CRM_WEB_FORM_FOR_VIEW';
insert into "0".modelfield
(form_id,                 section,                columntype, mandatory,         forder,       field_id) values
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_1',	false,	0,  	'FORM_TYPE'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_1',	false,	1,  	'DESCRIPTION'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_1',	false,	2,  	'CAPTCHA'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_1',	false,	3,  	'CAPTCHA_LABEL'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_1',	false,	4,  	'CAPTCHA_DESCRIPTION'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_2',	true,	0	,     'TITLE'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_2',	false,	1,  	'CONFIRMATION_MESSAGE'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_2',	false,	2,  	'REDIRECT'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_2',	false,	3,  	'FORM_URL'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_2',	false,	4,  	'NOTIFICATION_EMAIL_ADDRESS'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FIELD',	'COL_1',	false,	0,  	'IFRAME_CODE'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FIELD',	'COL_1',	false,	1,  	'FORM_PREVIEW');
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('CRM_WEB_FORM_FOR_VIEW', 'CONFIGURE_WEB_FORM', true, false, 0);
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('CRM_WEB_FORM_FOR_VIEW', 'CONFIGURE_WEB_FIELD', true, false, 1);



delete from "anv".modelfield where form_id='CRM_WEB_FORM_FOR_VIEW';
delete from "anv".customformsection where form_id='CRM_WEB_FORM_FOR_VIEW';
delete from "anv".customformsection where form_id='CRM_WEB_FORM_FOR_VIEW';
insert into "anv".modelfield
(form_id,                 section,                columntype, mandatory,         forder,       field_id) values
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_1',	false,	0,  	'FORM_TYPE'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_1',	false,	1,  	'DESCRIPTION'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_1',	false,	2,  	'CAPTCHA'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_1',	false,	3,  	'CAPTCHA_LABEL'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_1',	false,	4,  	'CAPTCHA_DESCRIPTION'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_2',	true,	0	,     'TITLE'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_2',	false,	1,  	'CONFIRMATION_MESSAGE'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_2',	false,	2,  	'REDIRECT'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_2',	false,	3,  	'FORM_URL'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FORM',	  'COL_2',	false,	4,  	'NOTIFICATION_EMAIL_ADDRESS'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FIELD',	'COL_1',	false,	0,  	'IFRAME_CODE'),
('CRM_WEB_FORM_FOR_VIEW',	'CONFIGURE_WEB_FIELD',	'COL_1',	false,	1,  	'FORM_PREVIEW');
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('CRM_WEB_FORM_FOR_VIEW', 'CONFIGURE_WEB_FORM', true, false, 0);
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('CRM_WEB_FORM_FOR_VIEW', 'CONFIGURE_WEB_FIELD', true, false, 1);
