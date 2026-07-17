delete
from "anv".modelfield
where form_id = 'LOCATION_FORM'
  and field_id = 'OWNERS';
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('LOCATION_FORM', 'OWNERS', false, false, 'COL_2', 'GENERAL_DETAILS', 2);


delete
from "anv".form_property
where form_id = 'LOCATION_FORM';
insert into "311555".form_property (form_id, settingsjsondata)
values ('LOCATION_FORM', '[
    {
        "code": "NAME",
        "title": "Name",
        "widget": "TextBox",
        "changed": false,
        "disabled": false,
        "required": true,
        "aliasName": "NAME",
        "selectedId": null,
        "defaultValue": "",
        "systemRequired": true
    },
    {
        "code": "COUNTRY",
        "title": "Country",
        "widget": "LOOKUP",
        "changed": false,
        "disabled": false,
        "required": true,
        "aliasName": "COUNTRY",
        "selectedId": null,
        "defaultValue": "",
        "systemRequired": true
    },
    {
        "code": "CITY",
        "title": "City",
        "widget": "TextArea",
        "changed": false,
        "disabled": false,
        "required": true,
        "aliasName": "CITY",
        "selectedId": null,
        "defaultValue": "",
        "systemRequired": true
    },
    {
        "code": "FAX",
        "title": "Fax",
        "widget": "TextBox",
        "changed": false,
        "disabled": false,
        "required": false,
        "aliasName": "FAX",
        "selectedId": null,
        "defaultValue": ""
    },
    {
        "code": "EMAIL",
        "title": "Email",
        "widget": "TextBox",
        "changed": false,
        "disabled": false,
        "required": false,
        "aliasName": "EMAIL",
        "selectedId": null,
        "defaultValue": ""
    },
    {
        "code": "STATE",
        "title": "State",
        "widget": "UNKNOWN",
        "changed": false,
        "disabled": false,
        "required": false,
        "aliasName": "STATE",
        "selectedId": null,
        "defaultValue": ""
    },
    {
        "code": "PHONE",
        "title": "Phone",
        "widget": "TextBox",
        "changed": false,
        "disabled": false,
        "required": false,
        "aliasName": "PHONE",
        "selectedId": null,
        "defaultValue": ""
    },
    {
        "code": "ZIP_CODE",
        "title": "Zip code",
        "widget": "TextBox",
        "changed": false,
        "disabled": false,
        "required": true,
        "aliasName": "ZIP_CODE",
        "selectedId": null,
        "defaultValue": ""
    },
    {
        "code": "CITY_DESTRICT",
        "title": "District",
        "widget": "UNKNOWN",
        "changed": false,
        "disabled": false,
        "required": false,
        "aliasName": "CITY_DESTRICT",
        "selectedId": null,
        "defaultValue": ""
    },
    {
        "code": "PARENT",
        "title": "Parent",
        "widget": "LOOKUP",
        "changed": false,
        "disabled": false,
        "required": false,
        "aliasName": "PARENT",
        "selectedId": null,
        "defaultValue": ""
    },
    {
        "code": "OWNERS",
        "title": "Owners",
        "widget": "LOOKUP",
        "changed": false,
        "disabled": false,
        "required": false,
        "aliasName": "OWNERS",
        "selectedId": null,
        "defaultValue": ""
    }
]');
