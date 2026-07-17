
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
    "widget": "Lookup",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "APPROVER",
    "title": "Approver",
    "aliasName": "APPROVER",
    "changed": false,
    "required": true,
    "widget": "Lookup",
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
    "disabled": false
  },
  {
    "code": "BENEFIT_TYPE",
    "title": "Type",
    "aliasName": "BENEFIT_TYPE",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LEFT_QUANTITY",
    "title": "Left Quantity",
    "aliasName": "LEFT_QUANTITY",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
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
    "disabled": false
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