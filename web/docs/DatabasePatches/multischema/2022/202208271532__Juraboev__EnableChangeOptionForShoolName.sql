update "90826".form_property
set settingsjsondata = '[
  {
    "code": "SCHOOL_NAME",
    "title": "School Name",
    "aliasName": "SCHOOL_NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false
  },
  {
    "code": "DEGREE",
    "title": "Degree",
    "aliasName": "DEGREE",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false

  },
  {
    "code": "FIELD_OF_STUDY",
    "title": "Field Of Study",
    "aliasName": "FIELD_OF_STUDY",
    "changed": false,
    "required": true,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "START_DATE",
    "title": "Start Date",
    "aliasName": "START_DATE",
    "changed": false,
    "required": false,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
   {
    "code": "DUE_DATE",
    "title": "End Date",
    "aliasName": "DUE_DATE",
    "changed": false,
    "required": false,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "COUNTRY",
    "title": "Country",
    "aliasName": "COUNTRY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]'
where form_id = 'EDUCATION_FORM';