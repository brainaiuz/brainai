delete  from "anv".form_property where form_id ='HRMS_COMPANY_NEWS_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('HRMS_COMPANY_NEWS_FORM',
        '[
  {
    "code": "NEWS_SUBJECT",
    "title": "Subject",
    "aliasName": "NEWS_SUBJECT",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "NEWS_SHORT_DESCRIPTION",
    "title": "Short Description",
    "aliasName": "NEWS_SHORT_DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "Unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "NEWS_FULL_TEXT",
    "title": "Full Text",
    "aliasName": "NEWS_FULL_TEXT",
    "changed": false,
    "required": true,
    "widget": "Unknown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "NEWS_AUTHOR",
    "title": "Author",
    "aliasName": "NEWS_AUTHOR",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "NEWS_PUBLISH_DATE",
    "title": "Date",
    "aliasName": "NEWS_PUBLISH_DATE",
    "changed": false,
    "required": false,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "NEWS_VISIBILITY",
    "title": "Visibility",
    "aliasName": "NEWS_VISIBILITY",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "NEWS_CATEGORIES",
    "title": "Category",
    "aliasName": "NEWS_CATEGORIES",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LOCATION_FIELD",
    "title": "Location",
    "aliasName": "LOCATION_FIELD",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
]');