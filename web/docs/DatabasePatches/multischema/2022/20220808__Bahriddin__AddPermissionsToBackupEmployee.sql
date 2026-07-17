-- Backup Employee List View
delete
from permission
where code = 'HRMS_BACKUPS_EMPLOYEE_LIST';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BACKUPS_EMPLOYEE_LIST',
        'HRMS',
        'Backup Employee',
        (select count(id) from permission where parent = (select id from permission where code = 'HRMS_MAIN_MENU')) + 1, (select id from permission where code = 'HRMS_MAIN_MENU'), 'HRMS_MODULE');

delete
from "anv".permission_context
where permissioncode = 'HRMS_BACKUPS_EMPLOYEE_LIST';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BACKUPS_EMPLOYEE_LIST', 'HRMS');

delete
from "anv".rolepermission
where permissioncode = 'HRMS_BACKUPS_EMPLOYEE_LIST';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_BACKUPS_EMPLOYEE_LIST', 'ALLOW', 'HR'),
       ('HRMS_BACKUPS_EMPLOYEE_LIST', 'ALLOW', 'ADMIN');

-- Backup Employee Add
delete
from permission
where code = 'HRMS_BACKUPS_EMPLOYEE_ADD';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BACKUPS_EMPLOYEE_ADD',
        'HRMS',
        'Add',
        1,
        (select id from permission where code = 'HRMS_BACKUPS_EMPLOYEE_LIST'),
        'HRMS_MODULE');

delete
from "anv".permission_context
where permissioncode = 'HRMS_BACKUPS_EMPLOYEE_ADD';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BACKUPS_EMPLOYEE_ADD', 'HRMS');

delete
from "anv".rolepermission
where permissioncode = 'HRMS_BACKUPS_EMPLOYEE_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_BACKUPS_EMPLOYEE_ADD', 'ALLOW', 'HR'),
       ('HRMS_BACKUPS_EMPLOYEE_ADD', 'ALLOW', 'ADMIN');


-- Backup Employee Edit
delete
from permission
where code = 'HRMS_BACKUPS_EMPLOYEE_EDIT';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BACKUPS_EMPLOYEE_EDIT',
        'HRMS',
        'Edit',
        2,
        (select id from permission where code = 'HRMS_BACKUPS_EMPLOYEE_LIST'),
        'HRMS_MODULE');

delete
from "anv".permission_context
where permissioncode = 'HRMS_BACKUPS_EMPLOYEE_EDIT';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BACKUPS_EMPLOYEE_EDIT', 'HRMS');

delete
from "anv".rolepermission
where permissioncode = 'HRMS_BACKUPS_EMPLOYEE_EDIT';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_BACKUPS_EMPLOYEE_EDIT', 'ALLOW', 'HR'),
       ('HRMS_BACKUPS_EMPLOYEE_EDIT', 'ALLOW', 'ADMIN');

-- Backup Employee Delete
delete
from permission
where code = 'HRMS_BACKUPS_EMPLOYEE_DELETE';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BACKUPS_EMPLOYEE_DELETE',
        'HRMS',
        'Delete',
        3,
        (select id from permission where code = 'HRMS_BACKUPS_EMPLOYEE_LIST'),
        'HRMS_MODULE');

delete
from "anv".permission_context
where permissioncode = 'HRMS_BACKUPS_EMPLOYEE_DELETE';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BACKUPS_EMPLOYEE_DELETE', 'HRMS');

delete
from "anv".rolepermission
where permissioncode = 'HRMS_BACKUPS_EMPLOYEE_DELETE';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_BACKUPS_EMPLOYEE_DELETE', 'ALLOW', 'HR'),
       ('HRMS_BACKUPS_EMPLOYEE_DELETE', 'ALLOW', 'ADMIN');

-- Backup Employee Summary
delete
from permission
where code = 'HRMS_BACKUPS_EMPLOYEE_SUMMARY';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('HRMS_BACKUPS_EMPLOYEE_SUMMARY',
        'HRMS',
        'Summary',
        4,
        (select id from permission where code = 'HRMS_BACKUPS_EMPLOYEE_LIST'),
        'HRMS_MODULE');

delete
from "anv".permission_context
where permissioncode = 'HRMS_BACKUPS_EMPLOYEE_SUMMARY';
insert into "anv".permission_context (permissioncode, contextcode)
values ('HRMS_BACKUPS_EMPLOYEE_SUMMARY', 'HRMS');

delete
from "anv".rolepermission
where permissioncode = 'HRMS_BACKUPS_EMPLOYEE_SUMMARY';
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('HRMS_BACKUPS_EMPLOYEE_SUMMARY', 'ALLOW', 'HR'),
       ('HRMS_BACKUPS_EMPLOYEE_SUMMARY', 'ALLOW', 'ADMIN');

