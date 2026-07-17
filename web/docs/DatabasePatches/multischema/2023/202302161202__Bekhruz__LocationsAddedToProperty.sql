update "anv".modelfield set hide = true where form_id = 'DEPARTMENT_FORM' and field_id = 'DEPARTMENT_LOCATION';
update "anv".modelfield set hide = true where form_id = 'LOCATION_FORM' and field_id = 'PARENT';

UPDATE "anv".form_property
SET    settingsjsonData = replace(settingsjsonData, '},
]', '}]')
WHERE form_id = 'DEPARTMENT_FORM';

UPDATE "anv".form_property
SET settingsjsonData = settingsjsonData::jsonb || '
  {
    "code": "DEPARTMENT_LOCATION",
    "title": "Location",
    "aliasName": "DEPARTMENT_LOCATION",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }'::jsonb
WHERE form_id = 'DEPARTMENT_FORM';

UPDATE "anv".form_property
SET    settingsjsonData = replace(settingsjsonData, '},
]', '}]')
WHERE form_id = 'LOCATION_FORM';

UPDATE "anv".form_property
SET settingsjsonData = settingsjsonData::jsonb || '
  {
    "code": "PARENT",
    "title": "Parent",
    "aliasName": "PARENT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }'::jsonb
WHERE form_id = 'LOCATION_FORM';