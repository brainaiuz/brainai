insert into permission (code, context, name, parent, modulecode)
values ('ACCOUNTING_SALES_INVOICE_COPYTOPI', 'ACCOUNTING', 'Copy SI to PI',
        (select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), 'ACCOUNTING_MODULE');

insert into "anv".permission_context (permissioncode, contextcode)
values ('ACCOUNTING_SALES_INVOICE_COPYTOPI', 'ACCOUNTING');