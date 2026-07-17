delete
from permission
where code in ('BACKUP_EMPLOYEE_SEE_ALL', 'BACKUP_EMPLOYEE_SEE_BY_TYPE');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('BACKUP_EMPLOYEE_SEE_ALL', 'HRMS', 'See All', 5,
        (select id from permission where code = 'HRMS_BACKUPS_EMPLOYEE_LIST'), 'HRMS_MODULE'),
       ('BACKUP_EMPLOYEE_SEE_BY_TYPE', 'HRMS', 'See By Type', 6,
        (select id from permission where code = 'HRMS_BACKUPS_EMPLOYEE_LIST'), 'HRMS_MODULE');

delete
from "anv".permission_context
where permissioncode in ('BACKUP_EMPLOYEE_SEE_ALL', 'BACKUP_EMPLOYEE_SEE_BY_TYPE');
insert into "anv".permission_context (permissioncode, contextcode)
values ('BACKUP_EMPLOYEE_SEE_ALL', 'HRMS'),
       ('BACKUP_EMPLOYEE_SEE_BY_TYPE', 'HRMS');