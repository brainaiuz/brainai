delete
from "anv".itemtable_settings
where section = 'ROTATION_ITEM_TABLE';
insert into "anv".itemtable_settings (section, settingsjsondata)
values ('ROTATION_ITEM_TABLE',
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
        "aliasName": "CURRENT_LOCATION",
        "changed": false,
        "clickable": false,
        "code": "CURRENT_LOCATION",
        "disabled": false,
        "hasDefault": false,
        "minValue": 0,
        "order": 1,
        "required": true,
        "selected": true,
        "title": "Current Location",
        "width": 15
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
        "aliasName": "NEW_LOCATION",
        "changed": false,
        "clickable": false,
        "code": "NEW_LOCATION",
        "disabled": false,
        "hasDefault": false,
        "minValue": 0,
        "order": 3,
        "required": false,
        "selected": true,
        "title": "New Location",
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