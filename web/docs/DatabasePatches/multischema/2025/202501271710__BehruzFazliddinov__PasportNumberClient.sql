insert into "anv".modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory)
values ('ACCOUNT_FORM', 'PASSPORT_NUM', 'COL_2', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 5, false);

insert into "anv".modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory)
values ('CLIENT_FORM', 'PASSPORT_NUM', 'COL_2', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 5, false);

insert into "anv".modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory)
values ('SUPPLIER_FORM', 'PASSPORT_NUM', 'COL_2', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 5, false);


UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "PASSPORT_NUM",
          "title": "Passport Number",
          "aliasName": "PASSPORT_NUM",
          "changed": false,
          "required": false,
          "widget": "TextBox"
        }'::jsonb,
        true)
WHERE form_id = 'ACCOUNT_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'PASSPORT_NUM');

UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "PASSPORT_NUM",
          "title": "Passport Number",
          "aliasName": "PASSPORT_NUM",
          "changed": false,
          "required": false,
          "widget": "TextBox"
        }'::jsonb,
        true)
WHERE form_id = 'CLIENT_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'PASSPORT_NUM');

UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "PASSPORT_NUM",
          "title": "Passport Number",
          "aliasName": "PASSPORT_NUM",
          "changed": false,
          "required": false,
          "widget": "TextBox"
        }'::jsonb,
        true)
WHERE form_id = 'SUPPLIER_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'PASSPORT_NUM');




insert into "0".modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory)
values ('ACCOUNT_FORM', 'PASSPORT_NUM', 'COL_2', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 5, false);

insert into "0".modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory)
values ('CLIENT_FORM', 'PASSPORT_NUM', 'COL_2', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 5, false);

insert into "0".modelfield(form_ID, field_ID, columntype, fsection, forder, mandatory)
values ('SUPPLIER_FORM', 'PASSPORT_NUM', 'COL_2', 'CRM_ACCOUNT_FINANCIAL_INFORMATION', 5, false);


UPDATE "0".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "PASSPORT_NUM",
          "title": "Passport Number",
          "aliasName": "PASSPORT_NUM",
          "changed": false,
          "required": false,
          "widget": "TextBox"
        }'::jsonb,
        true)
WHERE form_id = 'ACCOUNT_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'PASSPORT_NUM');

UPDATE "0".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "PASSPORT_NUM",
          "title": "Passport Number",
          "aliasName": "PASSPORT_NUM",
          "changed": false,
          "required": false,
          "widget": "TextBox"
        }'::jsonb,
        true)
WHERE form_id = 'CLIENT_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'PASSPORT_NUM');

UPDATE "0".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "PASSPORT_NUM",
          "title": "Passport Number",
          "aliasName": "PASSPORT_NUM",
          "changed": false,
          "required": false,
          "widget": "TextBox"
        }'::jsonb,
        true)
WHERE form_id = 'SUPPLIER_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'PASSPORT_NUM');

