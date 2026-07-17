delete
from "anv".itemtable_settings
where section = 'ITEMTABLE';
insert into "anv".itemtable_settings (section, settingsjsondata)
values ('ITEMTABLE',
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
            "aliasName": "DATE",
            "changed": false,
            "clickable": false,
            "code": "DATE",
            "disabled": false,
            "hasDefault": false,
            "minValue": 0,
            "order": 1,
            "required": true,
            "selected": true,
            "title": "Date",
            "width": 15
        },
        {
            "aliasName": "OVERTIME_HOURS",
            "changed": false,
            "clickable": false,
            "code": "OVERTIME_HOURS",
            "disabled": false,
            "hasDefault": false,
            "minValue": 0,
            "order": 3,
            "required": false,
            "selected": true,
            "title": "Overtime hours",
            "width": 15
        },
         {
            "aliasName": "OVERTIME_CATEGORY",
            "changed": false,
            "clickable": false,
            "code": "OVERTIME_CATEGORY",
            "disabled": false,
            "hasDefault": false,
            "minValue": 0,
            "order": 3,
            "required": false,
            "selected": true,
            "title": "Overtime Category",
            "width": 15
        }
    ]');
