delete from "anv".model where formid = 'CERTIFICATE_OF_EMPLOYMENT_FORM';
insert into "anv".model (active, formid, title, viewname)  values
(true,  'CERTIFICATE_OF_EMPLOYMENT_FORM',  'Certificate View', 'Certificates');


delete from "anv".customformsection where form_id = 'CERTIFICATE_OF_EMPLOYMENT_FORM';
insert into "anv".customformsection
(form_id,            section,     sorder , expanded) values
('CERTIFICATE_OF_EMPLOYMENT_FORM',	  'GENERAL_INFORMATION',   1 , true),
('CERTIFICATE_OF_EMPLOYMENT_FORM',	  'CONTENT',   2, true),
('CERTIFICATE_OF_EMPLOYMENT_FORM',	  'ATTACHMENTS',   3, false);


delete from "anv".modelfield where form_id = 'CERTIFICATE_OF_EMPLOYMENT_FORM' ;

insert into "anv".modelfield
(form_id,             fsection,                     section,                    fieldstyle,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id) values
('CERTIFICATE_OF_EMPLOYMENT_FORM',	  'GENERAL_INFORMATION',      'GENERAL_INFORMATION',    'field',      false,   'COL_1',	         true,        '',             'LOOKUP',      1,	       'EMPLOYEE'),
('CERTIFICATE_OF_EMPLOYMENT_FORM',	  'GENERAL_INFORMATION',      'GENERAL_INFORMATION',    'field',      false,   'COL_2',	         false ,         '',             'LOOKUP',       2,   	   'CERTIFICATE_TYPE'),
('CERTIFICATE_OF_EMPLOYMENT_FORM',	  'GENERAL_INFORMATION',      'GENERAL_INFORMATION',    'field',      false,   'COL_3',	         false ,         '',             'TextBox',       3,	       'NUMBER'),
('CERTIFICATE_OF_EMPLOYMENT_FORM',	  'GENERAL_INFORMATION',      'GENERAL_INFORMATION',    'field',      false,   'COL_1',	         false ,         '',             'LOOKUP',       4,	       'APPROVER'),
('CERTIFICATE_OF_EMPLOYMENT_FORM',	  'CONTENT',      'CONTENT',    'field',      false,   'COL_1',	         false,         '',             'TextBox',   1,	       'CONTENT'),
('CERTIFICATE_OF_EMPLOYMENT_FORM',	  'ATTACHMENTS',      'ATTACHMENTS',    'field',      false,   'COL_1',	         false,         '',             'DynamicTable',   6,	       'DOCUMENT_CELL_TREE');