insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BRIGADA_SEE_ALL', 'HRMS', 'See All', 5, (select id from permission where code = 'HRMS_BRIGADA'), 'RECRUITMENT_SYSTEM');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BRIGADA_SEE_ALL', 'HRMS');