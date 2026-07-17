
--permission
delete from permission where code in ('PAYROLL_SHOW_ALL_EMPLOYEE_LIST', 'PAYROLL_SHOW_DEPARTMENT_EMPLOYEE_LIST',
                                      'PAYROLL_SHOW_PROJECT_EMPLOYEE_LIST', 'PAYROLL_SHOW_LOCATION_EMPLOYEE_LIST', 'PAYROLL_SHOW_SUPERVISED_EMPLOYEE_LIST');

insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_SHOW_ALL_EMPLOYEE_LIST', 'PAYROLL', 'Show All', (select max(sorder) + 1 from permission where parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST')),
     (select id from permission where code = 'PAYROLL_EMPLOYEES_LIST'), 'PAYROLL');

insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_SHOW_DEPARTMENT_EMPLOYEE_LIST', 'PAYROLL', 'Show Department Employees', (select max(sorder) + 1 from permission where parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST')),
     (select id from permission where code = 'PAYROLL_EMPLOYEES_LIST'), 'PAYROLL');

insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_SHOW_PROJECT_EMPLOYEE_LIST', 'PAYROLL', 'Show Project Employees', (select max(sorder) + 1 from permission where parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST')),
     (select id from permission where code = 'PAYROLL_EMPLOYEES_LIST'), 'PAYROLL');

insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_SHOW_LOCATION_EMPLOYEE_LIST', 'PAYROLL', 'Show Location Employees', (select max(sorder) + 1 from permission where parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST')),
     (select id from permission where code = 'PAYROLL_EMPLOYEES_LIST'), 'PAYROLL');

insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_SHOW_SUPERVISED_EMPLOYEE_LIST', 'PAYROLL', 'Show Supervised Employees', (select max(sorder) + 1 from permission where parent=(select id from permission where code='PAYROLL_EMPLOYEES_LIST')),
     (select id from permission where code = 'PAYROLL_EMPLOYEES_LIST'), 'PAYROLL');

--permission context
delete from "anv".permission_context where permissioncode in ('PAYROLL_SHOW_ALL_EMPLOYEE_LIST', 'PAYROLL_SHOW_DEPARTMENT_EMPLOYEE_LIST',
                                                              'PAYROLL_SHOW_PROJECT_EMPLOYEE_LIST', 'PAYROLL_SHOW_LOCATION_EMPLOYEE_LIST', 'PAYROLL_SHOW_SUPERVISED_EMPLOYEE_LIST');

insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_SHOW_ALL_EMPLOYEE_LIST', 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_SHOW_DEPARTMENT_EMPLOYEE_LIST', 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_SHOW_PROJECT_EMPLOYEE_LIST', 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_SHOW_LOCATION_EMPLOYEE_LIST', 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_SHOW_SUPERVISED_EMPLOYEE_LIST', 'PAYROLL');

--rolepermission
delete from "anv".rolepermission where permissioncode in ('PAYROLL_SHOW_ALL_EMPLOYEE_LIST', 'PAYROLL_SHOW_DEPARTMENT_EMPLOYEE_LIST',
                                                          'PAYROLL_SHOW_PROJECT_EMPLOYEE_LIST', 'PAYROLL_SHOW_LOCATION_EMPLOYEE_LIST', 'PAYROLL_SHOW_SUPERVISED_EMPLOYEE_LIST');

insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SHOW_ALL_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SHOW_ALL_EMPLOYEE_LIST', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SHOW_ALL_EMPLOYEE_LIST', 'HR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SHOW_DEPARTMENT_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SHOW_DEPARTMENT_EMPLOYEE_LIST', 'TL', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SHOW_PROJECT_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SHOW_PROJECT_EMPLOYEE_LIST', 'PM', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SHOW_LOCATION_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SHOW_LOCATION_EMPLOYEE_LIST', 'ADMIN_LOCATION', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SHOW_SUPERVISED_EMPLOYEE_LIST', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values('PAYROLL_SHOW_SUPERVISED_EMPLOYEE_LIST', 'SUPERVISOR', 'ALLOW');