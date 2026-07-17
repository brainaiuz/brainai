update modelfield set sorder=sorder+1 where form_ID='PROJECT_FORM' and sorder>(select sorder from modelfield where form_ID='PROJECT_FORM' and field_ID='EMPLOYEE_ASSIGNMENT');
update "0".modelfield set sorder=sorder+1 where form_ID='PROJECT_FORM' and sorder>(select sorder from "0".modelfield where form_ID='PROJECT_FORM' and field_ID='EMPLOYEE_ASSIGNMENT');

insert into modelfield(form_ID,        field_ID,   mandatory,hide,   isCustomField, section,    defaultValue, widget,   systemmandatory, type,  usablebyworkflow,  nolabelfor, nowrapperfor, fullWidth,  split, sorder) values
                      ('PROJECT_FORM', 'BILLIBLE', false,    false,  false,         'DETAILS',  '',           'CheckBox',false,          'text', false,           '',         '',           false,      false, (select sorder+1 from modelfield where form_ID='PROJECT_FORM' and field_ID='EMPLOYEE_ASSIGNMENT'));

insert into "0".modelfield(form_ID,    field_ID,    mandatory,hide,   isCustomField, section,   defaultValue, widget,   systemmandatory, type,  usablebyworkflow,   nolabelfor, nowrapperfor, fullWidth,  split, sorder) values
                      ('PROJECT_FORM', 'BILLIBLE',  false,    false,  false,         'DETAILS', '',           'CheckBox',false,          'text', false,              '',         '',           false,      false, (select sorder+1 from "0".modelfield where form_ID='PROJECT_FORM' and field_ID='EMPLOYEE_ASSIGNMENT'));

update modelfield set sorder=sorder+1 where form_ID='PROJECT_SUMMARY_FORM' and sorder>(select sorder from modelfield where form_ID='PROJECT_SUMMARY_FORM' and field_ID='COMPLETED');
update "0".modelfield set sorder=sorder+1 where form_ID='PROJECT_SUMMARY_FORM' and sorder>(select sorder from "0".modelfield where form_ID='PROJECT_SUMMARY_FORM' and field_ID='COMPLETED');

insert into modelfield(form_ID,        field_ID,   mandatory,hide,   isCustomField, section,    defaultValue, widget,   systemmandatory, type,  usablebyworkflow,  nolabelfor, nowrapperfor, fullWidth,  split, sorder) values
                      ('PROJECT_SUMMARY_FORM', 'BILLIBLE', false,    false,  false,         'DETAILS',  '',           'CheckBox',false,          'text', false,           '',         '',           false,      false, (select sorder+1 from modelfield where form_ID='PROJECT_SUMMARY_FORM' and field_ID='COMPLETED'));


insert into "0".modelfield(form_ID,    field_ID,    mandatory,hide,   isCustomField, section,   defaultValue, widget,   systemmandatory, type,  usablebyworkflow,   nolabelfor, nowrapperfor, fullWidth,  split, sorder) values
                      ('PROJECT_SUMMARY_FORM', 'BILLIBLE',  false,    false,  false,         'DETAILS', '',           'CheckBox',false,          'text', false,              '',         '',           false,      false, (select sorder+1 from "0".modelfield where form_ID='PROJECT_SUMMARY_FORM' and field_ID='COMPLETED'));


UPDATE modelfield set sectionStyle  = 'slideDown-box  group expand hideCustomField' WHERE (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
UPDATE modelfield set fieldSetStyle = 'slideDown-content group labelLine' WHERE (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
UPDATE modelfield set fieldSetStyle = 'slideDown-content group nobrd' WHERE noLabelFor is not null AND noLabelFor != '' AND (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
UPDATE modelfield set halfSetStyle = 'halfSet-1 left' WHERE (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
UPDATE modelfield set halfSetStyle = '' WHERE noLabelFor is not null AND noLabelFor != '' AND (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
UPDATE modelfield set rowStyle = 'row hideCustomField' WHERE (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
UPDATE modelfield set fieldStyle = 'field' WHERE (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';

UPDATE "0".modelfield set sectionStyle  = 'slideDown-box  group expand hideCustomField' WHERE (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
UPDATE "0".modelfield set fieldSetStyle = 'slideDown-content group labelLine' WHERE (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
UPDATE "0".modelfield set fieldSetStyle = 'slideDown-content group nobrd' WHERE noLabelFor is not null AND noLabelFor != '' AND (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
UPDATE "0".modelfield set halfSetStyle = 'halfSet-1 left' WHERE (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
UPDATE "0".modelfield set halfSetStyle = '' WHERE noLabelFor is not null AND noLabelFor != '' AND (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
UPDATE "0".modelfield set rowStyle = 'row hideCustomField' WHERE (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
UPDATE "0".modelfield set fieldStyle = 'field' WHERE (form_id = 'PROJECT_FORM' or form_id = 'PROJECT_SUMMARY_FORM') and field_id = 'BILLIBLE';
