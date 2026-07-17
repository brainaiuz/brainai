
delete  from "anv".form_property where form_id ='REQUEST_FOR_QUOTE_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('REQUEST_FOR_QUOTE_FORM',
        '[
  {
    "code": "CUSTOMER",
    "title": "Customer",
    "aliasName": "CUSTOMER",
    "changed": false,
    "required": false,
    "widget": "LOOKUP"
  },
  {
    "code": "DATE",
    "title": "Request Date",
    "aliasName": "REQUEST_DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker"
  },
  {
    "code": "DUE_DATE",
    "title": "Due Date",
    "aliasName": "DUE_DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker"
  },
  {
    "code": "NUMBER",
    "title": "Number",
    "aliasName": "NUMBER",
    "changed": false,
    "required": true,
    "widget": "TextBox"
  },
  {
    "code": "SQ_NUMBER",
    "title": "SQ #",
    "aliasName": "SQ_NUMBER",
    "changed": false,
    "required": true,
    "widget": "TextBox"
  },
  {
    "code": "PROJECT_MANAGER",
    "title": "Project",
    "aliasName": "PROJECT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP"
  }
]');