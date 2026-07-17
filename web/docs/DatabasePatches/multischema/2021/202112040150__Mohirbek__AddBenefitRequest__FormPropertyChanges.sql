
delete  from "anv".form_property where form_id ='BENEFIT_REQUEST_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('BENEFIT_REQUEST_FORM',
        '[
  {
    "code": "REQUESTER",
    "title": "Requester",
    "aliasName": "REQUESTER",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "APPROVER",
    "title": "Approver",
    "aliasName": "APPROVER",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DATE_PERIOD",
    "title": "Date",
    "aliasName": "DATE_PERIOD",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true
   },
  {
    "code": "BENEFIT_TYPE",
    "title": "Type",
    "aliasName": "BENEFIT_TYPE",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
     "systemRequired": true
  },
  {
    "code": "REQUESTED_QUANTITY",
    "title": "Quantity",
    "aliasName": "REQUESTED_QUANTITY",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "DESCRIPTION",
    "title": "Description",
    "aliasName": "DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');