
--appga urilgan
delete from permission where code='ADD_SYSTEM_FILTER';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
		values ('ADD_SYSTEM_FILTER',   'SETTINGS', false, 'Add System Filter', 15, (select id from permission where code = 'SETTINGS_MAIN_MENU'), true, 'CORE');
