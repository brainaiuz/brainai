delete  from "anv".form_property where form_id ='ACTIVITY_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('ACTIVITY_FORM',
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
    "code": "CUSTOM_HTML_TEMPLATE",
    "title": "Template",
    "aliasName": "CUSTOM_HTML_TEMPLATE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');
