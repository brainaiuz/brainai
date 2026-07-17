delete  from "anv".form_property where form_id ='ACCOUNT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('ACCOUNT_FORM',
        '[
  {
    "code": "CRM_NOTE",
    "title": "Notes",
    "aliasName": "NOTE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_OWNER",
    "title": "Owners",
    "aliasName": "OWNER",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_NAME",
    "title": "Name",
    "aliasName": "NAME",
    "changed": false,
    "required": true,
    "widget": "Widget",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_PARENT",
    "title": "Parent Account",
    "aliasName": "PARENT_ACCOUNT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_NUMBER",
    "title": "Number",
    "aliasName": "NUMBER",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PRIMARY_CONTACT",
    "title": "Primary Contact",
    "aliasName": "PRIMARY_CONTACT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_TYPE",
    "title": "Type",
    "aliasName": "TYPE",
    "changed": false,
    "required": false,
    "widget": "MATRIX_TABLE",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_INDUSTRY",
    "title": "Industry",
    "aliasName": "INDUSTRY",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_EMAIL",
    "title": "Email",
    "aliasName": "EMAIL",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_PHONE",
    "title": "Phone",
    "aliasName": "PHONE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_FAX",
    "title": "Fax",
    "aliasName": "FAX",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_WEBSITE",
    "title": "WebSite",
    "aliasName": "WEBSITE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "REGISTRATION_NUMBER",
    "title": "Registration Number",
    "aliasName": "REGISTRATION_NUMBER",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "VAT_NUMBER",
    "title": "Vat Number",
    "aliasName": "VAT_NUMBER",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CURRENCY",
    "title": "Currency",
    "aliasName": "CURRENCY",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_ACCOUNT_TAX_TREATMENT",
    "title": "Tax Treatment",
    "aliasName": "TAX_TREATMENT",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
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
    "code": "CLIENT_INVOICE_TERM",
    "title": "Terms",
    "aliasName": "TERMS",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');