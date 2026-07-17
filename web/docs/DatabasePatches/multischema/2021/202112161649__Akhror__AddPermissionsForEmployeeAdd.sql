insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_QUICK_ADD_NEW_EMPLOYEE',   'HRMS', false, 'Quick Add', 12, (select id from permission where code = 'HRMS_EMPLOYEES'), true, 'HRMS_MODULE');

insert into "anv".permission_context(permissioncode,contextcode) values ('HRMS_QUICK_ADD_NEW_EMPLOYEE','HRMS');

insert into "anv".rolepermission(permissioncode, rolecode,access) values ('HRMS_QUICK_ADD_NEW_EMPLOYEE', 'HR', 'ALLOW');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_ADD_MULTI_EMPLOYEES',   'HRMS', false, 'Add Multi Employees', 13, (select id from permission where code = 'HRMS_EMPLOYEES'), true, 'HRMS_MODULE');

insert into "anv".permission_context(permissioncode,contextcode) values ('HRMS_ADD_MULTI_EMPLOYEES','HRMS');

insert into "anv".rolepermission(permissioncode, rolecode, access) values ('HRMS_ADD_MULTI_EMPLOYEES', 'HR', 'ALLOW');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('PM_EMPLOYEE_QUICK_ADD',   'PM', false, 'Quick Add', 7, (select id from permission where code = 'PM_EMPLOYEE_LIST'), true, 'PM');

insert into "anv".permission_context(permissioncode,contextcode) values ('PM_EMPLOYEE_QUICK_ADD','PM');

insert into "anv".rolepermission(permissioncode, rolecode,access) values ('PM_EMPLOYEE_QUICK_ADD', 'HR', 'ALLOW');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('PM_ADD_MULTI_EMPLOYEES',   'PM', false, 'Add Multi Employees', 8, (select id from permission where code = 'PM_EMPLOYEE_LIST'), true, 'PM');

insert into "anv".permission_context(permissioncode,contextcode) values ('PM_ADD_MULTI_EMPLOYEES','PM');

insert into "anv".rolepermission(permissioncode, rolecode, access) values ('PM_ADD_MULTI_EMPLOYEES', 'HR', 'ALLOW');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('PAYROLL_QUICK_ADD_NEW_EMPLOYEE',   'PAYROLL', false, 'Quick Add', 2, (select id from permission where code = 'PAYROLL_EMPLOYEES_LIST'), true, 'PAYROLL');

insert into "anv".permission_context(permissioncode,contextcode) values ('PAYROLL_QUICK_ADD_NEW_EMPLOYEE','PAYROLL');

insert into "anv".rolepermission(permissioncode, rolecode,access) values ('PAYROLL_QUICK_ADD_NEW_EMPLOYEE', 'HR', 'ALLOW');

insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('PAYROLL_ADD_MULTI_EMPLOYEES',   'PAYROLL', false, 'Add Multi Employees', 3, (select id from permission where code = 'PAYROLL_EMPLOYEES_LIST'), true, 'PAYROLL');

insert into "anv".permission_context(permissioncode,contextcode) values ('PAYROLL_ADD_MULTI_EMPLOYEES','PAYROLL');

insert into "anv".rolepermission(permissioncode, rolecode, access) values ('PAYROLL_ADD_MULTI_EMPLOYEES', 'HR', 'ALLOW');

