delete from permission where code in ('HRMS_SEND_EMAIL', 'HRMS_SEND_SMS');
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('HRMS_SEND_EMAIL', 'HRMS', false, 'Send Email', 40,
        (select id from permission where code = 'HRMS_EMPLOYEES'), true, 'HRMS_MODULE'),
       ('HRMS_SEND_SMS', 'HRMS', false, 'Send Sms', 40,
        (select id from permission where code = 'HRMS_EMPLOYEES'), true, 'HRMS_MODULE');

delete from "anv".permission_context where permissioncode in ('HRMS_SEND_EMAIL', 'HRMS_SEND_SMS');
insert into "anv".permission_context(permissioncode, contextcode)
values ('HRMS_SEND_EMAIL', 'HRMS'),
       ('HRMS_SEND_SMS', 'HRMS');

delete from permission where code in ('PAYROLL_SEND_EMAIL', 'PAYROLL_SEND_SMS');
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('PAYROLL_SEND_EMAIL', 'PAYROLL', false, 'Send Email', 40,
        (select id from permission where code = 'PAYROLL_EMPLOYEES_LIST'), true, 'PAYROLL'),
       ('PAYROLL_SEND_SMS', 'PAYROLL', false, 'Send Sms', 40,
        (select id from permission where code = 'PAYROLL_EMPLOYEES_LIST'), true, 'PAYROLL');

delete from "anv".permission_context where permissioncode in ('PAYROLL_SEND_EMAIL', 'PAYROLL_SEND_SMS');
insert into "anv".permission_context(permissioncode, contextcode)
values ('PAYROLL_SEND_EMAIL', 'PAYROLL'),
       ('PAYROLL_SEND_SMS', 'PAYROLL');

delete from permission where code in ('PM_SEND_EMAIL', 'PM_SEND_SMS');
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('PM_SEND_EMAIL', 'PM', false, 'Send Email', 40,
        (select id from permission where code = 'PM_EMPLOYEE_LIST'), true, 'PM'),
       ('PM_SEND_SMS', 'PM', false, 'Send Sms', 40,
        (select id from permission where code = 'PM_EMPLOYEE_LIST'), true, 'PM');

delete from "anv".permission_context where permissioncode in ('PM_SEND_EMAIL', 'PM_SEND_SMS');
insert into "anv".permission_context(permissioncode, contextcode)
values ('PM_SEND_EMAIL', 'PM'),
       ('PM_SEND_SMS', 'PM');


delete from "anv".rolepermission where permissioncode in ('HRMS_SEND_EMAIL', 'HRMS_SEND_SMS', 'PAYROLL_SEND_EMAIL', 'PAYROLL_SEND_SMS', 'PM_SEND_EMAIL', 'PM_SEND_SMS');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_SEND_EMAIL','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_SEND_EMAIL','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_SEND_EMAIL','ALLOW','HR');

insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_SEND_SMS','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_SEND_SMS','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('HRMS_SEND_SMS','ALLOW','HR');

insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PAYROLL_SEND_EMAIL','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PAYROLL_SEND_EMAIL','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PAYROLL_SEND_EMAIL','ALLOW','HR');

insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PAYROLL_SEND_SMS','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PAYROLL_SEND_SMS','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PAYROLL_SEND_SMS','ALLOW','HR');

insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PM_SEND_EMAIL','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PM_SEND_EMAIL','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PM_SEND_EMAIL','ALLOW','HR');

insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PM_SEND_SMS','ALLOW','ADMIN');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PM_SEND_SMS','ALLOW','DR');
insert into "anv".rolepermission(permissioncode,access,rolecode) values ('PM_SEND_SMS','ALLOW','HR');

