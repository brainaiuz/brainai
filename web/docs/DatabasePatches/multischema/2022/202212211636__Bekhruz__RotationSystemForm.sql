insert into "anv".customformsection
 (form_id,section,sorder,expanded) values
 ('ROTATION_FORM', 'INVOLVED_EMPLOYEES',   3, true );
insert into "anv".modelfield
(form_id,fsection,section,fieldstyle,fullwidth,hide,columntype,mandatory,sectionstyle,widget,forder,field_id) values
('ROTATION_FORM',   'INVOLVED_EMPLOYEES',      'INVOLVED_EMPLOYEES',    'field',true,false,'COL_1',false,'','UNKNOWN',0,'INVOLVED_EMPLOYEES');
delete from "anv".itemtable_settings where section = 'ROTATION_ITEM_TABLE';
insert into "anv".itemtable_settings (section, settingsjsondata) values ('ROTATION_ITEM_TABLE',
    '[
  {
    "aliasName": "EMPLOYEE",
    "changed": false,
    "clickable": true,
    "code": "EMPLOYEE",
    "disabled": false,
    "hasDefault": false,
    "minValue": 0,
    "order": 0,
    "required": true,
    "selected": true,
    "title": "Employee",
    "width": 25
  },
  {
      "aliasName": "CURRENT_DEPARTMENT",
    "changed": false,
    "clickable": false,
    "code": "CURRENT_DEPARTMENT",
    "disabled": false,
    "hasDefault": false,
    "minValue": 0,
    "order": 1,
    "required": true,
    "selected": true,
    "title": "Current Department",
    "width": 15
  },
  {
    "aliasName": "CURRENT_POSIITON",
    "changed": false,
    "clickable": false,
    "code": "CURRENT_POSIITON",
    "disabled": false,
    "hasDefault": false,
    "minValue": 0,
    "order": 2,
    "required": false,
    "selected": true,
    "title": "Current Position",
    "width": 15
  },
  {
      "aliasName": "NEW_DEPARTMENT",
    "changed": false,
    "clickable": false,
    "code": "NEW_DEPARTMENT",
    "disabled": false,
    "hasDefault": false,
    "minValue": 0,
    "order": 3,
    "required": false,
    "selected": true,
    "title": "New Department",
    "width": 15
  },
  {
    "aliasName": "NEW_POSITION",
    "changed": false,
    "clickable": false,
    "code": "NEW_POSITION",
    "disabled": false,
    "hasDefault": false,
    "minValue": 0,
    "order": 4,
    "required": false,
    "selected": true,
    "title": "New Position",
    "width": 15
  }
]');

update "anv".customformsection set section = 'INVOLVED_EMPLOYEES' where section = 'ITEMS' and form_id = 'ROTATION_FORM';
update "anv".modelfield set columntype = 'COL_1' where form_id = 'ROTATION_FORM' and field_id = 'NUMBER';
update "anv".modelfield set columntype = 'COL_2' where form_id = 'ROTATION_FORM' and field_id = 'DATE';
update "anv".modelfield set columntype = 'COL_3' where form_id = 'ROTATION_FORM' and field_id = 'APPROVERS';
delete from "anv".customformsection where section = 'ROTATION_DEPARTMENT' and form_id = 'ROTATION_FORM';
delete from "anv".customformsection where section = 'ROTATION_POSITION' and form_id = 'ROTATION_FORM';
delete from "anv".modelfield where field_id = 'EMPLOYEES' and form_id = 'ROTATION_FORM';
delete from "anv".modelfield where field_id = 'CURRENT_DEPARTMENT' and form_id = 'ROTATION_FORM';
delete from "anv".modelfield where field_id = 'NEW_DEPARTMENT' and form_id = 'ROTATION_FORM';
delete from "anv".modelfield where field_id = 'CURRENT_POSITION' and form_id = 'ROTATION_FORM';
delete from "anv".modelfield where field_id = 'NEW_POSITION' and form_id = 'ROTATION_FORM';