
delete from permission where code = 'SETTINGS_EMPLOYEE_LIST';
delete from permission where code = 'SETTINGS_ROLE_LIST';
delete from permission where code = 'SETTINGS_MANAGE_ROLE';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('SETTINGS_EMPLOYEE_LIST', 'SETTINGS', 'Employees', 1, (select id from permission where code = 'PERMISSION_MANAGEMENT'), 'CORE'),
       ('SETTINGS_ROLE_LIST', 'SETTINGS', 'Roles', 2, (select id from permission where code = 'PERMISSION_MANAGEMENT'), 'CORE'),
       ('SETTINGS_MANAGE_ROLE', 'SETTINGS', 'Permissions', 3, (select id from permission where code = 'PERMISSION_MANAGEMENT'), 'CORE');

delete from "anv".permission_context where permissioncode = 'PERMISSION_MANAGEMENT';
delete from "anv".permission_context where permissioncode = 'SETTINGS_EMPLOYEE_LIST';
delete from "anv".permission_context where permissioncode = 'SETTINGS_ROLE_LIST';
delete from "anv".permission_context where permissioncode = 'SETTINGS_MANAGE_ROLE';
insert into "anv".permission_context (permissioncode, contextcode)
values ('PERMISSION_MANAGEMENT', 'SETTINGS'),
       ('SETTINGS_EMPLOYEE_LIST', 'SETTINGS'),
       ('SETTINGS_ROLE_LIST', 'SETTINGS'),
       ('SETTINGS_MANAGE_ROLE', 'SETTINGS');

delete from "anv".rolepermission where permissioncode = 'PERMISSION_MANAGEMENT';
delete from "anv".rolepermission where permissioncode = 'SETTINGS_EMPLOYEE_LIST';
delete from "anv".rolepermission where permissioncode = 'SETTINGS_ROLE_LIST';
delete from "anv".rolepermission where permissioncode = 'SETTINGS_MANAGE_ROLE';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('PERMISSION_MANAGEMENT', 'ALLOW', 'ADMIN'),
       ('SETTINGS_EMPLOYEE_LIST', 'ALLOW', 'ADMIN'),
       ('SETTINGS_ROLE_LIST', 'ALLOW', 'ADMIN'),
       ('SETTINGS_MANAGE_ROLE', 'ALLOW', 'ADMIN');