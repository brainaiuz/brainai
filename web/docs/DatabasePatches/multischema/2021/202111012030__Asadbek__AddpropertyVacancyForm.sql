
delete  from "anv".form_property where form_id ='VACANCY_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('VACANCY_FORM',
        '[
  {
    "code": "contractPeriod",
    "title": "Contract Period",
    "aliasName": "CONTRACT_PERIOD",
    "changed": false,
    "required": false,
    "widget": "CONTRACT_PERIOD",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
   {
    "code": "VACANCY_ATTACHMENTS",
    "title": "Attachments",
    "aliasName": "VACANCY_ATTACHMENTS",
    "changed": false,
    "required": false,
    "widget": "WIDGET",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "VACANCY_NOTES",
    "title": "Notes",
    "aliasName": "VACANCY_NOTES",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyNumberID",
    "title": "vacancy ID",
    "aliasName": "VACANCY_NUMBER",
    "changed": false,
    "required": true,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyManager",
    "title": "Manager",
    "aliasName": "MANAGER",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyBackupManager",
    "title": "Backup Manager",
    "aliasName": "BACKUP_MANAGER",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyPosition",
    "title": "Position",
    "aliasName": "POSITION",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyLocation",
    "title": "Location",
    "aliasName": "LOCATION",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyJobTitle",
    "title": "Job Title",
    "aliasName": "JOB_TITLE",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "PROJECT",
    "title": "Project",
    "aliasName": "PROJECT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "COUNTRY",
    "title": "Country",
    "aliasName": "COUNTRY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "COUNTRYEMBASSY",
    "title": "Embassy Only",
    "aliasName": "COUNTRYEMBASSY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "gender",
    "title": "Gender",
    "aliasName": "GENDER",
    "changed": false,
    "required": false,
    "widget": "WIDGET",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "proposedSalary",
    "title": "Proposed Salary",
    "aliasName": "PROPOSED_SALARY",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "jobRequirement",
    "title": "Job requirements",
    "aliasName": "JOB_REQUIREMENT",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyType",
    "title": "Vacancy Type",
    "aliasName": "VACANCY_TYPE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "religion",
    "title": "Religion",
    "aliasName": "RELIGION",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyDescription",
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
    "code": "vacancyStartDate",
    "title": "Start Date",
    "aliasName": "START_DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyEndDate",
    "title": "End Date",
    "aliasName": "END_DATE",
    "changed": false,
    "required": true,
    "widget": "DatePicker",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyStatus",
    "title": "Status",
    "aliasName": "STATUS",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyPlaceCount",
    "title": "Vacant Place Count",
    "aliasName": "VACANCY_PLACE_COUNT",
    "changed": false,
    "required": false,
    "widget": "TextBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyJobType",
    "title": "Job Type",
    "aliasName": "JOB_TYPE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyJobFamily",
    "title": "Job Family",
    "aliasName": "JOB_FAMILY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyResponsibilities",
    "title": "Responsibilities",
    "aliasName": "RESPONSIBILITIES",
    "changed": false,
    "required": false,
    "widget": "TextArea",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "vacancyRequiredDegree",
    "title": "Required Degree",
    "aliasName": "REQUIRED_DEGREE",
    "changed": false,
    "required": false,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },
  {
    "code": "department",
    "title": "Department",
    "aliasName": "DEPARTMENT",
    "changed": false,
    "required": true,
    "widget": "LOOKUP",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }
]')