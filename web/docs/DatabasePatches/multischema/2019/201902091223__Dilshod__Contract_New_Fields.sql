
---For zero

delete from "0".modelfield where field_id='contractStartDate' and form_id='CONTRACT_FORM';
delete from "0".modelfield where field_id='contractEndDate' and form_id='CONTRACT_FORM';

insert into "0".modelfield
   (form_id,                          fsection,              columntype,            forder,       field_id ) values
  ('CONTRACT_FORM',	  'DETAILS',  'COL_3', 	            0,	          'contractStartDate' );


insert into "0".modelfield
   (form_id,                          fsection,              columntype,            forder,       field_id ) values
  ('CONTRACT_FORM',	  'DETAILS',  'COL_3', 	            1,	          'contractEndDate' );


---Reorder fields
UPDATE  "0".modelfield set forder=1 where field_id='contractStartDate' and  form_id='CONTRACT_FORM' and fsection='DETAILS';
UPDATE  "0".modelfield set forder=2 where field_id='contractEndDate' and  form_id='CONTRACT_FORM' and fsection='DETAILS';
UPDATE  "0".modelfield set forder=3 where field_id='DUE_DATE_REMINDER' and  form_id='CONTRACT_FORM' and fsection='DETAILS';
UPDATE  "0".modelfield set forder=4 where field_id='REGISTRATION_DATE' and  form_id='CONTRACT_FORM' and fsection='DETAILS';


---For all

delete from "anv".modelfield where field_id='contractStartDate' and form_id='CONTRACT_FORM';
delete from "anv".modelfield where field_id='contractEndDate' and form_id='CONTRACT_FORM';


insert into "anv".modelfield
   (form_id,                          fsection,              columntype,            forder,       field_id ) values
  ('CONTRACT_FORM',	  'DETAILS',  'COL_3', 	            0,	          'contractStartDate' );


insert into "anv".modelfield
   (form_id,                          fsection,              columntype,            forder,       field_id ) values
  ('CONTRACT_FORM',	  'DETAILS',  'COL_3', 	            1,	          'contractEndDate' );



---Reorder fields

UPDATE  "anv".modelfield set forder=1 where field_id='contractStartDate' and  form_id='CONTRACT_FORM' and fsection='DETAILS';
UPDATE  "anv".modelfield set forder=2 where field_id='contractEndDate' and  form_id='CONTRACT_FORM' and fsection='DETAILS';
UPDATE  "anv".modelfield set forder=3 where field_id='DUE_DATE_REMINDER' and  form_id='CONTRACT_FORM' and fsection='DETAILS';
UPDATE  "anv".modelfield set forder=4 where field_id='REGISTRATION_DATE' and  form_id='CONTRACT_FORM' and fsection='DETAILS';