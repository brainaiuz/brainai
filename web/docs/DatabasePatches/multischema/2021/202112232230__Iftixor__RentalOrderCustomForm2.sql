
 delete from "anv".model where formid = 'RENTAL_ORDER_FORM';
 insert into "anv".model (active, formid, title, viewname)  values
 (true,  'RENTAL_ORDER_FORM',  'Rental Order', 'rentalOrdersView');


 delete from "anv".customformsection where form_id = 'RENTAL_ORDER_FORM';
 insert into "anv".customformsection
 (form_id,section,sorder,expanded) values
 ('RENTAL_ORDER_FORM',	  'INFORMATION',   0, true ),
 ('RENTAL_ORDER_FORM',	  'ITEMS',   1, true ),
 ('RENTAL_ORDER_FORM',	  'ADDITIONAL_INFORMATION',   2, false );


delete from "anv".modelfield where form_id = 'RENTAL_ORDER_FORM' ;
insert into "anv".modelfield
(form_id,fsection,section,fieldstyle,fullwidth,hide,columntype,mandatory,sectionstyle,widget,forder,field_id) values
('RENTAL_ORDER_FORM',	  'INFORMATION','INFORMATION','field',    false,      false,   'COL_1',	         true,         '',             'LOOKUP',       0,	    'CUSTOMER'),
('RENTAL_ORDER_FORM',	  'INFORMATION','INFORMATION','field',    false,      false,   'COL_1',	         true,         '',             'LOOKUP',       1,	    'CLIENT_INVOICE_TERM'),
('RENTAL_ORDER_FORM',	  'INFORMATION',      'INFORMATION',    'field',    false,      false,   'COL_2',	         false ,        '',             'DatePicker',       1, 'DATE'),
('RENTAL_ORDER_FORM',	  'INFORMATION',      'INFORMATION',    'field',    false,      false,   'COL_2',	         false ,        '',             'DatePicker',       2, 'TAX_CALC_TYPE'),
('RENTAL_ORDER_FORM',	  'INFORMATION',      'INFORMATION',    'field',    false,      false,   'COL_3',	         false ,        '',             'TextBox',      0, 	       'NUMBER'),
('RENTAL_ORDER_FORM',	  'ITEMS',      'ITEMS',    'field',    true ,      false,   'COL_1',	        false,          '',             'UNKNOWN',        0,	       'ITEMS');



delete from "anv".container_item where propertyID is null ;
delete from "anv".container_item where moduleID = (select id from "anv".mymodule where code='RENTAL_ORDER_MODULE' limit 1) and propertyID = (select id from "anv".property where objectName='rentalOrders' limit 1);

delete from "anv".mymodule where code = 'RENTAL_ORDER_MODULE';
insert into "anv".mymodule (code,name,section,active) values ('RENTAL_ORDER_MODULE','Rental Order Management','accounting',false );

delete from "anv".property where objectName = 'rentalOrders';
insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('rentalOrders', 'Rental Order', 'Rental Order', 'Rental Orders', 'RO', 'accounting', false);

insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code='RENTAL_ORDER_MODULE' limit 1), (select id from "anv".property where objectName='rentalOrders' limit 1), (select id from "anv".container where code='accounting' limit 1), 21, 'accounting');