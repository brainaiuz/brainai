delete from permission where code='SYSTEM_LOGS';
insert into permission (code, context, name, sorder, parent,modulecode) values
('SYSTEM_LOGS', 'SETTINGS', 'System Logs', (select max(sorder)+1 from permission where parent = (select id from permission where code='SETTINGS_MAIN_MENU')),
 (select id from permission where code ='SETTINGS_MAIN_MENU'), 'CORE');


delete from "0".rolepermission where permissioncode='SYSTEM_LOGS';
insert into "0".rolepermission (permissioncode, rolecode, access) values('SYSTEM_LOGS', 'ADMIN','ALLOW');

delete from "0".permission_context where permissioncode='SYSTEM_LOGS';
insert into "0".permission_context(permissioncode, contextcode) values('SYSTEM_LOGS', 'SETTINGS');

delete from "anv".rolepermission where permissioncode='SYSTEM_LOGS';
insert into "anv".rolepermission (permissioncode, rolecode, access) values('SYSTEM_LOGS', 'ADMIN','ALLOW');

delete from "anv".permission_context where permissioncode='SYSTEM_LOGS';
insert into "anv".permission_context(permissioncode, contextcode) values('SYSTEM_LOGS', 'SETTINGS');