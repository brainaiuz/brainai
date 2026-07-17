delete from "anv".reportingpermission where code in ('REPORTING_SAVE_BUTTON','REPORTING_DELETE_BUTTON','REPORTING_SHARE_BUTTON','REPORTING_EXPORT_BUTTON','REPORTING_SHOW_HIDE_OPTIONS_BUTTON','REPORTING_SHOW_HIDE_DETAILS_BUTTON','REPORTING_CUSTOMIZE_COLUMNS_BUTTON');
insert into "anv".reportingpermission (code, context, name, sorder, parent, modulecode) values
('REPORTING_SAVE_BUTTON', 'REPORTING', 'Save', 1, (select id from "anv".reportingpermission where code = 'REPORTING_MAIN_MENU'), 'REPORTING_SYSTEM'),
('REPORTING_DELETE_BUTTON', 'REPORTING', 'Delete', 1, (select id from "anv".reportingpermission where code = 'REPORTING_MAIN_MENU'), 'REPORTING_SYSTEM'),
('REPORTING_SHARE_BUTTON', 'REPORTING', 'Share', 1, (select id from "anv".reportingpermission where code = 'REPORTING_MAIN_MENU'), 'REPORTING_SYSTEM'),
('REPORTING_EXPORT_BUTTON', 'REPORTING', 'Export', 1, (select id from "anv".reportingpermission where code = 'REPORTING_MAIN_MENU'), 'REPORTING_SYSTEM'),
('REPORTING_SHOW_HIDE_OPTIONS_BUTTON', 'REPORTING', 'Show/hide options', 1, (select id from "anv".reportingpermission where code = 'REPORTING_MAIN_MENU'), 'REPORTING_SYSTEM'),
('REPORTING_SHOW_HIDE_DETAILS_BUTTON', 'REPORTING', 'Show/hide details', 1, (select id from "anv".reportingpermission where code = 'REPORTING_MAIN_MENU'), 'REPORTING_SYSTEM'),
('REPORTING_CUSTOMIZE_COLUMNS_BUTTON', 'REPORTING', 'Customize Column', 1, (select id from "anv".reportingpermission where code = 'REPORTING_MAIN_MENU'), 'REPORTING_SYSTEM');


delete from "anv".permission_context where permissioncode in ('REPORTING_SAVE_BUTTON','REPORTING_DELETE_BUTTON','REPORTING_SHARE_BUTTON','REPORTING_EXPORT_BUTTON','REPORTING_SHOW_HIDE_OPTIONS_BUTTON','REPORTING_SHOW_HIDE_DETAILS_BUTTON','REPORTING_CUSTOMIZE_COLUMNS_BUTTON');
insert into "anv".permission_context (permissioncode, contextcode) values ('REPORTING_SAVE_BUTTON',  'REPORTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('REPORTING_DELETE_BUTTON',  'REPORTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('REPORTING_SHARE_BUTTON',  'REPORTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('REPORTING_EXPORT_BUTTON',  'REPORTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('REPORTING_SHOW_HIDE_OPTIONS_BUTTON',  'REPORTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('REPORTING_SHOW_HIDE_DETAILS_BUTTON',  'REPORTING');
insert into "anv".permission_context (permissioncode, contextcode) values ('REPORTING_CUSTOMIZE_COLUMNS_BUTTON',  'REPORTING');

delete from "anv".rolepermission where permissioncode in ('REPORTING_SAVE_BUTTON','REPORTING_DELETE_BUTTON','REPORTING_SHARE_BUTTON','REPORTING_EXPORT_BUTTON','REPORTING_SHOW_HIDE_OPTIONS_BUTTON','REPORTING_SHOW_HIDE_DETAILS_BUTTON','REPORTING_CUSTOMIZE_COLUMNS_BUTTON');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SAVE_BUTTON', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SAVE_BUTTON', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SAVE_BUTTON', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SAVE_BUTTON', 'ACCOUNTANT','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SAVE_BUTTON', 'MEM','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_DELETE_BUTTON', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_DELETE_BUTTON', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_DELETE_BUTTON', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_DELETE_BUTTON', 'ACCOUNTANT','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_DELETE_BUTTON', 'MEM','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHARE_BUTTON', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHARE_BUTTON', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHARE_BUTTON', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHARE_BUTTON', 'ACCOUNTANT','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHARE_BUTTON', 'MEM','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_EXPORT_BUTTON', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_EXPORT_BUTTON', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_EXPORT_BUTTON', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_EXPORT_BUTTON', 'ACCOUNTANT','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_EXPORT_BUTTON', 'MEM','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHOW_HIDE_OPTIONS_BUTTON', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHOW_HIDE_OPTIONS_BUTTON', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHOW_HIDE_OPTIONS_BUTTON', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHOW_HIDE_OPTIONS_BUTTON', 'ACCOUNTANT','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHOW_HIDE_OPTIONS_BUTTON', 'MEM','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHOW_HIDE_DETAILS_BUTTON', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHOW_HIDE_DETAILS_BUTTON', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHOW_HIDE_DETAILS_BUTTON', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHOW_HIDE_DETAILS_BUTTON', 'ACCOUNTANT','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_SHOW_HIDE_DETAILS_BUTTON', 'MEM','ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_CUSTOMIZE_COLUMNS_BUTTON', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_CUSTOMIZE_COLUMNS_BUTTON', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_CUSTOMIZE_COLUMNS_BUTTON', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_CUSTOMIZE_COLUMNS_BUTTON', 'ACCOUNTANT','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REPORTING_CUSTOMIZE_COLUMNS_BUTTON', 'MEM','ALLOW');
