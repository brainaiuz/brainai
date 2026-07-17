insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_ROTATION', 'HRMS', 'Rotation', (select count(id)
                                              from permission
                                              where parent = (select id from permission where code = 'HRMS_ACTIVITIES_VIEW')) +
                                             1,
        (select id from permission where code = 'HRMS_MAIN_MENU'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_ROTATION_ADD', 'HRMS', 'Add', 1, (select id from permission where code = 'HRMS_ROTATION'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_ROTATION_EDIT', 'HRMS', 'Edit', 2, (select id from permission where code = 'HRMS_ROTATION'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_ROTATION_SUMMARY', 'HRMS', 'Summary', 4, (select id from permission where code = 'HRMS_ROTATION'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_ROTATION_DELETE', 'HRMS', 'Delete', 3, (select id from permission where code = 'HRMS_ROTATION'), 'RECRUITMENT_SYSTEM');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_ROTATION', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_ROTATION_ADD', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_ROTATION_EDIT', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_ROTATION_SUMMARY', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_ROTATION_DELETE', 'HRMS');



