insert into permission(code, context, name, sorder, parent, modulecode) values ('CUSTOM_FIELD_DISABLED_FIELD','SETTINGS','Edit disabled field',1,(select id from permission where code='CUSTOM_FIELD_SETTINGS'),'CORE');

---Zero
insert into "0".rolepermission(permissioncode,access,rolecode) values ('CUSTOM_FIELD_DISABLED_FIELD','ALLOW','ADMIN');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('CUSTOM_FIELD_DISABLED_FIELD','ALLOW','DR');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('CUSTOM_FIELD_DISABLED_FIELD','ALLOW','ACCOUNTANT');

delete from "0".permission_context where permissioncode = 'CUSTOM_FIELD_DISABLED_FIELD' and contextcode='SETTINGS';
insert into "0".permission_context(permissioncode,contextcode) values ('CUSTOM_FIELD_DISABLED_FIELD','SETTINGS');


--All

insert into "anv".rolepermission(permissioncode,access,rolecode) values ('CUSTOM_FIELD_DISABLED_FIELD','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('CUSTOM_FIELD_DISABLED_FIELD','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('CUSTOM_FIELD_DISABLED_FIELD','ALLOW','ACCOUNTANT');

delete from "anv".permission_context where permissioncode = 'CUSTOM_FIELD_DISABLED_FIELD' and contextcode='SETTINGS';
insert into "anv".permission_context(permissioncode,contextcode) values ('CUSTOM_FIELD_DISABLED_FIELD','SETTINGS');
