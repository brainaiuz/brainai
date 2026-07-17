delete  from "anv".form_property where form_id ='ROTATION_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('ROTATION_FORM',
'[
  {
    "code": "NUMBER",
    "title": "Number",
    "aliasName": "NUMBER",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
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
    "code": "INVOLVED_EMPLOYEES",
    "title": "Involved employee",
    "aliasName": "INVOLVED_EMPLOYEES",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "APPROVERS",
    "title": "Approvers",
    "aliasName": "APPROVERS",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }

]');
