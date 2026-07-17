delete
from "anv".form_property
where form_id = 'CHART_OF_ACCOUNT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('CHART_OF_ACCOUNT_FORM',
        '[
  {

    "code": "CHART_ACCOUNT_TYPE",
    "title": "Type",
    "aliasName": "CHART_ACCOUNT_TYPE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
    },
    {
    "code": "CHART_ACCOUNT_CODE",
    "title": "Code",
    "aliasName": "CHART_ACCOUNT_CODE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
    },
    {
    "code": "CHART_ACCOUNT_NAME",
    "title": "Name",
    "aliasName": "CHART_ACCOUNT_NAME",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
    },
    {
    "code": "CHART_ACCOUNT_PARENT",
    "title": "Parent",
    "aliasName": "CHART_ACCOUNT_PARENT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
     "selectedId": null,
    "defaultValue": "",
    "disabled": false
    },
    {
    "code": "CHART_ACCOUNT_DESCRIPTION",
    "title": "Description",
    "aliasName": "CHART_ACCOUNT_DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextBox",
     "selectedId": null,
    "defaultValue": "",
    "disabled": false
    },
	{
    "code": "SHOW_IN_EXPENCE",
    "title": "Show in expense claim",
    "aliasName": "SHOW_IN_EXPENCE",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
	},
	{
    "code": "STATUS_OF_ACCOUNT",
    "title": "Active",
    "aliasName": "STATUS_OF_ACCOUNT",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
	},
	{
    "code": "ENABLE_PAYMENTS",
    "title": "Enable payments to this account",
    "aliasName": "ENABLE_PAYMENTS",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
	}

]');

delete
from "0".form_property
where form_id = 'CHART_OF_ACCOUNT_FORM';
insert into "0".form_property (form_id, settingsjsondata)
values ('CHART_OF_ACCOUNT_FORM',
        '[
  {

    "code": "CHART_ACCOUNT_TYPE",
    "title": "Type",
    "aliasName": "CHART_ACCOUNT_TYPE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
    },
    {
    "code": "CHART_ACCOUNT_CODE",
    "title": "Code",
    "aliasName": "CHART_ACCOUNT_CODE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
    },
    {
    "code": "CHART_ACCOUNT_NAME",
    "title": "Name",
    "aliasName": "CHART_ACCOUNT_NAME",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
    },
    {
    "code": "CHART_ACCOUNT_PARENT",
    "title": "Parent",
    "aliasName": "CHART_ACCOUNT_PARENT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
     "selectedId": null,
    "defaultValue": "",
    "disabled": false
    },
    {
    "code": "CHART_ACCOUNT_DESCRIPTION",
    "title": "Description",
    "aliasName": "CHART_ACCOUNT_DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextBox",
     "selectedId": null,
    "defaultValue": "",
    "disabled": false
    },
	{
    "code": "SHOW_IN_EXPENCE",
    "title": "Show in expense claim",
    "aliasName": "SHOW_IN_EXPENCE",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
	},
	{
    "code": "STATUS_OF_ACCOUNT",
    "title": "Active",
    "aliasName": "STATUS_OF_ACCOUNT",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
	},
	{
    "code": "ENABLE_PAYMENTS",
    "title": "Enable payments to this account",
    "aliasName": "ENABLE_PAYMENTS",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
	}

]');