delete  from "anv".form_property where form_id ='COURSE_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('COURSE_FORM',
        '[

  {
    "code": "SUBJECT",
    "title": "SUBJECT",
    "aliasName": "SUBJECT",
    "changed": false,
    "required": true,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "CODE",
    "title": "Course Code",
    "aliasName": "CODE",
    "changed": false,
    "required": true,
    "widget": "Numbering",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "NAME",
    "title": "NAME",
    "widget": "TextBox",
    "changed": false,
    "disabled": false,
    "required": true,
    "aliasName": "NAME",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "DESCRIPTION",
    "title": "DESCRIPTION",
    "widget": "TextArea2",
    "changed": false,
    "disabled": false,
    "required": false,
    "aliasName": "DESCRIPTION",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "DURATION",
    "title": "DURATION",
    "aliasName": "DURATION",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false

  },
  {
    "code": "VALIDITY",
    "title": "VALIDITY",
    "aliasName": "VALIDITY",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PRICEPERSTUDENT",
    "title": "PRICE PER STUDENT",
    "aliasName": "PRICEPERSTUDENT",
    "changed": false,
    "required": true,
    "widget": "MultiTableNewUI",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PREREQUISITE",
    "title": "PREREQUISITE",
    "aliasName": "PREREQUISITE",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "OTHER_PREREQUISITE",
    "title": "OTHER PREREQUISITE",
    "aliasName": "OTHER_PREREQUISITE",
    "changed": false,
    "required": false,
    "widget": "TextArea2",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "COURSEREQUIREMENTS",
    "title": "COURSE REQUIREMENTS",
    "aliasName": "COURSEREQUIREMENTS",
    "changed": false,
    "required": true,
    "widget": "CustomList",
    "selectedId": null,
    "defaultValue": false,
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "INSTRUCTORS",
    "title": "INSTRUCTORS",
    "aliasName": "INSTRUCTORS",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": false,
    "disabled": false,
    "systemRequired": true
  }

]');

