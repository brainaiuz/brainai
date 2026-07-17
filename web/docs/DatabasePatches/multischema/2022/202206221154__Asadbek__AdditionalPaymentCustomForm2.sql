
 delete from "anv".model where formid = 'ADDITIONAL_PAYMENT_FORM';
 insert into "anv".model (active, formid, title, viewname)  values
 (true,  'ADDITIONAL_PAYMENT_FORM',  'Additional payment', 'AdditionalPayment');


 delete from "anv".customformsection where form_id = 'ADDITIONAL_PAYMENT_FORM';
 insert into "anv".customformsection
 (form_id,section,sorder,expanded) values
 ('ADDITIONAL_PAYMENT_FORM',	  'BASIC_INFORMATION',   0, true ),
 ('ADDITIONAL_PAYMENT_FORM',	  'EMPLOYEES',   1, true ),
 ('ADDITIONAL_PAYMENT_FORM',	  'ADDITIONAL_INFORMATION',   2, false );


delete from "anv".modelfield where form_id = 'ADDITIONAL_PAYMENT_FORM' ;
insert into "anv".modelfield
(form_id,fsection,section,fieldstyle,fullwidth,hide,columntype,mandatory,sectionstyle,widget,forder,field_id) values
('ADDITIONAL_PAYMENT_FORM',	'BASIC_INFORMATION','BASIC_INFORMATION','field',false,false,'COL_1',false,'','LOOKUP',0,'EMPLOYEE'),
('ADDITIONAL_PAYMENT_FORM',	'BASIC_INFORMATION','BASIC_INFORMATION','field',false,false,'COL_1',false,'','LOOKUP',1,'CATEGORY'),
('ADDITIONAL_PAYMENT_FORM',	'BASIC_INFORMATION','BASIC_INFORMATION','field',false,false,'COL_1',false,'','LOOKUP',2,'PAYMENT_DATE'),

('ADDITIONAL_PAYMENT_FORM',	'BASIC_INFORMATION','BASIC_INFORMATION','field',false,false,'COL_2',false,'','LOOKUP',0,'PERIOD'),
('ADDITIONAL_PAYMENT_FORM',	'BASIC_INFORMATION','BASIC_INFORMATION','field',false,false,'COL_2',false,'','LOOKUP',1,'PAYMENT_TYPE'),
('ADDITIONAL_PAYMENT_FORM',	'BASIC_INFORMATION','BASIC_INFORMATION','field',false,false,'COL_2',false,'','LOOKUP',2,'APPROVERS'),

('ADDITIONAL_PAYMENT_FORM',	'BASIC_INFORMATION','BASIC_INFORMATION','field',false,false,'COL_3',false,'','LOOKUP',0,'REFERENCE'),
('ADDITIONAL_PAYMENT_FORM',	'BASIC_INFORMATION','BASIC_INFORMATION','field',false,false,'COL_3',false,'','LOOKUP',1,'AMOUNT'),
('ADDITIONAL_PAYMENT_FORM',	'BASIC_INFORMATION','BASIC_INFORMATION','field',false,false,'COL_3',false,'','LOOKUP',2,'SHOW_PAYSLIP'),

('ADDITIONAL_PAYMENT_FORM',	  'EMPLOYEES',      'EMPLOYEES',    'field',true,false,'COL_1',false,'','UNKNOWN',0,'ITEMS');