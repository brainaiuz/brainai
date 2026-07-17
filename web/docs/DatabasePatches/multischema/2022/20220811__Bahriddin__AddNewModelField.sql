delete
from "anv".modelfield
where form_id = 'BACKUPS_EMPLOYEE_FORM'
  and field_id = 'APPROVERS';
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('BACKUPS_EMPLOYEE_FORM', 'APPROVERS', true, false, 'COL_3', 'BASIC_INFORMATION', 3);


delete
from "anv".form_property
where form_id = 'BACKUPS_EMPLOYEE_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('BACKUPS_EMPLOYEE_FORM',
        '[{
    "code": "EMPLOYEE",
    "title": "Employee",
    "aliasName": "EMPLOYEE",
    "changed": false,
    "required": true,
    "widget": "LOOKUP"
  },{
    "code": "EMPLOYEES",
    "title": "Employee",
    "aliasName": "EMPLOYEE",
    "changed": false,
    "required": true,
    "widget": "LOOKUP"
  },{
    "code": "APPROVERS",
    "title": "Approver",
    "aliasName": "APPROVERS",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }]');