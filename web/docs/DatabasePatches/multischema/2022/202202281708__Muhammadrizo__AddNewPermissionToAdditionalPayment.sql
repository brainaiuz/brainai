
insert into permission (code, context, name, sorder, parent, modulecode)
values ('PAYMENT_TYPE_EDIT', 'PAYROLL',   'Payment Type Edit',
(select count(id) from permission where parent = (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_LIST')) + 1,
 (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_LIST'),      'PAYROLL');

insert into "anv".permission_context (permissioncode, contextcode) values ('PAYMENT_TYPE_EDIT',  'PAYROLL'),
                                                                          ('PAYMENT_TYPE_EDIT',  'HRMS');

insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYMENT_TYPE_EDIT', 'ALLOW',  'HR'),
                                                                           ('PAYMENT_TYPE_EDIT', 'ALLOW',  'DR');



