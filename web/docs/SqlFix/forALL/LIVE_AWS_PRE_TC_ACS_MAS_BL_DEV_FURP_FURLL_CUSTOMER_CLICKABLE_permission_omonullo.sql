--CUSTOMER_CLICKABLE

delete from permission where code = 'CUSTOMER_CLICKABLE';

INSERT INTO permission (code, context, name, sorder, parent, iscore, modulecode)
VALUES ('CUSTOMER_CLICKABLE', 'ACCOUNTING', 'Client link clickable', 1, (SELECT id
                                                                         FROM permission
                                                                         WHERE
                                                                           code = 'ACCOUNTING_SALES_INVOICE_SUMMARY' AND
                                                                           context = 'ACCOUNTING'), FALSE,
        'SALES_INVOICING');

delete from "anv".rolepermission where permissioncode = 'CUSTOMER_CLICKABLE';

insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('CUSTOMER_CLICKABLE', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('CUSTOMER_CLICKABLE', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('CUSTOMER_CLICKABLE', 'ALLOW', 'ACCOUNTANT');

delete from "0".rolepermission where permissioncode = 'CUSTOMER_CLICKABLE';

insert into "0".rolepermission (permissioncode, access, rolecode)
values ('CUSTOMER_CLICKABLE', 'ALLOW', 'DR');
insert into "0".rolepermission (permissioncode, access, rolecode)
values ('CUSTOMER_CLICKABLE', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode)
values ('CUSTOMER_CLICKABLE', 'ALLOW', 'ACCOUNTANT');