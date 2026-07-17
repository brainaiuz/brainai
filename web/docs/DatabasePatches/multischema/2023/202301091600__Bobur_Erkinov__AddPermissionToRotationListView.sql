insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_ROTATION_EXPORT', 'HRMS', 'Export', 3, (select id from permission where code = 'HRMS_ROTATION'), 'RECRUITMENT_SYSTEM');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_ROTATION_EXPORT', 'HRMS');