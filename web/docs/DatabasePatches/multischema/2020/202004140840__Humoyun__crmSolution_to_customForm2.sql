
insert into "0".model (active, formid, title, viewname)  values
(true,  'SOLUTION_FORM',  'Solution Form', 'Solution');

insert into "0".customformsection
(form_id,            section) values
('SOLUTION_FORM',	  'CRM_SOLUTION_INFORMATION'),
('SOLUTION_FORM',	  'CRM_SOLUTION_DESCRIPTION'),
('SOLUTION_FORM',	  'ATTACHMENTS');


delete from "0".modelfield where form_id = 'SOLUTION_FORM' ;

insert into "0".modelfield
(form_id,           fsection,                        section,                       nolabelfor,             fieldstyle,      columntype,   fieldsetstyle,                           rowstyle,                mandatory,      sectionstyle,                                      widget,        forder,     field_id) values
('SOLUTION_FORM',	  'CRM_SOLUTION_INFORMATION',      'CRM_SOLUTION_INFORMATION',    '',                     'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',     false,        'slideDown-box  group expand hideCustomField',     'TextBox',      1,	       'TITLE'),
('SOLUTION_FORM',	  'CRM_SOLUTION_INFORMATION',      'CRM_SOLUTION_INFORMATION',    '',                     'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',     false,        'slideDown-box  group expand hideCustomField',     'DropDown',     2,         'ASSIGNEE'),
('SOLUTION_FORM',	  'CRM_SOLUTION_INFORMATION',      'CRM_SOLUTION_INFORMATION',    '',                     'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',     false,        'slideDown-box  group expand hideCustomField',     'DropDown',     3,         'STATUS'),
('SOLUTION_FORM',	  'CRM_SOLUTION_DESCRIPTION',      'CRM_SOLUTION_DESCRIPTION',    '',                     'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',     false,        'slideDown-box  group expand hideCustomField',     'TextArea',     4,         'CRM_SOLUTION_QUESTION'),
('SOLUTION_FORM',	  'CRM_SOLUTION_DESCRIPTION',      'CRM_SOLUTION_DESCRIPTION',    '',                     'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',     false,        'slideDown-box  group expand hideCustomField',     'TextArea',     5,         'CRM_SOLUTION_ANSWER'),
('SOLUTION_FORM',	  'ATTACHMENTS',                   'ATTACHMENTS',                 'viewForm,editForm',    'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',     false,        'slideDown-box  group expand hideCustomField',     'UNKNOWN',      10 ,       'ATTACHMENTS');



insert into "anv".model (active, formid, title, viewname)  values
(true,  'SOLUTION_FORM',  'Solution Form', 'Solution');

insert into "anv".customformsection
(form_id,            section) values
('SOLUTION_FORM',	  'CRM_SOLUTION_INFORMATION'),
('SOLUTION_FORM',	  'CRM_SOLUTION_DESCRIPTION'),
('SOLUTION_FORM',	  'ATTACHMENTS');


delete from "anv".modelfield where form_id = 'SOLUTION_FORM' ;

insert into "anv".modelfield
(form_id,           fsection,                        section,                       nolabelfor,             fieldstyle,      columntype,   fieldsetstyle,                           rowstyle,                mandatory,      sectionstyle,                                      widget,        forder,     field_id) values
('SOLUTION_FORM',	  'CRM_SOLUTION_INFORMATION',      'CRM_SOLUTION_INFORMATION',    '',                     'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',     false,        'slideDown-box  group expand hideCustomField',     'TextBox',      1,	       'TITLE'),
('SOLUTION_FORM',	  'CRM_SOLUTION_INFORMATION',      'CRM_SOLUTION_INFORMATION',    '',                     'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',     false,        'slideDown-box  group expand hideCustomField',     'DropDown',     2,         'ASSIGNEE'),
('SOLUTION_FORM',	  'CRM_SOLUTION_INFORMATION',      'CRM_SOLUTION_INFORMATION',    '',                     'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',     false,        'slideDown-box  group expand hideCustomField',     'DropDown',     3,         'STATUS'),
('SOLUTION_FORM',	  'CRM_SOLUTION_DESCRIPTION',      'CRM_SOLUTION_DESCRIPTION',    '',                     'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',     false,        'slideDown-box  group expand hideCustomField',     'TextArea',     4,         'CRM_SOLUTION_QUESTION'),
('SOLUTION_FORM',	  'CRM_SOLUTION_DESCRIPTION',      'CRM_SOLUTION_DESCRIPTION',    '',                     'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',     false,        'slideDown-box  group expand hideCustomField',     'TextArea',     5,         'CRM_SOLUTION_ANSWER'),
('SOLUTION_FORM',	  'ATTACHMENTS',                   'ATTACHMENTS',                 'viewForm,editForm',    'field',         'COL_1',	     'slideDown-content group labelLine',     'row hideCustomField',     false,        'slideDown-box  group expand hideCustomField',     'UNKNOWN',      10 ,       'ATTACHMENTS');
