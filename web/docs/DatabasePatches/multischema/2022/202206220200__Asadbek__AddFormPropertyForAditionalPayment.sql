
delete  from "anv".form_property where form_id ='ADDITIONAL_PAYMENT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('ADDITIONAL_PAYMENT_FORM',
        '[
  {
    "code": "EMPLOYEE",
    "title": "Employee",
    "aliasName": "EMPLOYEE",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PAYMENT_DATE",
    "title": "Payment Date",
    "aliasName": "PAYMENT_DATE",
    "changed": false,
    "required": false,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PERIOD",
    "title": "Period",
    "aliasName": "PERIOD",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PAYMENT_TYPE",
    "title": "Payment type",
    "aliasName": "PAYMENT_TYPE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "AMOUNT",
    "title": "Fixed Amount",
    "aliasName": "AMOUNT",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CATEGORY",
    "title": "Category",
    "aliasName": "CATEGORY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "REFERENCE",
    "title": "Reference",
    "aliasName": "REFERENCE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SHOW_PAYSLIP",
    "title": "Show in payslip",
    "aliasName": "SHOW_PAYSLIP",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "APPROVERS",
    "title": "Approver",
    "aliasName": "APPROVERS",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');