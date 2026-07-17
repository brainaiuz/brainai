delete  from "anv".form_property where form_id ='BRIGADA_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('BRIGADA_FORM',
'[
  {
    "code": "NUMBER",
    "title": "Number",
    "aliasName": "NUMBER",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
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
    "code": "STATUS",
    "title": "Status",
    "aliasName": "STATUS",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": true,
    "disabled": false
  },
  {
    "code": "INVOLVED_EMPLOYEE",
    "title": "Project Employees",
    "aliasName": "INVOLVED_EMPLOYEE",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "BACKUP_MANAGER",
    "title": "Backup Manager(s)",
    "aliasName": "BACKUP_MANAGER",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
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
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');


delete  from "anv".form_property where form_id ='SHIFT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('SHIFT_FORM',
'[
  {
    "code": "monthPicker",
    "title": "Period",
    "aliasName": "monthPicker",
    "changed": false,
    "required": false,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "APPROVERS",
    "title": "Approvers",
    "aliasName": "APPROVERS",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "MANAGER",
    "title": "Manager",
    "aliasName": "MANAGER",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "BACKUP_MANAGER",
    "title": "Backup Manager(s)",
    "aliasName": "BACKUP_MANAGER",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');