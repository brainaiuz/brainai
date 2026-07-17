insert into permission (code, context, name, sorder, parent, modulecode)
values('SETTINGS_DASHBOARD_LIST', 'SETTINGS', 'Dashboard list', 30, (select id from permission where code = 'SETTINGS_MAIN_MENU'), 'CORE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('SETTINGS_DASHBOARD_ADD', 'SETTINGS', 'Dashboard Add', 31, (select id from permission where code = 'SETTINGS_DASHBOARD_LIST'), 'CORE'),
      ('SETTINGS_DASHBOARD_EDIT', 'SETTINGS', 'Dashboard Edit', 32, (select id from permission where code = 'SETTINGS_DASHBOARD_LIST'), 'CORE'),
      ('SETTINGS_DASHBOARD_DELETE', 'SETTINGS', 'Dashboard Delete', 33, (select id from permission where code = 'SETTINGS_DASHBOARD_LIST'), 'CORE');

insert into "0".permission_context (permissioncode, contextcode) values ('SETTINGS_DASHBOARD_LIST',  'SETTINGS');
insert into "0".permission_context (permissioncode, contextcode) values ('SETTINGS_DASHBOARD_ADD',  'SETTINGS');
insert into "0".permission_context (permissioncode, contextcode) values ('SETTINGS_DASHBOARD_EDIT',  'SETTINGS');
insert into "0".permission_context (permissioncode, contextcode) values ('SETTINGS_DASHBOARD_DELETE',  'SETTINGS');

insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_LIST', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_LIST', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_LIST', 'HR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_LIST', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_ADD', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_ADD', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_ADD', 'HR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_ADD', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_EDIT', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_EDIT', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_EDIT', 'HR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_EDIT', 'ACCOUNTANT','ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_DELETE', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_DELETE', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_DELETE', 'HR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_DELETE', 'ACCOUNTANT','ALLOW');

insert into "anv".permission_context (permissioncode, contextcode) values ('SETTINGS_DASHBOARD_LIST',  'SETTINGS');
insert into "anv".permission_context (permissioncode, contextcode) values ('SETTINGS_DASHBOARD_ADD',  'SETTINGS');
insert into "anv".permission_context (permissioncode, contextcode) values ('SETTINGS_DASHBOARD_EDIT',  'SETTINGS');
insert into "anv".permission_context (permissioncode, contextcode) values ('SETTINGS_DASHBOARD_DELETE',  'SETTINGS');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_LIST', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_LIST', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_LIST', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_LIST', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_ADD', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_ADD', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_ADD', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_EDIT', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_EDIT', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_EDIT', 'ACCOUNTANT','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_DELETE', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_DELETE', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_DELETE', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SETTINGS_DASHBOARD_DELETE', 'ACCOUNTANT','ALLOW');