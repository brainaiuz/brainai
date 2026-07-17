
--additional payment see all
delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_SEE_ALL';
insert into permission (code, context, name, sorder, parent, modulecode) values
('PAYROLL_ADDITIONAL_PAYMENT_SEE_ALL', 'PAYROLL', 'See All', (select max(sorder) + 1 from permission where parent=(select id from permission where code='PAYROLL_ADDITIONAL_PAYMENT_LIST')),
 (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_LIST'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_SEE_ALL';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_SEE_ALL', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_SEE_ALL';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_SEE_ALL', 'ALLOW', 'ADMIN'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_SEE_ALL', 'ALLOW', 'DR'),
                                                                           ('PAYROLL_ADDITIONAL_PAYMENT_SEE_ALL', 'ALLOW', 'HR');

--additional payment see own
delete from permission where code='PAYROLL_ADDITIONAL_PAYMENT_SEE_OWN';
insert into permission (code, context, name, sorder, parent, modulecode) values
    ('PAYROLL_ADDITIONAL_PAYMENT_SEE_OWN', 'PAYROLL', 'See Own', (select max(sorder) + 1 from permission where parent=(select id from permission where code='PAYROLL_ADDITIONAL_PAYMENT_LIST')),
     (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_LIST'),'PAYROLL');

delete from "anv".permission_context where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_SEE_OWN';
insert into "anv".permission_context (permissioncode, contextcode) values ('PAYROLL_ADDITIONAL_PAYMENT_SEE_OWN', 'PAYROLL');

delete from "anv".rolepermission where permissioncode = 'PAYROLL_ADDITIONAL_PAYMENT_SEE_OWN';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('PAYROLL_ADDITIONAL_PAYMENT_SEE_OWN', 'ALLOW', 'ADMIN');


