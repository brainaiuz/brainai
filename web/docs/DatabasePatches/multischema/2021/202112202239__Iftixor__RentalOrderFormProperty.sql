delete  from "anv".form_property where form_id ='RENTAL_ORDER_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('RENTAL_ORDER_FORM',
'[
  {
    "code": "CUSTOMER",
    "title": "Customer",
    "aliasName": "CUSTOMER",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
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
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DATE",
    "title": "Expiration",
    "aliasName": "DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CLIENT_INVOICE_TERM",
    "title": "Payment Terms",
    "aliasName": "CUSTOMER_TERMS",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "TAX_CALC_TYPE",
    "title": "Amounts",
    "aliasName": "TAX",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');