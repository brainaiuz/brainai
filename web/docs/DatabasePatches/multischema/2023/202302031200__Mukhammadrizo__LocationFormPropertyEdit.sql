delete from "anv".form_property where form_id = 'LOCATION_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('LOCATION_FORM', '[
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
    "code": "COUNTRY",
    "title": "Country",
    "aliasName": "COUNTRY",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true

  },
  {
    "code": "CITY",
    "title": "City",
    "aliasName": "CITY",
    "changed": false,
    "required": true,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "FAX",
    "title": "Fax",
    "aliasName": "FAX",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
   {
    "code": "EMAIL",
    "title": "Email",
    "aliasName": "EMAIL",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "STATE",
    "title": "State",
    "aliasName": "STATE",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PHONE",
    "title": "Phone",
    "aliasName": "PHONE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "ZIP_CODE",
    "title": "Zip code",
    "aliasName": "ZIP_CODE",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CITY_DESTRICT",
    "title": "District",
    "aliasName": "CITY_DESTRICT",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
]');