delete from "0".rolepermission where permissioncode = 'WORKSPACE_MAIN_MENU';
insert into "0".rolepermission (permissioncode, rolecode, access) values('WORKSPACE_MAIN_MENU','ADMIN','ALLOW');

delete from "anv".rolepermission where permissioncode = 'WORKSPACE_MAIN_MENU';
insert into "anv".rolepermission (permissioncode, rolecode, access) values('WORKSPACE_MAIN_MENU','ADMIN','ALLOW');
