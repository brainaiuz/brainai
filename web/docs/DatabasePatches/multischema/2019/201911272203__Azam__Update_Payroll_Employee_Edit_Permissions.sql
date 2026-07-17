delete from permission where code = 'PAYROLL_EMPLOYEE_EDIT';

insert into permission(code,context,name,sorder,ismainmenu,parent,iscore,modulecode)
values('PAYROLL_EMPLOYEE_EDIT','PAYROLL','Edit',2,'f',(SELECT ID FROM PERMISSION WHERE CODE = 'PAYROLL_EMPLOYEES_LIST'),'f','PAYROLL');

delete from "0".rolepermission where permissioncode = 'PAYROLL_EMPLOYEE_EDIT';
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEE_EDIT','ADMIN','ALLOW');
insert into "0".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEE_EDIT','DR','ALLOW');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_EMPLOYEE_EDIT';
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEE_EDIT','ADMIN','ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode,access) values('PAYROLL_EMPLOYEE_EDIT','DR','ALLOW');

delete from "0".permission_context where permissioncode = 'PAYROLL_EMPLOYEE_EDIT' and contextcode='PAYROLL';
insert into "0".permission_context (permissioncode, contextcode) values ('PAYROLL_EMPLOYEE_EDIT', 'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_EMPLOYEE_EDIT' and contextcode='PAYROLL';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_EMPLOYEE_EDIT', 'PAYROLL');

--update sorder
update permission set  sorder=3, name='View', parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST') where code='PAYROLL_EMPLOYEES_PAYROLL_SETTINGS';
update permission set  sorder=4, name='Full Access', parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST') where code='PAYROLL_EMPLOYEES_FULL_ACCESS';
update permission set  sorder=5, parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST') where code='PAYROLL_EMPLOYEE_BASIC_SALARY';
update permission set  sorder=6, parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST') where code='PAYROLL_EMPLOYEES_PAYSLIPS';
update permission set  sorder=7, name='Approval', parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST') where code='PAYROLL_EMPLOYEE_APPROVAL';