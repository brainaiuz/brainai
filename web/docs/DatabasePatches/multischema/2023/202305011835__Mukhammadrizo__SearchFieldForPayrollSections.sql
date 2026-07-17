delete from "anv".modelfield where form_id = 'MULTI_CASH_ADVANCE_FORM' and field_id='search';

insert into "anv".modelfield
(form_id,fsection,section,fieldstyle,fullwidth,hide,columntype,mandatory,sectionstyle,forder,field_id) values
('MULTI_CASH_ADVANCE_FORM','INFORMATION','INFORMATION','field',false,false,'COL_1',true,'', 3,'search');


delete from "anv".modelfield where form_id = 'ADDITIONAL_PAYMENT_FORM' and field_id='search';

insert into "anv".modelfield
(form_id,fsection,section,fieldstyle,fullwidth,hide,columntype,mandatory,sectionstyle,forder,field_id) values
('ADDITIONAL_PAYMENT_FORM',	'BASIC_INFORMATION','BASIC_INFORMATION','field',false,false,'COL_1',false,'',3,'search');