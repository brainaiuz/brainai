update "anv".modelfield set mandatory = false where form_id = 'LEAVE_REQUEST_FORM' and field_id in ('TAKE_LIVE_TYPE', 'DESCRIPTION');
update "anv".modelfield set hide = false where form_id = 'LEAVE_REQUEST_FORM' and field_id = 'APPROVER';
update "anv".modelfield set mandatory = false where form_id = 'LEAVE_REQUEST_FORM' and field_id = 'APPROVER';
update "anv".modelfield set mandatory = false where form_id= 'INCIDENT_FORM' and field_id = 'VISIBILITY';


delete
from "anv".form_property
where form_id = 'LEAVE_REQUEST_FORM';

insert into "anv".form_property(form_id, settingsjsondata)
values ('LEAVE_REQUEST_FORM', '[
  {

    "code": "EMPLOYEES",
    "title": "Employee",
    "aliasName": "EMPLOYEE",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "DATE_PERIOD",
    "title": "Leave Period",
    "aliasName": "DATEPERIOD",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "REASON",
    "title": "Reason",
    "aliasName": "REASON",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "DESCRIPTION",
    "title": "Description",
    "aliasName": "DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "APPROVER",
    "title": "Approver",
    "aliasName": "APPROVER",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "TAKE_LIVE_TYPE",
    "title": "Take Leave By",
    "aliasName": "TAKELEAVETYPE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "TYPE",
    "title": "Type",
    "aliasName": "TYPE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');