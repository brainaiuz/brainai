delete  from "anv".form_property where form_id ='CERTIFICATE_OF_EMPLOYMENT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('CERTIFICATE_OF_EMPLOYMENT_FORM',
        '[
  {
    "code": "CERTIFICATE_TYPE",
    "title": "Type",
    "aliasName": "CERTIFICATE_TYPE",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
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
    "code": "EMPLOYEE",
    "title": "Employee",
    "aliasName": "EMPLOYEE",
    "changed": false,
    "required": true,
    "widget": "Unknown",
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
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
]');