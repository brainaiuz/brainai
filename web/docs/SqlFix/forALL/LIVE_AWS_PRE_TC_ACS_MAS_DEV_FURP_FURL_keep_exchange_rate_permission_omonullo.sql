delete from permission where code = 'KEEP_EX_RATE';
insert into permission (code, context, name, sorder, parent, modulecode)
              values ('KEEP_EX_RATE', 'ACCOUNTING', 'Manage Exchange Rate being kept', 1, (select id from permission where code = 'ACCOUNTING_FINANCIAL_SETTINGS'), 'ACCOUNTING_MODULE');


delete from "anv".rolepermission where permissioncode = 'KEEP_EX_RATE';
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('KEEP_EX_RATE', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('KEEP_EX_RATE', 'ALLOW', 'ACCOUNTANT');
insert into "anv".rolepermission (permissioncode, access, rolecode) values ('KEEP_EX_RATE', 'ALLOW', 'DR');

delete from "0".rolepermission where permissioncode = 'KEEP_EX_RATE';
insert into "0".rolepermission (permissioncode, access, rolecode) values ('KEEP_EX_RATE', 'ALLOW', 'ADMIN');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('KEEP_EX_RATE', 'ALLOW', 'ACCOUNTANT');
insert into "0".rolepermission (permissioncode, access, rolecode) values ('KEEP_EX_RATE', 'ALLOW', 'DR');
