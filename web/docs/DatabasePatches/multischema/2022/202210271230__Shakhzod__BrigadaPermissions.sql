insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BRIGADA_SEE_BY_DEPARTMENT', 'HRMS', 'See Department', 6, (select id from permission where code = 'HRMS_BRIGADA'), 'RECRUITMENT_SYSTEM');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BRIGADA_SEE_BY_DEPARTMENT', 'HRMS');

insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BRIGADA_SEE_OWN', 'HRMS', 'See Own', 7, (select id from permission where code = 'HRMS_BRIGADA'), 'RECRUITMENT_SYSTEM');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BRIGADA_SEE_OWN', 'HRMS');