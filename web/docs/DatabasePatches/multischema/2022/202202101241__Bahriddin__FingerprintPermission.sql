insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_FINGERPRINT', 'SETTINGS', 'Fingerprint', (select count(id)
                                                        from permission
                                                        where parent = (select id from permission where code = 'HRMS_EDIT_DEPARTMENT')) +
                                                       1,
        (select id from permission where code = 'SETTINGS_MAIN_MENU'), 'HRMS_MODULE');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_FINGERPRINT', 'HRMS');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_FINGERPRINT', 'ALLOW', 'ADMIN');



insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_ADD_FINGERPRINT', 'SETTINGS', 'Add Fingerprint', (select count(id)
                                                                from permission
                                                                where parent = (select id from permission where code = 'HRMS_EDIT_DEPARTMENT')) +
                                                               1,
        (select id from permission where code = 'HRMS_FINGERPRINT'), 'HRMS_MODULE');

insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_ADD_FINGERPRINT', 'HRMS');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_ADD_FINGERPRINT', 'ALLOW', 'ADMIN');