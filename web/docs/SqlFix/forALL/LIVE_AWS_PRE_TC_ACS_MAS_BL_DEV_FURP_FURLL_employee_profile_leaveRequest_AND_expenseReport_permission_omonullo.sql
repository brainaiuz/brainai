--parent: HRMS_CURENT_EMPLOYEE_PROFILE_TAB

delete from permission where code = 'EMP_PROFILE_BASIC_SALARY';

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('EMP_PROFILE_BASIC_SALARY', 'HRMS', false, 'Basic Salary Field', 40, (select id from permission where code = 'HRMS_CURENT_EMPLOYEE_PROFILE_TAB'), false, 'HRMS_MODULE');

delete from "anv".rolepermission where permissioncode = 'EMP_PROFILE_BASIC_SALARY';

insert into "anv".rolepermission (permissioncode, rolecode,access) values('EMP_PROFILE_BASIC_SALARY','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('EMP_PROFILE_BASIC_SALARY','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('EMP_PROFILE_BASIC_SALARY','HR','ALLOW');

delete from "0".rolepermission where permissioncode = 'EMP_PROFILE_BASIC_SALARY';

insert into "0".rolepermission (permissioncode, rolecode,access) values('EMP_PROFILE_BASIC_SALARY','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('EMP_PROFILE_BASIC_SALARY','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('EMP_PROFILE_BASIC_SALARY','HR','ALLOW');