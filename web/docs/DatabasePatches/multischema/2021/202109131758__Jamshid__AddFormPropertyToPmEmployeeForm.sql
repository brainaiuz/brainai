delete from "anv".form_property where form_id = 'PM_EMPLOYEE_FORM';

insert into "anv".form_property(form_id, settingsjsondata) values('PM_EMPLOYEE_FORM', '[
  {
    "code": "FIRST_NAME",
    "title": "First Name1",
    "aliasName": "FIRST NAME",
    "changed": true,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "Firstname",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "LAST_NAME",
    "title": "Last Name1",
    "aliasName": "LAST NAME",
    "changed": true,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "Last Name1",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "MIDDLE_NAME",
    "title": "Middle Name1",
    "aliasName": "MIDDLE NAME",
    "changed": true,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "Middle Name1",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "BIRTH_DAY",
    "title": "Date of birth",
    "aliasName": "DATE OF BIRTH",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "MARTIAL_STATUS",
    "title": "Marital status",
    "aliasName": "MARITAL STATUS",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "selectedId": 443,
    "defaultValue": "Single",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "GENDER",
    "title": "Gender",
    "aliasName": "GENDER",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "EMAIL",
    "title": "Email1",
    "aliasName": "EMAIL",
    "changed": true,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "Email1",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "PHONE",
    "title": "Phone1",
    "aliasName": "PHONE",
    "changed": true,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "Phone1",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "IM_ADDRESS",
    "title": "IM address1",
    "aliasName": "IM_ADDRESS",
    "changed": true,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "IM address1",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "WEB_ADDRESS",
    "title": "Web Address1",
    "aliasName": "WEB_ADDRESS",
    "changed": true,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "Web Address1",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "DEPARTMENT",
    "title": "Department1",
    "aliasName": "DEPARTMENT",
    "changed": true,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": 14,
    "defaultValue": "GAS",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "POSITION",
    "title": "POSITION",
    "aliasName": "POSITION",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": 5,
    "defaultValue": "Driver- Bike",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "WAGE_RATE",
    "title": "Wage rate",
    "aliasName": "WAGE_RATE",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "55",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "CLIENT_CHARGE_RATE",
    "title": "Client Charge Rate",
    "aliasName": "CLIENT_CHARGE_RATE",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "66",
    "disabled": true,
    "roleEdit": []
  },
  {
    "code": "LOCATION",
    "title": "Location",
    "aliasName": "LOCATION",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": 7,
    "defaultValue": "Namangan",
    "disabled": true,
    "roleEdit": []
  }
]');