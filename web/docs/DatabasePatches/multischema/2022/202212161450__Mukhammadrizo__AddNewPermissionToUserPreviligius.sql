insert into permission (code, context, name, sorder, parent, modulecode) values('SETTINGS_USER_LIST', 'SETTINGS', 'Users', 1, (select id from permission where code ='PERMISSION_MANAGEMENT'), 'TASK_MANAGEMENT');

insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_USER_LIST', 'ADMIN','ALLOW');
insert into "0".permission_context(permissioncode, contextcode) values('SETTINGS_USER_LIST', 'SETTINGS');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_USER_LIST', 'ADMIN','ALLOW');
insert into "anv".permission_context(permissioncode, contextcode) values('SETTINGS_USER_LIST', 'SETTINGS');