
delete  from "anv".form_property where form_id ='FIXED_ASSET_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('FIXED_ASSET_FORM',
        '[
  {
    "code": "OWNER",
    "title": "Owner",
    "aliasName": "OWNER",
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
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CODE",
    "title": "Number",
    "aliasName": "CODE",
    "changed": false,
    "required": false,
    "widget": "Unknown",
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
  },
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
    "code": "COST",
    "title": "Cost",
    "aliasName": "COST",
    "changed": false,
    "required": true,
    "widget": "AutoNumber",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PURCHASE_DATE",
    "title": "Purchase Date",
    "aliasName": "PURCHASE_DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "USEFUL_LIFE",
    "title": "Useful Life (in years)",
    "aliasName": "USEFUL_LIFE",
    "changed": false,
    "required": true,
    "widget": "AutoNumber",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
   {
    "code": "SHOW_DESCRIPTION_IN_BARCODE",
    "title": "Add description to QR Code",
    "aliasName": "SHOW_DESCRIPTION_IN_BARCODE",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "RESIDUAL_VALUE",
    "title": "Residual Value",
    "aliasName": "RESIDUAL_VALUE",
    "changed": false,
    "required": true,
    "widget": "Unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "TAX_VALUE",
    "title": "Tax",
    "aliasName": "TAX_VALUE",
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
    "aliasName": "TAX_CALC_TYPE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
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
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "QUANTITY",
    "title": "Quantity",
    "aliasName": "QUANTITY",
    "changed": false,
    "required": false,
    "widget": "AutoNumber",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ACCOUNT_NAME",
    "title": "Account",
    "aliasName": "ACCOUNT_NAME",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "FIXED_ASSET_ACCOUNT",
    "title": "Accumulated Depreciation Account",
    "aliasName": "FIXED_ASSET_ACCOUNT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "EXPENSE_ACCOUNT",
    "title": "Depreciation Expense Account",
    "aliasName": "EXPENSE_ACCOUNT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "RELATED_ITEM",
    "title": "Converted Item",
    "aliasName": "RELATED_ITEM",
    "changed": false,
    "required": false,
    "widget": "Unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');


update "0".customformsection set active = false where form_id = 'FIXED_ASSET_FORM' and section = 'DEPRECIATION_ACCOUNT';
update "0".modelfield set fsection = 'FIXED_ASSET_FINANCING' where form_id = 'FIXED_ASSET_FORM' and field_id in ('FIXED_ASSET_ACCOUNT', 'EXPENSE_ACCOUNT');

update "anv".customformsection set active = false where form_id = 'FIXED_ASSET_FORM' and section = 'DEPRECIATION_ACCOUNT';
update "anv".modelfield set fsection = 'FIXED_ASSET_FINANCING' where form_id = 'FIXED_ASSET_FORM' and field_id in ('FIXED_ASSET_ACCOUNT', 'EXPENSE_ACCOUNT');

