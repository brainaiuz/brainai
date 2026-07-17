insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_GROUP_PLACEMENT', 'HRMS', 'Group Placement', (select count(id)
                                                            from permission
                                                            where parent = (select id from permission where code = 'HRMS_ATTENDANCE_REPORT')) +
                                                           1,
        (select id from permission where code = 'HRMS_MAIN_MENU'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_GROUP_PLACEMENT_ADD', 'HRMS', 'Add', 1, (select id from permission where code = 'HRMS_GROUP_PLACEMENT'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_GROUP_PLACEMENT_EDIT', 'HRMS', 'Edit', 2, (select id from permission where code = 'HRMS_GROUP_PLACEMENT'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_GROUP_PLACEMENT_SUMMARY', 'HRMS', 'Summary', 4, (select id from permission where code = 'HRMS_GROUP_PLACEMENT'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_GROUP_PLACEMENT_DELETE', 'HRMS', 'Delete', 3, (select id from permission where code = 'HRMS_GROUP_PLACEMENT'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_GROUP_PLACEMENT_PDF', 'HRMS', 'Pdf', 5, (select id from permission where code = 'HRMS_GROUP_PLACEMENT'), 'RECRUITMENT_SYSTEM');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_GROUP_PLACEMENT', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_GROUP_PLACEMENT_ADD', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_GROUP_PLACEMENT_EDIT', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_GROUP_PLACEMENT_SUMMARY', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_GROUP_PLACEMENT_DELETE', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_GROUP_PLACEMENT_PDF', 'HRMS');