
--additional payment supervisor add
delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD', 'PAYROLL', 'Add Supervisor', 5, (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_ADD'), 'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_SUPERVISOR_ADD', 'ALLOW', 'HR');

