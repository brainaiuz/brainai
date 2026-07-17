delete from "anv".form_property where form_id = 'CANDIDATE_FORM';

insert into "anv".form_property(form_id, settingsjsondata) values('CANDIDATE_FORM', '[
  {

    "code": "FIRST_NAME",
    "title": "First Name",
    "aliasName": "FIRSTNAME",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
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
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "GENDER",
    "title": "Gender",
    "aliasName": "GENDER",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "MARTIAL_STATUS",
    "title": "Maritial Status",
    "aliasName": "MARTIAL_STATUS",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LAST_NAME",
    "title": "Last Name",
    "aliasName": "LASTNAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PHONE",
    "title": "Phone",
    "aliasName": "Phone",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ADDRESS",
    "title": "Contact Addresses",
    "aliasName": "ADDRESS",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LANGUAGE",
    "title": "Language",
    "aliasName": "LANGUAGE",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CANDIDATE_PROJECT",
    "title": "Project",
    "aliasName": "PROJECT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CURRENT_EMPLOYER",
    "title": "Current employer",
    "aliasName": "EMPLOYER",
    "changed": false,
    "required": false,
    "widget": "TextBox",
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
    "code": "ALLOWANCES",
    "title": "Allowances",
    "aliasName": "ALLOWANCES",
    "changed": false,
    "required": false,
    "widget": "MULTITABLE",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ATTACHMENTS",
    "title": "Attachments",
    "aliasName": "ATTACHMENTS",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "VACANCIES",
    "title": "Matched Vacancies",
    "aliasName": "VACANCIES",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "BIRTH_DAY",
    "title": "Date of birth",
    "aliasName": "BIRTHDAY",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
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
    "code": "EXPECTED_SALARY",
    "title": "Expected salary",
    "aliasName": "EXPECTED_SALARY",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "NUMBER",
    "title": "Number",
    "aliasName": "NUMBER",
    "changed": false,
    "required": false,
    "widget": "TextBox",
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
    "code": "WORK_EXPERIENCE",
    "title": "Work experience",
    "aliasName": "WORK_EXPERIENCE",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SKILLS",
    "title": "Skills",
    "aliasName": "SKILLS",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CREATED_DATE",
    "title": "Created date",
    "aliasName": "CREATED_DATE",
    "changed": false,
    "required": false,
    "widget": "DatePicker",
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
  }
]');