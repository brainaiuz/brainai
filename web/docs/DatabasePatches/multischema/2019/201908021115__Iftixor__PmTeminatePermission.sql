

insert into permission(code, context, name, sorder, parent, modulecode) values ('PM_TERMINATE_EMPLOYMENT','PM','Terminate', 10, (select id from permission where code='PM_EMPLOYEE_LIST'),'PM');

insert into "0".rolepermission(permissioncode,access,rolecode) values ('PM_TERMINATE_EMPLOYMENT','ALLOW','ADMIN');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('PM_TERMINATE_EMPLOYMENT','ALLOW','DR');
insert into "0".rolepermission(permissioncode,access,rolecode) values ('PM_TERMINATE_EMPLOYMENT','ALLOW','HR');

delete from "0".permission_context where permissioncode = 'PM_TERMINATE_EMPLOYMENT' and contextcode='PM';
insert into "0".permission_context(permissioncode,contextcode) values ('PM_TERMINATE_EMPLOYMENT','PM');


insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PM_TERMINATE_EMPLOYMENT','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PM_TERMINATE_EMPLOYMENT','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PM_TERMINATE_EMPLOYMENT','ALLOW','HR');

delete from "anv".permission_context where permissioncode = 'PM_TERMINATE_EMPLOYMENT' and contextcode='PM';
insert into "anv".permission_context(permissioncode,contextcode) values ('PM_TERMINATE_EMPLOYMENT','PM');

update permission set sorder=11 where code='PM_EMPLOYEE_REMOVE';
update permission set sorder=12 where code='SHOW_IMPORT_EMPLOYEE';
