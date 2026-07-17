
delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_FULL_EDIT';
insert into permission (code, context, name, sorder, parent, modulecode)
values ('PAYROLL_ADDITIONAL_PAYMENT_FULL_EDIT', 'PAYROLL', 'Full Edit', (SELECT max(sorder) + 1 from permission WHERE code = 'PAYROLL_ADDITIONAL_PAYMENT_SEE_OWN'),
        (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_LIST'), 'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_FULL_EDIT';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_FULL_EDIT', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_FULL_EDIT';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_FULL_EDIT', 'ALLOW', 'DR'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_FULL_EDIT', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_FULL_EDIT', 'ALLOW', 'ACCOUNTANT');
