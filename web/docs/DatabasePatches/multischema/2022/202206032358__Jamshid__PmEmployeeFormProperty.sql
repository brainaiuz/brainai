delete from "anv".form_property where form_id = 'PM_EMPLOYEE_FORM';
insert into "anv".form_property(form_id, settingsjsondata) values('PM_EMPLOYEE_FORM', '[
  {
    "code": "FIRST_NAME",
    "title": "First Name",
    "aliasName": "FIRST NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LAST_NAME",
    "title": "Last Name",
    "aliasName": "LAST NAME",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "MIDDLE_NAME",
    "title": "Middle Name",
    "aliasName": "MIDDLE NAME",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "BIRTH_DAY",
    "title": "Date of birth",
    "aliasName": "DATE OF BIRTH",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "MARTIAL_STATUS",
    "title": "Marital status",
    "aliasName": "MARITAL STATUS",
    "changed": false,
    "required": false,
    "widget": "DropDown",
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
    "disabled": false
  },
  {
    "code": "EMAIL",
    "title": "Email",
    "aliasName": "EMAIL",
    "changed": false,
    "required": true,
    "widget": "TextBox",
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
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "IM_ADDRESS",
    "title": "IM address",
    "aliasName": "IM_ADDRESS",
    "changed": false,
    "required": false,
    "widget": "TextBox",
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
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT",
    "title": "Department",
    "aliasName": "DEPARTMENT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "POSITION",
    "title": "Position",
    "aliasName": "POSITION",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "WAGE_RATE",
    "title": "Wage rate",
    "aliasName": "WAGE_RATE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CLIENT_CHARGE_RATE",
    "title": "Client Charge Rate",
    "aliasName": "CLIENT_CHARGE_RATE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LOCATION",
    "title": "Location",
    "aliasName": "LOCATION",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "defaultValue": "",
    "disabled": false
  }
]');