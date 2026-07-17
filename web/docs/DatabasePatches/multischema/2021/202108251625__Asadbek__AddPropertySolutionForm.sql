delete  from "anv".form_property where form_id ='SOLUTION_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('SOLUTION_FORM',
        '[
  {
    "code": "TITLE",
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
    "code": "ASSIGNEE",
    "title": "Assignee",
    "aliasName": "ASSIGNEE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "STATUS",
    "title": "Status",
    "aliasName": "STATUS",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_SOLUTION_QUESTION",
    "title": "Question",
    "aliasName": "CRM_SOLUTION_QUESTION",
    "changed": false,
    "required": true,
    "widget": "Unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CRM_SOLUTION_ANSWER",
    "title": "Answer",
    "aliasName": "CRM_SOLUTION_ANSWER",
    "changed": false,
    "required": true,
    "widget": "Unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },,
  {
    "code": "ATTACHMENTS",
    "title": "Attachments",
    "aliasName": "ATTACHMENTS",
    "changed": false,
    "required": false,
    "widget": "Unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
]');