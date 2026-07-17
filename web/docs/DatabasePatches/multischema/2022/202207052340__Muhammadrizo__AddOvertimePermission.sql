insert into permission (code, context, name, sorder, parent, modulecode)
select 'PAYROLL_OVERTIME_LIST',
       'PAYROLL',
       'Overtime',
       (select count(id) from permission where parent = (select id from permission where code = 'PAYROLL_MAIN_MENU')) +
       1,
       (select id from permission where code = 'PAYROLL_MAIN_MENU'),
       'PAYROLL' where NOT EXISTS (SELECT id from permission where code='PAYROLL_OVERTIME_LIST');

insert into "anv".permission_context (permissioncode, contextcode)
select 'PAYROLL_OVERTIME_LIST', 'PAYROLL' where NOT EXISTS (select permissioncode from "anv".permission_context where permissioncode = 'PAYROLL_OVERTIME_LIST');

insert into "anv".rolepermission (permissioncode, access, rolecode)
select 'PAYROLL_OVERTIME_LIST', 'ALLOW', 'DR' where NOT EXISTS (select id from "anv".rolepermission where permissioncode = 'PAYROLL_OVERTIME_LIST');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('PAYROLL_OVERTIME_LIST', 'ALLOW', 'HR'),
       ('PAYROLL_OVERTIME_LIST', 'ALLOW', 'ADMIN');



insert into permission (code, context, name, sorder, parent, modulecode)
select 'PAYROLL_OVERTIME_ADD',
       'PAYROLL',
       'Add',
       1,
       (select id from permission where code = 'PAYROLL_OVERTIME_LIST'),
       'PAYROLL' where NOT EXISTS (SELECT id from permission where code='PAYROLL_OVERTIME_ADD');

insert into "anv".permission_context (permissioncode, contextcode)
select 'PAYROLL_OVERTIME_ADD', 'PAYROLL' where NOT EXISTS (select permissioncode from "anv".permission_context where permissioncode = 'PAYROLL_OVERTIME_ADD');

insert into "anv".rolepermission (permissioncode, access, rolecode)
select 'PAYROLL_OVERTIME_ADD', 'ALLOW', 'DR' where NOT EXISTS (select id from "anv".rolepermission where permissioncode = 'PAYROLL_OVERTIME_ADD');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('PAYROLL_OVERTIME_ADD', 'ALLOW', 'DR'),
       ('PAYROLL_OVERTIME_ADD', 'ALLOW', 'ADMIN');


insert into permission (code, context, name, sorder, parent, modulecode)
select 'PAYROLL_OVERTIME_EDIT',
       'PAYROLL',
       'Edit',
       2,
       (select id from permission where code = 'PAYROLL_OVERTIME_LIST'),
       'PAYROLL' where NOT EXISTS (SELECT id from permission where code='PAYROLL_OVERTIME_EDIT');

insert into "anv".permission_context (permissioncode, contextcode)
select 'PAYROLL_OVERTIME_EDIT', 'PAYROLL' where NOT EXISTS (select permissioncode from "anv".permission_context where permissioncode = 'PAYROLL_OVERTIME_EDIT');

insert into "anv".rolepermission (permissioncode, access, rolecode)
select 'PAYROLL_OVERTIME_EDIT', 'ALLOW', 'DR' where NOT EXISTS (select id from "anv".rolepermission where permissioncode = 'PAYROLL_OVERTIME_EDIT');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('PAYROLL_OVERTIME_EDIT', 'ALLOW', 'HR'),
       ('PAYROLL_OVERTIME_EDIT', 'ALLOW', 'ADMIN');



insert into permission (code, context, name, sorder, parent, modulecode)
select 'PAYROLL_OVERTIME_SUMMARY',
       'PAYROLL',
       'Summary',
       3,
       (select id from permission where code = 'PAYROLL_OVERTIME_LIST'),
       'PAYROLL' where NOT EXISTS (SELECT id from permission where code='PAYROLL_OVERTIME_SUMMARY');

insert into "anv".permission_context (permissioncode, contextcode)
select 'PAYROLL_OVERTIME_SUMMARY', 'PAYROLL' where NOT EXISTS (select permissioncode from "anv".permission_context where permissioncode = 'PAYROLL_OVERTIME_SUMMARY');

insert into "anv".rolepermission (permissioncode, access, rolecode)
select 'PAYROLL_OVERTIME_SUMMARY', 'ALLOW', 'DR' where NOT EXISTS (select id from "anv".rolepermission where permissioncode = 'PAYROLL_OVERTIME_SUMMARY');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('PAYROLL_OVERTIME_SUMMARY', 'ALLOW', 'HR'),
       ('PAYROLL_OVERTIME_SUMMARY', 'ALLOW', 'ADMIN');


insert into permission (code, context, name, sorder, parent, modulecode)
select 'PAYROLL_OVERTIME_DELETE',
       'PAYROLL',
       'Delete',
       3,
       (select id from permission where code = 'PAYROLL_OVERTIME_LIST'),
       'PAYROLL' where NOT EXISTS (SELECT id from permission where code='PAYROLL_OVERTIME_DELETE');

insert into "anv".permission_context (permissioncode, contextcode)
select 'PAYROLL_OVERTIME_DELETE', 'PAYROLL' where NOT EXISTS (select permissioncode from "anv".permission_context where permissioncode = 'PAYROLL_OVERTIME_DELETE');

insert into "anv".rolepermission (permissioncode, access, rolecode)
select 'PAYROLL_OVERTIME_DELETE', 'ALLOW', 'DR' where NOT EXISTS (select id from "anv".rolepermission where permissioncode = 'PAYROLL_OVERTIME_DELETE');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('PAYROLL_OVERTIME_DELETE', 'ALLOW', 'HR'),
       ('PAYROLL_OVERTIME_DELETE', 'ALLOW', 'ADMIN');

