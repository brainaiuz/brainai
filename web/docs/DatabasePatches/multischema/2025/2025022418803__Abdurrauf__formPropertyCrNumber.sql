UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
            "code": "CR_NUMBER",
            "title": "Cr Number",
            "aliasName": "CR_NUMBER",
            "changed": false,
            "required": false,
            "widget": "DropDown"
        }'::jsonb,
        true)
WHERE form_id = 'ACCOUNT_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'CR_NUMBER');

UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
            "code": "CR_NUMBER",
            "title": "Cr Number",
            "aliasName": "CR_NUMBER",
            "changed": false,
            "required": false,
            "widget": "DropDown"
        }'::jsonb,
        true)
WHERE form_id = 'CLIENT_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'CR_NUMBER');

UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
            "code": "CR_NUMBER",
            "title": "Cr Number",
            "aliasName": "CR_NUMBER",
            "changed": false,
            "required": false,
            "widget": "DropDown"
        }'::jsonb,
        true)
WHERE form_id = 'SUPPLIER_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'CR_NUMBER');
