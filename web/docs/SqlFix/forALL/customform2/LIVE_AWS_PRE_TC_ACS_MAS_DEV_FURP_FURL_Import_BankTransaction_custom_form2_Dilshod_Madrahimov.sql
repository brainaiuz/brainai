delete from "model"  where formid = 'IMPORT_BANK_TRANSACTION_FORM';
delete from "modelfield"  where form_id = 'IMPORT_BANK_TRANSACTION_FORM';

delete from "anv"."model"  where formid = 'IMPORT_BANK_TRANSACTION_FORM';
delete from "anv"."modelfield"  where form_id = 'IMPORT_BANK_TRANSACTION_FORM';


insert into model(formID, active, title) values('IMPORT_BANK_TRANSACTION_FORM',true, 'Import Bank Transfer');
insert into modelfield(form_ID,                             field_ID,               sorder,     mandatory,   fullWidth,    isCustomField,     section,                  defaultValue,   widget,        systemmandatory,  split,   noLabelFor) values
                      ('IMPORT_BANK_TRANSACTION_FORM',    'HAS_CSV_HEADER',          1,          false,       false,        false,             'REQUIRED_INFORMATIONS',      '',             'CheckBox',    false,            false,   ''),
                      ('IMPORT_BANK_TRANSACTION_FORM',    'CASH_ACCOUNT_NAME',       2,          true,        false,        false,             'REQUIRED_INFORMATIONS',      '',             'DropDown',    true,             false,   ''),
                      ('IMPORT_BANK_TRANSACTION_FORM',    'BANK_ACCOUNT_NAME',       3,          true,        false,        false,             'REQUIRED_INFORMATIONS',      '',             'DropDown',    true,             false,   ''),
                      ('IMPORT_BANK_TRANSACTION_FORM',    'DATE',                    4,          true,        false,        false,             'REQUIRED_INFORMATIONS',      '',             'DropDown',    true,             false,    ''),
                      ('IMPORT_BANK_TRANSACTION_FORM',    'AMOUNT',                  5,          true,        false,        false,             'REQUIRED_INFORMATIONS',      '',             'DropDown',    true,             false,   ''),
                      ('IMPORT_BANK_TRANSACTION_FORM',    'ACCOUNT_CODE',            6,          true,       false,        false,              'REQUIRED_INFORMATIONS',      '',             'DropDown',    true,             false,   '');



insert into modelfield(form_ID,                             field_ID,               sorder,     mandatory,   fullWidth,    isCustomField,     section,                  defaultValue,   widget,        systemmandatory,  split,   noLabelFor) values
                      ('IMPORT_BANK_TRANSACTION_FORM',    'NARRATION',               7,          false,       false,        false,             'OPTIONAL_INFORMATIONS',      '',             'DropDown',    false,            false,   ''),
                      ('IMPORT_BANK_TRANSACTION_FORM',    'REFERENCE',               8,          false,       false,        false,             'OPTIONAL_INFORMATIONS',      '',             'DropDown',    false,            false,   ''),
                      ('IMPORT_BANK_TRANSACTION_FORM',    'DESCRIPTION',             9,          false,       false,        false,             'OPTIONAL_INFORMATIONS',      '',             'DropDown',    false,            false,   ''),
                      ('IMPORT_BANK_TRANSACTION_FORM',    'NAME',                    10,         false,       false,        false,             'OPTIONAL_INFORMATIONS',      '',             'DropDown',    false,            false,   '');



UPDATE modelfield set sectionStyle = 'slideDown-box  group expand hideCustomField' where form_ID ='IMPORT_BANK_TRANSACTION_FORM';
UPDATE modelfield set fieldSetStyle = 'slideDown-content group labelLine' where form_ID ='IMPORT_BANK_TRANSACTION_FORM';
UPDATE modelfield set fieldSetStyle = 'slideDown-content group nobrd' WHERE noLabelFor is not null AND noLabelFor != '' and form_ID ='IMPORT_BANK_TRANSACTION_FORM';
UPDATE modelfield set halfSetStyle = 'halfSet-1 left' where form_ID ='IMPORT_BANK_TRANSACTION_FORM';
UPDATE modelfield set halfSetStyle = '' WHERE noLabelFor is not null AND noLabelFor != '' and form_ID ='IMPORT_BANK_TRANSACTION_FORM';
UPDATE modelfield set rowStyle = 'row hideCustomField' where form_ID ='IMPORT_BANK_TRANSACTION_FORM';
UPDATE modelfield set fieldStyle = 'field' where form_ID ='IMPORT_BANK_TRANSACTION_FORM';
