-------------Insert REPMISSIONS (public schema)--------
insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_ACTIVITY_SEE_ATTACHMENTS', 'CRM', 'f', 'See attachments', '7', (select id from permission where code='CRM_ACTIVITIES_LIST'), 'ACTIVITIES');

insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('CRM_ACTIVITY_SEE_ALL_ATTACHMENTS', 'CRM', 'f', 'See All attachments', '8', (select id from permission where code='CRM_ACTIVITIES_LIST'), 'ACTIVITIES');

-------------------------------------------------------------------for 'anv'-----------
----------------------- SEE_ATTACHMENTS ----- default -----
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_ACTIVITY_SEE_ATTACHMENTS', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_ACTIVITY_SEE_ATTACHMENTS', 'DR', 'ALLOW');

----------------------- SEE_ALL_ATTACHMENTS ----- default -----
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_ACTIVITY_SEE_ALL_ATTACHMENTS', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('CRM_ACTIVITY_SEE_ALL_ATTACHMENTS', 'DR', 'ALLOW');

-------------------------------------------------------------------for '0'-----------
----------------------- SEE_ATTACHMENTS ----- default -----
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_ACTIVITY_SEE_ATTACHMENTS', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_ACTIVITY_SEE_ATTACHMENTS', 'DR', 'ALLOW');

----------------------- SEE_ALL_ATTACHMENTS ----- default -----
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_ACTIVITY_SEE_ALL_ATTACHMENTS', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('CRM_ACTIVITY_SEE_ALL_ATTACHMENTS', 'DR', 'ALLOW');