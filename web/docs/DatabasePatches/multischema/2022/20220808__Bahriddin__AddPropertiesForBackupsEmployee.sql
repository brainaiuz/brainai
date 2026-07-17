delete
from "anv".container_item
where propertyid = (select id from "anv".property where objectname = 'backupsEmployee' limit 1);

delete
from "anv".property
where objectname = 'backupsEmployee';


delete
from "anv".modelfield
where form_id = 'BACKUPS_EMPLOYEE_FORM'
  and field_id = 'BACKUPS_EMPLOYEE';

delete
from "anv".modelfield
where form_id = 'BACKUPS_EMPLOYEE_FORM'
  and field_id = 'BACKUP_EMPLOYEE';


delete
from "anv".form_property
where form_id = 'BACKUPS_EMPLOYEE_FORM';


delete
from "anv".model
where formid = 'BACKUPS_EMPLOYEE_FORM';

delete
from "anv".customformsection
where form_id = 'BACKUPS_EMPLOYEE_FORM';


insert into "anv".property (objectName, defaultName, singular, plural, shortcut, moduleCode, isactive)
values ('backupsEmployee', 'Backup Employee', 'Backup Employee', 'Backup Employees', 'BCE', 'hrms', false);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('BACKUPS_EMPLOYEE_FORM', 'BACKUPS_EMPLOYEE', false, false, 'COL_3', 'BASIC_INFORMATION', 1);

insert into "anv".modelfield (form_id, field_id, mandatory, hide, columntype, fsection, forder)
values ('BACKUPS_EMPLOYEE_FORM', 'BACKUP_EMPLOYEE', false, false, 'COL_3', 'BASIC_INFORMATION', 2);


insert into "anv".container_item(moduleID, propertyID, containerId, sorder, moduleCode)
values ((select id from "anv".mymodule where code = 'HRMS_MODULE' limit 1),
       (select id from "anv".property where objectName='backupsEmployee' limit 1),
       (select id from "anv".container where code='hrmsMain' limit 1), 17, 'hrms');



insert into "anv".form_property (form_id, settingsjsondata)
values ('BACKUPS_EMPLOYEE_FORM',
        '[{
    "code": "EMPLOYEE",
    "title": "Employee",
    "aliasName": "EMPLOYEE",
    "changed": false,
    "required": true,
    "widget": "LOOKUP"
  },{
    "code": "EMPLOYEE",
    "title": "Employee",
    "aliasName": "EMPLOYEE",
    "changed": false,
    "required": true,
    "widget": "LOOKUP"
  }]');



insert into "anv".model (formid, title, viewname, active)
values ('BACKUPS_EMPLOYEE_FORM', 'Backup Employee', 'BackupsEmployee', true);

insert into "anv".customformsection (form_id, section, sorder, expanded)
values ('BACKUPS_EMPLOYEE_FORM', 'BASIC_INFORMATION', 0, true);