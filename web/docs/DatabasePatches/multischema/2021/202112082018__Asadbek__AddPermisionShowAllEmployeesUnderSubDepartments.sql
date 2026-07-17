delete from permission where code='SHOW_ALL_EMPLOYEES_UNDER_SUB_DEPARTMENTS';
insert into permission (code, context, name, sorder, parent, modulecode) values
('SHOW_ALL_EMPLOYEES_UNDER_SUB_DEPARTMENTS', 'HRMS', 'Show all employees under sub departments', 3,(select id from permission where code = 'HRMS_EMPLOYEES'),'HRMS_MODULE');

delete from "anv".permission_context where permissioncode = 'SHOW_ALL_EMPLOYEES_UNDER_SUB_DEPARTMENTS';
insert into "anv".permission_context (permissioncode, contextcode) values ('SHOW_ALL_EMPLOYEES_UNDER_SUB_DEPARTMENTS', 'HRMS');


delete from "anv".rolepermission where permissioncode = 'SHOW_ALL_EMPLOYEES_UNDER_SUB_DEPARTMENTS';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('SHOW_ALL_EMPLOYEES_UNDER_SUB_DEPARTMENTS', 'ALLOW', 'DR'),
                                                                           ('SHOW_ALL_EMPLOYEES_UNDER_SUB_DEPARTMENTS', 'ALLOW', 'TL'),
                                                                           ('SHOW_ALL_EMPLOYEES_UNDER_SUB_DEPARTMENTS', 'ALLOW', 'ADMIN');