insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_SHIFT_SEE_OWN_BY_DEPARTMENT', 'HRMS', 'See Own By Department', 8, (select id from permission where code = 'HRMS_SHIFT' limit 1), 'RECRUITMENT_SYSTEM');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_SEE_OWN_BY_DEPARTMENT', 'HRMS');