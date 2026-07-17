insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_RECALL_EMPLOYEE', 'HRMS', 'Recall Employee',
        (select count(id) from permission where parent = (select id from permission where code = 'HRMS_LIVE_REQUEST')) +
        1,
        (select id from permission where code = 'HRMS_LIVE_REQUEST'), 'LEAVE_MANAGEMENT');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_RECALL_EMPLOYEE', 'HRMS');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_RECALL_EMPLOYEE', 'ALLOW', 'ADMIN');
