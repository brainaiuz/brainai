insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BRIGADA_PDF', 'HRMS', 'Pdf', 8, (select id from permission where code = 'HRMS_BRIGADA'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BRIGADA_EXCEL', 'HRMS', 'Excel', 9, (select id from permission where code = 'HRMS_BRIGADA'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_SHIFT_PDF', 'HRMS', 'Pdf', 5, (select id from permission where code = 'HRMS_SHIFT'), 'RECRUITMENT_SYSTEM');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BRIGADA_PDF', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BRIGADA_EXCEL', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_PDF', 'HRMS');