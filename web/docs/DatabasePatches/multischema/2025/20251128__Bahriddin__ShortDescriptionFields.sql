insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, hide)
values ('DEPARTMENT_FORM', 'DEPARTMENT_SHORT_DESCRIPTION', false, 'COL_2', 'DEPARTMENT_DETAILS', 1, true);


update "anv".form_property
set settingsjsondata ='[
  {
    "code": "DEPARTMENT_NAME",
    "title": "Department Name",
    "aliasName": "DEPARTMENT_NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "DEPARTMENT_DESCRIPTION",
    "title": "Description",
    "aliasName": "DEPARTMENT_DESCRIPTION",
    "changed": false,
    "required": true,
    "widget": "TextArea",
    "disabled": false,
    "roleEdit": [],
    "systemRequired": false,
    "minChar": "",
    "information": false,
    "informationText": "",
    "approvalRelated": false
  },
    {
    "code": "DEPARTMENT_SHORT_DESCRIPTION",
    "title": "Short Description",
    "aliasName": "DEPARTMENT_SHORT_DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "disabled": false,
    "roleEdit": [],
    "systemRequired": false,
    "minChar": "",
    "information": false,
    "informationText": "",
    "approvalRelated": false
  },
  {
    "code": "DEPARTMENT_START_DATE",
    "title": "Start date",
    "aliasName": "DEPARTMENT_START_DATE",
    "changed": false,
    "required": false,
    "widget": "DatePicker",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "DEPARTMENT_CREATED_BY",
    "title": "Created by",
    "aliasName": "DEPARTMENT_CREATED_BY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "DEPARTMENT_EMAIL",
    "title": "E-mail",
    "aliasName": "DEPARTMENT_EMAIL",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "DEPARTMENT_PARENT",
    "title": "Reports To",
    "aliasName": "DEPARTMENT_PARENT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "disabled": false,
    "roleEdit": [],
    "systemRequired": false,
    "minChar": "",
    "information": false,
    "informationText": "",
    "approvalRelated": false
  },
  {
    "code": "DEPARTMENT_LEADER",
    "title": "Department Leader",
    "aliasName": "DEPARTMENT_LEADER",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "DEPARTMENT_LEADER2",
    "title": "Department Leader 2",
    "aliasName": "DEPARTMENT_LEADER2",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "DEPARTMENT_LEADER3",
    "title": "Department Leader 3",
    "aliasName": "DEPARTMENT_LEADER3",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "DEPARTMENT_LEADER4",
    "title": "Department Leader 4",
    "aliasName": "DEPARTMENT_LEADER4",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "DEPARTMENT_LEADER5",
    "title": "Department Leader 5",
    "aliasName": "DEPARTMENT_LEADER5",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "DEPARTMENT_LOCATION",
    "title": "Location",
    "aliasName": "DEPARTMENT_LOCATION",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  }
]'
where form_id = 'DEPARTMENT_FORM';