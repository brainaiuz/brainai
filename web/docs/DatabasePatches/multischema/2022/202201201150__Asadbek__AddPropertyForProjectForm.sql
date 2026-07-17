delete  from "anv".form_property where form_id ='PROJECT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('PROJECT_FORM',
        '[
  {
    "code": "NUMBER",
    "title": "Number",
    "aliasName": "NUMBER",
    "changed": false,
    "required": true,
    "widget": "Numbering",
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
    "code": "EMPLOYEE_ASSIGNMENT",
    "title": "Employee Assignment",
    "aliasName": "EMPLOYEE_ASSIGNMENT",
    "changed": false,
    "required": false,
    "widget": "DropDown",
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
    "code": "PARENT",
    "title": "Parent",
    "aliasName": "PARENT",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LOCATION",
    "title": "Location",
    "aliasName": "LOCATION",
    "changed": false,
    "required": false,
    "widget": "DropDown",
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
    "code": "ADD_NEW_ITEMS",
    "title": "Add Employee",
    "aliasName": "ADD_NEW_ITEMS",
    "changed": false,
    "required": false,
    "widget": "Unknow",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "INVOLVED_EMPLOYEE",
    "title": "Project Employees",
    "aliasName": "INVOLVED_EMPLOYEE",
    "changed": false,
    "required": false,
    "widget": "Unknow",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "BACKUP_MANAGER",
    "title": "Backup Manager(s)",
    "aliasName": "BACKUP_MANAGER",
    "changed": false,
    "required": false,
    "widget": "Unknow",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "MANAGER",
    "title": "Manager",
    "aliasName": "MANAGER",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
]');