
delete
from "anv".form_property
where form_id = 'POSITION_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('POSITION_FORM', '[
      {
       "code": "POSITION_TITLE",
       "title": "Position Title",
       "aliasName": "POSITION_TITLE",
       "changed": false,
       "required": true,
       "widget": "TextBox",
       "selectedId": null,
       "defaultValue": "",
       "disabled": false,
       "systemRequired": true
  },
          {
       "code": "POSITION_CODE",
       "title": "Position Code",
       "aliasName": "POSITION_CODE",
       "changed": false,
       "required": false,
       "widget": "TextArea",
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
       "code": "AVAILABLE",
       "title": "Available",
       "aliasName": "AVAILABLE",
       "changed": false,
       "required": false,
       "widget": "DatePicker",
       "selectedId": null,
       "defaultValue": "",
       "disabled": false
  },
          {
       "code": "END_DATE",
       "title": "End Date",
       "aliasName": "END_DATE",
       "changed": false,
       "required": false,
       "widget": "DatePicker",
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
       "widget": "DropDown",
       "selectedId": null,
       "defaultValue": "",
       "disabled": false
  },
          {
       "code": "REG_TEMP",
       "title": "Reg/Temp",
       "aliasName": "REG_TEMP",
       "changed": false,
       "required": false,
       "widget": "DropDown",
       "selectedId": null,
       "defaultValue": "",
       "disabled": false
  },
          {
       "code": "FULL_PART_TIME",
       "title": "Full/Part Time",
       "aliasName": "FULL_PART_TIME",
       "changed": false,
       "required": true,
       "widget": "DropDown",
       "selectedId": null,
       "defaultValue": "",
       "disabled": false
  },
          {
       "code": "DEPARTMENT",
       "title": "Department",
       "aliasName": "DEPARTMENT",
       "changed": false,
       "required": false,
       "widget": "DropDown",
       "selectedId": null,
       "defaultValue": "",
       "disabled": false,
	   "systemRequired": false

  },
          {
       "code": "ESTIBLISHED",
       "title": "Established",
       "aliasName": "ESTIBLISHED",
       "changed": false,
       "required": false,
       "widget": "DatePicker",
       "selectedId": null,
       "defaultValue": "",
       "disabled": false
  },{
       "code": "LOCATION",
       "title": "Location",
       "aliasName": "LOCATION",
       "changed": false,
       "required": false,
       "widget": "DropDown",
       "selectedId": null,
       "defaultValue": "",
       "disabled": false,
	   "systemRequired": false

  }, {
       "code": "COUNT",
       "title": "Count",
       "aliasName": "COUNT",
       "changed": false,
       "required": false,
       "widget": "TextBox",
       "selectedId": null,
       "defaultValue": "",
       "disabled": false,
       "systemRequired": false
  }, {
       "code": "TYPE",
       "title": "Type",
       "aliasName": "TYPE",
       "changed": false,
       "required": false,
       "widget": "LOOKUP",
       "selectedId": null,
       "defaultValue": "",
       "disabled": false,
       "systemRequired": false
  }
]');
