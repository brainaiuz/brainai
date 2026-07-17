insert into permission (code, context, name, parent, modulecode)
values ('GDN_PDF', 'ACCOUNTING', 'GDN PDF',
        (select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), 'SALES_ORDERS');

insert into "anv".permission_context (permissioncode, contextcode)
values ('GDN_PDF', 'ACCOUNTING');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('GDN_PDF', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('GDN_PDF', 'ALLOW', 'ACCOUNTANT');

insert into permission (code, context, name, parent, modulecode)
values ('GDN_EXCEL', 'ACCOUNTING', 'GDN EXCEL',
        (select id from permission where code = 'ACCOUNTING_SALES_ORDER_LIST'), 'SALES_ORDERS');

insert into "anv".permission_context (permissioncode, contextcode)
values ('GDN_EXCEL', 'ACCOUNTING');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('GDN_EXCEL', 'ALLOW', 'DR');
insert into "anv".rolepermission (permissioncode, access, rolecode)
values ('GDN_EXCEL', 'ALLOW', 'ACCOUNTANT');


