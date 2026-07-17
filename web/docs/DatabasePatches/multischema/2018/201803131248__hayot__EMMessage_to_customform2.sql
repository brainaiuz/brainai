delete from "0".model where formid='MESSAGE_FORM';
delete from "anv".model where formid='MESSAGE_FORM';
insert into "0".model(active, formid, title, viewname) values(true, 'MESSAGE_FORM', 'Mail list form', 'Mail');
insert into "anv".model(active, formid, title, viewname) values(true, 'MESSAGE_FORM', 'Mail list form', 'Mail');

delete from "0".modelfield where form_id='MESSAGE_FORM';
delete from "0".customformsection where form_id='MESSAGE_FORM';
insert into "0".modelfield
(form_id,           fsection,          columntype, mandatory,          forder,       field_id) values
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            0,	          'CRM_MESSAGE_FULLNAME'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  true, 	            1,	          'CRM_MESSAGE_FROM'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            2,	          'CRM_MESSAGE_REPLYTO'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  true, 	            3,	          'CRM_MESSAGE_SUBJECT'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            4,	          'CRM_MESSAGE_PREHEADER'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_2',	  false, 	            0,	          'CRM_MESSAGE_SOURCE'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_2',	  false, 	            1,	          'CRM_MESSAGE_DATETABLE'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_2',	  false, 	            2,	          'CRM_MESSAGE_TIMETABLE'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_2',	  true, 	            3,	          'CRM_MESSAGE_SUBSCRIPTION_LISTS'),

('MESSAGE_FORM',	'CRM_MESSAGE_CONTENT',  'COL_1',	  false, 	            0,	          'CRM_MESSAGE_CATEGORY'),
('MESSAGE_FORM',	'CRM_MESSAGE_CONTENT',  'COL_1',	  false, 	            1,	          'CRM_MESSAGE_FORMAT'),
('MESSAGE_FORM',	'CRM_MESSAGE_CONTENT',  'COL_1',	  true, 	            2,	          'CRM_MESSAGE_FIELD'),
('MESSAGE_FORM',	'CRM_MESSAGE_CONTENT',  'COL_1',	  false, 	            3,            'CRM_MESSAGE_SENT'),
('MESSAGE_FORM',	'CRM_MESSAGE_CONTENT',  'COL_1',	  false, 	            4,	          'CRM_MESSAGE_ANTI_SPAN'),

('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_1',	  false, 	            0,	          'CRM_MESSAGE_ENTITIES_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_1',	  false, 	            1,	          'CRM_MESSAGE_SENT_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_1',	  true, 	            2,	          'CRM_MESSAGE_UNSUBSCRIBES_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_1',	  false, 	            3,            'CRM_MESSAGE_DELIVERY_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_1',	  false, 	            4,	          'CRM_MESSAGE_DELIVERY_RATE'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_1',	  false, 	            5,	          'CRM_MESSAGE_BOUNCED_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_2',	  false, 	            6,	          'CRM_MESSAGE_BOUNCED_RATE'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_2',	  false, 	            7,	          'CRM_MESSAGE_VIEW_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_2',	  false, 	            8,	          'CRM_MESSAGE_VIEW_RATE'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_2',	  false, 	            9,	          'CRM_MESSAGE_CLICK_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_2',	  false, 	           10,	          'CRM_MESSAGE_CLICK_RATE'),

('MESSAGE_FORM',	'ATTACHMENTS',          'COL_1',	  false, 	            0,	          'ATTACHMENTS');
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('MESSAGE_FORM', 'CRM_MESSAGE_DETAILS', true, false, 0);
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('MESSAGE_FORM', 'CRM_MESSAGE_CONTENT', true, false, 1);
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('MESSAGE_FORM', 'CRM_MESSAGE_STATISTICS', true, false, 2);
INSERT INTO "0".customformsection (form_id, section, active, custom, sorder) VALUES ('MESSAGE_FORM', 'ATTACHMENTS', true, false, 3);

delete from "anv".modelfield where form_id='MESSAGE_FORM';
delete from "anv".customformsection where form_id='MESSAGE_FORM';
insert into "anv".modelfield
(form_id,           fsection,          columntype, mandatory,          forder,       field_id) values
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            0,	          'CRM_MESSAGE_FULLNAME'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  true, 	            1,	          'CRM_MESSAGE_FROM'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            2,	          'CRM_MESSAGE_REPLYTO'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  true, 	            3,	          'CRM_MESSAGE_SUBJECT'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_1',	  false, 	            4,	          'CRM_MESSAGE_PREHEADER'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_2',	  false, 	            0,	          'CRM_MESSAGE_SOURCE'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_2',	  false, 	            1,	          'CRM_MESSAGE_DATETABLE'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_2',	  false, 	            2,	          'CRM_MESSAGE_TIMETABLE'),
('MESSAGE_FORM',	'CRM_MESSAGE_DETAILS',  'COL_2',	  true, 	            3,	          'CRM_MESSAGE_SUBSCRIPTION_LISTS'),

('MESSAGE_FORM',	'CRM_MESSAGE_CONTENT',  'COL_1',	  false, 	            0,	          'CRM_MESSAGE_CATEGORY'),
('MESSAGE_FORM',	'CRM_MESSAGE_CONTENT',  'COL_1',	  false, 	            1,	          'CRM_MESSAGE_FORMAT'),
('MESSAGE_FORM',	'CRM_MESSAGE_CONTENT',  'COL_1',	  true, 	            2,	          'CRM_MESSAGE_FIELD'),
('MESSAGE_FORM',	'CRM_MESSAGE_CONTENT',  'COL_1',	  false, 	            3,            'CRM_MESSAGE_SENT'),
('MESSAGE_FORM',	'CRM_MESSAGE_CONTENT',  'COL_1',	  false, 	            4,	          'CRM_MESSAGE_ANTI_SPAN'),

('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_1',	  false, 	            0,	          'CRM_MESSAGE_ENTITIES_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_1',	  false, 	            1,	          'CRM_MESSAGE_SENT_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_1',	  true, 	            2,	          'CRM_MESSAGE_UNSUBSCRIBES_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_1',	  false, 	            3,            'CRM_MESSAGE_DELIVERY_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_1',	  false, 	            4,	          'CRM_MESSAGE_DELIVERY_RATE'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_1',	  false, 	            5,	          'CRM_MESSAGE_BOUNCED_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_2',	  false, 	            6,	          'CRM_MESSAGE_BOUNCED_RATE'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_2',	  false, 	            7,	          'CRM_MESSAGE_VIEW_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_2',	  false, 	            8,	          'CRM_MESSAGE_VIEW_RATE'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_2',	  false, 	            9,	          'CRM_MESSAGE_CLICK_COUNT'),
('MESSAGE_FORM',	'CRM_MESSAGE_STATISTICS',  'COL_2',	  false, 	           10,	          'CRM_MESSAGE_CLICK_RATE'),

('MESSAGE_FORM',	'ATTACHMENTS',          'COL_1',	  false, 	            0,	          'ATTACHMENTS');
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('MESSAGE_FORM', 'CRM_MESSAGE_DETAILS', true, false, 0);
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('MESSAGE_FORM', 'CRM_MESSAGE_CONTENT', true, false, 1);
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('MESSAGE_FORM', 'CRM_MESSAGE_STATISTICS', true, false, 2);
INSERT INTO "anv".customformsection (form_id, section, active, custom, sorder) VALUES ('MESSAGE_FORM', 'ATTACHMENTS', true, false, 3);

