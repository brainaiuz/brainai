

DELETE FROM permission WHERE code = 'ACCOUNTING_SALES_QUOTE_CLOSED';

insert into permission (code, context, ismainmenu, name, sorder, parent, modulecode)
values ('ACCOUNTING_SALES_QUOTE_CLOSED', 'ACCOUNTING', 'f', 'Closed', '15',
(select id from permission where code='ACCOUNTING_SALES_QUOTE_LIST'), 'SALES_QUOTES');

insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_SALES_QUOTE_CLOSED', 'ADMIN', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_SALES_QUOTE_CLOSED', 'DR', 'ALLOW');
insert into "anv".rolepermission (permissioncode, rolecode, access) values ('ACCOUNTING_SALES_QUOTE_CLOSED', 'ACCOUNTANT', 'ALLOW');
insert into "anv".permission_context (permissioncode, contextcode) values ('ACCOUNTING_SALES_QUOTE_CLOSED',  'ACCOUNTING');

