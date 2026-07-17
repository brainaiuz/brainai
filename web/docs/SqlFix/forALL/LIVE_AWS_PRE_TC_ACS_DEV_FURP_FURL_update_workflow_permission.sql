update permission set sorder=11, parent=(select id from permission where code = 'SETTINGS_MAIN_MENU'), name='Workflow Settings',
	ismainmenu=false where code = 'SETTINGS_WORKFLOW';

delete from "0".rolepermission where permissioncode = 'SETTINGS_WORKFLOW';

insert into "0".rolepermission (permissioncode, rolecode,access) values('SETTINGS_WORKFLOW','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('SETTINGS_WORKFLOW','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('SETTINGS_WORKFLOW','HR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('SETTINGS_WORKFLOW','PM','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('SETTINGS_WORKFLOW','ACCOUNTANT','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('SETTINGS_WORKFLOW','SALESMAN','ALLOW');

delete from "anv".rolepermission where permissioncode = 'SETTINGS_WORKFLOW';

insert into "anv".rolepermission (permissioncode, rolecode,access) values('SETTINGS_WORKFLOW','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('SETTINGS_WORKFLOW','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('SETTINGS_WORKFLOW','HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('SETTINGS_WORKFLOW','PM','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('SETTINGS_WORKFLOW','ACCOUNTANT','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('SETTINGS_WORKFLOW','SALESMAN','ALLOW');
