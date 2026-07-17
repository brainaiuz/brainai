delete from "anv".form_property where form_id = 'OVERTIME_FORM';
insert into "anv".form_property (form_id, settingsjsondata)
values ('OVERTIME_FORM',
        '[
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
  }]');



delete from "anv".model where formid = 'OVERTIME_FORM';
insert into "anv".model (formid, title, viewname, active)
values ('OVERTIME_FORM', 'Overtime', 'Overtime', true);

delete from "anv".customformsection where form_id = 'OVERTIME_FORM';
insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('OVERTIME_FORM', 'BASIC_INFORMATION', 0, true);
insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('OVERTIME_FORM', 'ITEMTABLE', 1, true);

delete from "anv".modelfield where form_id = 'OVERTIME_FORM';
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('OVERTIME_FORM', 'CATEGORY', true, false, 'COL_3', 'BASIC_INFORMATION', 0);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('OVERTIME_FORM', 'DATE', true, false, 'COL_3', 'BASIC_INFORMATION', 1);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('OVERTIME_FORM', 'DEPARTMENT', true, false, 'COL_3', 'BASIC_INFORMATION', 2);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('OVERTIME_FORM', 'APPROVERS', true, false, 'COL_3', 'BASIC_INFORMATION', 3);
insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('OVERTIME_FORM', 'ITEMTABLE', true, false, 'COL_1', 'ITEMTABLE', 0);