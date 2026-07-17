insert into "anv".modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory)
values ('ACCOUNT_FORM', 'VAT_CATEGORIES', 'COL_2', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 6, false);

insert into "anv".modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory)
values ('SUPPLIER_FORM', 'VAT_CATEGORIES', 'COL_2', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 6, false);




UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "VAT_CATEGORIES",
          "title": "Vat Categories",
          "aliasName": "VAT_CATEGORIES",
          "changed": false,
          "required": false,
          "widget": "DropDown"
        }'::jsonb,
        true)
WHERE form_id = 'ACCOUNT_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'VAT_CATEGORIES');


UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "VAT_CATEGORIES",
          "title": "Vat Categories",
          "aliasName": "VAT_CATEGORIES",
          "changed": false,
          "required": false,
          "widget": "DropDown"
        }'::jsonb,
        true)
WHERE form_id = 'SUPPLIER_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'VAT_CATEGORIES');





insert into "0".modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory)
values ('ACCOUNT_FORM', 'VAT_CATEGORIES', 'COL_2', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 6, false);

insert into "0".modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory)
values ('SUPPLIER_FORM', 'VAT_CATEGORIES', 'COL_2', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 6, false);




UPDATE "0".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "VAT_CATEGORIES",
          "title": "Vat Categories",
          "aliasName": "VAT_CATEGORIES",
          "changed": false,
          "required": false,
          "widget": "DropDown"
        }'::jsonb,
        true)
WHERE form_id = 'ACCOUNT_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'VAT_CATEGORIES');


UPDATE "0".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "VAT_CATEGORIES",
          "title": "Vat Categories",
          "aliasName": "VAT_CATEGORIES",
          "changed": false,
          "required": false,
          "widget": "DropDown"
        }'::jsonb,
        true)
WHERE form_id = 'SUPPLIER_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'VAT_CATEGORIES');

