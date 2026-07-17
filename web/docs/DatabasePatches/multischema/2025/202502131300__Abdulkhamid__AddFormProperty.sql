delete  from "anv".form_property where form_id ='COURSE_BOOKING_FORM';

insert into "anv".form_property (form_id, settingsjsondata)
values ('COURSE_BOOKING_FORM',
        '[

  {
    "code": "COMPANY_NAME",
    "title": "COMPANY NAME",
    "aliasName": "COMPANY_NAME",
    "changed": false,
    "required": true,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "COMPANY_NUMBER",
    "title": "COMPANY NUMBER",
    "aliasName": "COMPANY_NUMBER",
    "changed": false,
    "required": true,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PHONE_NUMBER",
    "title": "PHONE NUMBER",
    "widget": "DataListBox",
    "changed": false,
    "disabled": false,
    "required": false,
    "aliasName": "PHONE_NUMBER",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "FAX_NUMBER",
    "title": "FAX NUMBER",
    "widget": "DataListBox",
    "changed": false,
    "disabled": false,
    "required": true,
    "aliasName": "FAX_NUMBER",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "CUSTOMER_EMAIL",
    "title": "CUSTOMER EMAIL",
    "aliasName": "CUSTOMER_EMAIL",
    "changed": false,
    "required": false,
    "widget": "KpiDataGrid",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false

  },
  {
    "code": "TRAINING_VENUE",
    "title": "TRAINING VENUE",
    "aliasName": "TRAINING_VENUE",
    "changed": false,
    "required": true,
    "widget": "KpiDataGrid",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
 {
    "code": "TYPE",
    "title": "TYPE",
    "aliasName": "TYPE",
    "changed": false,
    "required": false,
    "widget": "KpiDataGrid",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }

]');
