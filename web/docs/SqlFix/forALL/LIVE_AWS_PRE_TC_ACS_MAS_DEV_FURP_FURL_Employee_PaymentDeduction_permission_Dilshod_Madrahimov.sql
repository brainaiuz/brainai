delete from permission where code = 'HRMS_PAYROLL_DEDUCTION_CATEGORIES';

insert into permission (code, context, name, sorder, parent, iscore, modulecode)
                values ('HRMS_PAYROLL_DEDUCTION_CATEGORIES', 'HRMS', 'Show Payments/Deductions table', 8, (select id from permission where code = 'HRMS_EMPLOYEE_PROFILE'), false, 'HRMS_MODULE');

delete from "anv".rolepermission where permissioncode = 'HRMS_PAYROLL_DEDUCTION_CATEGORIES';
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_PAYROLL_DEDUCTION_CATEGORIES', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_PAYROLL_DEDUCTION_CATEGORIES', 'DR',    'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('HRMS_PAYROLL_DEDUCTION_CATEGORIES', 'HR',    'ALLOW');

delete from "0".rolepermission where permissioncode = 'HRMS_PAYROLL_DEDUCTION_CATEGORIES';
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_PAYROLL_DEDUCTION_CATEGORIES', 'ADMIN', 'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_PAYROLL_DEDUCTION_CATEGORIES', 'DR',    'ALLOW');
insert into "0".rolepermission (permissioncode, rolecode, access) values ('HRMS_PAYROLL_DEDUCTION_CATEGORIES', 'HR',    'ALLOW');