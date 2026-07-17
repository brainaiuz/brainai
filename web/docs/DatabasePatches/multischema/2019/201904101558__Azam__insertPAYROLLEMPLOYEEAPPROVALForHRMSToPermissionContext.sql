delete from "0".permission_context where permissioncode = 'PAYROLL_EMPLOYEE_APPROVAL' and contextcode='HRMS';
delete from "anv".permission_context where permissioncode = 'PAYROLL_EMPLOYEE_APPROVAL' and contextcode='HRMS';

insert into "0".permission_context (permissioncode, contextcode) values ('PAYROLL_EMPLOYEE_APPROVAL', 'HRMS');
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_EMPLOYEE_APPROVAL', 'HRMS');