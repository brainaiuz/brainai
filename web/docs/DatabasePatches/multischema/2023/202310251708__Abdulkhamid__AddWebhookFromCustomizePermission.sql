insert into permission (code, context,  name,  parent, iscore, modulecode)
values ('ADD_WEBHOOK_FROM_CUSTOMIZE',   'SETTINGS', 'Add webhook from customize',  (select id from permission where code = 'SETTINGS_WORKFLOW'), true, 'CORE');

insert into "anv".permission_context(permissioncode,contextcode) values ('ADD_WEBHOOK_FROM_CUSTOMIZE','SETTINGS');

insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('ADD_WEBHOOK_FROM_CUSTOMIZE', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('ADD_WEBHOOK_FROM_CUSTOMIZE', 'ALLOW', 'DR');