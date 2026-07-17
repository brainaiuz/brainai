delete  from "anv".form_property where form_id ='DEPARTMENT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('DEPARTMENT_FORM',
        '[
  {
    "code": "DEPARTMENT_NAME",
    "title": "Department Name",
    "aliasName": "DEPARTMENT_NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT_DESCRIPTION",
    "title": "Description",
    "aliasName": "DEPARTMENT_DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT_START_DATE",
    "title": "Start date",
    "aliasName": "DEPARTMENT_START_DATE",
    "changed": false,
    "required": false,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT_CREATED_BY",
    "title": "Created by",
    "aliasName": "DEPARTMENT_CREATED_BY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT_EMAIL",
    "title": "E-mail",
    "aliasName": "DEPARTMENT_EMAIL",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT_PARENT",
    "title": "Reports To",
    "aliasName": "DEPARTMENT_PARENT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT_LEADER",
    "title": "Department Leader",
    "aliasName": "DEPARTMENT_LEADER",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT_LEADER2",
    "title": "Department Leader 2",
    "aliasName": "DEPARTMENT_LEADER2",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT_LEADER3",
    "title": "Department Leader 3",
    "aliasName": "DEPARTMENT_LEADER3",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT_LEADER4",
    "title": "Department Leader 4",
    "aliasName": "DEPARTMENT_LEADER4",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT_LEADER5",
    "title": "Department Leader 5",
    "aliasName": "DEPARTMENT_LEADER5",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
]');