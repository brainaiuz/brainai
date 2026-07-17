UPDATE "anv".form_property
SET settingsjsondata = '[
  {
    "code": "CATEGORY",
    "title": "Category",
    "aliasName": "CATEGORY",
    "changed": false,
    "required": false,
    "widget": "LOOKUP"
  },{
    "code": "DATE",
    "title": "Date",
    "aliasName": "DATE",
    "changed": false,
    "required": false,
    "widget": "DatePicker"
  },{
    "code": "DEPARTMENT",
    "title": "Department",
    "aliasName": "DEPARTMENT",
    "changed": false,
    "required": false,
    "widget": "LOOKUP"
  },{
    "code": "APPROVERS",
    "title": "Approver",
    "aliasName": "APPROVERS",
    "changed": false,
    "required": false,
    "widget": "UNKNOWN",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  },{
    "code": "APPLY_SUB_DEPARTMENT",
    "title": "Show sub department employees",
    "aliasName": "APPLY_SUB_DEPARTMENT",
    "changed": false,
    "required": false,
    "widget": "CheckBox",
    "selectedId": null,
    "defaultValue": "",
    "disabled": false
  }]'
where form_id = 'OVERTIME_FORM';

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('OVERTIME_FORM', 'APPLY_SUB_DEPARTMENT', false, false, 'COL_1', 'BASIC_INFORMATION', 2);