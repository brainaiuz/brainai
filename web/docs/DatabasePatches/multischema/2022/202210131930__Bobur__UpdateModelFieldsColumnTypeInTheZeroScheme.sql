
UPDATE  "65892".customformsection
SET expanded = true
WHERE form_id = 'IMPORT_BANK_TRANSACTION_FORM';

UPDATE  "65892".modelfield
SET columntype = 'COL_1'
WHERE form_id = 'IMPORT_BANK_TRANSACTION_FORM'and field_id in ('HAS_CSV_HEADER','DATE','NAME','REFERENCE','PROJECT_CODE');

UPDATE  "65892".modelfield
SET columntype = 'COL_2'
WHERE form_id = 'IMPORT_BANK_TRANSACTION_FORM'and field_id in ('AMOUNT','ACCOUNT_CODE','DESCRIPTION','NARRATION');

UPDATE  "65892".modelfield
SET columntype = 'COL_3'
WHERE form_id = 'IMPORT_BANK_TRANSACTION_FORM'and field_id in ('BANK_ACCOUNT_NAME','TAX_CALC_TYPE','TAX_VALUE');


