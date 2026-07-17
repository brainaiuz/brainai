update "anv".form_property
set settingsjsondata = '[
  {
    "code": "GOAL_TITLE",
    "title": "Title",
    "aliasName": "TITLE",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "GOAL_PROORDEP",
    "title": "Department",
    "aliasName": "DEPARTMENT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },{
    "code": "LOCATION",
    "title": "Location",
    "aliasName": "LOCATION",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },{
    "code": "DEPARTMENT_GOAL_WEIGHT_WIDGET",
    "title": "Weight",
    "aliasName": "DEPARTMENT_GOAL_WEIGHT_WIDGET",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DEPARTMENT_TARGET_GOAL_WIDGET",
    "title": "Target And Actual",
    "aliasName": "DEPARTMENT_TARGET_GOAL_WIDGET",
    "changed": false,
    "required": true,
    "widget": "unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "GOAL_RESOLVER",
    "title": "Manager",
    "aliasName": "MANAGER",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "GOAL_START_DATE",
    "title": "Period",
    "aliasName": "Period",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "GOAL_DESCRIPTION",
    "title": "Description",
    "aliasName": "DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]'
where form_id='DEPARTMENT_GOAL_FORM';


insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('DEPARTMENT_GOAL_FORM', 'DEPARTMENT_TARGET_GOAL_WIDGET', false, 'COL_2', 'GOAL_DETAILS', 3);


insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('DEPARTMENT_GOAL_FORM', 'LOCATION', true, 'COL_1', 'GOAL_DETAILS', 1);


insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder)
values ('DEPARTMENT_GOAL_FORM', 'DEPARTMENT_GOAL_WEIGHT_WIDGET', true, 'COL_2', 'GOAL_DETAILS', 2);


delete
from "anv".modelfield
where form_id='DEPARTMENT_GOAL_FORM' and field_id in ('CRM_NOTE', 'GOAL_STATUS', 'COMPANY_GOAL', 'GOAL_ACTION_STEPS', 'GOAL_MEASUREMENT_UNIT', 'LINKS');

delete
from "anv".customformsection
where  form_id = 'DEPARTMENT_GOAL_FORM' and section in('NOTES','LINKS2','ADDITIONAL_INFORMATION');



