insert into permission (code, context, name, parent, modulecode)
values ('ADDITIONAL_PAYMENT_LINE_ITEM_DELETE', 'PAYROLL', 'Line Item Delete',
        (select id from permission where code = 'PAYROLL_ADDITIONAL_PAYMENT_LIST'), 'PAYROLL');
insert into "anv".permission_context (permissioncode, contextcode)
values ('ADDITIONAL_PAYMENT_LINE_ITEM_DELETE', 'PAYROLL');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('ADDITIONAL_PAYMENT_LINE_ITEM_DELETE', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('ADDITIONAL_PAYMENT_LINE_ITEM_DELETE', 'ALLOW', 'ACCOUNTANT');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('ADDITIONAL_PAYMENT_LINE_ITEM_DELETE', 'ALLOW', 'ADMIN');