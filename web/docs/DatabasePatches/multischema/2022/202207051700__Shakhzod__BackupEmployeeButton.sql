insert into permission (code, context, name, parent, modulecode)
values ('BACKUP_EMPLOYEE_BUTTON', 'HRMS', 'Backup Button',
        (select id from permission where code = 'HRMS_LIVE_REQUEST'), 'LEAVE_MANAGEMENT');

insert into "anv".permission_context (permissioncode, contextcode)
values ('BACKUP_EMPLOYEE_BUTTON', 'HRMS');