
delete  from "anv".form_property where form_id ='CASE_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('CASE_FORM',
        '[
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
    "code": "REPORTED_BY",
    "title": "Reported By",
    "aliasName": "REPORTED_BY",
    "changed": false,
    "required": false,
    "widget": "CASE_MULTI_WIDGET",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CASE_DESCRIPTION",
    "title": "Description",
    "aliasName": "DESCRIPTION",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "TYPE",
    "title": "Type",
    "aliasName": "TYPE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CASE_ORIGIN",
    "title": "Origin",
    "aliasName": "ORIGIN",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CASE_REASON",
    "title": "Reason",
    "aliasName": "REASON",
    "changed": false,
    "required": false,
    "widget": "DropDown",
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
    "required": true,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ASSIGNEE",
    "title": "Assigned to",
    "aliasName": "ASSIGNEE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "RESOLVER",
    "title": "Resolver",
    "aliasName": "RESOLVER",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "INTERNAL_STATUS",
    "title": "Internal Status",
    "aliasName": "INTERNAL_STATUS",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_NOTE",
    "title": "Notes",
    "aliasName": "NOTE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CASE_ID",
    "title": "Case ID",
    "aliasName": "CASE_ID",
    "changed": false,
    "required": false,
    "widget": "WIDGET",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "INTERNAL_UPDATED_DATE",
    "title": "IS Updated Date",
    "aliasName": "INTERNAL_UPDATED_DATE",
    "changed": false,
    "required": false,
    "widget": "WIDGET",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');