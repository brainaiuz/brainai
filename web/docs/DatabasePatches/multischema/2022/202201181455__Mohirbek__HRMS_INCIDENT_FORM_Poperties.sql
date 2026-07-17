
delete  from "anv".form_property where form_id ='INCIDENT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('INCIDENT_FORM',
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
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "DESCRIPTION",
    "title": "Description",
    "aliasName": "DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
	{
    "code": "RELATED_EMPLOYEES",
    "title": "Related Employee",
    "aliasName": "RELATED_EMPLOYEES",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
    {
    "code": "VISIBILITY",
    "title": "Visibility",
    "aliasName": "VISIBILITY",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PERIOD",
    "title": "Period",
    "aliasName": "PERIOD",
    "changed": false,
    "required": true,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
   },
  {
    "code": "STATUS",
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
    "code": "PRIORITY",
    "title": "Priority",
    "aliasName": "PRIORITY",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "REPORTED_BY",
    "title": "Reported by",
    "aliasName": "REPORTED_BY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false

  },
   {
    "code": "RESOLVER",
    "title": "Resolver/Owner",
    "aliasName": "RESOLVER",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }

]');