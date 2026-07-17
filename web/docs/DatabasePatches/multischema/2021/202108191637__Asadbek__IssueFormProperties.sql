delete  from "anv".form_property where form_id ='ISSUE_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('ISSUE_FORM',
        '[
  {
    "code": "PROJECT_FIELD",
    "title": "Project",
    "aliasName": "PROJECT_FIELD",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
     "code": "NUMBER",
    "title": "Number",
    "aliasName": "NUMBER",
    "changed": false,
    "required": true,
    "widget": "Unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "NAME",
    "title": "Name",
    "aliasName": "NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
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
    "code": "VISIBILITY",
    "title": "Visibility",
    "aliasName": "VISIBILITY",
    "changed": false,
    "required": true,
    "widget": "Unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PERIOD",
    "title": "Period",
    "aliasName": "PERIOD",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PRIORITY",
    "title": "Priority",
    "aliasName": "PRIORITY",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "STATUS",
    "title": "Status",
    "aliasName": "STATUS",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "REPORTED_BY",
    "title": "Reported by",
    "aliasName": "REPORTED_BY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "RESOLVER",
    "title": "Resolver/Owner",
    "aliasName": "RESOLVER",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ENABLE_TIME_SHEET",
    "title": "&nbsp;Enable Timesheet",
    "aliasName": "ENABLE_TIME_SHEET",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "BILLABLE",
    "title": "Billable",
    "aliasName": "BILLABLE",
    "changed": false,
    "required": false,
    "widget": "Unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ASSIGNEES",
    "title": "Assignees",
    "aliasName": "ASSIGNEES",
    "changed": false,
    "required": true,
    "widget": "Unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
]');