delete from "0".permission_context where permissioncode = 'MYWORKSPACE_MAIN_MENU' and contextcode = 'MYWORKSPACE';
delete from "anv".permission_context where permissioncode = 'MYWORKSPACE_MAIN_MENU' and contextcode = 'MYWORKSPACE';

delete from context where code='MYWORKSPACE';
delete from permission where modulecode='MYWORKSPACE_MODULE';

insert into context (code) values ('MYWORKSPACE');
insert into permission (code, context, name, sorder, parent, modulecode, isMainMenu)
values('MYWORKSPACE_MAIN_MENU', 'MYWORKSPACE', 'My Workspace', 1, 0, 'MYWORKSPACE_MODULE', true);

insert into "0".permission_context (permissioncode, contextcode) values ('MYWORKSPACE_MAIN_MENU',  'MYWORKSPACE');
delete from "0".rolepermission where permissioncode = 'MYWORKSPACE_MAIN_MENU';
delete from "0".mymodule where code = 'MYWORKSPACE_MODULE';
insert into "0".mymodule (code) values('MYWORKSPACE_MODULE');
insert into "0".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'HR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'ACCOUNTANT','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'ADMIN_LOCATION','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'SALESMAN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'TL','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'PM','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'SALESPERSON','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'MEM','ALLOW');

insert into "anv".permission_context (permissioncode, contextcode) values ('MYWORKSPACE_MAIN_MENU',  'MYWORKSPACE');
delete from "anv".rolepermission where permissioncode = 'MYWORKSPACE_MAIN_MENU';
delete from "anv".mymodule where code = 'MYWORKSPACE_MODULE';
insert into "anv".mymodule (code) values('MYWORKSPACE_MODULE');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'HR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'ACCOUNTANT','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'ADMIN_LOCATION','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'SALESMAN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'TL','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'PM','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'SALESPERSON','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('MYWORKSPACE_MAIN_MENU', 'MEM','ALLOW');
