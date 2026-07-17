delete  from "anv".form_property where form_id ='PAYROLL_CASH_ADVANCE_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('PAYROLL_CASH_ADVANCE_FORM',
'[
  {
    "code": "EMPLOYEE",
    "title": "Requester",
    "aliasName": "EMPLOYEE",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "REQUESTED_AMOUNT",
    "title": "Requested Amount",
    "aliasName": "REQUESTED_AMOUNT",
    "changed": false,
    "required": true,
    "widget": "AutoNumber",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "REQUESTED_DATE",
    "title": "Date",
    "aliasName": "REQUESTED_DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": true,
    "disabled": false
  },
  {
    "code": "PAYMENT_TERMS",
    "title": "Payment Terms",
    "aliasName": "PAYMENT_TERMS",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PAYMENT_AMOUNT",
    "title": "Payment Amount",
    "aliasName": "PAYMENT_AMOUNT",
    "changed": false,
    "required": true,
    "widget": "AutoNumber",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "REFERENCE",
    "title": "Reference",
    "aliasName": "REFERENCE",
    "changed": false,
    "required": falce,
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
    "required": true,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "PAYMENT_METHOD",
    "title": "Payment Method",
    "aliasName": "PAYMENT_METHOD",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CATEGORY",
    "title": "Category",
    "aliasName": "CATEGORY",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PURPOSE",
    "title": "Purpose",
    "aliasName": "PURPOSE",
    "changed": false,
    "required": false,
    "widget": "TextArea",
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
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "EXCHANGE_RATE",
    "title": "Exchange Rate",
    "aliasName": "EXCHANGE_RATE",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');