insert into permission (code, context, name,  parent, modulecode)
values ('CLIENT_SEND_SMS', 'ACCOUNTING', 'Send SMS',  (select id from permission where code = 'ACCOUNTING_CUSTOMER_LIST'), 'ACCOUNTING_MODULE');

insert into "anv".permission_context (permissioncode, contextcode)
values ('CLIENT_SEND_SMS', 'ACCOUNTING');

insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('CLIENT_SEND_SMS', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('CLIENT_SEND_SMS', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('CLIENT_SEND_SMS', 'ALLOW', 'ACCOUNTANT');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('CLIENT_SEND_SMS', 'ALLOW', 'SALESMAN');
