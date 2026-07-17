delete  from "anv".form_property where form_id ='OPPORTUNITY_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('OPPORTUNITY_FORM',
        '[
  {
    "code": "CRM_OPPORTUNITY_PROBABILITY",
    "title": "Probability (%)",
    "aliasName": "PROBABILITY",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_OPPORTUNITY_ASSIGNEE",
    "title": "Assignee",
    "aliasName": "ASSIGNEE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_OPPORTUNITY_BACKUP_ASSIGNEE",
    "title": "Backup Assignee",
    "aliasName": "BACKUP_ASSIGNEE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_OPPORTUNITY_NUMBER",
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
    "code": "CRM_OPPORTUNITY_NAME",
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
    "code": "CRM_OPPORTUNITY_ACCOUNT_NAME",
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
    "code": "CRM_OPPORTUNITY_CONTACT_NAME",
    "title": "Contact",
    "aliasName": "CONTACT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_OPPORTUNITY_LEAD_SOURCE",
    "title": "Source",
    "aliasName": "LEAD_SOURCE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_OPPORTUNITY_CAMPAIGN_SOURCE",
    "title": "Campaign",
    "aliasName": "CAMPAIGN",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_OPPORTUNITY_TYPE",
    "title": "Type",
    "aliasName": "TYPE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CURRENCY",
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
    "code": "CRM_OPPORTUNITY_NEXT_STEP",
    "title": "Next Step",
    "aliasName": "NEXT_STEP",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_OPPORTUNITY_AMOUNT",
    "title": "Amount",
    "aliasName": "AMOUNT",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_OPPORTUNITY_EXPECTED_REVENUE",
    "title": "Expected Revenue",
    "aliasName": "EXPECTED_REVENUE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
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
    "code": "CRM_OPPORTUNITY_CLOSING_DATE",
    "title": "Close Date",
    "aliasName": "CLOSING_DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_OPPORTUNITY_STAGE",
    "title": "Stage",
    "aliasName": "STAGE",
    "changed": false,
    "required": true,
    "widget": "DropDown",
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
  }
]');