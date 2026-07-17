delete from permission where code = 'ACCOUNTING_SALES_INVOICE_FULL_DELETE_ACCESS';
insert into permission (code, context, ismainmenu, name, sorder, parent, iscore, modulecode)
values ('ACCOUNTING_SALES_INVOICE_FULL_DELETE_ACCESS', 'ACCOUNTING', false, 'Full delete access', 50,
        (select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), true, 'SALES_INVOICING');


delete from "anv".permission_context where permissioncode = 'ACCOUNTING_SALES_INVOICE_FULL_DELETE_ACCESS';
insert into "anv".permission_context(permissioncode, contextcode)
values ('ACCOUNTING_SALES_INVOICE_FULL_DELETE_ACCESS', 'ACCOUNTING');

delete from "anv".rolepermission where permissioncode = 'ACCOUNTING_SALES_INVOICE_FULL_DELETE_ACCESS';
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_INVOICE_FULL_DELETE_ACCESS', 'ALLOW', 'DR');
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_INVOICE_FULL_DELETE_ACCESS', 'ALLOW', 'ADMIN');
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_INVOICE_FULL_DELETE_ACCESS', 'ALLOW', 'ACCOUNTANT');
insert into "anv".rolepermission(permissioncode, access, rolecode)
            values ('ACCOUNTING_SALES_INVOICE_FULL_DELETE_ACCESS', 'ALLOW', 'SALESMANAGER');