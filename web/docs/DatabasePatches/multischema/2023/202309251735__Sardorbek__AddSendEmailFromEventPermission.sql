insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('CRM_SEND_EMAIL', 'CRM', true, 'Send Email From Event',
        (select count(id)
         from permission
         where parent = (select id from permission where code = 'CRM_ACTIVITIES_LIST')) + 1,
        (select id from permission where code = 'CRM_ACTIVITIES_LIST'), true, 'CORE');

insert into "anv".permission_context(permissioncode, contextcode)
values ('CRM_SEND_EMAIL', 'CRM');

insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('CRM_SEND_EMAIL', 'ALLOW', 'ADMIN');