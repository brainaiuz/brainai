delete from "anv".form_property where form_id = 'DEPENDENT_FORM';

insert into "anv".form_property(form_id, settingsjsondata) values('DEPENDENT_FORM', '[
  {
    "code": "FIRST_NAME",
    "title": "First Name",
    "aliasName": "FIRST NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "test",
    "disabled": false,
    "roleEdit": []
  },
  {
    "code": "LAST_NAME",
    "title": "Last Name",
    "aliasName": "LAST NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "test",
    "disabled": false,
    "roleEdit": []
  },
  {
    "code": "ADDRESS",
    "title": "Street Address 1",
    "aliasName": "STREET ADDRESS 1",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "test",
    "disabled": false,
    "roleEdit": []
  },
  {
    "code": "ADDRESS_2",
    "title": "Street Address 2",
    "aliasName": "STREET ADDRESS 2",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "test",
    "disabled": false,
    "roleEdit": []
  },
  {
    "code": "CITY",
    "title": "City",
    "aliasName": "CITY",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "test",
    "disabled": false,
    "roleEdit": []
  },
  {
    "code": "PHONE",
    "title": "Phone 1",
    "aliasName": "PHONE 1",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "test",
    "disabled": false,
    "roleEdit": []
  },
  {
    "code": "RELATIONSHIP",
    "title": "Relationship",
    "aliasName": "RELATIONSHIP",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "disabled": false,
    "roleEdit": []
  },
  {
    "code": "COUNTRY",
    "title": "Country",
    "aliasName": "COUNTRY",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": 38,
    "defaultValue": "Austria",
    "disabled": false,
    "roleEdit": []
  },
  {
    "code": "MIDDLE_NAME",
    "title": "Middle Name",
    "aliasName": "MIDDLE NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "test",
    "disabled": false,
    "roleEdit": []
  }
]');