insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_SHIFT_SEE_ALL', 'HRMS', 'See All', 3, (select id from permission where code = 'HRMS_SHIFT'), 'RECRUITMENT_SYSTEM');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_SEE_ALL', 'HRMS');