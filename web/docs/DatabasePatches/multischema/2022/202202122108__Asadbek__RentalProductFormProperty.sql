delete  from "anv".form_property where form_id ='RENTAL_PRODUCT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('RENTAL_PRODUCT_FORM',
'[
  {
    "code": "NAME",
    "title": "Name",
    "aliasName": "NAME",
    "changed": false,
    "required": true,
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
    "widget": "Numbering",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ACTIVE",
    "title": "Active",
    "aliasName": "ACTIVE",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": true,
    "disabled": false
  },
  {
    "code": "PURCHASE_PRICE",
    "title": "Purchase Price",
    "aliasName": "PURCHASE_PRICE",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SALES_PRICE",
    "title": "Selling Price",
    "aliasName": "SALES_PRICE",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PURCHASE_ACCOUNT",
    "title": "Purchase Account",
    "aliasName": "PURCHASE_ACCOUNT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SALES_ACCOUNT",
    "title": "Sales Account",
    "aliasName": "SALES_ACCOUNT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
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
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "BRAND",
    "title": "Brand",
    "aliasName": "BRAND",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "TAX",
    "title": "Tax Rate",
    "aliasName": "TAX",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SUPPLIERS",
    "title": "Suppliers",
    "aliasName": "SUPPLIERS",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "BARCODE",
    "title": "Barcode",
    "aliasName": "BARCODE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "OVERTIME_HOURS",
    "title": "Extra Hour",
    "aliasName": "EXTRA_HOUR",
    "changed": false,
    "required": false,
    "widget": "AutoNumber",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "OVERTIME_END_DATE",
    "title": "Extra Day",
    "aliasName": "EXTRA_DAY",
    "changed": false,
    "required": false,
    "widget": "AutoNumber",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SECURITY_TIME",
    "title": "Security Time",
    "aliasName": "SECURITY_TIME",
    "changed": false,
    "required": false,
    "widget": "AutoNumber",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "TYPE",
    "title": "Rental Type",
    "aliasName": "TYPE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');