delete  from "anv".form_property where form_id ='ADD_COURSE_SUBJECT_VIEW';
insert into "anv".form_property (form_id, settingsjsondata)
values ('ADD_COURSE_SUBJECT_VIEW',
        '[
  {
    "code": "NAME",
    "title": "NAME",
    "aliasName": "NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "SUBJECT_PARENT",
    "title": "SUBJECT_PARENT",
    "aliasName": "SUBJECT_PARENT",
    "changed": false,
    "required": true,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DESCRIPTION",
    "title": "DESCRIPTION",
    "aliasName": "DESCRIPTION",
    "changed": false,
    "required": true,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');

delete  from "anv".form_property where form_id ='INSTRUCTOR_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('INSTRUCTOR_FORM',
        '[

  {
    "code": "FIRST_NAME",
    "title": "FIRST NAME",
    "aliasName": "FIRST_NAME",
    "changed": false,
    "required": true,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LAST_NAME",
    "title": "LAST NAME",
    "aliasName": "LAST_NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "MIDDLE_NAME",
    "title": "MIDDLE NAME",
    "widget": "TextBox",
    "changed": false,
    "disabled": false,
    "required": true,
    "aliasName": "MIDDLE_NAME",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "BIRTH_DAY",
    "title": "BIRTH DAY",
    "widget": "UNKNOWN",
    "changed": false,
    "disabled": false,
    "required": false,
    "aliasName": "BIRTH_DAY",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "GENDER",
    "title": "GENDER",
    "aliasName": "GENDER",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false

  },
  {
    "code": "MARTIAL_STATUS",
    "title": "MARTIAL STATUS",
    "aliasName": "MARTIAL_STATUS",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LANGUAGE",
    "title": "LANGUAGE",
    "aliasName": "LANGUAGE",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LOCATION",
    "title": "LOCATION",
    "aliasName": "LOCATION",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "EMAIL",
    "title": "OTHER EMAIL",
    "aliasName": "EMAIL",
    "changed": false,
    "required": true,
    "widget": "MULTITABLE",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PHONE",
    "title": "PHONE",
    "aliasName": "PHONE",
    "changed": false,
    "required": false,
    "widget": "MULTITABLE",
    "selectedId": null,
    "defaultValue": false,
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "IM_ADDRESS",
    "title": "IM_ADDRESS",
    "aliasName": "IM_ADDRESS",
    "changed": false,
    "required": false,
    "widget": "MULTITABLE",
    "selectedId": null,
    "defaultValue": false,
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "WEB_ADDRESS",
    "title": "WEB_ADDRESS",
    "aliasName": "WEB_ADDRESS",
    "changed": false,
    "required": false,
    "widget": "MULTITABLE",
    "selectedId": null,
    "defaultValue": false,
    "disabled": false,
    "systemRequired": true
  }, {
    "code": "COURSES",
    "title": "COURSES",
    "aliasName": "COURSES",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": false,
    "disabled": false,
    "systemRequired": true
  },
   {
    "code": "ADDRESS",
    "title": "ADDRESS",
    "aliasName": "ADDRESS",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": false,
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "ACCOUNT_STATUS",
    "title": "ACCOUNT_STATUS",
    "aliasName": "ACCOUNT_STATUS",
    "changed": false,
    "required": false,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": false,
    "disabled": false,
    "systemRequired": false
  },{
    "code": "ATTACHMENTS",
    "title": "ATTACHMENTS",
    "aliasName": "ATTACHMENTS",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": false,
    "disabled": false,
    "systemRequired": false
  }
]');


delete  from "anv".form_property where form_id ='BOOKING_ITEMS_ADD_VIEW';
insert into "anv".form_property (form_id, settingsjsondata)
values ('BOOKING_ITEMS_ADD_VIEW',
        '[

  {
    "code": "DESCRIPTION",
    "title": "DESCRIPTION",
    "aliasName": "DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "NAME",
    "title": "NAME",
    "aliasName": "NAME",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "NUMBER",
    "title": "NUMBER",
    "widget": "Numbering",
    "changed": false,
    "disabled": false,
    "required": false,
    "aliasName": "NUMBER",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "CATEGORY",
    "title": "CATEGORY",
    "widget": "DataListBox",
    "changed": false,
    "disabled": false,
    "required": true,
    "aliasName": "CATEGORY",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "STATUS",
    "title": "STATUS",
    "aliasName": "STATUS",
    "changed": false,
    "required": false,
    "widget": "HTML",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false

  },
  {
    "code": "RESERVATION_HISTORY",
    "title": "RESERVATION_HISTORY STATUS",
    "aliasName": "RESERVATION_HISTORY",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "LOCATION_FIELD",
    "title": "LOCATION_FIELD",
    "aliasName": "LOCATION_FIELD",
    "changed": false,
    "required": true,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }

]');
delete  from "anv".form_property where form_id ='ATTENDENCE_SHEET_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('ATTENDENCE_SHEET_FORM',
        '[

  {
    "code": "LOCATION",
    "title": "LOCATION",
    "aliasName": "LOCATION",
    "changed": false,
    "required": true,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "DATE",
    "title": "DATE",
    "aliasName": "DATE",
    "changed": false,
    "required": true,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "INSTRUCTOR",
    "title": "INSTRUCTOR",
    "widget": "DataListBox",
    "changed": false,
    "disabled": false,
    "required": false,
    "aliasName": "INSTRUCTOR",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "COURSE",
    "title": "COURSE",
    "widget": "DataListBox",
    "changed": false,
    "disabled": false,
    "required": true,
    "aliasName": "COURSE",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "INSTRUCTOR_ATTENDENCE",
    "title": "INSTRUCTOR ATTENDENCE",
    "aliasName": "INSTRUCTOR_ATTENDENCE",
    "changed": false,
    "required": true,
    "widget": "KpiDataGrid",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false

  },
  {
    "code": "STUDENTS_ATTENDENCE",
    "title": "STUDENTS ATTENDENCE",
    "aliasName": "STUDENTS_ATTENDENCE",
    "changed": false,
    "required": true,
    "widget": "KpiDataGrid",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }

]');
