insert into permission (code, context, name, parent, modulecode)
values ('SHOW_EXTERNAL_EMPLOYEE', 'HRMS', 'Show External Employee', (select id from permission where code = 'HRMS_EMPLOYEES'), 'HRMS_MODULE');
insert into "anv".permission_context (permissioncode, contextcode)
values ('SHOW_EXTERNAL_EMPLOYEE', 'HRMS');