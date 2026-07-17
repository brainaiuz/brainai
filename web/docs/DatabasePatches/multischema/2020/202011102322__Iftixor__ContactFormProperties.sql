delete  from "anv".form_property where form_id ='CONTACT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('CONTACT_FORM',
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
    "code": "MIDDLE_NAME",
    "title": "Middle Name",
    "aliasName": "MIDDLE_NAME",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "OTHER_NAME",
    "title": "Other Name",
    "aliasName": "OTHER_NAME",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "BIRTH_DAY",
    "title": "Date of birth",
    "aliasName": "BIRTH_DAY",
    "changed": false,
    "required": false,
    "widget": "BIRTH_DAY",
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
    "code": "DEPARTMENT",
    "title": "Department",
    "aliasName": "DEPARTMENT",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "REF_IND_NUMBER",
    "title": "Ref Ind Number",
    "aliasName": "REF_IND_NUMBER",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ASSETS",
    "title": "Assets Under Management",
    "aliasName": "ASSETS",
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
    "code": "RELATIONSHIP",
    "title": "Relationship",
    "aliasName": "RELATIONSHIP",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "REPORTS_TO",
    "title": "Supervisor",
    "aliasName": "SUPERVISOR",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "OWNER",
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
  }
]');