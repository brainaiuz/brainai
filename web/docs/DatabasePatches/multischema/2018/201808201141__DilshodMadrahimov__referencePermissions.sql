delete from permission WHERE code in('REFERENCE_LIST','REFERENCE_ADD','REFERENCE_EDIT','REFERENCE_DELETE');

delete from "0".permission_context where permissioncode in('REFERENCE_LIST','REFERENCE_ADD','REFERENCE_EDIT','REFERENCE_DELETE') and contextcode in('SETTINGS','HRMS');
delete from "anv".permission_context where permissioncode in('REFERENCE_LIST','REFERENCE_ADD','REFERENCE_EDIT','REFERENCE_DELETE') and contextcode in('SETTINGS','HRMS');

delete from "0".rolepermission where permissioncode in('REFERENCE_LIST','REFERENCE_ADD','REFERENCE_EDIT','REFERENCE_DELETE');
delete from "anv".rolepermission where permissioncode in('REFERENCE_LIST','REFERENCE_ADD','REFERENCE_EDIT','REFERENCE_DELETE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('REFERENCE_LIST', 'SETTINGS', 'Reference List', 1, 0, 'CORE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('REFERENCE_ADD', 'SETTINGS', 'Reference Add', 2, (select id from permission WHERE code='REFERENCE_LIST'), 'CORE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('REFERENCE_EDIT', 'SETTINGS', 'Reference Edit', 3, (select id from permission WHERE code='REFERENCE_LIST'), 'CORE');

insert into permission (code, context, name, sorder, parent, modulecode)
values('REFERENCE_DELETE', 'SETTINGS', 'Reference Delete', 4, (select id from permission WHERE code='REFERENCE_LIST'), 'CORE');

insert into "0".permission_context (permissioncode, contextcode) values('REFERENCE_LIST', 'SETTINGS');
insert into "0".permission_context (permissioncode, contextcode) values('REFERENCE_ADD', 'SETTINGS');
insert into "0".permission_context (permissioncode, contextcode) values('REFERENCE_EDIT', 'SETTINGS');
insert into "0".permission_context (permissioncode, contextcode) values('REFERENCE_DELETE', 'SETTINGS');

insert into "0".rolepermission (permissioncode, rolecode, access) values('REFERENCE_LIST', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('REFERENCE_ADD', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('REFERENCE_EDIT', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('REFERENCE_DELETE', 'ADMIN','ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode, access) values('REFERENCE_LIST', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REFERENCE_ADD', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REFERENCE_EDIT', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('REFERENCE_DELETE', 'ADMIN','ALLOW');

insert into "anv".permission_context (permissioncode, contextcode) values('REFERENCE_LIST', 'SETTINGS');
insert into "anv".permission_context (permissioncode, contextcode) values('REFERENCE_ADD', 'SETTINGS');
insert into "anv".permission_context (permissioncode, contextcode) values('REFERENCE_EDIT', 'SETTINGS');
insert into "anv".permission_context (permissioncode, contextcode) values('REFERENCE_DELETE', 'SETTINGS');