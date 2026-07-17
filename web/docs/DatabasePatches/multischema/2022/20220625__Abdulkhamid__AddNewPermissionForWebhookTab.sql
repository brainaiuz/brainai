insert into permission (code, context,  name,  parent, iscore, modulecode)
values ('WEBHOOK_RESPONSE_TAB_VIEW',   'SETTINGS', 'Webhook Response',  (select id from permission where code = 'SETTINGS_WORKFLOW'), true, 'CORE');

insert into "anv".permission_context(permissioncode,contextcode) values ('WEBHOOK_RESPONSE_TAB_VIEW','SETTINGS');
