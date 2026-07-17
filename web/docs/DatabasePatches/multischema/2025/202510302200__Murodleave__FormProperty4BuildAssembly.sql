
delete  from "anv".form_property where form_id ='BUILD_ASSEMBLY_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('BUILD_ASSEMBLY_FORM',
        '[
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
    "code": "PRODUCT",
    "title": "Product",
    "aliasName": "PRODUCT",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "DATE",
    "title": "Date",
    "aliasName": "DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "QUANTITY",
    "title": "Quantity",
    "aliasName": "QUANTITY",
    "changed": false,
    "required": true,
    "widget": "Textbox",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "WAREHOUSE",
    "title": "Warehouse",
    "aliasName": "WAREHOUSE",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "ASSEMBLY_ITEMS",
    "title": "Assembly Items",
    "aliasName": "ASSEMBLY_ITEMS",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "APPROVERS",
    "title": "Approvers",
    "aliasName": "APPROVERS",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": ""
  }
]')