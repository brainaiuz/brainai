delete from "anv".itemtable_settings where section = 'EXPERIENCE_ITEM_TABLE';
insert into "anv".itemtable_settings (section, settingsjsondata) values ('EXPERIENCE_ITEM_TABLE',
                                                                         '[
                                                                       {
                                                                         "aliasName": "HIRE_DATE",
                                                                         "changed": false,
                                                                         "clickable": true,
                                                                         "code": "HIRE_DATE",
                                                                         "disabled": false,
                                                                         "hasDefault": false,
                                                                         "minValue": 0,
                                                                         "order": 0,
                                                                         "required": false,
                                                                         "selected": true,
                                                                         "title": "Hire Date",
                                                                         "width": 25
                                                                       },{
                                                                           "aliasName": "RESIGN_DATE",
                                                                         "changed": false,
                                                                         "clickable": false,
                                                                         "code": "RESIGN_DATE",
                                                                         "disabled": false,
                                                                         "hasDefault": false,
                                                                         "minValue": 0,
                                                                         "order": 1,
                                                                         "required": false,
                                                                         "selected": true,
                                                                         "title": "Resign Date",
                                                                         "width": 25
                                                                       },
                                                                       {
                                                                         "aliasName": "INDUSTRY",
                                                                         "changed": false,
                                                                         "clickable": false,
                                                                         "code": "INDUSTRY",
                                                                         "disabled": false,
                                                                         "hasDefault": false,
                                                                         "minValue": 0,
                                                                         "order": 2,
                                                                         "required": false,
                                                                         "selected": true,
                                                                         "title": "Industry",
                                                                         "width": 25
                                                                       },
                                                                       {
                                                                         "aliasName": "POSITION",
                                                                         "changed": false,
                                                                         "clickable": false,
                                                                         "code": "POSITION",
                                                                         "disabled": false,
                                                                         "hasDefault": false,
                                                                         "minValue": 0,
                                                                         "order": 3,
                                                                         "required": false,
                                                                         "selected": true,
                                                                         "title": "Position",
                                                                         "width": 25
                                                                       }
                                                                     ]');