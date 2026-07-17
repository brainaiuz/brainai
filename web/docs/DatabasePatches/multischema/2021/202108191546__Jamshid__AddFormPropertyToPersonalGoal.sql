delete
from "anv".form_property
where form_id = 'PERSONAL_GOAL_FORM';

insert into "anv".form_property(form_id, settingsjsondata)
values ('PERSONAL_GOAL_FORM', '[
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
	"code": "GOAL_ACTUAL",
    "title": "Actual",
    "aliasName": "ACTUAL",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
	"code": "GOAL_TARGET",
    "title": "Target",
    "aliasName": "Target",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "GOAL_PERSONAL_ASSINESS",
    "title": "Assignee",
    "aliasName": "ASSIGNEE",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "GOAL_STATUS",
    "title": "Status",
    "aliasName": "STATUS",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "GOAL_VALIDITY_PERIOD",
    "title": "Validity Period",
    "aliasName": "VALIDITY PERIOD",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "GOAL_MEASUREMENT_UNIT",
    "title": "Measurement Unit",
    "aliasName": "MEASUREMENT UNIT",
    "changed": false,
    "required": false,
    "widget": "TextBox",
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
  },
  {
    "code": "GOAL_ACTION_STEPS",
    "title": "Action Steps",
    "aliasName": "ACTION STEPS",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');