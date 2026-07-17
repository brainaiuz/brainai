insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_SHIFT_MONTH', 'HRMS', 'Monthly', (select sorder from permission where code ='HRMS_SHIFT' limit 1)+1, (select id from permission where code = 'HRMS_SHIFT' limit 1), 'RECRUITMENT_SYSTEM');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_MONTH', 'HRMS');

insert into "0".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_MONTH', 'HRMS');


insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_SHIFT_CUSTOM', 'HRMS', 'Custom', (select sorder from permission where code ='HRMS_SHIFT' limit 1)+1, (select id from permission where code = 'HRMS_SHIFT' limit 1), 'RECRUITMENT_SYSTEM');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_CUSTOM', 'HRMS');

insert into "0".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_CUSTOM', 'HRMS');


insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_SHIFT_WEEK', 'HRMS', 'Weekly', (select sorder from permission where code ='HRMS_SHIFT' limit 1)+1, (select id from permission where code = 'HRMS_SHIFT' limit 1), 'RECRUITMENT_SYSTEM');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_WEEK', 'HRMS');

insert into "0".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_WEEK', 'HRMS');


