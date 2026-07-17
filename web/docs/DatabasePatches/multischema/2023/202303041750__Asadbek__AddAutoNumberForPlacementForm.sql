delete from "anv".modelfield where form_id = 'PLACEMENT_FORM' and field_id = 'placementCode';

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder) values('PLACEMENT_FORM', 'placementCode', false, false, 'COL_3', 'PLACEMENT_BASIC_INFORMATION', 0);


delete from "anv".form_property where form_id = 'PLACEMENT_FORM';

insert into "anv".form_property(form_id, settingsjsondata) values('PLACEMENT_FORM', '[
  {

    "code": "placementcandidate",
    "title": "Candidate",
    "aliasName": "CANDIDATE",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "placementDateOffer",
    "title": "Date Offered",
    "aliasName": "DATEOFFER",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "placementProject",
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
    "code": "placementLocation",
    "title": "Location",
    "aliasName": "LOCATION",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "placementPosition",
    "title": "Position",
    "aliasName": "POSITION",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "placementDepartment",
    "title": "Department",
    "aliasName": "DEPARTMENT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "placementCode",
    "title": "Code",
    "aliasName": "PLACEMENT_CODE",
    "changed": false,
    "required": true,
    "widget": "Unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true
  }
]');
