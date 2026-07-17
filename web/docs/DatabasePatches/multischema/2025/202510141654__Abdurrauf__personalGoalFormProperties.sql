UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "GOAL_NUMBER",
          "title": "Number",
          "aliasName": "NUMBER",
          "changed": false,
          "required": true,
          "widget": "Unknown",
          "selectedId": null,
          "defaultValue": "",
          "disabled": false
        }'::jsonb,
        true)
WHERE form_id = 'PERSONAL_GOAL_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'GOAL_NUMBER');

UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "PROJECT_GOAL_LOOKUP",
          "title": "Project goal",
          "aliasName": "PROJECT_GOAL",
          "changed": false,
          "required": false,
          "widget": "LOOKUP",
          "selectedId": null,
          "defaultValue": "",
          "disabled": false
        }'::jsonb,
        true)
WHERE form_id = 'PERSONAL_GOAL_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'PROJECT_GOAL_LOOKUP');

UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "GOAL_WEIGHT",
          "title": "Weight",
          "aliasName": "GOAL_WEIGHT",
          "changed": false,
          "required": false,
          "widget": "Unknown",
          "selectedId": null,
          "defaultValue": "",
          "disabled": false
        }'::jsonb,
        true)
WHERE form_id = 'PERSONAL_GOAL_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'GOAL_WEIGHT');

UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "ATTACHMENTS",
          "title": "Attachments",
          "aliasName": "ATTACHMENTS",
          "changed": false,
          "required": false,
          "widget": "Unknown",
          "selectedId": null,
          "defaultValue": "",
          "disabled": false
        }'::jsonb,
        true)
WHERE form_id = 'PERSONAL_GOAL_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'ATTACHMENTS');

UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "CRM_NOTE",
          "title": "Notes",
          "aliasName": "NOTE",
          "changed": false,
          "required": false,
          "widget": "Unknown",
          "selectedId": null,
          "defaultValue": "",
          "disabled": false
        }'::jsonb,
        true)
WHERE form_id = 'PERSONAL_GOAL_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'CRM_NOTE');

UPDATE "anv".form_property
SET settingsjsondata = jsonb_insert(
        settingsjsondata::jsonb,
        '{-1}',
        '{
          "code": "LINKS",
          "title": "Links",
          "aliasName": "LINKS",
          "changed": false,
          "required": false,
          "widget": "Unknown",
          "selectedId": null,
          "defaultValue": "",
          "disabled": false
        }'::jsonb,
        true)
WHERE form_id = 'PERSONAL_GOAL_FORM'
  AND NOT EXISTS (SELECT 1
                  FROM jsonb_array_elements(settingsjsondata::jsonb) AS element
                  WHERE element ->> 'code' = 'LINKS');