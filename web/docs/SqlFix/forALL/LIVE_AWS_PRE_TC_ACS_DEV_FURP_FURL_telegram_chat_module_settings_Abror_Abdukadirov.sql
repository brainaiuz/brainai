insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('HRMS_TELEGRAM_CHAT_LIST', 'HRMS', 'f', 'Telegram Chat List', coalesce((select max(sorder) from permission where parent = (select id from permission where code='HRMS_SECTION_TAB')), 0) + 1, (select id from permission where code='HRMS_SECTION_TAB'), 'TELEGRAM_CHATS');

insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TELEGRAM_CHAT_LIST', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TELEGRAM_CHAT_LIST', 'DR', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_TELEGRAM_CHAT_LIST', 'HR', 'ALLOW');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TELEGRAM_CHAT_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TELEGRAM_CHAT_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_TELEGRAM_CHAT_LIST', 'HR', 'ALLOW');

