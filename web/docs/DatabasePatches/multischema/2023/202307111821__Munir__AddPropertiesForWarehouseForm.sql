delete
from "anv".form_property
where form_id = 'WAREHOUSE_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('WAREHOUSE_FORM',
        '[
      {
            "code": "NAME",
            "title": "Name",
            "aliasName": "NAME",
            "changed": false,
            "required": true,
            "widget": "TextBox",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
      },
      {     "code" : "OWNERS",
            "title": "Assignee",
            "aliasName": "OWNERS",
            "changed": false,
            "required": false,
            "widget": "Unknown",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
      },
      {     "code" : "NUMBER",
            "title": "Number",
            "aliasName": "Number",
            "changed": false,
            "required": true,
            "widget": "Unknown",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
      },
      {     "code" : "NOTES",
            "title": "Description",
            "aliasName": "NOTES",
            "changed": false,
            "required": false,
            "widget": "TextArea",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
      }
        ]');


delete
from "0".form_property
where form_id = 'WAREHOUSE_FORM';
insert into "0".form_property (form_id, settingsjsondata)
values ('WAREHOUSE_FORM',
        '[
      {
            "code": "NAME",
            "title": "Name",
            "aliasName": "NAME",
            "changed": false,
            "required": true,
            "widget": "TextBox",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
      },
      {     "code" : "OWNERS",
            "title": "Assignee",
            "aliasName": "OWNERS",
            "changed": false,
            "required": false,
            "widget": "Unknown",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
      },
      {     "code" : "NUMBER",
            "title": "Number",
            "aliasName": "Number",
            "changed": false,
            "required": true,
            "widget": "Unknown",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
      },
      {     "code" : "NOTES",
            "title": "Description",
            "aliasName": "NOTES",
            "changed": false,
            "required": false,
            "widget": "TextArea",
            "selectedId": null,
            "defaultValue": "",
            "disabled": false
      }
        ]');