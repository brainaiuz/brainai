
---delete
DELETE FROM permission WHERE code in('HRMS_TALENT_PROFILE_LIST','HRMS_TALENT_PROFILE_VIEW','HRMS_TALENT_PROFILE_ADD','HRMS_TALENT_PROFILE_EDIT','HRMS_TALENT_PROFILE_DELETE');


insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
 VALUES('HRMS_TALENT_PROFILE_LIST','HRMS','f','Talent Profile List',(select max(sorder)+1 from permission where code='HRMS_MAIN_MENU'),(select id from permission where code='HRMS_MAIN_MENU'),'HRMS_MODULE')

insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values
('HRMS_TALENT_PROFILE_VIEW', 'HRMS', 'f', 'Talent Profile View', '1', (select id from permission where code='HRMS_TALENT_PROFILE_LIST'), 'HRMS_MODULE'),
('HRMS_TALENT_PROFILE_ADD', 'HRMS', 'f', 'Talent Profile Add', '2', (select id from permission where code='HRMS_TALENT_PROFILE_LIST'), 'HRMS_MODULE'),
('HRMS_TALENT_PROFILE_EDIT', 'HRMS', 'f', 'Talent Profile Edit', '3', (select id from permission where code='HRMS_TALENT_PROFILE_LIST'), 'HRMS_MODULE'),
('HRMS_TALENT_PROFILE_DELETE', 'HRMS', 'f', 'Talent Profile Delete', '4', (select id from permission where code='HRMS_TALENT_PROFILE_LIST'), 'HRMS_MODULE');



-------------------------------------------------------------------for 'anv'-----------
DELETE from "anv".rolepermission WHERE permissioncode in ('HRMS_TALENT_PROFILE_LIST','HRMS_TALENT_PROFILE_VIEW','HRMS_TALENT_PROFILE_ADD','HRMS_TALENT_PROFILE_EDIT','HRMS_TALENT_PROFILE_DELETE');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_LIST', 'ACCOUNTANT', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_LIST', 'PM', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_VIEW', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_VIEW', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_VIEW', 'ACCOUNTANT', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_VIEW', 'PM', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_ADD', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_ADD', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_ADD', 'ACCOUNTANT', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_ADD', 'PM', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_EDIT', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_EDIT', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_EDIT', 'ACCOUNTANT', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_EDIT', 'PM', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_DELETE', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_DELETE', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_DELETE', 'ACCOUNTANT', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_DELETE', 'PM', 'ALLOW');


insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_TALENT_PROFILE_LIST',  'HRMS');
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_TALENT_PROFILE_VIEW',  'HRMS');
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_TALENT_PROFILE_ADD',  'HRMS');
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_TALENT_PROFILE_EDIT',  'HRMS');
insert into "anv".permission_context (permissioncode, contextcode) values ('HRMS_TALENT_PROFILE_DELETE',  'HRMS');

-------------------------------------------------------------------for '0'-----------
DELETE from "0".rolepermission WHERE permissioncode in ('HRMS_TALENT_PROFILE_LIST','HRMS_TALENT_PROFILE_VIEW','HRMS_TALENT_PROFILE_ADD','HRMS_TALENT_PROFILE_EDIT','HRMS_TALENT_PROFILE_DELETE');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_LIST', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_LIST', 'PM', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_VIEW', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_VIEW', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_VIEW', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_VIEW', 'PM', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_ADD', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_ADD', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_ADD', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_ADD', 'PM', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_EDIT', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_EDIT', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_EDIT', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_EDIT', 'PM', 'ALLOW');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_DELETE', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_DELETE', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_DELETE', 'ACCOUNTANT', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TALENT_PROFILE_DELETE', 'PM', 'ALLOW');


insert into "0".permission_context (permissioncode, contextcode) values ('HRMS_TALENT_PROFILE_LIST',  'HRMS');
insert into "0".permission_context (permissioncode, contextcode) values ('HRMS_TALENT_PROFILE_VIEW',  'HRMS');
insert into "0".permission_context (permissioncode, contextcode) values ('HRMS_TALENT_PROFILE_ADD',  'HRMS');
insert into "0".permission_context (permissioncode, contextcode) values ('HRMS_TALENT_PROFILE_EDIT',  'HRMS');
insert into "0".permission_context (permissioncode, contextcode) values ('HRMS_TALENT_PROFILE_DELETE',  'HRMS');


