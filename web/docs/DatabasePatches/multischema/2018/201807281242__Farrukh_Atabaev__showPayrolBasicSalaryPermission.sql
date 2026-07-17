delete from permission where code = 'PAYROLL_EMPLOYEE_BASIC_SALARY';

insert into permission(code,context,name,sorder,ismainmenu,parent,iscore,modulecode)
values('PAYROLL_EMPLOYEE_BASIC_SALARY','PAYROLL','Show Basic Salary',(SELECT max(sorder)+1 from permission WHERE code='PAYROLL_EMPLOYEES_LIST'),'f',(SELECT ID FROM PERMISSION WHERE CODE = 'PAYROLL_EMPLOYEES_LIST'),'f','PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_EMPLOYEE_BASIC_SALARY';
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEE_BASIC_SALARY','DR','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEE_BASIC_SALARY','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEE_BASIC_SALARY','ACCOUNTANT','ALLOW');
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_EMPLOYEE_BASIC_SALARY',  'PAYROLL');

delete from "0".rolepermission where permissioncode = 'PAYROLL_EMPLOYEE_BASIC_SALARY';
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEE_BASIC_SALARY','DR','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEE_BASIC_SALARY','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEE_BASIC_SALARY','ACCOUNTANT','ALLOW');
insert into "0".permission_context (permissioncode, contextcode) values ('PAYROLL_EMPLOYEE_BASIC_SALARY',  'PAYROLL');

