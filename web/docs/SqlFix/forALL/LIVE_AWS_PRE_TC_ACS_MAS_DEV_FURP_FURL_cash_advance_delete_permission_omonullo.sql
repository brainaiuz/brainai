delete from permission where code = 'CASH_ADVANCE_PAYMENT_DELETE';
insert into permission (code, context, name, sorder, parent, iscore, modulecode)
    values ('CASH_ADVANCE_PAYMENT_DELETE', 'PAYROLL', 'Cash Advance Payment Delete', 1, (select id from permission where code = 'PAYROLL_CASH_ADVANCE_VIEW'), true, 'CORE');

delete from "anv".rolepermission where permissioncode = 'CASH_ADVANCE_PAYMENT_DELETE';
insert into "anv".rolepermission (permissioncode, access, rolecode)
    values ('CASH_ADVANCE_PAYMENT_DELETE', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode)
    values ('CASH_ADVANCE_PAYMENT_DELETE', 'ALLOW', 'DR');

delete from "0".rolepermission where permissioncode = 'CASH_ADVANCE_PAYMENT_DELETE';
insert into "0".rolepermission (permissioncode, access, rolecode)
    values ('CASH_ADVANCE_PAYMENT_DELETE', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode)
    values ('CASH_ADVANCE_PAYMENT_DELETE', 'ALLOW', 'DR');