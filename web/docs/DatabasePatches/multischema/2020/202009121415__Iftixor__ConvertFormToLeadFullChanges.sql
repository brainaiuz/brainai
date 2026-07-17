delete  from "anv".form_property where form_id ='LEAD_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('LEAD_FORM',
        '[
  {
    "code": "FIRST_NAME",
    "title": "First Name",
    "aliasName": "FIRST_NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LAST_NAME",
    "title": "Last Name",
    "aliasName": "LAST_NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_NAME",
    "title": "Company",
    "aliasName": "COMPANY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_TYPE",
    "title": "Account Type",
    "aliasName": "ACCOUNT_TYPE",
    "changed": false,
    "required": false,
    "widget": "MATRIX_TABLE",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "JOB_TITLE",
    "title": "Job Title",
    "aliasName": "JOB_TITLE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_INDUSTRY",
    "title": "Industry",
    "aliasName": "INDUSTRY",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "EMAIL",
    "title": "Email",
    "aliasName": "EMAIL",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PHONE",
    "title": "Phone",
    "aliasName": "PHONE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "IM_ADDRESS",
    "title": "IM Address",
    "aliasName": "IM_ADDRESS",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "WEB_ADDRESS",
    "title": "Web Address",
    "aliasName": "WEB_ADDRESS",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LEAD_OWNER",
    "title": "Owner",
    "aliasName": "OWNER",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_CAMPAIGN_NAME",
    "title": "Campaign",
    "aliasName": "CAMPAIGN",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "EMAIL_OPT_OUT",
    "title": "Email opt-out",
    "aliasName": "EMAIL_OPT_OUT",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
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
    "code": "ASSIGNEE",
    "title": "Assignee",
    "aliasName": "ASSIGNEE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "BACKUP_ASSIGNEE",
    "title": "Backup Assignee",
    "aliasName": "BACKUP_ASSIGNEE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LEAD_SOURCE",
    "title": "Source",
    "aliasName": "SOURCE",
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
    "code": "RATING",
    "title": "Rating",
    "aliasName": "RATING",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');