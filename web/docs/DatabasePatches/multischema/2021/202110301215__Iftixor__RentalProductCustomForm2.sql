
delete from "anv".model where formid = 'RENTAL_PRODUCT_FORM';
insert into "anv".model (active, formid, title, viewname)  values
(true,  'RENTAL_PRODUCT_FORM',  'Rental Product', 'RentalProductsView');


delete from "anv".customformsection where form_id = 'RENTAL_PRODUCT_FORM';
insert into "anv".customformsection
(form_id,section,sorder,expanded) values
('RENTAL_PRODUCT_FORM',	  'PRODUCT_INFORMATION',   0, true ),
('RENTAL_PRODUCT_FORM',	  'ITEMS',   1, true ),
('RENTAL_PRODUCT_FORM',	  'FINANCIAL_INFORMATION',   2, true ),
('RENTAL_PRODUCT_FORM',	  'ATTACHMENTS',   3, true ),
('RENTAL_PRODUCT_FORM',	  'ADDITIONAL_INFORMATION',   4, false );


delete from "anv".modelfield where form_id = 'RENTAL_PRODUCT_FORM' ;
insert into "anv".modelfield
(form_id,fsection,section,fieldstyle,fullwidth,hide,columntype,mandatory,sectionstyle,widget,forder,field_id) values
('RENTAL_PRODUCT_FORM',	  'PRODUCT_INFORMATION','PRODUCT_INFORMATION','field',    false,      false,   'COL_2',	         true,         '',             'LOOKUP',       0,	    'NAME'),
('RENTAL_PRODUCT_FORM',	  'PRODUCT_INFORMATION',      'PRODUCT_INFORMATION',    'field',    false,      false,   'COL_3',	         false ,        '',             'LOOKUP',       1, 'SUPPLIERS'),
('RENTAL_PRODUCT_FORM',	  'PRODUCT_INFORMATION',      'PRODUCT_INFORMATION',    'field',    false,      false,   'COL_3',	         false,        '',             'TextBox',       2,   	    'ACTIVE'),
('RENTAL_PRODUCT_FORM',	  'PRODUCT_INFORMATION',      'PRODUCT_INFORMATION',    'field',    false,      false,   'COL_2',	         false,         '',             'DatePicker',   2,'BRAND'),
('RENTAL_PRODUCT_FORM',	  'PRODUCT_INFORMATION',      'PRODUCT_INFORMATION',    'field',    false,      false,   'COL_2',	         false ,         '',             'UNKNOWN',     1,'CATEGORY'),
('RENTAL_PRODUCT_FORM',	  'PRODUCT_INFORMATION',      'PRODUCT_INFORMATION',    'field',    false,      false,   'COL_3',	         false ,        '',             'TextBox',      0, 	       'NUMBER'),
('RENTAL_PRODUCT_FORM',	  'PRODUCT_INFORMATION',      'PRODUCT_INFORMATION',    'field',    false,      true,   'COL_3',	         false,         '',             'UNKNOWN',      1000,	    'BARCODE'),
('RENTAL_PRODUCT_FORM',	  'ITEMS',      'ITEMS',    'field',    true ,      false,   'COL_1',	        false,          '',             'UNKNOWN',        0,	       'ITEMS'),
('RENTAL_PRODUCT_FORM',	  'ITEMS',      'ITEMS',    'field',    false ,      false,   'COL_2',	        false,          '',             'UNKNOWN',        1,	       'OVERTIME_HOURS'),
('RENTAL_PRODUCT_FORM',	  'ITEMS',      'ITEMS',    'field',    false ,      false,   'COL_2',	        false,          '',             'UNKNOWN',        0,	       'OVERTIME_END_DATE'),
('RENTAL_PRODUCT_FORM',	  'ITEMS',      'ITEMS',    'field',    false ,      false,   'COL_3',	        false,          '',             'UNKNOWN',        1,	       'SECURITY_TIME'),
('RENTAL_PRODUCT_FORM',	  'ITEMS',      'ITEMS',    'field',    false ,      false,   'COL_3',	        false,          '',             'UNKNOWN',        2,	       'TYPE'),
('RENTAL_PRODUCT_FORM',	  'FINANCIAL_INFORMATION',      'FINANCIAL_INFORMATION',    'field',    false,      false,   'COL_1',	         false,         '',             'UNKNOWN',      0,'SALES_PRICE'),
('RENTAL_PRODUCT_FORM',	  'FINANCIAL_INFORMATION',      'FINANCIAL_INFORMATION',    'field',    false,      false,   'COL_1',	         false,         '',             'UNKNOWN',      1,'SALES_ACCOUNT'),
('RENTAL_PRODUCT_FORM',	  'FINANCIAL_INFORMATION',      'FINANCIAL_INFORMATION',    'field',    false,      false,   'COL_2',	         false,         '',             'UNKNOWN',     0,'PURCHASE_PRICE'),
('RENTAL_PRODUCT_FORM',	  'FINANCIAL_INFORMATION',      'FINANCIAL_INFORMATION',    'field',    false,      false,   'COL_2',	         false,         '',             'UNKNOWN',   1,'PURCHASE_ACCOUNT'),
('RENTAL_PRODUCT_FORM',	  'FINANCIAL_INFORMATION',      'FINANCIAL_INFORMATION',    'field',    false,      false,   'COL_3',	         false,         '',             'UNKNOWN',   1,'TAX'),
('RENTAL_PRODUCT_FORM',	  'PRODUCT_INFORMATION',      'ATTACHMENTS',    'field',    false,      false,   'COL_1',	         false,         '',             'UNKNOWN',   0,'IMAGE_UPLOAD'),
('RENTAL_PRODUCT_FORM',	  'ATTACHMENTS',      'ATTACHMENTS',    'field',    false,      false,   'COL_1',	         false,         '',             'UNKNOWN',   0,'ATTACHMENTS');



update company set selectFunctioncolumn =(select setval('"anv".mymodule_id_seq', (select max(id) from "anv".mymodule))) where id=(select id from company limit 1);


delete from "anv".container_item where propertyID is null ;
delete from "anv".container_item where moduleID = (select id from "anv".mymodule where code='PRODUCT_RENTAL_ITEMS' limit 1) and propertyID = (select id from "anv".property where objectName='rentalProducts' limit 1);

delete from "anv".mymodule where code = 'PRODUCT_RENTAL_ITEMS';
insert into "anv".mymodule (code,name,section,active) values ('PRODUCT_RENTAL_ITEMS','Rental Product management','accounting',false );

delete from "anv".property where objectName = 'rentalProducts';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('rentalProducts', 'Rental Item', 'Rental Item', 'Rental Items', 'RP', 'accounting', false);

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='PRODUCT_RENTAL_ITEMS' limit 1), (select id from "anv".property where objectName='rentalProducts' limit 1), (select id from "anv".container where code='accounting' limit 1), 20, 'accounting');