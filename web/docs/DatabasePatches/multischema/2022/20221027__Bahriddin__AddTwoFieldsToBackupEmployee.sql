delete
from "anv".modelfield
where form_id = 'BACKUPS_EMPLOYEE_FORM'
  and field_id = 'POSITION';

delete
from "anv".modelfield
where form_id = 'BACKUPS_EMPLOYEE_FORM'
  and field_id = 'DEPARTMENT';

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('BACKUPS_EMPLOYEE_FORM', 'POSITION', false, false, 'COL_3', 'BASIC_INFORMATION', 3);


insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('BACKUPS_EMPLOYEE_FORM', 'DEPARTMENT', false, false, 'COL_3', 'BASIC_INFORMATION', 4);


update "anv".form_property
set settingsjsondata='[{
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
  },{
    "code": "DEPARTMENT",
    "title": "Department",
    "aliasName": "DEPARTMENT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP"
  },{
    "code": "POSITION",
    "title": "Position",
    "aliasName": "POSITION",
    "changed": false,
    "required": true,
    "widget": "LOOKUP"
  }]'
where form_id = 'BACKUPS_EMPLOYEE_FORM';
