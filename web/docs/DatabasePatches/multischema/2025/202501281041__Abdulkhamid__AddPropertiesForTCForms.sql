delete  from "anv".form_property where form_id ='STUDENT_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('STUDENT_FORM',
        '[

  {
    "code": "studentFirstName",
    "title": "First Name",
    "aliasName": "studentFirstName",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "studentLastName",
    "title": "Last Name",
    "aliasName": "studentLastName",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "studentEMail",
    "title": "Email",
    "widget": "TextBox",
    "changed": false,
    "disabled": false,
    "required": false,
    "aliasName": "studentEMail",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "studentPhone",
    "title": "Phone",
    "widget": "TextBox",
    "changed": false,
    "disabled": false,
    "required": false,
    "aliasName": "studentPhone",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "studentGender",
    "title": "Gender",
    "aliasName": "studentGender",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false

  },
  {
    "code": "studentCustomer",
    "title": "Customer",
    "aliasName": "studentCustomer",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "studentDateOfBirth",
    "title": "Date Of Birth",
    "aliasName": "studentDateOfBirth",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "studentCompanyEmployeeNumber",
    "title": "Company Employee Number",
    "aliasName": "studentCompanyEmployeeNumber",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "studentDepartmentCode",
    "title": "Number",
    "aliasName": "studentDepartmentCode",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "studentSafetyPP",
    "title": "Residence #",
    "aliasName": "studentSafetyPP",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": false,
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "refIndNumber",
    "title": "Ref Ind #",
    "aliasName": "refIndNumber",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": false,
    "disabled": false,
    "systemRequired": true
  },
  {
    "code": "studentStatus",
    "title": "Status",
    "aliasName": "studentStatus",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "nationality",
    "title": "Nationality",
    "aliasName": "nationality",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }

]');

delete  from "anv".form_property where form_id ='SCHEDULED_COURSE_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('SCHEDULED_COURSE_FORM',
        '[
  {
    "code": "location",
    "title": "Location",
    "aliasName": "location",
    "changed": false,
    "required": true,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "course",
    "title": "Course",
    "aliasName": "course",
    "changed": false,
    "required": true,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "instructor",
    "title": "Instructor",
    "aliasName": "instructor",
    "changed": false,
    "required": true,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "language",
    "title": "Language",
    "aliasName": "language",
    "changed": false,
    "required": true,
    "widget": "DataListBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "assessor",
    "title": "Assessor",
    "widget": "DataListBox",
    "changed": false,
    "disabled": false,
    "required": true,
    "aliasName": "assessor",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "numberOfSeats",
    "title": "Number Of Seats",
    "widget": "TextBox",
    "changed": false,
    "disabled": false,
    "required": true,
    "aliasName": "numberOfSeats",
    "selectedId": null,
    "defaultValue": ""
  },
  {
    "code": "duration",
    "title": "Duration",
    "aliasName": "duration",
    "changed": false,
    "required": false,
    "widget": "TextArea2",
    "selectedId": null,
    "defaultValue": "",
    "disabled": true

  },
  {
    "code": "overtime",
    "title": "overtime",
    "aliasName": "overtime",
    "changed": false,
    "required": true,
    "widget": "KpiCheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "startDate",
    "title": "Start Date",
    "aliasName": "startDate",
    "changed": false,
    "required": true,
    "widget": "KpiDatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "courserequirements",
    "title": "courserequirements",
    "aliasName": "courserequirements",
    "changed": false,
    "required": false,
    "widget": "FlowPanel",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]');