delete from permission where code = 'SETTINGS_TWILIO_LIST' or code = 'SETTINGS_ASTERISK_LIST';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('SETTINGS_TWILIO_LIST',   'SETTINGS', false, 'Twilio Settings', 15, (select id from permission where code = 'SETTINGS_INTEGRATION' limit 1), true, 'CORE'),
('SETTINGS_ASTERISK_LIST',   'SETTINGS', false, 'Asterisk Settings', 15, (select id from permission where code = 'SETTINGS_INTEGRATION' limit 1), true, 'CORE');

delete from "anv".permission_context where permissioncode = 'SETTINGS_TWILIO_LIST' or permissioncode = 'SETTINGS_ASTERISK_LIST';
insert into "anv".permission_context(permissioncode,contextcode) values ('SETTINGS_TWILIO_LIST','SETTINGS'), ('SETTINGS_ASTERISK_LIST','SETTINGS');
