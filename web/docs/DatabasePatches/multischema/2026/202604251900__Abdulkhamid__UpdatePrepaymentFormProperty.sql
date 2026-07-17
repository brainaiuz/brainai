update "anv".form_property set settingsjsondata = '[
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
},
  {
    "code": "BANK_FEE_ACCOUNT",
    "title": "Bank Fee Account",
    "aliasName": "BANK_FEE_ACCOUNT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
  "disabled": false
  },{
    "code": "FEE_TYPE",
    "title": "Type",
    "aliasName": "FEE_TYPE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
  "disabled": false
  },
  {
    "code": "AMOUNT_PERCENTAGE",
    "title": "Fee Amount",
    "aliasName": "AMOUNT_PERCENTAGE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
  "disabled": false
  }
]' where form_id='SUPPLIER_CREDIT_FORM';