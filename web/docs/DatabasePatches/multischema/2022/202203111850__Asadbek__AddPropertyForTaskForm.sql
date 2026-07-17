delete  from "anv".form_property where form_id ='TASK_MAX_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('TASK_MAX_FORM',
        '[
  {
    "code": "CLIENT",
    "title": "Customer",
    "aliasName": "CLIENT",
    "changed": false,
    "required": false,
    "widget": "Unknow",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PROJECT",
    "title": "Project",
    "aliasName": "PROJECT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true

  },
  {
    "code": "NUMBER",
    "title": "Number",
    "aliasName": "NUMBER",
    "changed": false,
    "required": true,
    "widget": "Numbering",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true

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
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "START_DATE",
    "title": "Start date",
    "aliasName": "START_DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DUE_DATE",
    "title": "Due date",
    "aliasName": "DUE_DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "WORKFLOW_DATE",
    "title": "Date",
    "aliasName": "WORKFLOW_DATE",
    "changed": false,
    "required": true,
    "widget": "Unknow",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "WORKFLOW_TIME_BASED",
    "title": "Execution time",
    "aliasName": "WORKFLOW_TIME_BASED",
    "changed": false,
    "required": false,
    "widget": "Unknow",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "BILLIBLE",
    "title": "Billable",
    "aliasName": "BILLIBLE",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PRIORITY",
    "title": "Priority",
    "aliasName": "PRIORITY",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "STATUS",
    "title": "Status",
    "aliasName": "STATUS",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "TIME_SPENT",
    "title": "Time spent today",
    "aliasName": "TIME_SPENT",
    "changed": false,
    "required": false,
    "widget": "Unknow",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "TASK_AMOUNT",
    "title": "Task Amount",
    "aliasName": "TASK_AMOUNT",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PARENT_WORKSTREAM",
    "title": "Workstream",
    "aliasName": "PARENT_WORKSTREAM",
    "changed": false,
    "required": false,
    "widget": "Unknow",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PREDECESSOR_TASK",
    "title": "Predecessor",
    "aliasName": "PREDECESSOR_TASK",
    "changed": false,
    "required": false,
    "widget": "Unknow",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SUCCESSOR_TASK",
    "title": "Successor",
    "aliasName": "SUCCESSOR_TASK",
    "changed": false,
    "required": false,
    "widget": "Unknow",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DUE_DATE_REMINDER",
    "title": "Due Date Reminder",
    "aliasName": "DUE_DATE_REMINDER",
    "changed": false,
    "required": false,
    "widget": "Unknow",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "RECURRENING",
    "title": "Recurring",
    "aliasName": "RECURRENING",
    "changed": false,
    "required": false,
    "widget": "Unknow",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "TASK_NOTES",
    "title": "Notes",
    "aliasName": "TASK_NOTES",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
]');