insert into "anv".genericsettings (key, value)
values ('ENABLE_AI_POSITION_FILL', 'YES');

insert into "anv".modelfield (form_id, field_id, mandatory, columntype, fsection, forder, hide)
values ('POSITION_FORM', 'SALARY_BASIS', false, 'COL_3', 'BASIC_INFORMATION', 3, false);

INSERT INTO "anv".customformsection (custom, expanded, form_id, ispagination, label, section, sorder,
                                        customformlocalizationid)
VALUES (false, true, 'POSITION_FORM', null, null, 'KNOWLEDE_AND_SKILLS', 3, null);



update "anv".form_property
set settingsjsondata ='[
  {
    "code": "POSITION_TITLE",
    "title": "Position Title",
    "aliasName": "POSITION_TITLE",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true,
    "approvalRelated": false
  },{
    "code": "SALARY_BASIS",
    "title": "Salary Basis",
    "aliasName": "SALARY_BASIS",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "POSITION_CODE",
    "title": "Position Code",
    "aliasName": "POSITION_CODE",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": true,
    "approvalRelated": false
  },
  {
    "code": "DESCRIPTION",
    "title": "Description",
    "aliasName": "DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextArea2",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "AVAILABLE",
    "title": "Available",
    "aliasName": "AVAILABLE",
    "changed": false,
    "required": false,
    "widget": "DatePicker",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "END_DATE",
    "title": "End Date",
    "aliasName": "END_DATE",
    "changed": false,
    "required": false,
    "widget": "DatePicker",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "STATUS",
    "title": "Status",
    "aliasName": "STATUS",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "REG_TEMP",
    "title": "Reg/Temp",
    "aliasName": "REG_TEMP",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "FULL_PART_TIME",
    "title": "Full/Part Time",
    "aliasName": "FULL_PART_TIME",
    "changed": false,
    "required": true,
    "widget": "DropDown",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "DEPARTMENT",
    "title": "Department",
    "aliasName": "DEPARTMENT",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "ESTIBLISHED",
    "title": "Established",
    "aliasName": "ESTIBLISHED",
    "changed": false,
    "required": false,
    "widget": "DatePicker",
    "defaultValue": "",
    "disabled": false,
    "roleEdit": [],
    "systemRequired": false,
    "minChar": "",
    "information": false,
    "informationText": "",
    "approvalRelated": false
  },
  {
    "code": "LOCATION",
    "title": "Location",
    "aliasName": "LOCATION",
    "changed": false,
    "required": false,
    "widget": "DropDown",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "COUNT",
    "title": "Count",
    "aliasName": "COUNT",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "TYPE",
    "title": "Type",
    "aliasName": "TYPE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
  {
    "code": "COEFFICENT",
    "title": "Coefficient",
    "aliasName": "COEFFICENT",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },{
    "code": "KNOWLEDGE",
    "title": "Knowledge",
    "aliasName": "KNOWLEDGE",
    "changed": false,
    "required": false,
    "widget": "TextArea2",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },  {
    "code": "PERSONAL_QUALITIES",
    "title": "Personal Qualities",
    "aliasName": "PERSONAL_QUALITIES",
    "changed": false,
    "required": false,
    "widget": "TextArea2",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },  {
    "code": "POSITION_RESPONSIBILITIES",
    "title": "Responsibilities",
    "aliasName": "POSITION_RESPONSIBILITIES",
    "changed": false,
    "required": false,
    "widget": "TextArea2",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },  {
    "code": "POSITION_DESCRIPTION",
    "title": "Description",
    "aliasName": "POSITION_DESCRIPTION",
    "changed": false,
    "required": false,
    "widget": "TextArea2",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },  {
    "code": "JOB_REQUIREMENT",
    "title": "Job Requirements",
    "aliasName": "JOB_REQUIREMENT",
    "changed": false,
    "required": false,
    "widget": "TextArea2",
    "defaultValue": "",
    "disabled": false,
    "systemRequired": false,
    "approvalRelated": false
  },
]'
where form_id = 'POSITION_FORM';


