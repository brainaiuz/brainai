delete
from permission
where code = 'PAYROLL_SETTINGS_EMPLOYER_CONTRIBUTION';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('PAYROLL_SETTINGS_EMPLOYER_CONTRIBUTION', 'PAYROLL', false, 'Employer Contribution Add', 50,
        (select id from permission where code = 'PAYROLL_EMPLOYEE_ADD'), true, 'PAYROLL');

delete
from "anv".permission_context
where permissioncode = 'PAYROLL_SETTINGS_EMPLOYER_CONTRIBUTION';
insert into "anv".permission_context(permissioncode, contextcode)
values ('PAYROLL_SETTINGS_EMPLOYER_CONTRIBUTION', 'PAYROLL');


delete from "anv".rolepermission where permissioncode = 'PAYROLL_SETTINGS_EMPLOYER_CONTRIBUTION';
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('PAYROLL_SETTINGS_EMPLOYER_CONTRIBUTION', 'ALLOW', 'HR');

delete
from permission
where code = 'PAYROLL_PENDING_CHANGES';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('PAYROLL_PENDING_CHANGES', 'PAYROLL', false, 'Pending Changes', 50,
        (select id from permission where code = 'PAYROLL_MAIN_CONTENT'), true, 'PAYROLL');

delete
from "anv".permission_context
where permissioncode = 'PAYROLL_PENDING_CHANGES';
insert into "anv".permission_context(permissioncode, contextcode)
values ('PAYROLL_PENDING_CHANGES', 'PAYROLL');


delete from "anv".rolepermission where permissioncode = 'PAYROLL_PENDING_CHANGES';
insert into "anv".rolepermission(permissioncode, access, rolecode)
values ('PAYROLL_PENDING_CHANGES', 'ALLOW', 'HR');