INSERT INTO "0".customformsection (active, custom, form_id, section, sorder) VALUES
    (true, false, 'HRMS_EMPLOYEE_FORM', 'EXPERIENCE',
     (select max(sorder) from "0".customformsection where form_id = 'HRMS_EMPLOYEE_FORM')+1);

insert into "0".modelfield
(form_id,fsection,section,fieldstyle,fullwidth,hide,columntype,mandatory,sectionstyle,widget,forder,field_id) values
    ('HRMS_EMPLOYEE_FORM',   'EXPERIENCE',      'EXPERIENCE',    'field',true,false,'COL_1',false,'','UNKNOWN',0,'EXPERIENCE');


delete from "0".itemtable_settings where section = 'EXPERIENCE_ITEM_TABLE';
insert into "0".itemtable_settings (section, settingsjsondata) values ('EXPERIENCE_ITEM_TABLE',
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
                                                                         "selected": false,
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
                                                                         "selected": false,
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
                                                                         "selected": false,
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
                                                                         "selected": false,
                                                                         "title": "Position",
                                                                         "width": 25
                                                                       }
                                                                     ]');