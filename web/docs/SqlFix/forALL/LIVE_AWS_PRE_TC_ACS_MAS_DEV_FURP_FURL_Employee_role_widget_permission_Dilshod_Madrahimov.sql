DELETE FROM permission WHERE code='HRMS_SHOW_EMPLOYEE_ROLE_WIDGET';
DELETE FROM permission WHERE code='PM_SHOW_EMPLOYEE_ROLE_WIDGET';

DELETE FROM "anv".rolepermission WHERE permissioncode='HRMS_SHOW_EMPLOYEE_ROLE_WIDGET';
DELETE FROM "0".rolepermission WHERE permissioncode='HRMS_SHOW_EMPLOYEE_ROLE_WIDGET';

DELETE FROM "anv".rolepermission WHERE permissioncode='PM_SHOW_EMPLOYEE_ROLE_WIDGET';
DELETE FROM "0".rolepermission WHERE permissioncode='PM_SHOW_EMPLOYEE_ROLE_WIDGET';

insert into permission(code,context,name,sorder,ismainmenu,parent,iscore,modulecode)
values('HRMS_SHOW_EMPLOYEE_ROLE_WIDGET','HRMS','Employee show role widget',2,false,(select id from permission where code = 'HRMS_ADD_NEW_EMPLOYEE'),false,'HRMS_MODULE');


insert into "0".rolepermission (permissioncode, rolecode,access) values('HRMS_SHOW_EMPLOYEE_ROLE_WIDGET','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('HRMS_SHOW_EMPLOYEE_ROLE_WIDGET','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('HRMS_SHOW_EMPLOYEE_ROLE_WIDGET','HR','ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode,access) values('HRMS_SHOW_EMPLOYEE_ROLE_WIDGET','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('HRMS_SHOW_EMPLOYEE_ROLE_WIDGET','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('HRMS_SHOW_EMPLOYEE_ROLE_WIDGET','HR','ALLOW');


insert into permission(code,context,name,sorder,ismainmenu,parent,iscore,modulecode)
values('PM_SHOW_EMPLOYEE_ROLE_WIDGET','PM','Employee show role widget',2,false,(select id from permission where code = 'PM_EMPLOYEE_LIST'),false,'PM');


insert into "0".rolepermission (permissioncode, rolecode,access) values('PM_SHOW_EMPLOYEE_ROLE_WIDGET','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PM_SHOW_EMPLOYEE_ROLE_WIDGET','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PM_SHOW_EMPLOYEE_ROLE_WIDGET','PM','ALLOW');


insert into "anv".rolepermission (permissioncode, rolecode,access) values('PM_SHOW_EMPLOYEE_ROLE_WIDGET','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PM_SHOW_EMPLOYEE_ROLE_WIDGET','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PM_SHOW_EMPLOYEE_ROLE_WIDGET','PM','ALLOW');
