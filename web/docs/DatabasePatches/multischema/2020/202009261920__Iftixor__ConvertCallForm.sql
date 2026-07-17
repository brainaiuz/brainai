delete  from "anv".form_property where form_id ='LOGACALL_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('LOGACALL_FORM',
        '[
  {
    "code": "RELATED_TO",
    "title": "Related to",
    "aliasName": "RELATED_TO",
    "changed": false,
    "required": false,
    "widget": "RELATED_WIDGET",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SUBJECT",
    "title": "Subject",
    "aliasName": "SUBJECT",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "WHEN",
    "title": "When",
    "aliasName": "WHEN",
    "changed": false,
    "required": true,
    "widget": "DateTime",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DESCRIPTION",
    "title": "Description",
    "aliasName": "DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SHARED_WITH",
    "title": "Share",
    "aliasName": "SHARED_WITH",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "GUESTS",
    "title": "Guests",
    "aliasName": "GUESTS",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "RECURRING_WIDGET",
    "title": "Recurrence",
    "aliasName": "RECURRING_WIDGET",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CLONE",
    "title": "Clone",
    "aliasName": "CLONE",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ENABLE_REMINDER",
    "title": "Reminders",
    "aliasName": "ENABLE_REMINDER",
    "changed": false,
    "required": false,
    "widget": "REMINDER_WIDGET",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CALL_TYPE",
    "title": "Call Type",
    "aliasName": "CALL_TYPE",
    "changed": false,
    "required": false,
    "widget": "CALL_TYPE_WIDGET",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CALL_DETAILS",
    "title": "Call Details",
    "aliasName": "CALL_DETAILS",
    "changed": false,
    "required": false,
    "widget": "CALL_DETAILS_WIDGET",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');