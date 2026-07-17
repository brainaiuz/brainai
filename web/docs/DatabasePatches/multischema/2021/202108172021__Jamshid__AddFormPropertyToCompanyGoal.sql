delete from "anv".form_property where form_id = 'COMPANY_GOAL_FORM';

insert into "anv".form_property(form_id, settingsjsondata) values('COMPANY_GOAL_FORM', '[
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
    "code": "GOAL_RESOLVER",
    "title": "Manager",
    "aliasName": "MANAGER",
    "changed": false,
    "required": false,
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
  },
  {
    "code": "GOAL_OUTCOME",
    "title": "Outcome",
    "aliasName": "OUTCOME",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');