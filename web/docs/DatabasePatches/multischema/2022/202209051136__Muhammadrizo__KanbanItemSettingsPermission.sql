insert into permission (code, context, name, sorder, parent, modulecode)
values ('KANBAN_ITEM_CUSTOMIZATION_SETTINGS', 'SETTINGS', 'Kanban Customization',
        (select count(id)
         from permission
         where parent = (select id from permission where code = 'SETTINGS_CUSTOMIZATION')) + 1,
        (select id from permission where code = 'SETTINGS_CUSTOMIZATION'), 'TASK_MANAGEMENT');

insert into "anv".permission_context (permissioncode, contextcode)
values ('KANBAN_ITEM_CUSTOMIZATION_SETTINGS', 'SETTINGS');

insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('KANBAN_ITEM_CUSTOMIZATION_SETTINGS', 'ALLOW', 'ADMIN');

