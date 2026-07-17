delete  from "anv".form_property where form_id ='ACTIVITY_VIEW_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('ACTIVITY_VIEW_FORM',
        '[
  {
    "code": "SUBJECT",
    "title": "Subject",
    "aliasName": "SUBJECT",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "WHEN",
    "title": "When",
    "aliasName": "WHEN",
    "changed": false,
    "required": true,
    "widget": "DateTime",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
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
    "code": "SHARED_WITH",
    "title": "Share",
    "aliasName": "SHARED_WITH",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
   {
    "code": "INVITATION_RESPONSE",
    "title": "Invitation response",
    "aliasName": "INVITATION_RESPONSE",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');