insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_SHIFT', 'HRMS', 'Shift', (select count(id)
                                        from permission
                                        where parent = (select id from permission where code = 'HRMS_ATTENDANCE_REPORT')) +
                                       1,
        (select id from permission where code = 'HRMS_MAIN_MENU'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_SHIFT_ADD', 'HRMS', 'Add', 1, (select id from permission where code = 'HRMS_SHIFT'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_SHIFT_EDIT', 'HRMS', 'Edit', 2, (select id from permission where code = 'HRMS_SHIFT'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_SHIFT_SUMMARY', 'HRMS', 'Summary', 4, (select id from permission where code = 'HRMS_SHIFT'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_SHIFT_DELETE', 'HRMS', 'Delete', 3, (select id from permission where code = 'HRMS_SHIFT'), 'RECRUITMENT_SYSTEM');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_ADD', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_EDIT', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_SUMMARY', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_SHIFT_DELETE', 'HRMS');



insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BRIGADA', 'HRMS', 'Brigada', (select count(id)
                                            from permission
                                            where parent = (select id from permission where code = 'HRMS_SHIFT')) +
                                           1,
        (select id from permission where code = 'HRMS_MAIN_MENU'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BRIGADA_ADD', 'HRMS', 'Add', 1, (select id from permission where code = 'HRMS_BRIGADA'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BRIGADA_EDIT', 'HRMS', 'Edit', 2, (select id from permission where code = 'HRMS_BRIGADA'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BRIGADA_SUMMARY', 'HRMS', 'Summary', 4, (select id from permission where code = 'HRMS_BRIGADA'), 'RECRUITMENT_SYSTEM');
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BRIGADA_DELETE', 'HRMS', 'Delete', 3, (select id from permission where code = 'HRMS_BRIGADA'), 'RECRUITMENT_SYSTEM');


insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BRIGADA', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BRIGADA_ADD', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BRIGADA_EDIT', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BRIGADA_SUMMARY', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BRIGADA_DELETE', 'HRMS');

