delete
from "anv".modelfield
where form_id = 'BACKUPS_EMPLOYEE_FORM'
  and field_id = 'REASON';

delete
from "anv".modelfield
where form_id = 'BACKUPS_EMPLOYEE_FORM'
  and field_id = 'PERCENTAGE';


delete
from "anv".modelfield
where form_id = 'BACKUPS_EMPLOYEE_FORM'
  and field_id = 'DESCRIPTION';


delete
from "anv".modelfield
where form_id = 'BACKUPS_EMPLOYEE_FORM'
  and field_id = 'GRANT_SIGNING_AUTHORITY';



insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('BACKUPS_EMPLOYEE_FORM', 'REASON', false, false, 'COL_3', 'BASIC_INFORMATION', 3);


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('BACKUPS_EMPLOYEE_FORM', 'PERCENTAGE', false, false, 'COL_3', 'BASIC_INFORMATION', 4);


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('BACKUPS_EMPLOYEE_FORM', 'DESCRIPTION', false, false, 'COL_3', 'BASIC_INFORMATION', 5);



insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('BACKUPS_EMPLOYEE_FORM', 'GRANT_SIGNING_AUTHORITY', false, false, 'COL_3', 'BASIC_INFORMATION', 6);



update "anv".form_property
set settingsjsondata='[
  {
    "code": "EMPLOYEE",
    "title": "Employee",
    "aliasName": "EMPLOYEE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP"
  },
  {
    "code": "EMPLOYEES",
    "title": "Employee",
    "aliasName": "EMPLOYEE",
    "changed": false,
    "required": true,
    "widget": "LOOKUP"
  },
  {
    "code": "APPROVERS",
    "title": "Approver",
    "aliasName": "APPROVERS",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT",
    "title": "Department",
    "aliasName": "DEPARTMENT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP"
  },
  {
    "code": "POSITION",
    "title": "Position",
    "aliasName": "POSITION",
    "changed": false,
    "required": true,
    "widget": "LOOKUP"
  },
  {
    "code": "REASON",
    "title": "Reason",
    "aliasName": "REASON",
    "changed": false,
    "required": false,
    "widget": "LOOKUP"
  },
  {
    "code": "PERCENTAGE",
    "title": "Percentage",
    "aliasName": "PERCENTAGE",
    "changed": false,
    "required": false,
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
    "code": "GRANT_SIGNING_AUTHORITY",
    "title": "Grant signing authority",
    "aliasName": "GRANT_SIGNING_AUTHORITY",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]'
where form_id = 'BACKUPS_EMPLOYEE_FORM';




