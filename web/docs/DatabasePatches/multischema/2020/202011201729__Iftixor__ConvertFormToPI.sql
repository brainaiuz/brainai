
delete  from "anv".form_property where form_id ='PURCHASE_INVOICE_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('PURCHASE_INVOICE_FORM',
        '[
    {
    "code": "inputcrmaccount",
    "title": "Supplier",
    "aliasName": "SUPPLIER",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "inputdate",
    "title": "Date",
    "aliasName": "DATE",
    "changed": false,
    "required": false,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "inputduedate",
    "title": "Due Date",
    "aliasName": "DUE_DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SUPPLIER_INVOICE_TERM",
    "title": "Terms",
    "aliasName": "SUPPLIER_TERMS",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "inputnumber",
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
    "code": "inputcurrency",
    "title": "Currency",
    "aliasName": "CURRENCY",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "inputtaxcalc",
    "title": "Amounts",
    "aliasName": "TAX",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "inputinstruction",
    "title": "Instruction",
    "aliasName": "INSTRUCTION",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "inputreference",
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
    "code": "PROJECT",
    "title": "Project",
    "aliasName": "PROJECT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ACCOUNTS_PAYABLE",
    "title": "Accounts Payable",
    "aliasName": "ACCOUNTS_PAYABLE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PO_NUMBER",
    "title": "PO Number",
    "aliasName": "PO_NUMBER",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');