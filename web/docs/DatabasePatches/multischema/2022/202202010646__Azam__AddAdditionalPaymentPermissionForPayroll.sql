
--additional payment group add
delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD';
insert into permission (code, context, name, sorder, parent, modulecode) values
('PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD', 'PAYROLL', 'Add Group', 1, (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_ADD'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_GROUP_ADD', 'ALLOW', 'HR');

--additional payment employee add
delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD', 'PAYROLL', 'Add Employee', 2, (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_ADD'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_EMPLOYEE_ADD', 'ALLOW', 'HR');
--additional payment department add
delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD', 'PAYROLL', 'Add Department', 3, (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_ADD'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD', 'ALLOW', 'TL'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_DEPARTMENT_ADD', 'ALLOW', 'HR');

--additional payment location add
delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD', 'PAYROLL', 'Add Location', 4, (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_ADD'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_LOCATION_ADD', 'ALLOW', 'HR');

