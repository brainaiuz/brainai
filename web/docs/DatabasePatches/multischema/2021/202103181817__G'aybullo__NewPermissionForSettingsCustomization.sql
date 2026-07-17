insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('SETTINGS_CUSTOMIZATION_REFERENCE',   'SETTINGS', false, 'Reference', 15, (select id from permission where code = 'SETTINGS_CUSTOMIZATION'), true, 'CORE');

insert into "anv".permission_context(permissioncode,contextcode) values ('SETTINGS_CUSTOMIZATION_REFERENCE','SETTINGS');