UPDATE "anv".form_property
SET settingsjsonData = settingsjsonData::jsonb || '[
  {
    "code": "BACKUP_EMPLOYEE",
    "title": "Backup Employee",
    "aliasName": "BACKUP_EMPLOYEE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }]'::jsonb
WHERE form_id = 'LEAVE_REQUEST_FORM';