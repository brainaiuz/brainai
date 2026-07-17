insert into modelfield(form_ID, field_ID,sorder,mandatory,hide,systemmandatory,section,widget,noLabelFor,type)values
                     ('CASE_FORM','INTERNAL_STATUS',(select max(sorder) from modelfield where form_ID = 'CASE_FORM' and section = 'CASE_INFORMATION'),false,true,false,'CASE_INFORMATION','DropDown','','Text'),
                     ('CASE_FORM','INTERNAL_UPDATED_DATE',(select max(sorder) from modelfield where form_ID = 'CASE_FORM' and section = 'CASE_INFORMATION'),false,true,false,'CASE_INFORMATION','DatePicker','','Date');

update modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');
update modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');
update modelfield set fieldSetStyle = 'slideDown-content group nobrd' where noLabelFor is not null and noLabelFor != '' and form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');
update modelfield set halfSetStyle = 'halfSet-1' where form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');
update modelfield set halfSetStyle = '' where noLabelFor is not null and noLabelFor != '' and form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');
update modelfield set rowStyle = 'row hideCustomField' where form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');
update modelfield set fieldStyle = 'field' where form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');


insert into "0".modelfield(form_ID, field_ID,sorder,mandatory,hide,systemmandatory,section,widget,noLabelFor,type)values
                     ('CASE_FORM','INTERNAL_STATUS',(select max(sorder) from "0".modelfield where form_ID = 'CASE_FORM' and section = 'CASE_INFORMATION'),false,true,false,'CASE_INFORMATION','DropDown','','Text'),
                     ('CASE_FORM','INTERNAL_UPDATED_DATE',(select max(sorder) from "0".modelfield where form_ID = 'CASE_FORM' and section = 'CASE_INFORMATION'),false,true,false,'CASE_INFORMATION','DatePicker','','Date');

update "0".modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');
update "0".modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');
update "0".modelfield set fieldSetStyle = 'slideDown-content group nobrd' where noLabelFor is not null and noLabelFor != '' and form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');
update "0".modelfield set halfSetStyle = 'halfSet-1' where form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');
update "0".modelfield set halfSetStyle = '' where noLabelFor is not null and noLabelFor != '' and form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');
update "0".modelfield set rowStyle = 'row hideCustomField' where form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');
update "0".modelfield set fieldStyle = 'field' where form_id='CASE_FORM' and (field_id = 'INTERNAL_STATUS' or field_id = 'INTERNAL_UPDATED_DATE');


delete from "0".reference where code = '_CASE_INTERNAL_STATUS';
insert into "0".reference (code, name, isremovable, issystemreference) values ('_CASE_INTERNAL_STATUS', 'Case Internal Status', false, true);

delete from "anv".reference where code = '_CASE_INTERNAL_STATUS';
insert into "anv".reference (code, name, isremovable, issystemreference) values ('_CASE_INTERNAL_STATUS', 'Case Internal Status', false, true);