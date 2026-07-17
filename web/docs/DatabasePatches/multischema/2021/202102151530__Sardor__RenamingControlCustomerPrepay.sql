delete  from "anv".form_property where form_id ='PREPAYMENT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('PREPAYMENT_FORM',
        '[
  {
    "code": "CRM_ACCOUNT_LOOKUP",
    "title": "Customer",
    "aliasName": "CRM_ACCOUNT_LOOKUP",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PAYMENT_ACCOUNT_LOOKUP",
    "title": "Paid to",
    "aliasName": "PAYMENT_ACCOUNT_LOOKUP",
    "changed": false,
    "required": true,
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
    "code": "DATE_FIELD",
    "title": "Date",
    "aliasName": "DATE_FIELD",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PREPAYMENT_AMOUNT",
    "title": "Amount",
    "aliasName": "PREPAYMENT_AMOUNT",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ACCOUNTS_RECEIVABLE_PAYABLE",
    "title": "Account",
    "aliasName": "ACCOUNTS_RECEIVABLE_PAYABLE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PREPAYMENT_NUMBER",
    "title": "Number",
    "aliasName": "PREPAYMENT_NUMBER",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP",
    "title": "Sales Quote",
    "aliasName": "SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
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
  },{
    "code": "DEPARTMENT",
    "title": "Department",
    "aliasName": "DEPARTMENT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },{
    "code": "SALE_INVOICE_LOOKUP",
    "title": "Sales Invoice",
    "aliasName": "SALE_INVOICE_LOOKUP",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },{
    "code": "POST_DATE",
    "title": "Post Dated",
    "aliasName": "POST_DATE",
    "changed": false,
    "required": false,
    "widget": null,
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]'
);

delete  from "anv".form_property where form_id ='SUPPLIER_CREDIT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('SUPPLIER_CREDIT_FORM',
        '[
  {
    "code": "CRM_ACCOUNT_LOOKUP",
    "title": "Supplier",
    "aliasName": "CRM_ACCOUNT_LOOKUP",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PAYMENT_ACCOUNT_LOOKUP",
    "title": "Paid to",
    "aliasName": "PAYMENT_ACCOUNT_LOOKUP",
    "changed": false,
    "required": true,
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
    "code": "DATE_FIELD",
    "title": "Date",
    "aliasName": "DATE_FIELD",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PREPAYMENT_AMOUNT",
    "title": "Amount",
    "aliasName": "PREPAYMENT_AMOUNT",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ACCOUNTS_RECEIVABLE_PAYABLE",
    "title": "Account",
    "aliasName": "ACCOUNTS_RECEIVABLE_PAYABLE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PREPAYMENT_NUMBER",
    "title": "Number",
    "aliasName": "PREPAYMENT_NUMBER",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP",
    "title": "Sales Quote",
    "aliasName": "SALE_QUOTE_OR_PURCHASE_ORDER_LOOKUP",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
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
  },{
    "code": "DEPARTMENT",
    "title": "Department",
    "aliasName": "DEPARTMENT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },{
    "code": "SALE_INVOICE_LOOKUP",
    "title": "Sales Invoice",
    "aliasName": "SALE_INVOICE_LOOKUP",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },{
    "code": "POST_DATE",
    "title": "Post Dated",
    "aliasName": "POST_DATE",
    "changed": false,
    "required": false,
    "widget": null,
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]'
);