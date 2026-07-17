insert into modelfield(form_ID,                             field_ID,               sorder,     mandatory,   fullWidth,    isCustomField,     section,                  defaultValue,   widget,        systemmandatory,  split,   noLabelFor) values
                      ('IMPORT_ADDITIONAL_PAYMENT_FORM',    'PAYMENT_DATE',         12,         false,        false,        false,            'MAIN_PANEL',             '',             'DropDown',    false,            false,   '');

UPDATE modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_ID ='IMPORT_ADDITIONAL_PAYMENT_FORM' and field_id = 'PAYMENT_DATE';
UPDATE modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_ID ='IMPORT_ADDITIONAL_PAYMENT_FORM' and field_id = 'PAYMENT_DATE';
UPDATE modelfield set fieldSetStyle = 'slideDown-content group nobrd' WHERE noLabelFor is not null AND noLabelFor != '' and form_ID ='IMPORT_ADDITIONAL_PAYMENT_FORM' and field_id = 'PAYMENT_DATE';
UPDATE modelfield set halfSetStyle = 'halfSet-1 left' where form_ID ='IMPORT_ADDITIONAL_PAYMENT_FORM' and field_id = 'PAYMENT_DATE';
UPDATE modelfield set halfSetStyle = '' WHERE noLabelFor is not null AND noLabelFor != '' and form_ID ='IMPORT_ADDITIONAL_PAYMENT_FORM' and field_id = 'PAYMENT_DATE';
UPDATE modelfield set rowStyle = 'row hideCustomField' where form_ID ='IMPORT_ADDITIONAL_PAYMENT_FORM' and field_id = 'PAYMENT_DATE';
UPDATE modelfield set fieldStyle = 'field' where form_ID ='IMPORT_ADDITIONAL_PAYMENT_FORM' and field_id = 'PAYMENT_DATE';
