delete from permission where code = 'MANAGE_KEEP_EX_RATE';
insert into permission (code, context, name, sorder, parent, modulecode)
                values ('MANAGE_KEEP_EX_RATE', 'ACCOUNTING', 'Keep PI Exchange Rate', 1, (select id from permission where code = 'ACCOUNTING_FINANCIAL_SETTINGS'), 'ACCOUNTING_MODULE');

delete from "anv".rolepermission where permissioncode = 'MANAGE_KEEP_EX_RATE' and access = 'ALLOW' and rolecode in ('ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('MANAGE_KEEP_EX_RATE', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('MANAGE_KEEP_EX_RATE', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('MANAGE_KEEP_EX_RATE', 'ALLOW', 'ACCOUNTANT');

delete from "0".rolepermission where permissioncode = 'MANAGE_KEEP_EX_RATE' and access = 'ALLOW' and rolecode in ('ADMIN', 'DR', 'ACCOUNTANT');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('MANAGE_KEEP_EX_RATE', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('MANAGE_KEEP_EX_RATE', 'ALLOW', 'DR');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('MANAGE_KEEP_EX_RATE', 'ALLOW', 'ACCOUNTANT');