
insert into permission (code, context, name, sorder, parent, modulecode) values
('BASIC_SALARY_EDIT',          'HRMS',   'Basic Salary Edit',   (select count(id) from permission where parent = (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_LIST')) + 1, (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_LIST'),      'PAYROLL'),
('ACCOUNTING_PURCHASE_INVOICE_APPROVE', 'ACCOUNTING',   'Approve Invoice', 14, (select id from permission where code = 'ACCOUNTING_PURCHASE_INVOICE_LIST'), 'PURCHASE_INVOICING');

insert into "anv".permission_context (permissioncode, contextcode) values ('BASIC_SALARY_EDIT',           'PAYROLL'),
                                                                          ('BASIC_SALARY_EDIT',  'HRMS');

insert into "anv".rolepermission (permissioncode, access, rolecode) values ('BASIC_SALARY_EDIT',          'ALLOW',  'HR'),
                                                                           ('BASIC_SALARY_EDIT',          'ALLOW',  'ADMIN'),
                                                                           ('BASIC_SALARY_EDIT', 'ALLOW',  'DR');

