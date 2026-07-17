insert into permission(code, context, name, sorder, parent, modulecode) values ('SETTINGS_CUSTOMIZATION','SETTINGS','Customization',(select sorder from permission where code='CUSTOM_FIELD_SETTINGS'),(select id from permission where code='SETTINGS_MAIN_MENU'),'CORE');

---Zero
insert into "0".rolepermission(permissioncode,access,rolecode) values ('SETTINGS_CUSTOMIZATION','ALLOW','ADMIN');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('SETTINGS_CUSTOMIZATION','ALLOW','DR');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('SETTINGS_CUSTOMIZATION','ALLOW','ACCOUNTANT');

delete from "0".permission_context where permissioncode = 'SETTINGS_CUSTOMIZATION' and contextcode='SETTINGS';
insert into "0".permission_context(permissioncode,contextcode) values ('SETTINGS_CUSTOMIZATION','SETTINGS');


--All

insert into "anv".rolepermission(permissioncode,access,rolecode) values ('SETTINGS_CUSTOMIZATION','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('SETTINGS_CUSTOMIZATION','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('SETTINGS_CUSTOMIZATION','ALLOW','ACCOUNTANT');

delete from "anv".permission_context where permissioncode = 'SETTINGS_CUSTOMIZATION' and contextcode='SETTINGS';
insert into "anv".permission_context(permissioncode,contextcode) values ('SETTINGS_CUSTOMIZATION','SETTINGS');