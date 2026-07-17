insert into permission(code, context, name, sorder, parent, modulecode)
values ('PAYSLIP_ADD_PAYMENT', 'PAYROLL', 'Add Payment', (select max(sorder)
                                                          from permission
                                                          where parent =
                                                                (select id from permission where code = 'PAYROLL_PAYSLIP_LIST')),
        (select id from permission where code = 'PAYROLL_PAYSLIP_LIST'), 'PAYROLL'),
       ('PAYSLIP_DELETE_PAYMENT', 'PAYROLL', 'Delete Payment', (select max(sorder)
                                                                from permission
                                                                where parent =
                                                                      (select id from permission where code = 'PAYROLL_PAYSLIP_LIST')),
        (select id from permission where code = 'PAYROLL_PAYSLIP_LIST'), 'PAYROLL');
insert into "anv".permission_context(permissioncode, contextcode)
values ('PAYSLIP_ADD_PAYMENT', 'PAYROLL'),
       ('PAYSLIP_DELETE_PAYMENT', 'PAYROLL');