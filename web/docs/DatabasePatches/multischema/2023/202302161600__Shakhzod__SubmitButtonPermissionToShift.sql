insert into permission (code, context, name, parent, modulecode)
values ('HRMS_SHIFT_SUBMIT', 'HRMS', 'Submit', (select id from permission where code = 'HRMS_SHIFT'), 'RECRUITMENT_SYSTEM');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_SUBMIT', 'HRMS');