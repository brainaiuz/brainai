

delete from "anv".model where formid = 'PRODUCT_CATEGORY_FORM';
insert into "anv".model (active, formid, title, viewname)  values
(true,  'PRODUCT_CATEGORY_FORM',  'Product Category', 'ProductCategoryStoreFront');


delete from "anv".customformsection where form_id = 'PRODUCT_CATEGORY_FORM';
insert into "anv".customformsection
(form_id,section,sorder,expanded) values
('PRODUCT_CATEGORY_FORM',	  'PRODUCT_CATEGORY_TITLE',   0, true ),
('PRODUCT_CATEGORY_FORM',	  'ADDITIONAL_INFORMATION',   0, true );


delete from "anv".modelfield where form_id = 'PRODUCT_CATEGORY_FORM' ;
insert into "anv".modelfield
(form_id,             fsection,                     section,                    fieldstyle, fullwidth,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id) values
('PRODUCT_CATEGORY_FORM',	  'PRODUCT_CATEGORY_TITLE',      'PRODUCT_CATEGORY_TITLE',    'field',    false,      false,   'COL_1',	         true,         '',             'TextBox',       1,	       'NAME'),
('PRODUCT_CATEGORY_FORM',	  'PRODUCT_CATEGORY_TITLE',      'PRODUCT_CATEGORY_TITLE',    'field',    false,      false,   'COL_1',	         false,        '',             'DataListBox',   2,   	    'CATEGORY'),
('PRODUCT_CATEGORY_FORM',	  'PRODUCT_CATEGORY_TITLE',      'PRODUCT_CATEGORY_TITLE',    'field',    false,      false,   'COL_2',	         false,         '',             'TextBox',      1,	        'ORDER'),
('PRODUCT_CATEGORY_FORM',	  'PRODUCT_CATEGORY_TITLE',      'PRODUCT_CATEGORY_TITLE',    'field',    false,      false,   'COL_2',	         false,         '',             'UNKNOWN',     2,	        'IMAGE_UPLOAD');

