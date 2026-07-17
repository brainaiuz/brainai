update "anv".productcategory set active=true where deleted is not true;


delete from "anv".modelfield where form_id = 'PRODUCT_CATEGORY_FORM' and field_id='ACTIVE';
insert into "anv".modelfield
(form_id,             fsection,                     section,                    fieldstyle, fullwidth,   hide,    columntype,       mandatory,    sectionstyle,   widget,         forder,     field_id) values
('PRODUCT_CATEGORY_FORM',	  'PRODUCT_CATEGORY_TITLE',      'PRODUCT_CATEGORY_TITLE',    'field',    false,      false,   'COL_2',	         false,         '',             'UNKNOWN',     4,	        'ACTIVE');


select * from "65159".modelfield where form_id = 'PRODUCT_CATEGORY_FORM';
