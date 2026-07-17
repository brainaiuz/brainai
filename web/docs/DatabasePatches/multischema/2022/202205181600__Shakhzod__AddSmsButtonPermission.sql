insert into permission (code, context, name, parent, modulecode)
values ('ACCOUNTING_SALES_INVOICE_SMS_BUTTON', 'ACCOUNTING', 'Send Sms',
        (select id from permission where code = 'ACCOUNTING_SALES_INVOICE_LIST'), 'SALES_INVOICING');

insert into "anv".permission_context (permissioncode, contextcode)
values ('ACCOUNTING_SALES_INVOICE_SMS_BUTTON', 'ACCOUNTING');

